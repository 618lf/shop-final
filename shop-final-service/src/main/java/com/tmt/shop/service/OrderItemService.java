package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.OrderItemDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;

/**
 * 订单明细 管理
 * @author 超级管理员
 * @date 2015-09-17
 */
@Service("shopOrderItemService")
public class OrderItemService extends BaseService<OrderItem,Long> implements OrderItemServiceFacade{
	
	@Autowired
	private OrderItemDao orderItemDao;
	
	@Override
	protected BaseDaoImpl<OrderItem, Long> getBaseDao() {
		return orderItemDao;
	}
	
	/**
	 *  更新
	 * @param order
	 */
	@Transactional
	public void update(Order order) {
		List<OrderItem> items = queryItemsByOrderId(order.getId());
		this.delete(items);
		this.save(order);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Order order) {
		List<OrderItem> items = order.getItems();
		for(OrderItem item: items) {
			item.setOrderId(order.getId());
		}
		this.batchInsert(items);
	}
	
	/**
	 * 查询订单的订单明细
	 * @param orderId
	 * @return
	 */
	public List<OrderItem> queryItemsByOrderId(Long orderId) {
		return this.queryForList("queryItemsByOrderId", orderId);
	}
	
	/**
	 * 查询订单的订单明细
	 * @param orderId
	 * @return
	 */
	public List<OrderItem> querySimpleItemsByOrderId(Long orderId) {
		return this.queryForList("querySimpleItemsByOrderId", orderId);
	}
	
	/**
	 * 删除
	 * @param items
	 */
	@Transactional
	public void delete(List<OrderItem> items) {
	   this.batchDelete(items);
	}
	
	/**
	 * 修改快照
	 * @param items
	 */
	@Transactional
	public void updateSnapshot(List<OrderItem> items) {
		this.batchUpdate("updateSnapshot", items);
	}
	
	/**
	 * 发货
	 * @param items
	 */
	@Transactional
	public void updateShipping(List<OrderItem> items) {
		this.batchUpdate("updateShipping", items);
	}
	
	/**
	 * 退货
	 * @param items
	 */
	@Transactional
	public void updateReturn(List<OrderItem> items) {
		this.batchUpdate("updateReturn", items);
	}
}