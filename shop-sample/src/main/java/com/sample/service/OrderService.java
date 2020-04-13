package com.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sample.dao.OrderDao;
import com.tmt.core.persistence.DS;

/**
 * 订单服务 -- 测试多数据源
 * 
 * @author lifeng
 */
@Service
public class OrderService {

	/**
	 * 测试多数据源
	 */
	@Autowired(required = false)
	private OrderDao orderDao;

	/**
	 * 使用声明式事务， 通过DS来设置数据源
	 */
	@DS
	@Transactional
	public void saveOrder() {
		orderDao.get(0L);
	}
}