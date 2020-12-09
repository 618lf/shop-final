package com.tmt.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.shop.entity.OrderAppraise;
import com.tmt.shop.service.OrderAppraiseServiceFacade;

/**
 * 订单管理 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
@Controller("shopOrderAppraiseController")
@RequestMapping(value = "${adminPath}/shop/order/appraise")
public class OrderAppraiseController {

	@Autowired
	private OrderAppraiseServiceFacade orderAppraiseService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(OrderAppraise orderAppraise, Model model){
		return "/shop/OrderAppraiseList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param productAppraise
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(OrderAppraise orderAppraise, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(orderAppraise, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		return orderAppraiseService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param invoice
	 * @param model
	 */
	@RequestMapping("form")
	public String form(OrderAppraise orderAppraise, Model model) {
		orderAppraise = this.orderAppraiseService.get(orderAppraise.getId());
		model.addAttribute("appraise", orderAppraise);
		return "/shop/OrderAppraiseForm";
	}
	
	//查询条件
	private void initQc(OrderAppraise orderAppraise, Criteria c) {
        if(StringUtils.isNotBlank(orderAppraise.getSn())) {
           c.andEqualTo("SN", orderAppraise.getSn());
        }
        if(StringUtils.isNotBlank(orderAppraise.getCreateName())) {
           c.andEqualTo("CREATE_NAME", orderAppraise.getCreateName());
        }
        if(orderAppraise.getState() != null) {
           c.andEqualTo("STATE", orderAppraise.getState());
        }
	}
}
