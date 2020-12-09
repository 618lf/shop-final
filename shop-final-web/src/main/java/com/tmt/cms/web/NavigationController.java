package com.tmt.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.cms.entity.Navigation;
import com.tmt.cms.service.NavigationServiceFacade;
import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;

/**
 * 导航管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Controller("cmsNavigationController")
@RequestMapping(value = "${adminPath}/cms/navigation")
public class NavigationController extends BaseController{
	
	@Autowired
	private NavigationServiceFacade navigationService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Navigation navigation, Model model){
		return "/cms/NavigationList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param navigation
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Navigation navigation, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(navigation, c);
		return navigationService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param navigation
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Navigation navigation, Model model) {
	    if(navigation != null && !IdGen.isInvalidId(navigation.getId())) {
		   navigation = this.navigationService.get(navigation.getId());
		} else {
		   if(navigation == null) {
			  navigation = new Navigation();
		   }
		   navigation.setId(IdGen.INVALID_ID);
		   navigation.setIsTarget(Navigation.NO);
		   navigation.setOrders(1);
		}
		model.addAttribute("navigation", navigation);
		return "/cms/NavigationForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Navigation navigation, Model model, RedirectAttributes redirectAttributes) {
		this.navigationService.save(navigation);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改导航管理", navigation.getName(), "成功"));
		redirectAttributes.addAttribute("id", navigation.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/cms/navigation/form");
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
		List<Navigation> navigations = Lists.newArrayList();
		for(Long id: idList) {
			Navigation navigation = new Navigation();
			navigation.setId(id);
			navigations.add(navigation);
		}
		this.navigationService.delete(navigations);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(Navigation navigation, Criteria c) {
        if(StringUtils.isNotBlank(navigation.getName())) {
           c.andEqualTo("NAME", navigation.getName());
        }
	}
	
}
