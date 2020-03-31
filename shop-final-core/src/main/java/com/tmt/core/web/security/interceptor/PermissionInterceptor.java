package com.tmt.core.web.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import com.tmt.core.security.permission.Permission;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;

/**
 * 权限验证
 * 
 * @author lifeng
 */
public class PermissionInterceptor extends MethodInterceptor {

	private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
	private final String ACCESS_DENIED_MSG = "访问受限,请登录或联系管理员授权!";

	@Override
	protected boolean doHandle(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {

		// 用户主体
		Subject subject = SecurityUtils.getSubject();

		// 获取方法级的权限信息
		Permission requiresRoles = handler.getRequiresRoles();
		Permission requiresPermissions = handler.getRequiresPermissions();

		// 是否继续
		boolean continued = true;

		// 配置了权限信息
		if (requiresPermissions != null && requiresPermissions != Permission.NONE) {
			continued = subject.isPermitted(requiresPermissions);
		}

		// 配置了角色信息
		if (continued && requiresRoles != null && requiresRoles != Permission.NONE) {
			continued = subject.hasRole(requiresRoles);
		}

		// token 失效
		if (!continued) {
			try {
				response.sendError(401, ACCESS_DENIED_MSG);
			} catch (Exception e) {
				logger.error("权限验证  Error", e);
			}
		}
		// 返回是否继续处理
		return continued;
	}
}
