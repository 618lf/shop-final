package com.tmt.api.web;

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

import com.tmt.api.entity.Group;
import com.tmt.api.service.GroupService;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.web.BaseController;

/**
 * 接口分组 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Controller("apiGroupController")
@RequestMapping(value = "${adminPath}/api/group")
public class GroupController extends BaseController{
	
	@Autowired
	private GroupService groupService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Group group, Model model){
		return "/api/GroupList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param group
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Group group, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(group, c);
		return groupService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param group
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Group group, Model model) {
	    if(group != null && !IdGen.isInvalidId(group.getId())) {
		   group = this.groupService.get(group.getId());
		} else {
		   if(group == null) {
			  group = new Group();
		   }
		   group.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("group", group);
		return "/api/GroupForm";
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
	public AjaxResult save(Group group) {
		this.groupService.save(group);
		return AjaxResult.success(group);
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
	public AjaxResult delete(Long[] idList) {
		List<Group> groups = Lists.newArrayList();
		for(Long id: idList) {
			Group group = new Group();
			group.setId(id);
			groups.add(group);
		}
		this.groupService.delete(groups);
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
			List<Group> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Group menu = new Group();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.groupService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(Group group, Criteria c) {
	}
	
}
