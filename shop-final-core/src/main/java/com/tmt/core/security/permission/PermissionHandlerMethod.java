package com.tmt.core.security.permission;

import java.lang.annotation.Annotation;
import java.util.concurrent.atomic.AtomicBoolean;

import com.tmt.core.security.annotation.Logical;
import com.tmt.core.security.annotation.RequiresPermissions;
import com.tmt.core.security.annotation.RequiresRoles;
import com.tmt.core.security.annotation.Token;
import com.tmt.core.security.permission.impl.AndPermission;
import com.tmt.core.security.permission.impl.OrPermission;
import com.tmt.core.security.permission.impl.SinglePermission;

/**
 * 保存方法的权限
 * 
 * @author lifeng
 */
public abstract class PermissionHandlerMethod {

	/**
	 * 缓存方法的权限注解
	 */
	protected Permission requiresRoles;
	protected Permission requiresPermissions;
	protected Token token;
	protected AtomicBoolean tokenInit = new AtomicBoolean(false);

	/**
	 * 角色
	 */
	public Permission getRequiresRoles() {
		if (this.requiresRoles == null) {
			RequiresRoles roles = (RequiresRoles) this.getMethodAnnotation(RequiresRoles.class);
			if (roles == null || roles.value().length == 0) {
				this.requiresRoles = Permission.NONE;
			} else if (roles.value().length == 1) {
				this.requiresRoles = new SinglePermission(roles.value()[0]);
			} else if (roles.logical() == Logical.AND) {
				this.requiresRoles = new AndPermission(roles.value());
			} else if (roles.logical() == Logical.OR) {
				this.requiresRoles = new OrPermission(roles.value());
			}
		}
		return this.requiresRoles;
	}

	/**
	 * 权限
	 */
	public Permission getRequiresPermissions() {
		if (this.requiresPermissions == null) {
			RequiresPermissions roles = (RequiresPermissions) this.getMethodAnnotation(RequiresPermissions.class);
			if (roles == null || roles.value().length == 0) {
				this.requiresPermissions = Permission.NONE;
			} else if (roles.value().length == 1) {
				this.requiresPermissions = new SinglePermission(roles.value()[0]);
			} else if (roles.logical() == Logical.AND) {
				this.requiresPermissions = new AndPermission(roles.value());
			} else if (roles.logical() == Logical.OR) {
				this.requiresPermissions = new OrPermission(roles.value());
			}
		}
		return this.requiresPermissions;
	}

	/**
	 * 获得token
	 * 
	 * @return
	 */
	public Token getToken() {
		if (tokenInit.compareAndSet(false, true)) {
			this.token = this.getMethodAnnotation(Token.class);
		}
		return token;
	}

	/**
	 * 获取方法的注解
	 * 
	 * @param <A>
	 * @param annotationType
	 * @return
	 */
	public abstract <A extends Annotation> A getMethodAnnotation(Class<A> annotationType);
}
