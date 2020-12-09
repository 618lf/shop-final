package com.tmt.cms.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.cms.entity.Category;
import com.tmt.cms.service.CategoryServiceFacade;
import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.TreeEntityUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;

/**
 * 文章分类 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Controller("cmsCategoryController")
@RequestMapping(value = "${adminPath}/cms/category")
public class CategoryController extends BaseController{
	
	@Autowired
	private CategoryServiceFacade categoryService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Category category, Model model){
        if (category != null && !IdGen.isInvalidId(category.getId())) {
			model.addAttribute("id", category.getId());
		}
		return "/cms/CategoryList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param category
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Category category, Model model){
        Map<String,Object> params = Maps.newHashMap();
		if(category!=null && !StringUtils.isBlank(category.getName())) {
		   params.put("NAME", category.getName());
		}
		if(!params.isEmpty()) {
		   List<Category> menus = this.categoryService.findByCondition(params);
		   if( menus != null && menus.size() != 0 ) {
			  StringBuffer sb = new StringBuffer(100);
			  for( Category menuItem: menus ) {
				 sb.append(menuItem.getParentIds());
				 sb.append(menuItem.getId()).append(",");
			  }
			  sb.append("-1");
			  params.clear();
			  params.put("IDS", sb.toString());
		   }
		}
		if(category!=null && category.getId() != null){
		   category = this.categoryService.get(category.getId());
		}
		List<Category> categorys = this.categoryService.findByCondition(params);
		if (categorys != null) {
			for(Category categoryItem : categorys){
				categoryItem.setId(categoryItem.getId());
				categoryItem.setParent(categoryItem.getParentId());
				categoryItem.setLevel(categoryItem.getLevel());
				categoryItem.setExpanded(Boolean.TRUE);
				categoryItem.setLoaded(Boolean.TRUE);
				categoryItem.setIsLeaf(Boolean.TRUE);
				if (categoryItem!=null && categoryItem.getId() != null && (new StringBuilder(",").append(categoryItem.getParentIds()).append(",").indexOf(new StringBuilder(",").append(categoryItem.getId()).append(",").toString()) != -1)) {
					categoryItem.setExpanded(Boolean.TRUE);
				}
			}
		}
		List<Category> copyMenus = TreeEntityUtils.sort(categorys);
		if (copyMenus != null && copyMenus.size() != 0 && !(category!=null && category.getId() != null)) {
			copyMenus.get(0).setExpanded(Boolean.TRUE);
		}
		Page page = new Page();
        page.setData(copyMenus);
        return page;
	}
	
	/**
	 * 表单
	 * @param category
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Category category, Model model) {
	    if(category != null && !IdGen.isInvalidId(category.getId())) {
		   category = this.categoryService.get(category.getId());
		} else {
		   if(category == null) {
			  category = new Category();
		   }
		   category.setId(IdGen.INVALID_ID);
           if(category.getParentId() == null){
			  category.setParentId(IdGen.ROOT_ID);
		   }
           category.setIsShow(Category.NO);
		}
		Category parent = this.categoryService.get(category.getParentId());
  		category.setParentId(parent.getId());
  		category.setParentName(parent.getName());
		model.addAttribute("category", category);
		return "/cms/CategoryForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Category category, Model model, RedirectAttributes redirectAttributes) {
		this.categoryService.save(category);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改文章分类", category.getName(), "成功"));
		redirectAttributes.addAttribute("id", category.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/cms/category/form");
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
		List<Category> categorys = Lists.newArrayList();
		for(Long id: idList) {
			Category category = new Category();
			category.setId(id);
			int iCount = this.categoryService.deleteCategoryCheck(category);
			if(iCount > 0) {
			   return AjaxResult.error("选择的栏目中包含子栏目,不能删除");
			}
			//验证
			categorys.add(category);
		}
		this.categoryService.delete(categorys);
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
			List<Category> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Category menu = new Category();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.categoryService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}
	
	/**
	 * 树组件支持
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		Map<String,Object> params = Maps.newHashMap();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<TreeVO> trees = this.categoryService.findTreeList(params);
		for (int i=0; i<trees.size(); i++){
			TreeVO e = trees.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId().toString()) && e.getParentIds().indexOf(new StringBuilder(",").append(extId).append(",").toString())==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent());
				map.put("name", e.getTreeName());
				map.put("module", e.getTreeType());
				mapList.add(map);
			}
		}
		return mapList;
	}
}
