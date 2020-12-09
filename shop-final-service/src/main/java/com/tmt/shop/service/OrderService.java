package com.tmt.shop.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.check.CheckHandler;
import com.tmt.shop.check.CheckWrap;
import com.tmt.shop.check.OrderCheckResult;
import com.tmt.shop.check.impl.InitCheckHandler;
import com.tmt.shop.check.impl.LimitCheckHandler;
import com.tmt.shop.check.impl.StockCheckHandler;
import com.tmt.shop.dao.OrderDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderCoupon;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.OrderPostage;
import com.tmt.shop.entity.OrderPromotion;
import com.tmt.shop.entity.OrderRank;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.PaymentMethod.Type;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Refunds;
import com.tmt.shop.entity.ReturnItem;
import com.tmt.shop.entity.Returns;
import com.tmt.shop.entity.Shipping;
import com.tmt.shop.entity.ShippingItem;
import com.tmt.shop.enums.OrderStatus;
import com.tmt.shop.enums.PayStatus;
import com.tmt.shop.enums.ShippingStatus;
import com.tmt.shop.exception.OrderErrorException;
import com.tmt.shop.utils.ComplexUtils;
import com.tmt.shop.utils.PaymentMethodUtils;
import com.tmt.shop.utils.SnUtils;
import com.tmt.system.entity.User;

/**
 * 订单 管理
 * @author 超级管理员
 * @date 2015-09-17
 */
@Service("shopOrderService")
public class OrderService extends BaseService<Order,Long> implements OrderServiceFacade{
	
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderItemService itemService;
	@Autowired
	private RefundsService refundsService;
	@Autowired
	private OrderLogService orderLogService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CartService cartService;
	@Autowired
	private OrderEventService eventService;
	@Autowired
	private OrderStateService stateService;
	@Autowired
	private CouponService couponService;
	@Autowired
	private UserOrdersService ordersService;
	@Autowired
	private UserRankService rankService;
	@Autowired
	private OrderPromotionService promotionService;
	@Autowired
	private OrderRankService orankService;
	@Autowired
	private OrderAppraiseService orderAppraise;
	@Autowired
	private OrderPostageService postageService;
	private CheckHandler checkHandler = null;
	private void initCheckHandler() {
		if (checkHandler == null) {
			checkHandler = new InitCheckHandler();
			CheckHandler lCheckHandler = new LimitCheckHandler();
			CheckHandler sCheckHandler = new StockCheckHandler();
			checkHandler.setNextHandler(lCheckHandler);
			lCheckHandler.setNextHandler(sCheckHandler);
		}
	}
	
	@Override
	protected BaseDaoImpl<Order, Long> getBaseDao() {
		return orderDao;
	}
	
	/**
	 * 得到订单ID
	 * @param sn
	 * @return
	 */
	@Override
	public Long getIdBySn(String sn) {
		return this.queryForAttr("getIdBySn", sn);
	}
	
	/**
	 * 删除
	 */
	@Override
	@Transactional
	public void delete(List<Order> orders) {
		this.batchDelete(orders);
		List<OrderItem> alls = Lists.newArrayList();
		for(Order order: orders) {
			List<OrderItem> items = itemService.queryItemsByOrderId(order.getId());
			alls.addAll(items);
		}
		itemService.delete(alls);
	}
	
	/**
	 * 下定单
	 * @param order
	 */
	@Override
	@Transactional
	public void book(Order order) {
		
		// 将临时金额设置到具体的金额上
		order.setDiscount(order.getTempDiscount());
		order.setAmount(order.getTempAmount());
		
		PaymentMethod paymentMethod = PaymentMethodUtils.getPaymentMethod(order.getPaymentMethod());
		
		// 当前时间
		Date now = new Date();
		
		// 无支付方式是有问题的
		if (paymentMethod == null) {
			throw new OrderErrorException("请选择支付方式");
		}
		
		order.setSn(SnUtils.createOrderSn());
		
		// 不需要支付(现阶段不可能)
		if (BigDecimalUtil.equalZERO(order.getAnountPayAble())) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PayStatus.paid);
		} 
		
		// 部分已支付(现阶段不可能)
		else if(BigDecimalUtil.biggerThenZERO(order.getAnountPayAble()) && BigDecimalUtil.biggerThenZERO(order.getAmountPaid())) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PayStatus.partialpaid);
		} 
		
		// 没有支付
		else { 
			order.setOrderStatus(OrderStatus.unconfirmed);
			order.setPaymentStatus(PayStatus.unpaid);
		}
		
		// 先付款的不需要审核
		if (paymentMethod.getType() == Type.BEFORE_PAYED) {
			order.setOrderStatus(OrderStatus.confirmed);
		}
		
		// 设置过期时间
		if (paymentMethod.getTimeout() != null && order.getPaymentStatus() == PayStatus.unpaid) {
			order.setExpire(DateUtils.addMinutes(now, paymentMethod.getTimeout()));
		} else {
			order.setExpire(null);
		}
		
		// 转为具体的商品
		ComplexUtils.products(order);
		
		// 设置限购记录（是否异步实现） --- 先同步实现
		List<OrderItem> items = order.getItems();
		for(OrderItem item: items) {
			productService.addGoodsLimit(item.getGoodsId(), order.getCreateId(), item.getQuantity());
		}
		
		// 设置货运状态 -- 未发货
		order.setType((byte)1);
		order.setShippingStatus(ShippingStatus.unshipped);
		order.setFlowState(order.initStatus()); // 保存实时状态
		this.insert(order);
		this.rankService.book(order); // 包邮的和这个在一起， 因为是会员触发的包邮
		this.couponService.useCode(order);
		this.itemService.save(order);
		this.promotionService.save(order);
		this.cartService.book(order);
		this.orankService.save(order);
		this.orderLogService.newOrder(order);
		
		// 下单事件
		eventService.book(order);
		stateService.book(order);
		
		//直接确认订单(不需要付款的)
		if (BigDecimalUtil.equalZERO(order.getAnountPayAble())) {
			this.confirm(order, null);
		}
	}
	
	/**
	 * 收款 -- 回调
	 */
	@Override
	@Transactional
	public boolean autoPayment(Payment payment) {
		Order temp = new Order(); temp.setId(payment.getOrderId()); this.lock(temp);
		Order order = this.get(payment.getOrderId());
		order.setAmountPaid(payment.getAmount());
		
		// 付款的时候就扣库存
		if (!order.isAllocatedStock()) {
			List<OrderItem> items = this.itemService.queryItemsByOrderId(order.getId());
			for(OrderItem item: items) {
				Product product = this.productService.lockStoreProduct(item.getProductId());
				product.setFreezeStore(product.getFreezeStore() + item.getQuantity() - item.getShippedQuantity());
				this.productService.freeStroe(product);
			}
			order.setAllocatedStock(Boolean.TRUE);
			this.update("updateAllocatedStock", order);
		}
		
		// 不需要再支付
		if (BigDecimalUtil.equalZERO(order.getAnountPayAble())) { 
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PayStatus.paid);
			order.setExpire(null);
		} 
		
		// 还有需要支付的
		else if(BigDecimalUtil.biggerThenZERO(order.getAnountPayAble()) && BigDecimalUtil.biggerThenZERO(order.getAmountPaid())) {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PayStatus.partialpaid);
			order.setExpire(null);
		} 
		
		// 未支付
		else {
			order.setOrderStatus(OrderStatus.confirmed);
			order.setPaymentStatus(PayStatus.unpaid);
		}
		order.setFlowState(order.initStatus());
		this.update("updateAutoPayment", order);
		this.orderLogService.paymentOrder(order, null);
		
		// 支付事件
		eventService.pay(order);
		stateService.pay(order);
		
		// 保存支付记录
		payment.setCreateId(order.getCreateId());
		payment.setCreateName(order.getCreateName());
		
		// 返回操作成功
		return true;
	}
	
	/**
	 * 收款  -- 手动
	 */
	@Override
	@Transactional
	public boolean manualPayment(Payment payment, User user) {
		Order temp = new Order(); temp.setId(payment.getOrderId()); this.lock(temp);
		Order order = this.get(payment.getOrderId());
		if (order != null && order.isPayable()) {
			
			// 付款的时候就扣库存
			if (!order.isAllocatedStock()) {
				List<OrderItem> items = this.itemService.queryItemsByOrderId(order.getId());
				for(OrderItem item: items) {
					Product product = this.productService.lockStoreProduct(item.getProductId());
					product.setFreezeStore(product.getFreezeStore() + item.getQuantity() - item.getShippedQuantity());
					this.productService.freeStroe(product);
				}
				order.setAllocatedStock(Boolean.TRUE);
				this.update("updateAllocatedStock", order);
			}
			
			order.setAmountPaid(BigDecimalUtil.add(order.getAmountPaid(), payment.getAmount()));
			order.setFee(order.getFee());
			
			if (order.getPaymentMethodType() == Type.BEFORE_PAYED) {
				if (BigDecimalUtil.equalZERO(order.getAnountPayAble()) ) {
					order.setOrderStatus(OrderStatus.confirmed);
					order.setPaymentStatus(PayStatus.paid);
					order.setExpire(null);
				} else if(BigDecimalUtil.biggerThenZERO(order.getAnountPayAble()) && BigDecimalUtil.biggerThenZERO(order.getAmountPaid())) {
					order.setOrderStatus(OrderStatus.confirmed);
					order.setPaymentStatus(PayStatus.partialpaid);
					order.setExpire(null);
				} else {
					order.setOrderStatus(OrderStatus.unconfirmed);
					order.setPaymentStatus(PayStatus.unpaid);
				}
			} else if(order.getPaymentMethodType() == Type.BEFORE_DELIVER) {
				if (BigDecimalUtil.equalZERO(order.getAnountPayAble()) ) {
					order.setPaymentStatus(PayStatus.paid);
					order.setOrderStatus(OrderStatus.confirmed);
					order.setExpire(null);
				} else if(BigDecimalUtil.biggerThenZERO(order.getAnountPayAble()) && BigDecimalUtil.biggerThenZERO(order.getAmountPaid())) {
					order.setPaymentStatus(PayStatus.partialpaid);
					order.setOrderStatus(OrderStatus.confirmed);
					order.setExpire(null);
				} else {
					order.setOrderStatus(OrderStatus.confirmed);
					order.setPaymentStatus(PayStatus.unpaid);
				}
			}
			order.setFlowState(order.initStatus());
			this.updateVersion("updateManualPayment", order);
			this.orderLogService.paymentOrder(order, user);
			
			// 支付事件
			eventService.pay(order);
			stateService.pay(order);
			
			return true;
		}
		
		// 不需要支付
		return false;
	}
	
	/**
	 * 订单确认(手动确认)
	 */
	@Override
	@Transactional
	public boolean confirm(Order order, User user) {
		Order _order = this.get(order.getId());
		if (_order != null && _order.isConfirmable()) {
			_order.setOrderStatus(OrderStatus.confirmed);
			_order.setFlowState(_order.initStatus());
			_order.setExpire(null);
			this.updateVersion("updateConfirm", _order);
			this.orderLogService.confirmOrder(order, user);
			stateService.confirm(_order);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 修改类型
	 * @param orders
	 */
	@Override
	@Transactional
	public void mdtype(Order order) {
		this.update("updateType", order);
	}
	
	/**
	 * 订单改价
	 */
	@Override
	@Transactional
	public void mdamount(Order order, User user) {
		Order _order = this.get(order.getId());
		if (_order != null && _order.isMdamountable()) {
			String content = StringUtils.format("订单价格由【%s】改为【%s】", _order.getAmount().setScale(2),order.getAmount().setScale(2));
		    _order.setAmount(BigDecimalUtil.smallerThenZERO(order.getAmount())?BigDecimal.ZERO:order.getAmount());
		    this.updateVersion("updateMdamount", _order);
		    this.orderLogService.mdamountOrder(order, user, content);
		}
	}
	
	/**
	 * 订单改发票
	 */
	@Override
	@Transactional
	public void mdinvoice(Order order, User user) {
		Order _order = this.get(order.getId());
		if (_order != null) {
		    String content = StringUtils.format("订单发票抬头由【%s】改为【%s】", _order.getInvoiceTitle(), order.getInvoiceTitle());
		    _order.setInvoiceTitle(order.getInvoiceTitle());
		    _order.setInvoiceUrl(order.getInvoiceUrl());
		    this.updateVersion("updateMdinvoice", _order);
		    this.orderLogService.mdinvoiceOrder(order, user, content);
		}
	}
	
	/**
	 * 订单改发货
	 */
	@Override
	@Transactional
	public void mdshipping(Order order, User user) {
		Order _order = this.get(order.getId());
		if(_order != null && _order.isMdshippingable()) {
		   String content = StringUtils.format("%s%s%s", _order.getConsignee(), _order.getPhone(), _order.getAddress());
		   content = StringUtils.format("【%s】修改为【%s%s%s】",content,  order.getConsignee(),
			      order.getPhone(), order.getAddress());
		   _order.setConsignee(order.getConsignee());
		   _order.setPhone(order.getPhone());
		   _order.setAddress(order.getAddress());
		   _order.setArea(order.getArea());
		   this.updateVersion("updateMdshipping", _order);
		   this.orderLogService.mdshippingOrder(order, user, content);
		}
	}
	
	/**
	 * 订单发货(扣库存)
	 * @param order
	 */
	@Override
	@Transactional
	public void shipping(Order order, User user, Shipping shipping) {
		List<OrderItem> items = order.getItems();
		if (!order.isAllocatedStock()) {
			for(OrderItem item: items) {
				Product product = this.productService.lockStoreProduct(item.getProductId());
				product.setFreezeStore(product.getFreezeStore() + item.getQuantity() - item.getShippedQuantity());
				this.productService.freeStroe(product);
			}
			order.setAllocatedStock(Boolean.TRUE);
			this.update("updateAllocatedStock", order);
		}
		List<ShippingItem> sItems = shipping.getItems();
		for(ShippingItem item: sItems) {
			OrderItem oItem = order.getOrderItem(item.getItemId());
			Product product = this.productService.lockStoreProduct(oItem.getProductId());
			product.setStore(item.getQuantity());//出库
			if (order.isAllocatedStock()) {
			    product.setFreezeStore(product.getFreezeStore() - item.getQuantity());
			}
			this.productService.outStroe(product);
			this.productService.freeStroe(product);
			oItem.setShippedQuantity(oItem.getShippedQuantity() + item.getQuantity());
		}
		if(order.getShippedQuantity() >= order.getProductQuantity()) {
		   order.setShippingStatus(ShippingStatus.shipped);
		} else if(order.getShippedQuantity() > 0){
		   order.setShippingStatus(ShippingStatus.partialshipment);
		}
		order.setExpire(null);
		// 申请退款的也能发货
		if (order.getPaymentStatus() == PayStatus.refunds_request) {
			if (BigDecimalUtil.equalZERO(order.getAnountPayAble())) {
				order.setPaymentStatus(PayStatus.paid);
			} else {
				order.setPaymentStatus(PayStatus.partialpaid);
			}
			// 取消申请退款
			this.update("updateApplyRefund", order);
		}
		order.setFlowState(order.initStatus());
		this.update("updateShipping", order);
		this.itemService.updateShipping(items);
		this.orderLogService.shippingOrder(order, user, StringUtils.format("订单发货，物流公司：%s，物流单据号：%s", shipping.getDeliveryCorp(), shipping.getTrackingNo()));
		
		// 订单发货
		eventService.shipping(order);
		stateService.shipping(order, shipping);
	}
	
	/**
	 * 取消发货（库存还原） ---  没用到
	 * @return
	 */
	@Override
	@Transactional
	public void unshipping(Order order, User user, Shipping shipping) {
		List<OrderItem> items = order.getItems();
		
		// 库存相关
		List<ShippingItem> sItems = shipping.getItems();
		for(ShippingItem item: sItems) {
			OrderItem oItem = order.getOrderItem(item.getItemId());
			Product product = this.productService.lockStoreProduct(oItem.getProductId());
			product.setStore(item.getQuantity());
			if (order.isAllocatedStock()) {
			    product.setFreezeStore(product.getFreezeStore() - oItem.getQuantity() + item.getQuantity()); // 这部分为冻结的库存
			}
			this.productService.unOutStroe(product);
			this.productService.freeStroe(product);
			oItem.setShippedQuantity(oItem.getShippedQuantity() - item.getQuantity());
		}
		
		// 标记为未扣库存
		if (order.isAllocatedStock()) {
			order.setAllocatedStock(Boolean.FALSE);
		}
		
		// 标记为未发货
		order.setShippingStatus(ShippingStatus.unshipped);
		order.setFlowState(order.initStatus());
		this.update("updateUnShipping", order);
		this.itemService.updateShipping(items);
		this.orderLogService.unShippingOrder(order, user, "订单取消发货");
		
		// 订单取消发货（暂时不处理取消发货）
		eventService.unshipping(order);
		stateService.unshipping(order);
	}
	
	/**
	 * 订单收货
	 * @param order
	 */
	@Override
	@Transactional
	public Boolean receipt(Order order, User user) {
		Order _order = this.get(order.getId());
		if (_order != null && _order.isReceiptable()) {
			_order.setShippingStatus(ShippingStatus.receipted);
			_order.setFlowState(_order.initStatus());
		    this.update("updateShipping", _order);
		    this.orderLogService.receiptOrder(_order, user);
		    this.orderAppraise.addAppraiseTask(_order);
		    this.eventService.receipt(_order);
		    this.stateService.receipt(_order);
		    return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 订单申请退货
	 * @param order
	 */
	@Override
	@Transactional
	public Boolean applyReturns(Order order) {
		Order _order = this.get(order.getId());
		if (_order != null && _order.isApplyReturn()) {
			_order.setShippingStatus(ShippingStatus.return_request);
		    _order.setType((byte)3);
		    _order.setFlowState(_order.initStatus());
		    this.mdtype(_order);
		    this.update("updateShipping", _order);
		    this.orderLogService.applyReturnsOrder(_order);
		    eventService.applyReturns(_order);
		    stateService.applyReturns(_order);
		    return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 退货
	 */
	@Override
	@Transactional
	public void returns(Order order, User user, Returns returns) {
		List<ReturnItem> items = returns.getItems();
		for(ReturnItem item: items) {
			OrderItem _item = order.getOrderItem(item.getItemId());
			_item.setReturnQuantity(_item.getReturnQuantity() + item.getQuantity());
		}
		if(order.getReturnQuantity() >= order.getShippedQuantity()) {
		   order.setShippingStatus(ShippingStatus.returned);
		} else if(order.getReturnQuantity() > 0) {
		   order.setShippingStatus(ShippingStatus.partialreturns);
		}
		order.setFlowState(order.initStatus());
		this.update("updateShipping", order);
		this.itemService.updateReturn(order.getItems());
		this.orderLogService.returnsOrder(order, user, StringUtils.format("订单退货，物流公司：%s，物流单据号：%s", returns.getDeliveryCorp(), returns.getTrackingNo()));
		// 退货事件
		eventService.returns(order);
		stateService.returns(order);
	}
	
	/**
	 * 订单申请退款(修改为维权订单)
	 * @param order
	 */
	@Override
	@Transactional
	public Boolean applyRefund(Order order) {
		Order _order = this.get(order.getId());
		if (_order != null && _order.isApplyRefund()) {
			_order.setPaymentStatus(PayStatus.refunds_request);
		    _order.setType((byte)3);
		    _order.setFlowState(_order.initStatus());
		    this.mdtype(_order);
		    this.update("updateApplyRefund", _order);
		    this.orderLogService.applyRefundOrder(_order);
		    eventService.applyRefund(_order);
		    stateService.applyRefund(_order);
		    return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 退款（退款提交申请后，有个处理过程）
	 */
	@Override
	@Transactional
	public void refundsProcess(Order order, User user, Refunds refunds) {
		// 设置为退款处理
		order.setPaymentStatus(PayStatus.refunds_process);
		order.setFlowState(order.initStatus());
		order.setExpire(null);
		this.update("updateRefundsProcess", order);
		this.orderLogService.processRefoundsOrder(order, user, StringUtils.format("订单退款处理中，退款账户：%s，金额：%s", refunds.getAccount(), refunds.getAmount()));
		// 退款事件
		eventService.refundsProcess(order);
		stateService.refundsProcess(order);
	}
	
	/**
	 * 退款
	 */
	@Override
	@Transactional
	public void refunds(Order order, User user, Refunds refunds) {
		order.setAmountPaid(BigDecimalUtil.sub(order.getAmountPaid(), refunds.getAmount()));
		order.setExpire(null);
		if(BigDecimalUtil.biggerThenZERO(order.getAmountPaid())) {
		   order.setPaymentStatus(PayStatus.partialrefunds);
		} else {
		   order.setPaymentStatus(PayStatus.refunded);
		}
		order.setFlowState(order.initStatus());
		this.update("updateRefunds", order);
		this.orderLogService.refoundsOrder(order, user, StringUtils.format("订单退款，退款账户：%s，金额：%s", refunds.getAccount(), refunds.getAmount()));
		// 退款事件
		eventService.refunds(order);
		stateService.refunds(order);
	}
	
	/**
	 * 完成（返回订单锁定的库存）
	 * -- 退货的不能这样入库
	 */
	@Override
	@Transactional
	public Boolean complete(Order order, User user) {
		this.lock(order);
		if (order != null && order.isCompleteable()) {
			order = this.getWithOrderItems(order.getId());
			// 解冻未发货的库存
			if (order.isAllocatedStock()) {
				List<OrderItem> items = order.getItems();
				for(OrderItem item: items) {
					Product product = this.productService.lockStoreProduct(item.getProductId());
					product.setFreezeStore(product.getFreezeStore() - item.getQuantity() + item.getShippedQuantity());
					this.productService.freeStroe(product);
				}
				order.setAllocatedStock(Boolean.FALSE);
			}
			order.setOrderStatus(OrderStatus.completed);
			order.setExpire(null);
			// 统一将发货状态置为签收
			if (order.getShippingStatus() == ShippingStatus.return_request) {
				order.setShippingStatus(ShippingStatus.receipted);
			}
			order.setFlowState(order.initStatus());
			this.update("updateComplete", order);
			this.orderAppraise.addAppraiseTask(order);
			this.stateService.complete(order);
			this.ordersService.complete(order);
			this.rankService.complete(order);
			this.promotionService.complete(order);
			this.orderLogService.completeOrder(order, user);
			
			// 返回处理成功
			return Boolean.TRUE;
		}
		
		// 返回处理失败
		return Boolean.FALSE;
	}
	
	/**
	 * 取消（返回订单锁定的库存）
	 * -- 退货的不能这样入库
	 */
	@Override
	@Transactional
	public Boolean cancel(Order order, User user) {
		this.lock(order);
		if (order != null && order.isCancelable()) {
			//入库存(下订单就分配库存的情况)
			if (order.isAllocatedStock()) {
				order = this.getWithOrderItems(order.getId());
				List<OrderItem> items = order.getItems();
				for(OrderItem item: items) {
					Product product = this.productService.lockStoreProduct(item.getProductId());
					product.setStore(item.getQuantity() - item.getShippedQuantity() + item.getReturnQuantity());//入库
					product.setFreezeStore(product.getFreezeStore() - item.getQuantity() + item.getShippedQuantity());
					this.productService.freeStroe(product);
					this.productService.inStroe(product);
					
					// 如果是限购商品则扣减购买数
					this.productService.reduceGoodsLimit(product.getGoodsId(), order.getCreateId(), item.getQuantity());
				}
				order.setAllocatedStock(Boolean.FALSE);
			} 
			// 无分配库存(但需要扣减购买数 -- 对于限购商品)
			else {
				order = this.getWithOrderItems(order.getId());
				List<OrderItem> items = order.getItems();
				for(OrderItem item: items) {
					Product product = this.productService.get(item.getProductId());
					// 如果是限购商品则扣减购买数
					this.productService.reduceGoodsLimit(product.getGoodsId(), order.getCreateId(), item.getQuantity());
				}
			}
			order.setOrderStatus(OrderStatus.cancelled);
			order.setExpire(null);
			order.setFlowState(order.initStatus());
			this.update("updateCancel", order);
			this.couponService.unuseCode(order);
			this.orderLogService.cancelOrder(order, user);
			this.stateService.cancel(order);
			this.rankService.unUseShipping(order);
			
			// 返回处理成功
			return Boolean.TRUE;
		}
		
		// 返回处理失败
		return Boolean.FALSE;
	}
	
	/**
	 * 锁定此订单
	 * @param order
	 */
	@Override
	@Transactional
	public void lock(Order order) {
		this.orderDao.lock(order);
	}
	
	/**
	 * 修改特殊处理
	 * @param order
	 */
	@Override
	@Transactional
	public void updateSpecial(Order order, User user) {
		this.update("updateSpecial", order);
		this.orderLogService.specialOrder(order, user, order.getSpecial());
	}
	
	/**
	 * 校验库存
	 * @param order
	 * @return
	 */
	@Override
	@Transactional
	public OrderCheckResult checkProduct(Order order, User user) {
		this.initCheckHandler();
		CheckWrap check = new CheckWrap();
		check.setOrder(order); check.setUser(user);
		OrderCheckResult result = this.checkHandler.doHandler(check);
		return result == null ? OrderCheckResult.success() : result;
	}
	
	/**
	 * 没有
	 */
	@Override
	public Order getWithOrderItems(Long id) {
		Order order = this.get(id);
		if(order != null) {
		   List<OrderItem> items = this.itemService.queryItemsByOrderId(id);
		   order.setItems(items);
		}
		return order;
	}
	@Override
	public Order getWithRefunds(Long id) {
		Order order = this.get(id);
		if( order != null) {
			List<Refunds> items = this.refundsService.queryRefundsByOrderId(id);
			order.setRefunds(items);
		}
		return order;
	}
	
	/**
	 * 保存快照
	 */
	@Override
	@Transactional
	public void updateSnapshot(List<OrderItem> items, List<Product> products) {
		this.itemService.updateSnapshot(items);
		this.productService.updateSnapshot(products);
	}

	/**
	 * 查询用户订单
	 */
	@Override
	public Page queryUserOrdersForPage(QueryCondition qc, PageParameters param) {
		Page page = this.queryForPageList("queryUserOrdersForPage", qc, param);
		List<Order> orders = page.getData();
		for(Order order: orders) {
			List<OrderItem> items = this.itemService.querySimpleItemsByOrderId(order.getId());
			order.setItems(items);
		}
		return page;
	}
	
	/**
	 * 订单类型
	 * @param orderId
	 * @return
	 */
	@Override
	public Byte getType(Long orderId) {
		return this.queryForAttr("getType", orderId);
	}
	
	/**
	 * 折扣详情
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> discounts(Long orderId) {
		List<OrderPromotion> promotions = this.promotionService.queryOrderPromotions(orderId);
		List<OrderCoupon> coupons = couponService.queryOrderCoupons(orderId);
		
		List<Map<String,Object>> discounts = Lists.newArrayList();
		
		// 促销
		for(OrderPromotion promotion: promotions) {
			Map<String,Object> discount = Maps.newHashMap();
			discount.put("type", 1);
			discount.put("name", promotion.getPromotionName());
			discount.put("typeName", promotion.getTypeName());
			discount.put("reduce", promotion.getReduce() == null ? BigDecimal.ZERO: promotion.getReduce());
			discount.put("mzs", promotion.getPromotionType() == 8);
			discounts.add(discount);
		}
		
		// 优惠券
		for(OrderCoupon coupon: coupons) {
			Map<String,Object> discount = Maps.newHashMap();
			discount.put("type", 2);
			discount.put("name", coupon.getCouponName());
			discount.put("val", coupon.getVal());
			discounts.add(discount);
		}
		
		// 会员折扣
		OrderRank rank = this.orankService.get(orderId);
		if (rank != null) {
			Map<String,Object> discount = Maps.newHashMap();
			discount.put("type", 3);
			discount.put("name", rank.getRanksAnme());
			discount.put("discount", rank.getDiscount() * 10);
			discount.put("val", rank.getReduce() == null ? BigDecimal.ZERO: rank.getReduce());
			discounts.add(discount);
		}
		
		// 包邮
		OrderPostage postage = this.postageService.get(orderId);
		if (postage != null) {
			Map<String,Object> discount = Maps.newHashMap();
			discount.put("type", 4);
			discount.put("name", postage.getPostagesAnme());
			discount.put("val", postage.getReduce() == null ? BigDecimal.ZERO: postage.getReduce());
			discounts.add(discount);
		}
		return discounts;
	}
	
	/**
	 * 是否有可用的优惠
	 * @param id
	 * @return
	 */
	public int hasOrderGiveAbleMzsPromotion(Long id) {
		return this.promotionService.hasOrderGiveAbleMzsPromotion(id);
	}
	
	/**
	 * 获取订单的满赠送 -- 每次处理一个
	 * @return
	 */
	public OrderPromotion getOrderGiveAbleMzsPromotion(Long id) {
		return this.promotionService.getOrderGiveAbleMzsPromotion(id);
	}
	
	/**
	 * 查询订单已处理的分享型促销
	 * @param id
	 * @return
	 */
	public List<OrderPromotion> queryOrderGivedFissionPromotion(Long id) {
		return this.promotionService.queryOrderGivedFissionPromotion(id);
	}
	
	/**
	 * 已发放促销
	 */
	@Transactional
	public void giveMzsPromotion(OrderPromotion op) {
	    this.promotionService.updateGiveMzsPromotion(op);
	}
	
	/**
	 * 拆分之后的更新操作 
	 */
	@Transactional
	public void split_update(Order master) {
		master.setOrderDesc(master.getProductName());
		this.update("updateOrderDesc", master);
		this.itemService.update(master);
	}
	
	/**
	 * 拆分之后的更新操作 
	 */
	@Transactional
	public void split_insert(Order order) {
		this.insert(order);
		this.itemService.save(order);
	}

	/**
	 * 最近的订单
	 */
	@Override
	public Long getNewestOrder(Long userId) {
		return this.queryForAttr("getNewestOrder", userId);
	}
}