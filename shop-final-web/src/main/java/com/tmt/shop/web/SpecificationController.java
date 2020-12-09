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
import com.tmt.shop.entity.Specification;
import com.tmt.shop.entity.SpecificationOption;
import com.tmt.shop.service.SpecificationServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 商品规格 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopSpecificationController")
@RequestMapping(value = "${adminPath}/shop/specification")
public class SpecificationController extends BaseController{
	
	@Autowired
	private SpecificationServiceFacade specificationService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Specification specification, Model model){
		return "/shop/SpecificationList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param specification
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Specification specification, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(specification, c);
		qc.setOrderByClause("CATEGORY_ID, SORT");
		return specificationService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param specification
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Specification specification, Model model) {
	    if(specification != null && !IdGen.isInvalidId(specification.getId())) {
		   specification = this.specificationService.getWithOptions(specification.getId());
		} else {
		   if( specification == null) {
			   specification = new Specification();
		   }
		   specification.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("specification", specification);
		return "/shop/SpecificationForm";
	}
	
	/**
	 * 复制
	 * @param specification
	 * @param model
	 */
	@RequestMapping("copy")
	public String copy(Specification specification, Model model) {
		specification = this.specificationService.getWithOptions(specification.getId());
		if(specification != null) {
		   specification.space();
		} else {
		   specification = new Specification();
		   specification.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("specification", specification);
		return "/shop/SpecificationForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Specification specification, Model model, RedirectAttributes redirectAttributes) {
		List<SpecificationOption> options = WebUtils.fetchItemsFromRequest(null, SpecificationOption.class, "items.");
		specification.setOptions(options);
		specification.userOptions(UserUtils.getUser());
		this.specificationService.save(specification);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品规格", specification.getName(), "成功"));
		redirectAttributes.addAttribute("id", specification.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/specification/form");
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
		List<Specification> specifications = Lists.newArrayList();
		for(Long id: idList) {
			Specification specification = new Specification();
			specification.setId(id);
			specifications.add(specification);
		}
		this.specificationService.delete(specifications);
		return AjaxResult.success();
	}
	
	/**
	 * 检查分类是否有规格
	 * @param categoryId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/category/{categoryId}")
	public AjaxResult hasSpecification(@PathVariable Long categoryId) {
		return AjaxResult.success(this.specificationService.hasSpecification(categoryId));
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
			List<Specification> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Specification menu = new Specification();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.specificationService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(Specification specification, Criteria c) {
        if(StringUtils.isNotBlank(specification.getName())) {
           c.andLike("NAME", specification.getName());
        }
        if(specification.getCategoryId() != null) {
           c.andEqualTo("CATEGORY_ID", specification.getCategoryId());
        }
	}
}