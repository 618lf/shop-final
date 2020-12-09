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
import com.tmt.shop.entity.Attribute;
import com.tmt.shop.entity.AttributeOption;
import com.tmt.shop.service.AttributeServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 商品属性 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopAttributeController")
@RequestMapping(value = "${adminPath}/shop/attribute")
public class AttributeController extends BaseController{
	
	@Autowired
	private AttributeServiceFacade attributeService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Attribute attribute, Model model){
		return "/shop/AttributeList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param attribute
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Attribute attribute, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(attribute, c);
		qc.setOrderByClause("CATEGORY_ID, SORT");
		return attributeService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param attribute
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Attribute attribute, Model model) {
	    if(attribute != null && !IdGen.isInvalidId(attribute.getId())) {
			attribute = this.attributeService.getWithOptions(attribute.getId());
		} else {
		   if( attribute == null) {
			   attribute = new Attribute();
		   }
		   attribute.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("attribute", attribute);
		return "/shop/AttributeForm";
	}
	
	/**
	 * 复制
	 * @param specification
	 * @param model
	 */
	@RequestMapping("copy")
	public String copy(Attribute attribute, Model model) {
		attribute = this.attributeService.getWithOptions(attribute.getId());
		if(attribute != null) {
			attribute.space();
		} else {
			attribute = new Attribute();
			attribute.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("attribute", attribute);
		return "/shop/AttributeForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Attribute attribute, Model model, RedirectAttributes redirectAttributes) {
		List<AttributeOption> options = WebUtils.fetchItemsFromRequest(null, AttributeOption.class, "items.");
		attribute.setOptions(options);
		attribute.userOptions(UserUtils.getUser());
		this.attributeService.save(attribute);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品属性", attribute.getName(), "成功"));
		redirectAttributes.addAttribute("id", attribute.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/attribute/form");
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
	public AjaxResult delete(Long[] idList, Model model, HttpServletResponse response) {
		List<Attribute> attributes = Lists.newArrayList();
		for(Long id:idList ) {
			Attribute attribute = new Attribute();
			attribute.setId(id);
			attributes.add(attribute);
		}
		this.attributeService.delete(attributes);
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
			List<Attribute> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Attribute menu = new Attribute();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.attributeService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(Attribute attribute, Criteria c) {
		if(StringUtils.isNotBlank(attribute.getName())) {
           c.andLike("NAME", attribute.getName());
        }
        if(attribute.getCategoryId() != null) {
           c.andEqualTo("CATEGORY_ID", attribute.getCategoryId());
        }
	}
	
}
