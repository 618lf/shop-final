package com.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sample.dao.OrderDao;

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
	 * 使用声明式事务
	 */
	public void saveOrder() {
		orderDao.get(0L);
	}
}