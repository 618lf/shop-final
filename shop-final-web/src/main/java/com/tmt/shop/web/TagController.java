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
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Tag;
import com.tmt.shop.service.TagServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 商品标签 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopTagController")
@RequestMapping(value = "${adminPath}/shop/tag")
public class TagController extends BaseController{
	
	@Autowired
	private TagServiceFacade tagService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Tag tag, Model model){
		return "/shop/TagList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param tag
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Tag tag, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(tag, c);
		return tagService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param tag
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Tag tag, Model model) {
	    if(tag != null && !IdGen.isInvalidId(tag.getId())) {
			tag = this.tagService.get(tag.getId());
		} else {
		   if( tag == null) {
			   tag = new Tag();
		   }
		   tag.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("tag", tag);
		return "/shop/TagForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Tag tag, Model model, RedirectAttributes redirectAttributes) {
		tag.userOptions(UserUtils.getUser());
		this.tagService.save(tag);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品标签", tag.getIcon(), "成功"));
		redirectAttributes.addAttribute("id", tag.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/tag/form");
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
		List<Tag> tags = Lists.newArrayList();
		for(Long id: idList) {
			Tag tag = new Tag();
			tag.setId(id);
			tags.add(tag);
		}
		this.tagService.delete(tags);
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
			List<Tag> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Tag menu = new Tag();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.tagService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(Tag tag, Criteria c) {}
}