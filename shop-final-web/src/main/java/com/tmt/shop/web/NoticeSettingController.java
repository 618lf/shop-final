package com.tmt.shop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.service.NoticeSettingServiceFacade;

/**
 * 通知设置 管理
 * @author 超级管理员
 * @date 2016-10-04
 */
@Controller("shopNoticeSettingController")
@RequestMapping(value = "${adminPath}/shop/notice/setting")
public class NoticeSettingController extends BaseController{
	
	@Autowired
	private NoticeSettingServiceFacade noticeSettingService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("")
	public String index(Model model){
		List<NoticeSetting> settings = this.noticeSettingService.getAll();
		model.addAttribute("settings", settings);
		return "/shop/NoticeSettingList";
	}
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("/config")
	public String config(Byte id, Model model){
		NoticeSetting setting = this.noticeSettingService.get(id);
		model.addAttribute("setting", setting);
		return "/shop/NoticeSettingForm";
	}
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@ResponseBody
	@RequestMapping("/config/save")
	public AjaxResult save(NoticeSetting setting){
	    this.noticeSettingService.updateTemplate(setting);
		return AjaxResult.success();
	}
	
	/**
	 * 模板消息ON
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("tmsg_on")
	public AjaxResult tmsg_on(NoticeSetting setting) {
		setting.setTemplateMsg(NoticeSetting.YES);
		this.noticeSettingService.updateTmsg(setting);
		return AjaxResult.success();
	}
	
	/**
	 * 模板消息OFF
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("tmsg_off")
	public AjaxResult tmsg_off(NoticeSetting setting) {
		setting.setTemplateMsg(NoticeSetting.NO);
		this.noticeSettingService.updateTmsg(setting);
		return AjaxResult.success();
	}
	
	/**
	 * 站内消息ON
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("sitemsg_on")
	public AjaxResult sitemsg_on(NoticeSetting setting) {
		setting.setSiteMsg(NoticeSetting.YES);
		this.noticeSettingService.updateSitemsg(setting);
		return AjaxResult.success();
	}
	
	/**
	 * 站内消息OFF
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("sitemsg_off")
	public AjaxResult sitemsg_off(NoticeSetting setting) {
		setting.setSiteMsg(NoticeSetting.NO);
		this.noticeSettingService.updateSitemsg(setting);
		return AjaxResult.success();
	}
	
	/**
	 * 站内消息ON
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("smsg_on")
	public AjaxResult smsg_on(NoticeSetting setting) {
		setting.setSmsMsg(NoticeSetting.YES);
		this.noticeSettingService.updateSmsg(setting);
		return AjaxResult.success();
	}
	
	/**
	 * 站内消息OFF
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("smsg_off")
	public AjaxResult smsg_off(NoticeSetting setting) {
		setting.setSmsMsg(NoticeSetting.NO);
		this.noticeSettingService.updateSmsg(setting);
		return AjaxResult.success();
	}
}
