package com.tmt.shop.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Epayment;
import com.tmt.shop.service.EpaymentServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 企业支付 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
@Controller("shopEpaymentController")
@RequestMapping(value = "${adminPath}/shop/epayment")
public class EpaymentController extends BaseController{
	
	@Autowired
	private EpaymentServiceFacade epaymentService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Epayment epayment, Model model){
		return "/shop/EpaymentList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param epayment
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Epayment epayment, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(epayment, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		return epaymentService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param epayment
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Epayment epayment, Model model) {
	    if(epayment != null && !IdGen.isInvalidId(epayment.getId())) {
			epayment = this.epaymentService.get(epayment.getId());
		} else {
		   if( epayment == null) {
			   epayment = new Epayment();
		   }
		   epayment.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("epayment", epayment);
		return "/shop/EpaymentForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Epayment epayment, Model model, RedirectAttributes redirectAttributes) {
		epayment.userOptions(UserUtils.getUser());
		this.epaymentService.save(epayment);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改企业支付", epayment.getEpayId(), "成功"));
		redirectAttributes.addAttribute("id", epayment.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/epayment/form");
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
		List<Epayment> epayments = Lists.newArrayList();
		for(Long id: idList) {
			Epayment epayment = new Epayment();
			epayment.setId(id);
			epayments.add(epayment);
		}
		this.epaymentService.delete(epayments);
		return AjaxResult.success();
	}
	
	/**
	 * 支付
	 * @param epayment
	 * @param model
	 */
	@RequestMapping("pay")
	public String pay(Model model) {
		model.addAttribute("epayment", new Epayment());
		return "/shop/EpaymentPay";
	}

	//查询条件
	private void initQc(Epayment epayment, Criteria c) {
        if(StringUtils.isNotBlank(epayment.getUserName())) {
           c.andEqualTo("USER_NAME", epayment.getUserName());
        }
        if(StringUtils.isNotBlank(epayment.getUserNo())) {
           c.andEqualTo("USER_NO", epayment.getUserNo());
        }
        if(epayment.getPayResult() != null) {
           c.andEqualTo("PAY_RESULT", epayment.getPayResult());
        }
	}
}
