package com.tmt.wechat.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.web.BaseController;
import com.tmt.system.utils.UserUtils;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.MetaSetting;
import com.tmt.wechat.service.AppServiceFacade;
import com.tmt.wechat.service.MetaSettingServiceFacade;

/**
 * 回复配置 管理
 * @author 超级管理员
 * @date 2016-09-27
 */
@Controller("wechatMetaSettingController")
@RequestMapping(value = "${spring.application.web.admin}/wechat/meta/setting")
public class MetaSettingController extends BaseController{
	
	@Autowired
	private MetaSettingServiceFacade metaSettingService;
	@Autowired
	private AppServiceFacade appService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("")
	public String def(Model model){
		List<App> apps = appService.getAll();
		model.addAttribute("apps", apps);
		return "/wechat/MetaSettingList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param menu
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("load")
	public AjaxResult load(MetaSetting setting){
		setting = this.metaSettingService.getByAppId(setting.getAppId());
		if (setting == null) {
			setting = new MetaSetting();
			setting.setAttentionType(MetaSetting.NO);
			setting.setDefaultType(MetaSetting.NO);
		}
		return AjaxResult.success(setting);
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
	public AjaxResult save(MetaSetting setting, HttpServletRequest requestss) {
		setting.userOptions(UserUtils.getUser());
		MetaSetting _setting = this.metaSettingService.getByAppId(setting.getAppId());
		if (_setting != null) {
			setting.setId(_setting.getId());
		}
		this.metaSettingService.save(setting);
		return AjaxResult.success();
	}
}