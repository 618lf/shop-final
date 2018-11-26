package com.tmt.system.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Group;
import com.tmt.system.entity.Menu;
import com.tmt.system.entity.Office;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.User;
import com.tmt.system.entity.User.UserStatus;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.service.GroupServiceFacade;
import com.tmt.system.service.OfficeServiceFacade;
import com.tmt.system.service.RoleServiceFacade;
import com.tmt.system.service.UserServiceFacade;
import com.tmt.system.utils.UserUtils;
import com.tmt.system.utils.YSMenuDisplay;

/**
 * 用户自身的相关操作
 * 只要有登录权限就有这个权限
 * @author root
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/self")
public class SelfController extends BaseController {

	@Autowired
	private UserServiceFacade userService;
	@Autowired
	private OfficeServiceFacade officeService;
	@Autowired
	private RoleServiceFacade roleService;
	@Autowired
	private GroupServiceFacade groupService;
	
	// 菜单显示
	private YSMenuDisplay menuDisplay = new YSMenuDisplay();
	
	//用户自己的信息修改 ----- 权限集合
	/**
	 * 个人信息
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("info")
	public String selfInfo(User user,Model model) {
		if(user == null || IdGen.isInvalidId(user.getId())) {
			user = UserUtils.getUser();
		}
		user = this.userService.get(user.getId());
		Office office = officeService.get(user.getOfficeId());
		if(office != null) {
			user.setOfficeId(office.getId());
			user.setOfficeName(office.getName());
		}
		
		//拥有的角色
		List<Role> roles = roleService.findByUserId(user.getId());
		if( roles != null && roles.size() != 0) {
			StringBuilder roleIds = new StringBuilder();
			StringBuilder roleNames = new StringBuilder();
			for( Role r:roles ) {
				roleIds.append(r.getId()).append(",");
				roleNames.append(r.getName()).append(",");
			}
			user.setRoleIds(roleIds.toString());
			user.setRoleNames(roleNames.toString());
		}
		//所属的组
		List<Group> groups = this.groupService.findByUserId(user.getId());
		if( groups != null && groups.size() != 0 ) {
			StringBuilder groupIds = new StringBuilder();
			StringBuilder groupNames = new StringBuilder();
			for( Group g:groups ) {
				groupIds.append(g.getId()).append(",");
				groupNames.append(g.getName()).append(",");
			}
			user.setGroupIds(groupIds.toString());
			user.setGroupNames(groupNames.toString());
		}
		model.addAttribute("user", user);
		model.addAttribute("officeId", user.getOfficeId());
		return "/system/UserSelfInfo";
	}
	
	/**
	 * 个人信息 - 保存
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("info/save")
	public String selfInfoSave(User user, Model model, RedirectAttributes redirectAttributes) {
		//ID不使用页面传递的数据
		user.setId(UserUtils.getUser().getId());
		this.userService.userUpdate(user);
		UserUtils.removeUserCache(user.getId());
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改用户", user.getName(), "成功"));
		redirectAttributes.addAttribute("id", user.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/self/info").toString());
	}
	
	/**
	 * 个人信息 - 修改密码
	 * @param user
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("initPassword")
	public Boolean initPassword(String newPassWord ){
		User user = UserUtils.getUser();
		user.setPassword(newPassWord);
		user.setUserStatus(UserStatus.MD_P);
		this.userService.updatePassWord(user);
		UserUtils.removeUserCache(user.getId());
		return Boolean.TRUE;
	}
	
	/**
	 * 校验账户
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("check/account")
	public Boolean checkAccount(UserAccount account){
		User user = UserUtils.getUser();
		account.setUserId(user.getId());
		int iCount = this.userService.checkUserAccount(account);
		if( iCount > 0 ) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 用户菜单导航
	 * @param id
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("menu")
	public AjaxResult userMenu(){
		User user = UserUtils.getUser();
		String menus = UserUtils.getUserMenus();
		if (menus == null) {
			List<Menu> menuList = UserUtils.getMenus(user);
			menus = menuDisplay.getUIMenu(menuList);
			UserUtils.cacheUserMenus(menus);
		}
		return AjaxResult.success(menus);
	}
	
	/**
	 * 默认返回用的系统菜单
	 * @return
	 */
	@RequestMapping("homePage")
	public String homePage(Model model){
		model.addAttribute("dateSx", DateUtil3.getDateSx());
		return "system/HomePage";
	}
}