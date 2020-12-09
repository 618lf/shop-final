package com.tmt.shop.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Parameter;
import com.tmt.shop.entity.ParameterOption;
import com.tmt.shop.service.ParameterServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 商品参数 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopParameterController")
@RequestMapping(value = "${adminPath}/shop/parameter")
public class ParameterController extends BaseController{
	
	@Autowired
	private ParameterServiceFacade parameterService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Parameter parameter, Model model){
		return "/shop/ParameterList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param parameter
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Parameter parameter, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(parameter, c);
	    qc.setOrderByClause("CATEGORY_ID, SORT");
		return parameterService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param parameter
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Parameter parameter, Model model) {
	    if(parameter != null && !IdGen.isInvalidId(parameter.getId())) {
			parameter = this.parameterService.getWithOptions(parameter.getId());
		} else {
		   if( parameter == null) {
			   parameter = new Parameter();
		   }
		   parameter.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("parameter", parameter);
		return "/shop/ParameterForm";
	}
	
	/**
	 * 复制
	 * @param specification
	 * @param model
	 */
	@RequestMapping("copy")
	public String copy(Parameter parameter, Model model) {
		parameter = this.parameterService.getWithOptions(parameter.getId());
		if(parameter != null) {
			parameter.space();
		} else {
			parameter = new Parameter();
			parameter.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("parameter", parameter);
		return "/shop/ParameterForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Parameter parameter, Model model, RedirectAttributes redirectAttributes) {
		List<ParameterOption> options = WebUtils.fetchItemsFromRequest(null, ParameterOption.class, "items.");
		parameter.setOptions(options);
		parameter.userOptions(UserUtils.getUser());
		this.parameterService.save(parameter);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品参数", parameter.getName(), "成功"));
		redirectAttributes.addAttribute("id", parameter.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/parameter/form");
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
		List<Parameter> parameters = Lists.newArrayList();
		for(Long id: idList) {
			Parameter parameter = new Parameter();
			parameter.setId(id);
			parameters.add(parameter);
		}
		this.parameterService.delete(parameters);
		return AjaxResult.success();
	}
	

    /**
	 * 批量修改栏目排序
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("sort")
	public AjaxResult updateSort(Model model, HttpServletRequest request, HttpServletResponse response) {
		String postData = request.getParameter("postData");
		List<Map<String,String>> maps = JsonMapper.fromJson(postData, ArrayList.class);
		if(maps != null && maps.size() != 0){
			List<Parameter> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Parameter menu = new Parameter();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.parameterService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(Parameter parameter, Criteria c) {
		if(StringUtils.isNotBlank(parameter.getName())) {
           c.andLike("NAME", parameter.getName());
        }
        if(parameter.getCategoryId() != null) {
           c.andEqualTo("CATEGORY_ID", parameter.getCategoryId());
        }
	}
}