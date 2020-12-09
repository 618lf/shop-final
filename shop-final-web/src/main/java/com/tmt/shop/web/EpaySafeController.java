package com.tmt.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.EpaySafe;
import com.tmt.shop.service.EpaySafeServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 企业支付安全设置 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
@Controller("shopEpaySafeController")
@RequestMapping(value = "${adminPath}/shop/epaySafe")
public class EpaySafeController extends BaseController{
	
	@Autowired
	private EpaySafeServiceFacade epaySafeService;
	
	/**
	 * 表单,只需要一个设置
	 * @param epaySafe
	 * @param model
	 */
	@RequestMapping("form")
	public String form(EpaySafe epaySafe, Model model) {
		epaySafe = this.epaySafeService.get(IdGen.ROOT_ID);
		if (epaySafe == null) {
			epaySafe = new EpaySafe();
			epaySafe.setId(IdGen.ROOT_ID);
		}
		model.addAttribute("epaySafe", epaySafe);
		return "/shop/EpaySafeForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(EpaySafe epaySafe, Model model, RedirectAttributes redirectAttributes) {
		epaySafe.userOptions(UserUtils.getUser());
		this.epaySafeService.save(epaySafe);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改企业支付安全设置", epaySafe.getManager(), "成功"));
		redirectAttributes.addAttribute("id", epaySafe.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/epaySafe/form");
	}
}
