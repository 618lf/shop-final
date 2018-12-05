package com.tmt.system.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserRunas;
import com.tmt.system.service.UserRunasServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 用户切换 管理
 * 
 * @author 超级管理员
 * @date 2016-02-19
 */
@Controller("systemUserRunasController")
@RequestMapping(value = "${spring.application.web.admin}/system/user/runas")
public class UserRunasController extends BaseController {

	@Autowired
	private UserRunasServiceFacade userRunasService;

	/**
	 * 查询我授权的用户
	 * 
	 * @param model
	 */
	@RequestMapping("/grants")
	public String grants(Model model) {
		List<UserRunas> runass = userRunasService.queryGrantUsers(UserUtils.getUser());
		model.addAttribute("runas", runass);
		return "/system/UserRunasList";
	}

	/**
	 * 授权某一个用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/grant")
	public AjaxResult grant(Long userId) {
		User user = UserUtils.getUser();
		if (user.getId().equals(userId)) {
			return AjaxResult.error("不能授权给自己");
		}
		UserRunas runas = new UserRunas();
		runas.setUserId(user.getId());
		runas.setGrantUserId(userId);
		this.userRunasService.grant(runas);
		return AjaxResult.success("授权成功");
	}

	/**
	 * 取消授权某一个用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/revoke")
	public AjaxResult revoke(Long userId) {
		User user = UserUtils.getUser();
		if (user.getId().equals(userId)) {
			return AjaxResult.error("不能授权给自己");
		}
		UserRunas runas = new UserRunas();
		runas.setUserId(user.getId());
		runas.setGrantUserId(userId);
		this.userRunasService.revoke(runas);
		return AjaxResult.success("取消授权成功");
	}

	/**
	 * 查询授权我运行的用户
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/runs")
	public AjaxResult runs() {
		return AjaxResult.success(userRunasService.queryRunasUsers(UserUtils.getUser()));
	}

	/**
	 * 切换到指定的用户 将切换用户的权限写入 switch 方法中
	 */
	@ResponseBody
	@RequestMapping("/switch")
	public AjaxResult switchto(Long userId) {
		User user = UserUtils.getUser();
		UserRunas runas = new UserRunas();
		runas.setUserId(userId);
		runas.setGrantUserId(UserUtils.getUser().getId());
		if (!user.getId().equals(userId) && (this.userRunasService.exists(runas) || user.isRoot())
				&& UserUtils.hasRole("admin:runas")) {
			User _user = UserUtils.getUser(userId);
			Boolean b = UserUtils.runas(_user);
			return b ? AjaxResult.success("切换用户成功") : AjaxResult.error("切换用户失败");
		}
		return AjaxResult.error("切换错误，没有相关权限！");
	}

	/**
	 * 释放
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/release")
	public AjaxResult release() {
		if (UserUtils.isRunAs()) {
			UserUtils.releaseRunAs();
		}
		return AjaxResult.success();
	}
}