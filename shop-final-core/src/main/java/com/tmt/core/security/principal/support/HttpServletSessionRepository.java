package com.tmt.core.security.principal.support;

import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.principal.Session;
import com.tmt.core.security.principal.SessionRepository;
import com.tmt.core.security.principal.support.HttpServletSessionRepository.HttpServletSession;
import com.tmt.core.utils.Maps;

/**
 * 基于Http Session 的管理方式
 * 
 * @author lifeng
 */
public class HttpServletSessionRepository implements SessionRepository<HttpServletSession> {

	private String SESSION_ATTR_PREFIX = "attr:";
	private String PRINCIPAL_ATTR = "p";
	private String AUTHENTICATED_ATTR = "authed";
	private String RUNASPRINCIPALS_ATTR = "rps";
	private int sessionTimeout = 1800;

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	@Override
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	@Override
	public HttpServletSession createSession(HttpServletRequest request, Principal principal, boolean authenticated) {
		HttpServletSession session = new HttpServletSession(request.getSession(true));
		session.principal = principal;
		session.authenticated = authenticated;
		session.saveDelta();
		return session;
	}

	@Override
	public HttpServletSession getSession(HttpServletRequest request, String sessonId) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		return this.loadSession(session);
	}

	@SuppressWarnings("unchecked")
	private HttpServletSession loadSession(HttpSession httpSession) {
		HttpServletSession session = new HttpServletSession(httpSession);
		session.principal = (Principal) (httpSession.getAttribute(PRINCIPAL_ATTR));
		session.authenticated = (Boolean) (httpSession.getAttribute(AUTHENTICATED_ATTR));
		session.runAsPrincipals = (Stack<Principal>) (httpSession.getAttribute(RUNASPRINCIPALS_ATTR));
		session.sessionAttrs = (Map<String, Object>) (httpSession.getAttribute(SESSION_ATTR_PREFIX));
		return session;
	}

	@Override
	public void removeSession(HttpServletRequest request, Session session) {
		HttpSession httpSession = request.getSession(false);
		if (httpSession != null) {
			httpSession.invalidate();
		}
		((HttpServletSession) session).stoped = true;
	}

	@Override
	public void invalidateSession(String sessionId) {

	}

	public class HttpServletSession implements Session {

		private boolean stoped;
		private HttpSession session;
		protected Principal principal = null;
		protected boolean authenticated = false;
		protected Stack<Principal> runAsPrincipals;
		protected Map<String, Object> sessionAttrs;

		public HttpServletSession(HttpSession session) {
			this.session = session;
		}

		@Override
		public String getId() {
			return session.getId();
		}

		@Override
		public Principal getPrincipal() {
			return principal;
		}

		@Override
		public boolean isAuthenticated() {
			return authenticated;
		}

		@Override
		public long getCreationTime() {
			return this.session.getCreationTime();
		}

		@Override
		public Stack<Principal> getRunAsPrincipals() {
			return runAsPrincipals;
		}

		@Override
		public void setRunAsPrincipals(Stack<Principal> runAsPrincipals) {
			this.runAsPrincipals = runAsPrincipals;
		}

		@Override
		public void setAuthenticated(boolean authenticated) {
			this.authenticated = authenticated;
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> T getAttribute(String attributeName) {
			return (T) (sessionAttrs != null ? sessionAttrs.get(attributeName) : null);
		}

		@Override
		public Set<String> getAttributeNames() {
			return sessionAttrs != null ? sessionAttrs.keySet() : null;
		}

		@Override
		public <T> void setAttribute(String attributeName, T attributeValue) {
			if (sessionAttrs == null) {
				sessionAttrs = Maps.newHashMap();
			}
			sessionAttrs.put(attributeName, attributeValue);
		}

		@Override
		public void removeAttribute(String attributeName) {
			if (sessionAttrs != null && attributeName != null) {
				sessionAttrs.remove(attributeName);
			}
		}

		@Override
		public void destory() {
			this.principal = null;
			this.runAsPrincipals = null;
			this.sessionAttrs = null;
		}

		/**
		 * 最后提交
		 */
		@Override
		public void onCommit() {
			if (!this.stoped) {
				this.saveDelta();
			}
		}

		public void saveDelta() {
			try {
				session.setAttribute(PRINCIPAL_ATTR, this.getPrincipal());
				session.setAttribute(AUTHENTICATED_ATTR, this.isAuthenticated());
				session.setAttribute(RUNASPRINCIPALS_ATTR, this.getRunAsPrincipals());
				session.setAttribute(SESSION_ATTR_PREFIX, sessionAttrs);
			} catch (Exception e) {
			}
		}

		public boolean equals(Object obj) {
			return obj instanceof Session && this.getId().equals(((Session) obj).getId());
		}

		public int hashCode() {
			return this.getId().hashCode();
		}
	}
}
