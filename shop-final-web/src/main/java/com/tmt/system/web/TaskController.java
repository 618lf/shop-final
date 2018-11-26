package com.tmt.system.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.SystemConstant;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.TaskProgress;
import com.tmt.system.service.TaskServiceFacade;

/**
 * 定时任务
 * @author lifeng
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/system/task")
public class TaskController extends BaseController{

	@Autowired
	private TaskServiceFacade taskService;
	
	@RequestMapping("list")
	public String list(){
		return "system/TaskList";
	}
	
	/**
	 * 任务列表
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Task task,Model model,Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		qc.setOrderByClause("(CASE WHEN TASK_STATUS = 'RUNNING' THEN 1 WHEN TASK_STATUS = 'RUNABLE' THEN 2 ELSE 3 END), NEXT_EXECUTE_TIME");
		page = this.taskService.queryForPage(qc, param);
		return page;
	}
	
	/**
	 * 任务表单
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping("form")
	public String form(Task task,Model model){
		if(task != null && task.getId() != null ) {
		   task = this.taskService.get(task.getId());
		} else {
		   if(task == null) {
			  task = new Task();
		   }
		   task.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("task", task);
		model.addAttribute("business", this.taskService.business());
		return "system/TaskForm";
	}
	
	/**
	 * 保存任务
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@RequestMapping("save")
	public String save(Task task,Model model,RedirectAttributes redirectAttributes) {
		this.taskService.save(task);
		addMessage(redirectAttributes, "保存定时任务'" + task.getName() + "'成功");
		redirectAttributes.addAttribute("id", task.getId());
		return "redirect:"+Globals.adminPath+"/system/task/form";
	}
	
	/**
	 * 删除任务
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public Boolean delete(Long[] idList , Model model,HttpServletResponse response) {
		List<Task> tasks = Lists.newArrayList();
		Boolean bFalg = Boolean.TRUE;
		for(Long id:idList) {
			Task task = new Task();
			task.setId(id);
			tasks.add(task);
		}
		this.taskService.delete(tasks);
		return bFalg;
	}
	
	/**
	 * 刷新缓存
	 * @return
	 */
	@ResponseBody
	@RequestMapping("refresh")
	public Boolean refresh() {
		CacheUtils.getSysCache().delete(SystemConstant.RUNNING_ABLE_TASKS);
		return true;
	}
	
	/**
	 * 启动任务
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("start")
	public Boolean start(Long id) {
		Task task = this.taskService.get(id);
		this.taskService.start(task);
		return Boolean.TRUE;
	}
	
	/**
	 * 暂停
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("pause")
	public Boolean pause(Long id){
		Task task = this.taskService.get(id);
		this.taskService.pause(task);
		return Boolean.TRUE;
	}
	
	/**
	 * 停止任务
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("stop")
	public Boolean stop(Long id){
		Task task = this.taskService.get(id);
		this.taskService.stop(task);
		return Boolean.TRUE;
	}
	
	/**
	 * 立即执行
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("execute")
	public Boolean execute(Long id){
		Task task = this.taskService.get(id);
		this.taskService.execute(task);
		return Boolean.TRUE;
	}
	
	/**
	 * 批量立即执行
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("executes")
	public Boolean executes(Long[] idList){
		for(Long id: idList) {
			Task task = this.taskService.get(id);
			this.taskService.execute(task);
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 执行进度
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("progress/{id}")
	public AjaxResult progress(@PathVariable Long id){
		Task task = this.taskService.get(id);
		TaskProgress progress = TaskProgress.getTaskProgress(task.getId());
		task.setProgress(progress);
		return AjaxResult.success(task);
	}
}