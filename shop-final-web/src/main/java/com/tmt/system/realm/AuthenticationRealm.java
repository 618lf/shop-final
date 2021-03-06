package com.tmt.system.realm;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.tmt.Constants;
import com.tmt.core.exception.CaptchaException;
import com.tmt.core.exception.ErrorCode;
import com.tmt.core.security.context.AuthenticationToken;
import com.tmt.core.security.context.AuthorizationInfo;
import com.tmt.core.security.context.SimpleAuthorizationInfo;
import com.tmt.core.security.context.ThreadContext;
import com.tmt.core.security.exception.AuthenticationException;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.realm.CachedRealm;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.PasswordUtils;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.core.web.ValidateCodeService;
import com.tmt.core.web.security.exception.AccountLockException;
import com.tmt.core.web.security.exception.CaptchatAuthenException;
import com.tmt.core.web.security.exception.UsernamePasswordException;
import com.tmt.system.entity.User;
import com.tmt.system.utils.SiteUtils;
import com.tmt.system.utils.UserUtils;

/**
 * 系统收集登录信息,授权,退出
 * 
 * @author lifeng
 */
public class AuthenticationRealm extends CachedRealm {

	/**
	 * 自己做验证
	 */
	@Override
	public Principal doAuthentication(HttpServletRequest request) throws AuthenticationException {

		// 获取的相关的参数
		String username = WebUtils.getCleanParam(request, AuthenticationToken.username);
		String password = WebUtils.getCleanParam(request, AuthenticationToken.password);
		String captcha = WebUtils.getCleanParam(request, AuthenticationToken.captcha);
		String captchaKey = ValidateCodeService.getValidateCodeKey(request);

		// 是否有用户信息
		if (!(StringUtils.hasText(username) && StringUtils.hasText(password))) {
			throw new UsernamePasswordException(ErrorCode.U_P_FAILURE);
		}

		// 用户是否需要验证验证码 -- 是否超过次数
		try {
			if (UserUtils.isValidateLoginCode(username, false)) {
				UserUtils.validateLoginCode(captchaKey, captcha);
			}
		} catch (CaptchaException e) {
			throw new CaptchatAuthenException(ErrorCode.CAPTCHA_FAILURE);
		}

		// 获得用户
		User user = UserUtils.getUserByAccount(username);
		if (user == null) {
			throw new UsernamePasswordException(ErrorCode.U_P_FAILURE);
		}

		// 用户锁定, root 用户不能锁定
		if (user.isLocked() && !user.isRoot()) {
			throw new AccountLockException(ErrorCode.ACCOUNT_LOCK_FAILURE);
		}

		// 验证密码
		boolean flag = PasswordUtils.validatePassword(password, user.getPassword());
		if (flag) {
			ThreadContext.put(Constants.USER_KEY, user);
			return new Principal(user.getId(), username);
		}

		// 直接抛出用户名密码错误
		throw new UsernamePasswordException(ErrorCode.U_P_FAILURE);
	}

	/**
	 * 授权 -- 存储在 cache中
	 */
	@Override
	public AuthorizationInfo doGetAuthorizationInfo(Principal principal) {
		User user = new User();
		user.setId(principal.getId());
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		List<String> permissions = UserUtils.getPermissions(user);
		for (String permission : permissions) {
			info.addPermission(permission);
		}
		List<String> roles = UserUtils.getRoles(user);
		for (String role : roles) {
			info.addRole(role);
		}
		if (user.isRoot() && (info.getRoles() == null || !info.getRoles().contains("admin"))) {
			info.addRole("admin");
		}
		if (user.isRoot() && (info.getRoles() == null || !info.getRoles().contains("admin:system"))) {
			info.addRole("admin:system");
		}
		return info;
	}

	/**
	 * 登录成功，清空密码错误计数
	 */
	@Override
	public void onLoginSuccess(Subject subject, HttpServletRequest request) {
		Principal principal = (Principal) subject.getPrincipal();
		UserUtils.clearValidateCode(principal.getAccount());

		// 得到当前用户
		User user = new User();
		user.setId(principal.getId());
		String _remoteAddr = WebUtils.getRemoteAddr(request);
		String _sessionId = subject.getSessionId();
		Byte isMultiLogin = SiteUtils.getSite().getIsMultiLogin();
		String sessionId = UserUtils.userLogin(user, _sessionId, _remoteAddr);
		// 是否允许同时在线
		if (0 == isMultiLogin) {
			// 将此 sessionId 失效
			String reason = StringUtils.format("您的帐号于%s在其他设备上登录，如果问题请及时联系管理员。设备IP【%s】",
					DateUtils.getTodayStr("yyyy-MM-dd HH:mm"), _remoteAddr);
			SecurityUtils.getSecurityManager().invalidate(sessionId, reason);
		}
	}

	/**
	 * 登录失败-- 可以记录失败的次数等。
	 */
	@Override
	public void onLoginFailure(HttpServletRequest request) {
		String username = WebUtils.getCleanParam(request, AuthenticationToken.username);
		UserUtils.isValidateLoginCode(username, true);
	}

	/**
	 * 退出登录，清除缓存等
	 */
	@Override
	public void onLogout(Subject subject) {
		Set<Principal> principals = subject.getPrincipals();
		for (Principal principal : principals) {
			UserUtils.removeUserCache(principal.getId());
		}
	}

	/**
	 * 存储失效的原因
	 */
	@Override
	public void onInvalidate(String sessionId, String reason) {
		String key = new StringBuilder("invalidate-reason:").append(sessionId).toString();
		CacheUtils.getSessCache().put(key, reason);
	}

	/**
	 * 获取session失效的原因
	 */
	@Override
	public String resolveReason(String sessionId) {
		String key = new StringBuilder("invalidate-reason:").append(sessionId).toString();
		String reason = CacheUtils.getSessCache().get(key);
		if (StringUtils.isNotBlank(reason)) {
			CacheUtils.getSessCache().delete(key);
		}
		return reason;
	}
}