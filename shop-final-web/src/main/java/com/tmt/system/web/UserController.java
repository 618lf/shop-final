package com.tmt.system.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.core.web.BaseController;
import com.tmt.system.entity.Group;
import com.tmt.system.entity.Office;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.User;
import com.tmt.system.entity.User.UserStatus;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.entity.UserSession;
import com.tmt.system.service.GroupServiceFacade;
import com.tmt.system.service.OfficeServiceFacade;
import com.tmt.system.service.RoleServiceFacade;
import com.tmt.system.service.UserServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 用户管理
 * 
 * @author root
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/user")
public class UserController extends BaseController {

	@Autowired
	private UserServiceFacade userService;
	@Autowired
	private OfficeServiceFacade officeService;
	@Autowired
	private RoleServiceFacade roleService;
	@Autowired
	private GroupServiceFacade groupService;

	/**
	 * 得到用户信息
	 * 
	 * @param topic
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get/{id}")
	public AjaxResult get(@PathVariable Long id) {
		return AjaxResult.success(UserUtils.getUser(id));
	}

	@RequestMapping("form")
	public String form(User user, Model model) {
		if (user != null && user.getId() != null && user.getId() != null) {
			user = this.userService.get(user.getId());
			Office office = officeService.get(user.getOfficeId());
			if (office != null) {
				user.setOfficeId(office.getId());
				user.setOfficeName(office.getName());
			}

			// 拥有的角色
			List<Role> roles = roleService.findByUserId(user.getId());
			if (roles != null && roles.size() != 0) {
				StringBuilder roleIds = new StringBuilder();
				StringBuilder roleNames = new StringBuilder();
				for (Role r : roles) {
					roleIds.append(r.getId()).append(",");
					roleNames.append(r.getName()).append(",");
				}
				user.setRoleIds(roleIds.toString());
				user.setRoleNames(roleNames.toString());
			}
			// 所属的组
			List<Group> groups = this.groupService.findByUserId(user.getId());
			if (groups != null && groups.size() != 0) {
				StringBuilder groupIds = new StringBuilder();
				StringBuilder groupNames = new StringBuilder();
				for (Group g : groups) {
					groupIds.append(g.getId()).append(",");
					groupNames.append(g.getName()).append(",");
				}
				user.setGroupIds(groupIds.toString());
				user.setGroupNames(groupNames.toString());
			}
		} else {
			if (user == null) {
				user = new User();
			}
			if (user.getOfficeId() != null) {
				Office office = officeService.get(user.getOfficeId());
				user.setOfficeId(office.getId());
				user.setOfficeName(office.getName());
			} else {
				Office office = new Office();
				user.setOfficeId(office.getId());
				user.setOfficeName(office.getName());
			}
			user.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("user", user);
		model.addAttribute("officeId", user.getOfficeId());
		return "/system/UserForm";
	}

	/**
	 * 保存用户
	 * 
	 * @param user
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(User user, Model model, RedirectAttributes redirectAttributes) {
		user.userOptions(UserUtils.getUser());
		this.userService.save(user);
		UserUtils.removeUserCache(user.getId());
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改用户", user.getName(), "成功"));
		redirectAttributes.addAttribute("id", user.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/user/form").toString());
	}

	/**
	 * 列表
	 * 
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(User user, Model model) {
		if (user != null && user.getOfficeId() != null) {
			model.addAttribute("officeId", user.getOfficeId());
		}
		return "/system/UserList";
	}

	/**
	 * 列表数据
	 * 
	 * @param user
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	@SuppressWarnings("deprecation")
	public Page page(User user, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		if (user != null && user.getOfficeId() != null && !IdGen.isInvalidId(user.getOfficeId())) {
			c.andEqualTo("U.OFFICE_ID", user.getOfficeId());
		}
		if (user != null && StringUtils.isNotEmpty(user.getName())) {
			c.andLike("U.NAME", user.getName());
		}
		if (user != null && StringUtils.isNotEmpty(user.getNo())) {
			String sql = StringUtils.format("((',%s,' LIKE CONCAT('%,', U.NO, ',%') ) OR (U.NO LIKE '%%s%'))",
					StringUtils.escapeDb(user.getNo()), StringUtils.escapeDb(user.getNo()));
			c.andConditionSql(sql);
		}
		if (user != null && user.getUserType() != null) {
			c.andEqualTo("U.USER_TYPE", user.getUserType());
		}
		if (user != null && user.getCreateDate() != null) {
			Date d1 = DateUtils.clearTime(user.getCreateDate());
			Date d2 = DateUtils.getDayLastTime(user.getCreateDate());
			c.andDateBetween("U.CREATE_DATE", d1, d2);
		}
		qc.setOrderByClause("U.LOGIN_DATE DESC, U.NO");
		page = this.userService.queryForPage(qc, param);
		// 加载用户组
		if (page.getData() != null && page.getData().size() != 0) {
			List<User> users = page.getData();
			for (User u : users) {
				u.setGroupNames(this.groupService.findGroupNamesByUserId(u.getId()));
				u.setName(StringUtils.isBlank(u.getName()) ? ("匿名 - " + u.getNo()) : u.getName());
			}
		}
		return page;
	}

	/**
	 * 删除用户
	 * 
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList, Model model, HttpServletResponse response) {
		List<User> users = Lists.newArrayList();
		Boolean bFalg = Boolean.TRUE;
		for (Long id : idList) {
			User user = new User();
			user.setId(id);
			if (IdGen.isRoot(user.getId())) {
				bFalg = Boolean.FALSE;
				break;
			}
			users.add(user);
			UserUtils.removeUserCache(user.getId());
		}
		if (!bFalg) {
			return AjaxResult.error("不能删除超级管理员");
		}
		this.userService.delete(users);
		return AjaxResult.success();
	}

	/**
	 * 锁住用户
	 * 
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("lockUser")
	public Boolean lockUser(Long[] idList, Model model, HttpServletResponse response) {
		List<User> users = Lists.newArrayList();
		for (Long id : idList) {
			User user = this.userService.get(id);
			user.setUserStatus(UserStatus.LOCK_U);
			users.add(user);
			UserUtils.removeUserCache(user.getId());
		}
		this.userService.lockUser(users);

		// 退出用户
		List<UserSession> sessions = userService.userSessions(users);
		for (UserSession session : sessions) {
			if (StringUtils.isNotBlank(session.getSessionId())) {
				String reason = StringUtils.format("您的帐号存在异常于%s在被管理员强制退出系统，如果问题请及时联系管理员。",
						DateUtils.getTodayStr("yyyy-MM-dd HH:mm"));
				SecurityUtils.getSecurityManager().invalidate(session.getSessionId(), reason);
			}
		}
		return Boolean.TRUE;
	}

	/**
	 * 解锁用户
	 * 
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("unLockUser")
	public Boolean unLockUser(Long[] idList, Model model, HttpServletResponse response) {
		List<User> users = Lists.newArrayList();
		for (Long id : idList) {
			User user = this.userService.get(id);
			user.setUserStatus(UserStatus.NARMAL);
			users.add(user);
			UserUtils.removeUserCache(user.getId());
		}
		this.userService.unLockUser(users);
		return Boolean.TRUE;
	}

	/**
	 * 初始化密码
	 * 
	 * @param user
	 * @param newPassWord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("initPassword")
	public Boolean initPassword(User user, String newPassWord) {
		if (user != null && !IdGen.isInvalidId(user.getId()) && !StringUtils.isBlank(newPassWord)) {
			user.setPassword(newPassWord);
			user.setUserStatus(UserStatus.MD_P);
			this.userService.updatePassWord(user);
			user = this.userService.get(user.getId());

			Map<String, Object> context = Maps.newHashMap();
			context.put("newPassWord", newPassWord);
		}
		return Boolean.TRUE;
	}

	/**
	 * 校验编号
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("check/no")
	public Boolean checkNo(User user) {
		int iCount = this.userService.checkUserNo(user);
		if (iCount > 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * 校验邮箱
	 * 
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("check/account")
	public Boolean checkAccount(UserAccount account) {
		int iCount = this.userService.checkUserAccount(account);
		if (iCount > 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}