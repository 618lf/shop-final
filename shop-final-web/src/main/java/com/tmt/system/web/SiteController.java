package com.tmt.system.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.email.SendEmailUtils;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Site;
import com.tmt.system.service.SiteServiceFacade;

/**
 * 站点设置 管理
 * @author 超级管理员
 * @date 2016-01-18
 */
@Controller("systemSiteController")
@RequestMapping(value = "${spring.application.web.admin}/system/site")
public class SiteController extends BaseController{
	
	@Autowired
	private SiteServiceFacade siteService;
	
	/**
	 * 表单 -- 只有一个站点
	 * @param site
	 * @param model
	 */
	@RequestMapping("form/base")
	public String baseForm(Site site, Model model) {
		site = this.siteService.get(IdGen.ROOT_ID);
		model.addAttribute("site", site);
		return "/system/SiteBaseForm";
	}
	
	/**
	 * 表单 -- 只有一个站点
	 * @param site
	 * @param model
	 */
	@RequestMapping("form/safe")
	public String safeForm(Site site, Model model) {
		site = this.siteService.get(IdGen.ROOT_ID);
		model.addAttribute("site", site);
		return "/system/SiteSafeForm";
	}
	
	/**
	 * 表单 -- 只有一个站点
	 * @param site
	 * @param model
	 */
	@RequestMapping("form/email")
	public String emialForm(Site site, Model model) {
		site = this.siteService.get(IdGen.ROOT_ID);
		model.addAttribute("site", site);
		return "/system/SiteEmailForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save/base")
	public String baseSave(Site site, Model model, RedirectAttributes redirectAttributes) {
		this.siteService.baseSave(site);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改站点设置", site.getName(), "成功"));
		redirectAttributes.addAttribute("id", site.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/site/form/base").toString());
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save/safe")
	public String safeSave(Site site, Model model, RedirectAttributes redirectAttributes) {
		this.siteService.safeSave(site);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改站点设置", site.getName(), "成功"));
		redirectAttributes.addAttribute("id", site.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/site/form/safe").toString()); 
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save/email")
	public String emailSave(Site site, Model model, RedirectAttributes redirectAttributes) {
		this.siteService.emailSave(site);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改站点设置", site.getName(), "成功"));
		redirectAttributes.addAttribute("id", site.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/site/form/email").toString()); 
	}
	
	/**
	 * 测试一封邮件
	 * 不能包含测试的字符
	 * @return
	 */
	@ResponseBody
	@RequestMapping("email_test")
	public AjaxResult test(String to) {
		Site site = siteService.getSite();
		String subject = StringUtil3.format("%s", site.getName());
		String[] toArray = new String[]{to};
		String[] ccArray = null;
		String content = "如果您收到这封邮件，说明邮件服务是正常！";
		Boolean bFlag = SendEmailUtils.sendNotificationMail(subject, toArray, ccArray, content);
		return AjaxResult.success(bFlag);
	}
}