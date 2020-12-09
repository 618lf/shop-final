package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.shop.dao.ShippingDao;
import com.tmt.shop.dao.ShippingItemDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Shipping;
import com.tmt.shop.entity.ShippingItem;
import com.tmt.shop.utils.SnUtils;
import com.tmt.system.entity.User;

/**
 * 发货管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopShippingService")
public class ShippingService extends BaseService<Shipping,Long> implements ShippingServiceFacade{
	
	@Autowired
	private OrderService orderService;
	@Autowired
	private ShippingDao shippingDao;
	@Autowired
	private ShippingItemDao shippingItemDao;
	
	@Override
	protected BaseDaoImpl<Shipping, Long> getBaseDao() {
		return shippingDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Shipping shipping) {
		this.update("updateSimple", shipping);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void shipping(Order order, User user, Shipping shipping) {
		Order _order = this.orderService.getWithOrderItems(order.getId());
		if (_order != null && _order.isShippingable()) {
			this.orderService.lock(_order);
			shipping.setSn(SnUtils.createShippingSn());
			this.insert(shipping);
			List<ShippingItem> items = shipping.getItems();
			for(ShippingItem item: items) {
				item.setShippingId(shipping.getId()); 
			}
			this.shippingItemDao.batchInsert(items);
			this.orderService.shipping(order, user, shipping); 
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Shipping> shippings) {
		List<ShippingItem> all = Lists.newArrayList();
		// 删除明细
		for(Shipping shipping: shippings) {
			List<ShippingItem> items = this.shippingItemDao.queryItemsByShippingId(shipping.getId());
			all.addAll(items);
		}
		this.batchDelete(shippings);
		this.shippingItemDao.batchDelete(all);
	}
	
	/**
	 * 并查询明细
	 * @param id
	 * @return
	 */
	public Shipping getWithItems(Long id) {
		Shipping shipping = this.get(id);
		if(shipping != null) {
			List<ShippingItem> items = this.shippingItemDao.queryItemsByShippingId(id);
			shipping.setItems(items);
		}
		return shipping;
	}
	
	/**
	 * 查询订单的发货信息
	 * @param orderId
	 * @return
	 */
	public List<Shipping> queryShippingsByOrderId(Long orderId) {
		return this.queryForList("queryShippingsByOrderId", orderId);
	}
}