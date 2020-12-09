package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.LabelVO;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.dao.OrderStateDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.OrderState;
import com.tmt.shop.entity.PaymentMethod.Type;
import com.tmt.shop.entity.Shipping;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.system.entity.Todo;
import com.tmt.system.entity.User;
import com.tmt.system.service.TodoServiceFacade;

/**
 * 订单状态 管理 -- 待办任务
 * @author 超级管理员
 * @date 2016-10-06
 */
@Service("shopOrderStateService")
public class OrderStateService extends BaseService<OrderState,Long> implements OrderStateServiceFacade, TodoServiceFacade{
	
	@Autowired
	private OrderStateDao orderStateDao;
	
	@Override
	protected BaseDao<OrderState, Long> getBaseDao() {
		return orderStateDao;
	}
	
	/**
	 * 设置过期时间
	 */
	@Override
	public void expire(OrderState state) {
		this.orderStateDao.update("updateExpire", state);
	}

	/**
	 * 置为可执行
	 */
	@Transactional
	public void enabled(List<OrderState> orderStates) {
		this.orderStateDao.updateEnabled(orderStates);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(OrderState orderState) {
		this.insert(orderState);
	}
	
    /**
     * 获取前几个 (会更新状态)
     * @param qc
     * @param size   
     * @return
     */
    @Override
    @Transactional
    public List<OrderState> queryUpdateAbles(int size) {
    	return orderStateDao.queryUpdateAbles(size);
    }
	
	/**
	 * 下单(需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void book(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.BOOK);
		event.setState((byte)0);
		event.setCreateDate(order.getCreateDate());
		event.setExpire(order.getExpire());
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
    
    /**
	 * 审核 - 不需要定时任务处理
	 * @param order
	 */
    @Override
	@Transactional
	public void confirm(Order order) {
    	OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.CONFIRM);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(null);
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
	
	/**
	 * 付款(不需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void pay(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.PAY);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		if (order.getPaymentMethodType() == Type.BEFORE_DELIVER) {
			event.setExpire(DateUtils.getDateByOffset(event.getCreateDate(), ShopConstant.RECEIPT_DAYS));
		} else {
			event.setExpire(null);
		}
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
	
	/**
	 * 发货(需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void shipping(Order order, Shipping shipping) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.SHIPPING);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(DateUtils.getDateByOffset(shipping.getCreateDate(), ShopConstant.RECEIPT_DAYS));
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
    
	/**
	 * 取消发货(需要定时任务处理) -- 标记为付款
	 * @param order
	 */
    @Override
	@Transactional
	public void unshipping(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.UN_SHIPPING);
		event.setState((byte)1); // 已处理
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(null);
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
	
	/**
	 * 收货(收货之后不需要做操作)
	 * -- 待办任务(不需要定时任务处理)
	 * -- 没有这个状态
	 * -- 确认收货后 -- 依然保留发货的过期时间
	 * @param order
	 */
    @Override
	@Transactional
	public void receipt(Order order) {
    	OrderState event = this.get(order.getId());
    	if (event == null) {
    		event = new OrderState();
    		event.setCreateDate(DateUtils.getTimeStampNow());
    		event.setExpire(DateUtils.getDateByOffset(event.getCreateDate(), ShopConstant.RECEIPT_DAYS));
    	}
		event.setId(order.getId());
		event.setOpt(OrderOpts.RECEIPT);
		event.setState((byte)0);
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
    
	/**
	 * 申请 - 退货(不需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void applyReturns(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.APPLY_RETURNS);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(null);
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
    
	/**
	 * 退货(不需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void returns(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.RETURNS);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(null);
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
    
    /**
	 * 申请 - 退款(不需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void applyRefund(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.APPLY_REFUND);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(null);
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
    
	/**
	 * 退款 - 处理(需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void refundsProcess(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.REFUND_PROCESS);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(event.getCreateDate());
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}
	
	/**
	 * 退款(需要定时任务处理)
	 * @param order
	 */
    @Override
	@Transactional
	public void refunds(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		event.setOpt(OrderOpts.REFUNDS);
		event.setState((byte)0);
		event.setCreateDate(DateUtils.getTimeStampNow());
		event.setExpire(event.getCreateDate());
		event.setOstate(order.getFlowState());
		this.orderStateDao.insert(event);
	}

    /**
	 * 取消后删除状态即可
	 */
	@Override
	public void cancel(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		this.orderStateDao.delete(event);
	}

	/**
	 * 完成后删除状态即可
	 */
	@Override
	public void complete(Order order) {
		OrderState event = new OrderState();
		event.setId(order.getId());
		this.orderStateDao.delete(event);
	}
	
	/**
	 * 实时统计
	 * @return
	 */
	@Override
	public List<LabelVO> statOrders() {
		return this.queryForGenericsList("statOrders", new Object());
	}

	/**
	 * 订单待办
	 * -- 待发货
	 * -- 退货申请
	 * -- 退款
	 */
	@Override
	public List<Todo> fetchTodos(User arg0) {
		String url = new StringBuilder().append("shop/order/list").toString();
		int confirms = orderStateDao.countByCondition("countByOstate", "待审核");
		int shippings = orderStateDao.countByCondition("countByOstate", "待发货");
		int pays = orderStateDao.countByCondition("countByOstate", "待付款");
		List<Todo> todos = Lists.newArrayList();
		todos.add(Todo.important("待审核", url, confirms, 2));
		todos.add(Todo.important("待发货", url, shippings, 2));
		todos.add(Todo.important("待付款", url, pays, 3));
		return todos;
	}
}