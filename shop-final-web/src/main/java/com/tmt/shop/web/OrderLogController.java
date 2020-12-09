package com.tmt.shop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderLog;
import com.tmt.shop.service.OrderLogServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;

/**
 * 订单记录 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopOrderLogController")
@RequestMapping(value = "${adminPath}/shop/order/log")
public class OrderLogController extends BaseController{
	
	@Autowired
	private OrderLogServiceFacade orderLogService;
	@Autowired
	private OrderServiceFacade orderService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("")
	public String list(Long orderId, Model model){
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("ORDER_ID", orderId);
		qc.setOrderByClause("CREATE_DATE");
		List<OrderLog> logs = orderLogService.queryByCondition(qc);
		model.addAttribute("logs", logs);
		Order order = this.orderService.get(orderId);
		model.addAttribute("order", order);
		return "/shop/OrderLogList";
	}
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("/flow")
	public String flow(Long orderId, Model model){
		Order order = this.orderService.get(orderId);
		model.addAttribute("order", order);
		return "/shop/OrderLogFlow";
	}
	
	/**
	 * JSon 格式的数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/json")
	public AjaxResult json(Long orderId) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("ORDER_ID", orderId);
		qc.setOrderByClause("CREATE_DATE");
		List<OrderLog> logs = orderLogService.queryByCondition(qc);
		return AjaxResult.success(logs);
	}
}