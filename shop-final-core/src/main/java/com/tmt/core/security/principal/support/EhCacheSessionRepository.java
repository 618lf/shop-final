package com.tmt.core.security.principal.support;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.tmt.core.cache.Cache;
import com.tmt.core.cache.CacheManager;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.principal.Session;
import com.tmt.core.security.principal.SessionRepository;
import com.tmt.core.security.principal.support.EhCacheSessionRepository.EhCacheSession;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;

/**
 * 用 EhCache存储 用户信息
 * 
 * @author lifeng
 */
public class EhCacheSessionRepository implements SessionRepository<EhCacheSession> {

	private String SESSION_PREFIX = "session:";
	private String SESSION_ATTR_PREFIX = "attr:";
	private String CREATION_TIME_ATTR = "ct";
	private String PRINCIPAL_ATTR = "p";
	private String AUTHENTICATED_ATTR = "authed";
	private String RUNASPRINCIPALS_ATTR = "rps";
	private int sessionTimeout = 1800;
	private final Cache sessionCache;

	/**
	 * Session cache
	 * 
	 * @param cacheManager
	 */
	public EhCacheSessionRepository(CacheManager cacheManager) {
		sessionCache = cacheManager.getCache("sessions");
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	@Override
	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	@Override
	public EhCacheSession createSession(HttpServletRequest request, Principal principal, boolean authenticated) {
		EhCacheSession session = new EhCacheSession();
		session.principal = principal;
		session.authenticated = authenticated;
		session.saveDelta();
		return session;
	}

	@Override
	public EhCacheSession getSession(HttpServletRequest request, String id) {
		String key = this.getSessionKey(id);
		Map<String, Object> entries = sessionCache.get(key);
		if (entries == null || entries.isEmpty()) {
			return null;
		}
		return loadSession(id, entries);
	}

	@SuppressWarnings("unchecked")
	private EhCacheSession loadSession(String id, Map<String, Object> entries) {
		EhCacheSession session = new EhCacheSession(id);
		for (Map.Entry<String, Object> entry : entries.entrySet()) {
			String key = entry.getKey();
			if (CREATION_TIME_ATTR.equals(key)) {
				session.creationTime = ((Long) entry.getValue());
			} else if (PRINCIPAL_ATTR.equals(key)) {
				session.principal = (Principal) (entry.getValue());
			} else if (AUTHENTICATED_ATTR.equals(key)) {
				session.authenticated = ((Boolean) entry.getValue());
			} else if (RUNASPRINCIPALS_ATTR.equals(key)) {
				session.runAsPrincipals = (Stack<Principal>) entry.getValue();
			} else if (key.startsWith(SESSION_ATTR_PREFIX)) {
				session._setAttribute(key.substring(SESSION_ATTR_PREFIX.length()), entry.getValue());
			}
		}
		return session;
	}

	@Override
	public void removeSession(HttpServletRequest request, Session session) {
		if (session != null && StringUtils.hasText(session.getId())) {
			sessionCache.delete(this.getSessionKey(session.getId()));
		}
	}

	@Override
	public void invalidateSession(String sessionId) {
		if (StringUtils.hasText(sessionId)) {
			sessionCache.delete(this.getSessionKey(sessionId));
		}
	}

	/**
	 * session 的名称
	 * 
	 * @param sessionId
	 * @return
	 */
	public String getSessionKey(String sessionId) {
		return new StringBuilder(SESSION_PREFIX).append(sessionId).toString();
	}

	/**
	 * session 的存储名称
	 * 
	 * @param attributeName
	 * @return
	 */
	public String getSessionAttrNameKey(String attributeName) {
		return new StringBuilder(SESSION_ATTR_PREFIX).append(attributeName).toString();
	}

	public class EhCacheSession implements Session {

		protected String id;
		protected long creationTime = System.currentTimeMillis();
		protected Principal principal = null;
		protected boolean authenticated = false;
		protected Stack<Principal> runAsPrincipals;
		protected Map<String, Object> sessionAttrs;

		public EhCacheSession() {
			this.id = UUID.randomUUID().toString();
		}

		public EhCacheSession(String id) {
			this.id = id;
		}

		@Override
		public String getId() {
			return id;
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
			return creationTime;
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
			this.id = null;
			this.principal = null;
			if (this.runAsPrincipals != null) {
				this.runAsPrincipals.clear();
			}
			this.runAsPrincipals = null;
			if (this.sessionAttrs != null) {
				sessionAttrs.clear();
			}
			this.sessionAttrs = null;
		}

		/**
		 * 最后提交
		 */
		@Override
		public void onCommit() {
			this.saveDelta();
		}

		// 初始化才刷新数据
		public void saveDelta() {
			String key = getSessionKey(this.id);
			Map<String, Object> delta = Maps.newHashMap();
			delta.put(CREATION_TIME_ATTR, this.getCreationTime());
			delta.put(PRINCIPAL_ATTR, this.getPrincipal());
			delta.put(AUTHENTICATED_ATTR, this.isAuthenticated());
			delta.put(RUNASPRINCIPALS_ATTR, this.getRunAsPrincipals());
			if (sessionAttrs != null) {
				Iterator<String> keys = sessionAttrs.keySet().iterator();
				while (keys.hasNext()) {
					String _key = keys.next();
					delta.put(getSessionAttrNameKey(_key), sessionAttrs.get(_key));
				}
			}
			sessionCache.put(key, delta);
		}

		public void _setAttribute(String key, Object v) {
			if (sessionAttrs == null) {
				sessionAttrs = Maps.newHashMap();
			}
			sessionAttrs.put(key, v);
		}

		public boolean equals(Object obj) {
			return obj instanceof Session && this.id.equals(((Session) obj).getId());
		}

		public int hashCode() {
			return this.id.hashCode();
		}
	}
}