package com.tmt.core.security.mgt.support;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.CollectionUtils;

import com.tmt.core.exception.ErrorCode;
import com.tmt.core.security.context.AuthenticationToken;
import com.tmt.core.security.context.AuthorizationInfo;
import com.tmt.core.security.exception.AuthenticationException;
import com.tmt.core.security.mgt.RememberMeManager;
import com.tmt.core.security.mgt.SecurityManager;
import com.tmt.core.security.permission.Permission;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.principal.PrincipalStrategy;
import com.tmt.core.security.realm.Realm;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.subject.support.DefaultSubject;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;

/**
 * 默认的管理器
 * 
 * @author lifeng
 */
public class DefaultSecurityManager implements SecurityManager {

	// 只支持一个域，会提供一个基础的域管理权限信息
	private Realm realm;
	private PrincipalStrategy principalStrategy;
	private RememberMeManager rememberMeManager;

	// 域
	public Realm getRealm() {
		return realm;
	}

	public void setRealm(Realm realm) {
		this.realm = realm;
	}

	// 身份 策略
	public PrincipalStrategy getPrincipalStrategy() {
		return principalStrategy;
	}

	public void setPrincipalStrategy(PrincipalStrategy principalStrategy) {
		this.principalStrategy = principalStrategy;
	}

	// 记住我
	public RememberMeManager getRememberMeManager() {
		return rememberMeManager;
	}

	public void setRememberMeManager(RememberMeManager rememberMeManager) {
		this.rememberMeManager = rememberMeManager;
	}

	public boolean isPermitted(Subject subject, Permission permission) {
		Set<String> _permissions = this.loadPermissions(subject);
		return !CollectionUtils.isEmpty(_permissions) && permission.implies(_permissions);
	}

	public boolean hasRole(Subject subject, Permission role) {
		Set<String> _roles = this.loadRoles(subject);
		return !CollectionUtils.isEmpty(_roles) && role.implies(_roles);
	}

	@Override
	public boolean isPermitted(Subject subject, String permission) {
		Set<String> _permissions = this.loadPermissions(subject);
		return !CollectionUtils.isEmpty(_permissions) && _permissions.contains(permission);
	}

	@Override
	public boolean[] isPermitted(Subject subject, String... permissions) {
		Set<String> _permissions = this.loadPermissions(subject);
		boolean[] result;
		if (permissions != null && permissions.length != 0) {
			int size = permissions.length;
			result = new boolean[size];
			int i = 0;
			for (String p : permissions) {
				result[i++] = !CollectionUtils.isEmpty(_permissions) && _permissions.contains(p);
			}
		} else {
			result = new boolean[0];
		}
		return result;
	}

	@Override
	public boolean isPermittedAll(Subject subject, String... permissions) {
		Set<String> _permissions = this.loadPermissions(subject);
		if (CollectionUtils.isEmpty(_permissions)) {
			return false;
		}
		if (permissions != null && permissions.length != 0) {
			for (String p : permissions) {
				if (!_permissions.contains(p)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public boolean hasRole(Subject subject, String role) {
		Set<String> _roles = this.loadRoles(subject);
		return !CollectionUtils.isEmpty(_roles) && _roles.contains(role);
	}

	@Override
	public boolean[] hasRoles(Subject subject, String... roles) {
		Set<String> _roles = this.loadRoles(subject);
		boolean[] result;
		if (roles != null && roles.length != 0) {
			int size = roles.length;
			result = new boolean[size];
			int i = 0;
			for (String p : roles) {
				result[i++] = !CollectionUtils.isEmpty(_roles) && _roles.contains(p);
			}
		} else {
			result = new boolean[0];
		}
		return result;
	}

	@Override
	public boolean hasAllRoles(Subject subject, String... roles) {
		Set<String> _roles = this.loadRoles(subject);
		if (CollectionUtils.isEmpty(_roles)) {
			return false;
		}
		if (roles != null && roles.length != 0) {
			for (String p : roles) {
				if (!_roles.contains(p)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 加载权限
	 * 
	 * @param subject
	 * @return
	 */
	private Set<String> loadPermissions(Subject subject) {
		Set<String> permissions = subject.getPermissions();
		if (permissions == null) {
			AuthorizationInfo authorization = realm.getCachedAuthorizationInfo(subject.getPrincipal());
			subject.setPermissions(authorization.getPermissions());
			subject.setRoles(authorization.getRoles());
		}
		return subject.getPermissions();
	}

	/**
	 * 加载角色
	 * 
	 * @param subject
	 * @return
	 */
	private Set<String> loadRoles(Subject subject) {
		Set<String> roles = subject.getRoles();
		if (roles == null) {
			AuthorizationInfo authorization = realm.getCachedAuthorizationInfo(subject.getPrincipal());
			subject.setPermissions(authorization.getPermissions());
			subject.setRoles(authorization.getRoles());
		}
		return subject.getRoles();
	}

	@Override
	public void login(Subject subject, HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		// 验证
		Principal principal;
		try {
			principal = realm.doAuthentication(request);
		} catch (AuthenticationException ae) {
			this.onLoginFailure(request, response);
			throw ae;
		}

		// 必须返回身份
		if (principal == null) {
			this.onLoginFailure(request, response);
			throw new AuthenticationException(ErrorCode.U_P_FAILURE);
		}

		// 登录成功 保存用户凭证，设置到subject中即可
		this.login(subject, principal, request, response);
	}

	/**
	 * 指定了身份来登录
	 */
	@Override
	public void login(Subject subject, Principal principal, HttpServletRequest request, HttpServletResponse response) {
		this.login(subject, principal, true, request, response);
	}

	/**
	 * 真实的登录
	 * 
	 * @param subject
	 * @param principal
	 * @param request
	 * @param response
	 */
	private void login(Subject subject, Principal principal, boolean authenticated, HttpServletRequest request,
			HttpServletResponse response) {
		// 设置相关信息到主体中
		subject.setPrincipal(principal);
		subject.setAuthenticated(authenticated);

		// 创建身份
		principalStrategy.createPrincipal(subject, request, response);

		// 通知登录成功
		this.onLoginSuccess(subject, request, response);
	}

	/**
	 * 退出登录
	 */
	@Override
	public void logout(Subject subject, HttpServletRequest request, HttpServletResponse response) {
		principalStrategy.invalidatePrincipal(subject, request, response);
		this.onLogout(subject, request, response);
	}

	/**
	 * 获取用户信息
	 */
	@Override
	public Subject createSubject(HttpServletRequest request, HttpServletResponse response) {

		// 基本的 Subject
		Subject subject = new DefaultSubject();

		// 获取身份
		principalStrategy.resolvePrincipal(subject, request, response);

		// session 失效的原因
		if (StringUtils.hasText(subject.getSessionId()) && subject.getSession() == null) {
			subject.setReason(this.realm.resolveReason(subject.getSessionId()));
		}

		// 判断是否有身份信息
		if (rememberMeManager != null && subject.getPrincipal() == null) {
			Principal rprincipal = null;
			if ((rprincipal = this.rememberMeManager.getPrincipal(request, response)) != null) {
				this.login(subject, rprincipal, false, request, response);
			}
		}

		return subject;
	}

	/**
	 * 将此身份失效
	 */
	@Override
	public void invalidate(String sessionId, String reason) {
		principalStrategy.invalidatePrincipal(sessionId);
		this.realm.onInvalidate(sessionId, reason);
	}

	/**
	 * 登录成功
	 * 
	 * @param subject
	 */
	void onLoginSuccess(Subject subject, HttpServletRequest request, HttpServletResponse response) {
		realm.onLoginSuccess(subject, request);

		// 记住我
		if (this.rememberMeManager != null) {
			String rememberMe = WebUtils.getCleanParam(request, AuthenticationToken.rememberMe);
			if (StringUtils.hasText(rememberMe) && ("on".equals(rememberMe) || "1".equals(rememberMe))) {
				this.rememberMeManager.onLoginSuccess(subject, request, response);
			}
		}
	}

	/**
	 * 登录失败
	 * 
	 * @param request
	 */
	void onLoginFailure(HttpServletRequest request, HttpServletResponse response) {
		realm.onLoginFailure(request);
		if (this.rememberMeManager != null) {
			this.rememberMeManager.onLoginFailure(request, response);
		}
	}

	/**
	 * 退出登录
	 */
	void onLogout(Subject subject, HttpServletRequest request, HttpServletResponse response) {
		realm.onLogout(subject);
		if (this.rememberMeManager != null) {
			this.rememberMeManager.onLogout(request, response);
		}
	}
}