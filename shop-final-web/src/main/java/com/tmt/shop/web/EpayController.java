package com.tmt.shop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.service.EpayServiceFacade;

/**
 * 支付账户 管理
 * @author 超级管理员
 * @date 2015-09-23
 */
@Controller("shopEpayController")
@RequestMapping(value = "${adminPath}/shop/epay")
public class EpayController extends BaseController{
	
	@Autowired
	private EpayServiceFacade epayService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("")
	public String list(Epay epay, Model model){
		List<Epay> epays = epayService.getAll();
		model.addAttribute("epays", epays);
		return "/shop/EpayList";
	}
	
	/**
	 * 表单
	 * @param epay
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Epay epay, Model model) {
	    if (epay != null && !IdGen.isInvalidId(epay.getId())) {
		    epay = this.epayService.get(epay.getId());
		}
	    
	    // 设置默认值
	    if (epay != null && IdGen.isInvalidId(epay.getId())) {
	    	if (epay.getType() == 1) {
	    		epay.setName("微信支付");
	    		epay.setIcon("/static/img/pay/logo_weixin.jpg");
	    		epay.setWebsite("http://pay.weixin.qq.com");
	    	} else if (epay.getType() == 2) {
	    		epay.setName("财付通");
	    		epay.setIcon("/static/img/pay/logo_tenpay.jpg");
	    		epay.setWebsite("http://mch.tenpay.com");
	    	} else if (epay.getType() == 3) {
	    		epay.setName("支付宝");
	    		epay.setIcon("/static/img/pay/logo_alipay.jpg");
	    		epay.setWebsite("http://b.alipay.com");
	    	}
	    }
	    
		model.addAttribute("epay", epay);
		return "/shop/EpayForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Epay epay, Model model, RedirectAttributes redirectAttributes) {
		this.epayService.save(epay);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改支付插件", epay.getName(), "成功"));
		redirectAttributes.addAttribute("id", epay.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/epay/form");
	}
}
