package com.sample.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sample.service.OrderService;

/**
 * 只是测试
 * 
 * @author lifeng
 */
@RestController
@RequestMapping("${spring.application.web.admin}/order")
public class OrderController {

	/**
	 * 多数据源的演示
	 */
	@Autowired(required = false)
	OrderService orderService;

	/**
	 * 获得订单
	 */
	@ResponseBody
	@RequestMapping("/get")
	public void get(String text) {
		System.out.println("中文测试：" + text);
		// orderService.saveOrder();
	}
}