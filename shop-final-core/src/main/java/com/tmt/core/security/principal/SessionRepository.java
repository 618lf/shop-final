package com.tmt.core.security.principal;

import javax.servlet.http.HttpServletRequest;

/**
 * Session 的存储方式
 * 
 * @author lifeng
 *
 * @param <S>
 */
public interface SessionRepository<S extends Session> {

	/**
	 * 设置存活时间
	 * 
	 * @param sessionTimeout
	 */
	void setSessionTimeout(int sessionTimeout);

	/**
	 * 保存session
	 * 
	 * @param session
	 */
	S createSession(HttpServletRequest request, Principal principal, boolean authenticated);

	/**
	 * 得到session
	 * 
	 * @param id
	 * @return
	 */
	S getSession(HttpServletRequest request, String sessonId);

	/**
	 * 删除session
	 * 
	 * @param id
	 */
	void removeSession(HttpServletRequest request, Session session);

	/**
	 * 失效session
	 * 
	 * @param id
	 */
	void invalidateSession(String sessionId);
}
