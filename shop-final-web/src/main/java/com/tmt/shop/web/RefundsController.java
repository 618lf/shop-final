package com.tmt.shop.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Refunds;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.RefundsServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 退款管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopRefundsController")
@RequestMapping(value = "${adminPath}/shop/refunds")
public class RefundsController extends BaseController{
	
	@Autowired
	private RefundsServiceFacade refundsService;
	@Autowired
	private OrderServiceFacade orderService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Refunds refunds, Model model){
		return "/shop/RefundsList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param refunds
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Refunds refunds, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(refunds, c);
		return refundsService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param refunds
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Refunds refunds, Model model) {
	    if(refunds != null && !IdGen.isInvalidId(refunds.getId())) {
			refunds = this.refundsService.get(refunds.getId());
		} else {
		   if( refunds == null) {
			   refunds = new Refunds();
		   }
		   refunds.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("refunds", refunds);
		return "/shop/RefundsForm";
	}
	
	/**
	 * 订单-退款
	 * @param payment
	 * @param model
	 */
	@RequestMapping("order")
	public String order(Order order, Model model) {
		order = orderService.get(order.getId());
		model.addAttribute("order", order);
		model.addAttribute("refunds", refundsService.prepareRefunds(order));
		return "/shop/RefundsMinForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxResult save(Refunds refunds) {
		if (!UserUtils.isPermitted("admin:order:refunds")) {
			return AjaxResult.error("没有退款的权限");
		}
		try {
			refunds.userOptions(UserUtils.getUser());
			this.refundsService.save(refunds, UserUtils.getUser());
			return AjaxResult.success();
		} catch (Exception e) {
			return AjaxResult.error(e.getMessage());
		}
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model, HttpServletResponse response) {
		List<Refunds> refundss = Lists.newArrayList();
		for(Long id: idList) {
			Refunds refunds = new Refunds();
			refunds.setId(id);
			refundss.add(refunds);
		}
		this.refundsService.delete(refundss);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(Refunds refunds, Criteria c) {
        if(StringUtils.isNotBlank(refunds.getOrderSn())) {
           c.andEqualTo("ORDER_SN",refunds.getOrderSn());
        }
        if(StringUtils.isNotBlank(refunds.getPayee())) {
           c.andEqualTo("PAYEE", refunds.getPayee());
        }
	}
}