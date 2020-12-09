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
import com.tmt.shop.entity.DeliveryTemplate;
import com.tmt.shop.service.DeliveryTemplateServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 快递单模板 管理
 * @author 超级管理员
 * @date 2016-02-23
 */
@Controller("shopDeliveryTemplateController")
@RequestMapping(value = "${adminPath}/shop/delivery/template")
public class DeliveryTemplateController extends BaseController{
	
	@Autowired
	private DeliveryTemplateServiceFacade deliveryTemplateService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(DeliveryTemplate deliveryTemplate, Model model){
		return "/shop/DeliveryTemplateList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param deliveryTemplate
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(DeliveryTemplate deliveryTemplate, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(deliveryTemplate, c);
		return deliveryTemplateService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param deliveryTemplate
	 * @param model
	 */
	@RequestMapping("form")
	public String form(DeliveryTemplate deliveryTemplate, Model model) {
	    if(deliveryTemplate != null && !IdGen.isInvalidId(deliveryTemplate.getId())) {
			deliveryTemplate = this.deliveryTemplateService.get(deliveryTemplate.getId());
		} else {
		   if( deliveryTemplate == null) {
			   deliveryTemplate = new DeliveryTemplate();
		   }
		   deliveryTemplate.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("template", deliveryTemplate);
		return "/shop/DeliveryTemplateForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(DeliveryTemplate deliveryTemplate, Model model, RedirectAttributes redirectAttributes) {
		deliveryTemplate.userOptions(UserUtils.getUser());
		this.deliveryTemplateService.save(deliveryTemplate);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改快递单模板", deliveryTemplate.getName(), "成功"));
		redirectAttributes.addAttribute("id", deliveryTemplate.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/delivery/template/form");
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
		List<DeliveryTemplate> deliveryTemplates = Lists.newArrayList();
		for(Long id: idList) {
			DeliveryTemplate deliveryTemplate = new DeliveryTemplate();
			deliveryTemplate.setId(id);
			deliveryTemplates.add(deliveryTemplate);
		}
		this.deliveryTemplateService.delete(deliveryTemplates);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(DeliveryTemplate deliveryTemplate, Criteria c) {
	}
	
}
