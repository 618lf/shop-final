package com.tmt.wechat.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.utils.UserUtils;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.service.AppServiceFacade;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 微信公众号 管理
 * @author 超级管理员
 * @date 2016-09-04
 */
@Controller("wechatAppController")
@RequestMapping(value = "${spring.application.web.admin}/wechat/app")
public class AppController extends BaseController{
	
	@Autowired
	private AppServiceFacade appService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(App app, Model model){
		return "/wechat/AppList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param app
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(App app, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(app, c);
		return appService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param app
	 * @param model
	 */
	@RequestMapping("form")
	public String form(App app, Model model) {
	    if(app != null && !IdGen.isInvalidId(app.getId())) {
		   app = this.appService.get(app.getId());
		} else {
		   if(app == null) {
			  app = new App();
		   }
		}
		model.addAttribute("app", app);
		return "/wechat/AppForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(App app, Model model, RedirectAttributes redirectAttributes) {
		app.userOptions(UserUtils.getUser());
		this.appService.save(app);
		WechatUtils.clearCache();
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改微信公众号", app.getName(), "成功"));
		redirectAttributes.addAttribute("id", app.getId());
		return WebUtils.redirectTo(Globals.adminPath, "/wechat/app/form");
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
	public AjaxResult delete(String[] idList , Model model, HttpServletResponse response) {
		List<App> apps = Lists.newArrayList();
		for(String id: idList) {
			App app = new App();
			app.setId(id);
			apps.add(app);
		}
		this.appService.delete(apps);
		WechatUtils.clearCache();
		return AjaxResult.success();
	}
	
	/**
	 * 树组件支持
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<App> trees = this.appService.getAll();
		for (int i=0; i<trees.size(); i++){
			App e = trees.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "O_-1");
			map.put("name", e.getName());
			mapList.add(map);
		}
		
		// 根目录
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "O_-1");
		map.put("pId", "O_-2");
		map.put("name", "微信公众号");
		mapList.add(map);
		return mapList;
	} 

	//查询条件
	private void initQc(App app, Criteria c) {
        if(StringUtil3.isNotBlank(app.getName())) {
           c.andEqualTo("NAME", app.getName());
        }
	}
}