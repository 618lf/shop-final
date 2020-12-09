package com.tmt.api.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.api.entity.Project;
import com.tmt.api.service.GroupService;
import com.tmt.api.service.ProjectService;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.web.BaseController;

/**
 * 项目 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Controller("apiProjectController")
@RequestMapping(value = "${adminPath}/api/project")
public class ProjectController extends BaseController{
	
	@Autowired
	private ProjectService projectService;
	@Autowired
	private GroupService groupService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Project project, Model model){
		return "/api/ProjectList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param project
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Project project, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(project, c);
		return projectService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param project
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Project project, Model model) {
	    if(project != null && !IdGen.isInvalidId(project.getId())) {
		   project = this.projectService.get(project.getId());
		} else {
		   if(project == null) {
			  project = new Project();
		   }
		   project.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("project", project);
		return "/api/ProjectForm";
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
	public AjaxResult save(Project project) {
		this.projectService.save(project);
		return AjaxResult.success(project.getId());
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
		List<Project> projects = Lists.newArrayList();
		for(Long id: idList) {
			Project project = new Project();
			project.setId(id);
			projects.add(project);
		}
		this.projectService.delete(projects);
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Project project, Criteria c) {}
	
	/**
	 * 管理
	 * @return
	 */
	@RequestMapping("manage")
	public String manage(Long id, Model model) {
		Project project = this.projectService.get(id);
		model.addAttribute("project", project);
		model.addAttribute("groups", this.groupService.queryByProject(id));
		return "/api/ProjectManage";
	}
}