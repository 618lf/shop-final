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
import org.springframework.web.bind.annotation.RequestParam;
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
import com.tmt.shop.entity.Brand;
import com.tmt.shop.service.BrandServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 品牌管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopBrandController")
@RequestMapping(value = "${adminPath}/shop/brand")
public class BrandController extends BaseController{
	
	@Autowired
	private BrandServiceFacade brandService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Brand brand, Model model){
		return "/shop/BrandList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param brand
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Brand brand, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(brand, c);
		return brandService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param brand
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Brand brand, Model model) {
	    if(brand != null && !IdGen.isInvalidId(brand.getId())) {
			brand = this.brandService.get(brand.getId());
		} else {
		   if( brand == null) {
			   brand = new Brand();
		   }
		   brand.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("brand", brand);
		return "/shop/BrandForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Brand brand, Model model, RedirectAttributes redirectAttributes) {
		brand.userOptions(UserUtils.getUser());
		this.brandService.save(brand);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改品牌管理", brand.getName(), "成功"));
		redirectAttributes.addAttribute("id", brand.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/brand/form");
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
		List<Brand> brands = Lists.newArrayList();
		for(Long id: idList) {
			Brand brand = new Brand();
			brand.setId(id);
			brands.add(brand);
		}
		this.brandService.delete(brands);
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
			List<Brand> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Brand menu = new Brand();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.brandService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(Brand brand, Criteria c) {
        if(StringUtils.isNotBlank(brand.getName())) {
           c.andEqualTo("NAME", brand.getName());
        }
	}
    
    /**
	 * 树组件支持
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Brand> brands = this.brandService.getAll();
		for (int i=0; i< brands.size(); i++){
			Brand e = brands.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "O_-1");
			map.put("name", e.getName());
			mapList.add(map);
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "O_-1");
		map.put("pId", "O_-2");
		map.put("name", "品牌管理");
		mapList.add(map);
		return mapList;
	}
	
	/**
	 * 表组件支持
	 */
	@RequestMapping(value = {"tableSelect"})
	public String tableSelect() {
	   return "/shop/BrandTableSelect";
	}
}
