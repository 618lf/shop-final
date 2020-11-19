package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.check.OrderCheckResult;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.OrderPromotion;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Refunds;
import com.tmt.shop.entity.Returns;
import com.tmt.shop.entity.Shipping;
import com.tmt.system.entity.User;

/**
 * 订单 管理
 * @author 超级管理员
 * @date 2015-09-17
 */
public interface OrderServiceFacade extends BaseServiceFacade<Order,Long> {
	
	/**
	 * 得到订单ID
	 * @param sn
	 * @return
	 */
	public Long getIdBySn(String sn);
	
	/**
	 * 删除
	 */
	public void delete(List<Order> orders);
	
	/**
	 * 修改类型
	 * @param orders
	 */
	public void mdtype(Order order);
	
	/**
	 * 下定单
	 * @param order
	 */
	public void book(Order order);
	
	/**
	 * 收款 -- 回调
	 */
	public boolean autoPayment(Payment payment);
	
	/**
	 * 收款  -- 手动
	 */
	public boolean manualPayment(Payment payment, User user);
	
	/**
	 * 订单确认
	 */
	public boolean confirm(Order order, User user);
	
	/**
	 * 订单改价
	 */
	public void mdamount(Order order, User user);
	
	/**
	 * 订单改发票
	 */
	public void mdinvoice(Order order, User user);
	
	/**
	 * 订单改发货
	 */
	public void mdshipping(Order order, User user);
	
	/**
	 * 订单发货(扣库存)
	 * @param order
	 */
	public void shipping(Order order, User user, Shipping shipping);
	
	/**
	 * 取消发货(库存)
	 * @param order
	 */
	public void unshipping(Order order, User user, Shipping shipping);
	
	/**
	 * 订单收货
	 * @param order
	 */
	public Boolean receipt(Order order, User user);
	
	/**
	 * 申请退货
	 * @param order
	 * @return
	 */
	public Boolean applyReturns(Order order);
	
	/**
	 * 退货
	 */
	public void returns(Order order, User user, Returns returns);
	
	/**
	 * 申请退款
	 * @param order
	 * @return
	 */
	public Boolean applyRefund(Order order);
	
	/**
	 * 退款处理
	 */
	public void refundsProcess(Order order, User user, Refunds refunds);
	
	/**
	 * 退款
	 */
	public void refunds(Order order, User user, Refunds refunds);
	
	/**
	 * 完成（返回订单锁定的库存）
	 * -- 退货的不能这样入库
	 */
	public Boolean complete(Order order, User user);
	
	/**
	 * 取消（返回订单锁定的库存）
	 * -- 退货的不能这样入库
	 */
	public Boolean cancel(Order order, User user);
	
	/**
	 * 锁定此订单
	 * @param order
	 */
	public void lock(Order order);
	
	/**
	 * 修改特殊处理
	 * @param order
	 */
	public void updateSpecial(Order order, User user);
	
	/**
	 * 校验商品
	 * @param order
	 * @return
	 */
	public OrderCheckResult checkProduct(Order order, User user);
	
	/**
	 * 明细
	 * @param id
	 * @return
	 */
	public Order getWithOrderItems(Long id);
	
	/**
	 * 退款信息
	 * @param id
	 * @return
	 */
	public Order getWithRefunds(Long id);
	
	/**
	 * 修改快照地址
	 * @param product
	 */
	public void updateSnapshot(List<OrderItem> items, List<Product> products);
	
	/**
	 * 查询用户订单列表（不需要返回很多数据）
	 * @return
	 */
	public Page queryUserOrdersForPage(QueryCondition qc, PageParameters param);
	
	/**
	 * 得到订单类型
	 * @param orderId
	 * @return
	 */
	public Byte getType(Long orderId);
	
	/**
	 * 折扣详情
	 * @param orderId
	 * @return
	 */
	public List<Map<String,Object>> discounts(Long orderId);
	
	/**
	 * 订单相关的满赠送
	 * @param id
	 * @return
	 */
	public OrderPromotion getOrderGiveAbleMzsPromotion(Long id);
	
	/**
	 * 是否有相关的满赠送
	 * @param id
	 * @return
	 */
	public int hasOrderGiveAbleMzsPromotion(Long id);
	
	/**
	 * 查询订单已处理的分享型促销
	 * @param id
	 * @return
	 */
	public List<OrderPromotion> queryOrderGivedFissionPromotion(Long id);
	
	/**
	 * 最后一次的下单
	 * @param userId
	 * @return
	 */
	public Long getNewestOrder(Long userId);
}