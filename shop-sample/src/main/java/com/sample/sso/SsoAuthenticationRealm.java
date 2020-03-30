package com.sample.sso;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.sample.sso.domain.User;
import com.tmt.core.security.context.AuthorizationInfo;
import com.tmt.core.security.exception.AuthenticationException;
import com.tmt.core.security.principal.Principal;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.realm.AuthenticationRealm;
import com.tmt.system.service.UserService;

/**
 * SSO 的 身份 和权限获取方式
 * 
 * @author lifeng
 */
public class SsoAuthenticationRealm extends AuthenticationRealm {

	/**
	 * 外部协商的用户 KEY
	 */
	private String USER_SESSION_KEY = "USER";

	@Autowired
	private UserService userService;

	/**
	 * 通过 Sso的用户信息转为本地的身份信息
	 */
	@Override
	public Principal doAuthentication(HttpServletRequest request) throws AuthenticationException {

		// SSO 用户
		User user = (User) request.getSession().getAttribute(USER_SESSION_KEY);

		// 本地用户
		com.tmt.system.entity.User sysUser = new com.tmt.system.entity.User();
		sysUser.setLoginName(user.personId);
		sysUser.setName(user.personName);
		UserAccount account = new UserAccount();
		account.setId(user.personId);
		account.setUser(sysUser);
		account.setType((byte)1);
		userService.registerByAccount(account);

		// 创建Principal
		Principal principal = new Principal();
		principal.setId(account.getUserId());
		principal.setAccount(account.getId());
		return principal;
	}

	/**
	 * 通过身份信息获取权限信息
	 */
	@Override
	public AuthorizationInfo doGetAuthorizationInfo(Principal principal) {
		return super.doGetAuthorizationInfo(principal);
	}
}
