package com.tmt.core.security.principal.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.tmt.core.security.cookie.Cookie;
import com.tmt.core.security.cookie.SimpleCookie;
import com.tmt.core.security.principal.PrincipalStrategy;
import com.tmt.core.security.principal.Session;
import com.tmt.core.security.principal.SessionRepository;
import com.tmt.core.security.subject.Subject;

/**
 * 基于cookie的身份策略一般用在后台 但也是一种简单的分布式session管理方案
 * 
 * @author lifeng
 */
public class CookiePrincipalStrategy implements PrincipalStrategy {

	private String cookieName = "SESSION";
	private String domain;
	private String path;
	private SessionRepository<? extends Session> sessionRepository;

	public SessionRepository<? extends Session> getSessionRepository() {
		return sessionRepository;
	}

	public void setSessionRepository(SessionRepository<? extends Session> sessionRepository) {
		this.sessionRepository = sessionRepository;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * 创建
	 * 
	 * @param subject
	 * @param request
	 * @param response
	 */
	@Override
	public void createPrincipal(Subject subject, HttpServletRequest request, HttpServletResponse response) {
		Session session = sessionRepository.createSession(request, subject.getPrincipal(), subject.isAuthenticated());
		subject.setSession(session);
		this.onNewSession(session, request, response);
	}

	/**
	 * 失效
	 */
	@Override
	public void invalidatePrincipal(Subject subject, HttpServletRequest request, HttpServletResponse response) {
		sessionRepository.removeSession(request, subject.getSession());
		this.onInvalidateSession(request, response);
	}

	/**
	 * 将此身份失效
	 */
	@Override
	public void invalidatePrincipal(String sessionId) {
		sessionRepository.invalidateSession(sessionId);
	}

	/**
	 * 获取身份
	 */
	@Override
	public void resolvePrincipal(Subject subject, HttpServletRequest request, HttpServletResponse response) {

		// 获取sesson
		Session session = null;
		String sessionId = this.getRequestedSessionId(request);
		if (StringUtils.hasText(sessionId)) {
			session = sessionRepository.getSession(request, sessionId);
			if (session == null) {
				this.onInvalidateSession(request, response);
			}

			// 保存 sessionId
			subject.setSessionId(sessionId);
		}

		// 保存 session 信息
		if (session != null) {
			subject.setSession(session);
		}
	}

	// 相关操作
	protected String getRequestedSessionId(HttpServletRequest request) {
		return this.readCookie(request);
	}

	protected void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		this.writeCookie(request, response, session.getId());
	}

	protected void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		this.removeCookie(request, response);
	}

	// 从cookie 中获取数据
	public String readCookie(HttpServletRequest request) {
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (javax.servlet.http.Cookie cookie : cookies) {
				if (this.cookieName.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	// 写入cookie
	public void writeCookie(HttpServletRequest request, HttpServletResponse response, String cookieValue) {
		Cookie sessionCookie = new SimpleCookie(this.cookieName);
		sessionCookie.setDomain(this.getDomain());
		sessionCookie.setPath(this.getPath());
		sessionCookie.setValue(cookieValue);
		sessionCookie.saveTo(request, response);
	}

	// 删除cookie
	public void removeCookie(HttpServletRequest request, HttpServletResponse response) {
		Cookie sessionCookie = new SimpleCookie(this.cookieName);
		sessionCookie.removeFrom(request, response);
	}
}