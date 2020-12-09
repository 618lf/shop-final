package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.ReturnItemDao;
import com.tmt.shop.dao.ReturnsDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.ReturnItem;
import com.tmt.shop.entity.Returns;
import com.tmt.shop.utils.SnUtils;
import com.tmt.system.entity.User;

/**
 * 退货管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopReturnsService")
public class ReturnsService extends BaseService<Returns,Long> implements ReturnsServiceFacade{
	
	@Autowired
	private ReturnsDao returnsDao;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ReturnItemDao returnItemDao;
	
	@Override
	protected BaseDaoImpl<Returns, Long> getBaseDao() {
		return returnsDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Order order, User user, Returns returns) {
		Order _order = this.orderService.getWithOrderItems(order.getId());
		if(_order != null && _order.isReturnable()) {
			this.orderService.lock(order);
			returns.setSn(SnUtils.createReturnsSn());
			this.insert(returns);
			List<ReturnItem> items = returns.getItems();
			for(ReturnItem item: items) {
				item.setReturnsId(returns.getId());
			}
			this.returnItemDao.batchInsert(items);
			this.orderService.returns(order, user, returns);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Returns> returnss) {
		this.batchDelete(returnss);
	}
	
	/**
	 * 查询订单相关的退货记录
	 * @param orderId
	 * @return
	 */
	public List<Returns> queryReturnsByOrderId(Long orderId) {
		return this.queryForList("queryReturnsByOrderId", orderId);
	}
	
	/**
	 * 并查询明细
	 * @param id
	 * @return
	 */
	public Returns getWithItems(Long id) {
		Returns returns = this.get(id);
		if(returns != null) {
			List<ReturnItem> items = this.returnItemDao.queryItemsByReturnsId(id);
			returns.setItems(items);
		}
		return returns;
	}
}
