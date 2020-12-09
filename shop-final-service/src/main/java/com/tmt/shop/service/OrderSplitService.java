package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.OrderSplitDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderSplit;

/**
 * 订单拆分 管理
 * @author 超级管理员
 * @date 2017-03-12
 */
@Service("shopOrderSplitService")
public class OrderSplitService extends BaseService<OrderSplit,Long> implements OrderSplitServiceFacade{
	
	@Autowired
	private OrderSplitDao orderSplitDao;
	@Autowired
	private OrderService orderService;
	
	@Override
	protected BaseDao<OrderSplit, Long> getBaseDao() {
		return orderSplitDao;
	}
	
	/**
	 * 得到订单的拆分次数
	 * @param orderId
	 * @return
	 */
	public int getSplitCount(Long orderId) {
		int count = this.countByCondition("getSplitCount", orderId);
		return count;
	}
	
	/**
	 * 订单拆分
	 * @param master
	 * @param orders
	 */
	@Transactional
	public Boolean split(Order master, List<Order> orders) {
		
		// 只有一个订单则不用拆分
		if (master == null || !master.isShippingable()
				|| orders.size() <= 1){
			return Boolean.FALSE;
		}
		
		// 逻辑主单
		String master_sn = master.getSn();
		
		// 多次拆分
		int count = this.getSplitCount(master.getId());
		if (count == 0) {
			OrderSplit split = this.get(master.getId());
			if (split != null) {
				master_sn = this.orderService.get(split.getOrders()).getSn();
				count = this.countByCondition("getSplitCount", split.getOrders());
			}
		}
		
		// 开始的值
		int index = count + 1;
		
		// 构建数据
		for(int i = 0; i< orders.size(); i++) {
			Order order = orders.get(i);
			if (i == 0) {
				master.setItems(order.getItems());
				this.orderService.split_update(master);
			} else {
				String sn = new StringBuilder(master_sn).append("_").append(index).toString();
				order.setSn(sn); index ++;
				this.orderService.split_insert(order);
				
				// 保存拆分关系
				OrderSplit split = new OrderSplit();
				split.setOrders(master.getId());
				split.setChild(order.getId());
				this.insert(split);
			}
		}
		
		return Boolean.TRUE;
	}
}