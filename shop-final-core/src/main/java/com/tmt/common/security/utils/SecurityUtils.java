package com.tmt.common.security.utils;

import com.tmt.common.security.context.ThreadContext;
import com.tmt.common.security.mgt.SecurityManager;
import com.tmt.common.security.subjct.Subject;

/**
 * 主要是获取Subject
 * 
 * @author lifeng
 */
public abstract class SecurityUtils {

	/**
	 * 系统唯一的一个在系统初始化时设置
	 */
	private static SecurityManager securityManager = null;
	
	/**
	 * 只要是在执行流程中就可以获取这个值 不要在web以外的地方使用
	 * 
	 * @return
	 */
	public static Subject getSubject() {
		return ThreadContext.getSubject();
	}

	/**
	 * 直接返回默认的管理器
	 * @return
	 */
	public static SecurityManager getSecurityManager() {
		return securityManager;
	}

	/**
	 * 设置为全局的管理器
	 * @param securityManager
	 */
	public static void setSecurityManager(SecurityManager securityManager) {
		SecurityUtils.securityManager = securityManager;
	}
}