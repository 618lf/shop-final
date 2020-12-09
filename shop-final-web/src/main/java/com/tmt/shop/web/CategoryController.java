package com.tmt.shop.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.staticize.StaticUtils;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.TreeEntityUtils;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.CategoryBrand;
import com.tmt.shop.entity.Store;
import com.tmt.shop.service.CategoryServiceFacade;
import com.tmt.shop.utils.StoreUtils;
import com.tmt.system.utils.UserUtils;

/**
 * 商品分类 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopCategoryController")
@RequestMapping(value = "${adminPath}/shop/category")
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
		return "/shop/CategoryList";
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
		if (category!=null && !StringUtils.isBlank(category.getName())) {
		    params.put("NAME", category.getName());
		}
		List<Category> menus = this.categoryService.findByCondition(params);
		if (menus != null && menus.size() != 0) {
			StringBuffer sb = new StringBuffer(100);
			for( Category menuItem: menus ) {
				 sb.append(menuItem.getParentIds());
				 sb.append(menuItem.getId()).append(",");
			}
			sb.append("-1");
			params.clear();
			params.put("IDS", sb.toString());
		}
		if (category!=null && category.getId() != null){
		    category = this.categoryService.get(category.getId());
		}
		List<Category> categorys = this.categoryService.findByCondition(params);
		if (categorys != null) {
			for(Category categoryItem : categorys){
				categoryItem.setId(categoryItem.getId());
				categoryItem.setParent(categoryItem.getParentId());
				categoryItem.setLevel(categoryItem.getLevel());
				categoryItem.setExpanded(Boolean.FALSE);
				categoryItem.setLoaded(Boolean.TRUE);
				categoryItem.setIsLeaf(Boolean.TRUE);
				if (categoryItem!=null && categoryItem.getId() != null && ( (","+categoryItem.getParentIds()+",").indexOf(","+categoryItem.getId()+",") != -1)) {
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
	    if (category != null && !IdGen.isInvalidId(category.getId())) {
			category = this.categoryService.get(category.getId());
		} else {
		   if( category == null) {
			   category = new Category();
		   }
		   category.setId(IdGen.INVALID_ID);
           if(category.getParentId() == null){
			  category.setParentId(IdGen.ROOT_ID);
		   }
		}
		Category parent = this.categoryService.get(category.getParentId());
  		category.setParentId(parent.getId());
  		category.setParentName(parent.getName());
  		category.setBrands(this.categoryService.queryBrandsByCategoryd(category.getId()));
		model.addAttribute("category", category);
		return "/shop/CategoryForm";
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
		//品牌
		List<CategoryBrand> brands = WebUtils.fetchItemsFromRequest(null, CategoryBrand.class, "brands.");
		category.setBrands(brands);
		
		category.userOptions(UserUtils.getUser());
		this.categoryService.save(category);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品分类", category.getName(), "成功"));
		redirectAttributes.addAttribute("id", category.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/category/form");
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
			categorys.add(category);
		}
		boolean bFlag =  this.categoryService.delete(categorys);
		if(!bFlag) {
		   return AjaxResult.error("删除的栏目中包含子栏目，不能删除");
		}
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
	 * 国家, 省, 市, 县, 街道
	 * 1     2  3   4  5
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/level/{level}")
	public AjaxResult levels(@PathVariable Integer level, Long parentId, String name) {
		return AjaxResult.success(categoryService.queryCategorysByLevel(level, parentId, name));
	}
	
	/**
	 * 阅览地址
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("view/{id}")
	public AjaxResult view(@PathVariable Long id) {
		Store store = StoreUtils.getDefaultStore();
		Category category = this.categoryService.get(id);
		return AjaxResult.success(StaticUtils.touchStaticizePage(store, "category", category));
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
			if (extId == null || (extId!=null && !extId.equals(e.getId().toString()) && e.getParentIds().indexOf(","+extId+",")==-1)){
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
	
	/**
	 * 返回json
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get/{id}")
	public Category get(@PathVariable Long id) {
		Store store = StoreUtils.getDefaultStore();
		Category category = this.categoryService.get(id);
		category.setUrl(StaticUtils.touchStaticizePage(store, "category", category));
		return category;
	}
}