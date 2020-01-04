package com.tmt.core.security.principal.support;

import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.cache.redis.RedisUtils;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.principal.Session;
import com.tmt.core.security.principal.SessionRepository;
import com.tmt.core.security.principal.support.RedisSessionRepository.RedisSession;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;

/**
 * redis 存储 session
 * @author lifeng
 */
public class RedisSessionRepository implements SessionRepository<RedisSession> {
	
	private String SESSION_PREFIX = "session:";
	private String SESSION_ATTR_PREFIX = "attr:";
	private String CREATION_TIME_ATTR = "ct";
	private String PRINCIPAL_ATTR = "p";
	private String AUTHENTICATED_ATTR = "authed";
	private String RUNASPRINCIPALS_ATTR = "rps";
	private int sessionTimeout = 1800;
	
	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	/**
	 * 创建session
	 */
	@Override
	public RedisSession createSession(Principal principal, boolean authenticated) {
		RedisSession session = new RedisSession();
		session.principal = principal;
		session.authenticated = authenticated;
		session.saveDelta();
		return session;
	}

	/**
	 * 获取session
	 */
	@Override
	public RedisSession getSession(String id) {
		String key = this.getSessionKey(id);
		Map<String, Object> entries = RedisUtils.hGetAll(key);
		if (entries == null || entries.isEmpty()) {
			return null;
		}
		RedisSession session = loadSession(id, entries);
		RedisUtils.expire(key, getSessionTimeout());
		return session;
	}
	
	@SuppressWarnings("unchecked")
	private RedisSession loadSession(String id, Map<String, Object> entries) {
		RedisSession session = new RedisSession(id);
		for (Map.Entry<String, Object> entry : entries.entrySet()) {
			 String key = entry.getKey();
			 if (CREATION_TIME_ATTR.equals(key)) {
				session.creationTime = ((Long) entry.getValue());
			 }
			 else if (PRINCIPAL_ATTR.equals(key)) {
				session.principal = (Principal) (entry.getValue());
			 }
			 else if (AUTHENTICATED_ATTR.equals(key)) {
				session.authenticated = ((Boolean) entry.getValue());
			 }
			 else if (RUNASPRINCIPALS_ATTR.equals(key)) {
				session.runAsPrincipals = (Stack<Principal>)entry.getValue();
			 }
			 else if (key.startsWith(SESSION_ATTR_PREFIX)) {
				session._setAttribute(key.substring(SESSION_ATTR_PREFIX.length()), entry.getValue());
			 }
		}
		return session;
	}
	
	/**
	 * 删除session
	 */
	@Override
	public void removeSession(Session session) {
		if (session != null && StringUtils.hasText(session.getId())) {
			RedisUtils.delete(this.getSessionKey(session.getId()));
		}
	}
	
	/**
	 * 删除session
	 */
	@Override
	public void invalidateSession(String sessionId) {
		if (StringUtils.hasText(sessionId)) {
			RedisUtils.delete(this.getSessionKey(sessionId));
		}
	}
	
	/**
	 * session 的名称
	 * @param sessionId
	 * @return
	 */
	public String getSessionKey(String sessionId) {
		return new StringBuilder(SESSION_PREFIX).append("sessions:").append(sessionId).toString();
	}
	
	/**
	 * session 的存储名称
	 * @param attributeName
	 * @return
	 */
	public String getSessionAttrNameKey(String attributeName) {
		return new StringBuilder(SESSION_ATTR_PREFIX).append(attributeName).toString();
	}
	
	/**
	 * redis session
	 * @author lifeng
	 */
	public class RedisSession implements Session {
		
		protected String id;
		protected long creationTime = System.currentTimeMillis();
		protected Principal principal = null;
		protected boolean authenticated = false;
		protected Stack<Principal> runAsPrincipals;
		protected Map<String, Object> sessionAttrs;
		
		public RedisSession() {
			this.id = UUID.randomUUID().toString();
		}
		
		public RedisSession(String id) {
			this.id = id;
		}
		
		@Override
		public Principal getPrincipal() {
			return principal;
		}
		
		@Override
		public boolean isAuthenticated() {
			return authenticated;
		}
		
		@Autowired
		public Stack<Principal> getRunAsPrincipals() {
			return runAsPrincipals;
		}

		@Override
		public String getId() {
			return this.id;
		}

		@Override
		public long getCreationTime() {
			return this.creationTime;
		}
		
		/**
		 * 会立即刷新数据
		 */
		@Override
		public void setRunAsPrincipals(Stack<Principal> runAsPrincipals) {
			this.runAsPrincipals = runAsPrincipals;
			this.putAndFlush(RUNASPRINCIPALS_ATTR, runAsPrincipals);
		}
		
		/**
		 * 会立即刷新数据
		 */
		@Override
		public void setAuthenticated(boolean authenticated) {
			this.authenticated = authenticated;
			this.putAndFlush(AUTHENTICATED_ATTR, runAsPrincipals);
		}
		
		/**
		 * 得到属性
		 */
		@Override
		@SuppressWarnings("unchecked")
		public <T> T getAttribute(String attributeName) {
			return (T) (sessionAttrs != null ? sessionAttrs.get(attributeName) : null);
		}

		/**
		 * 得到所有的属性名称
		 */
		@Override
		public Set<String> getAttributeNames() {
			return sessionAttrs != null ? sessionAttrs.keySet() : null;
		}

		/**
		 * 设置属性
		 */
		@Override
		public <T> void setAttribute(String attributeName, T attributeValue) {
			if (sessionAttrs == null) {
				sessionAttrs = Maps.newHashMap();
			}
			sessionAttrs.put(attributeName, attributeValue);
			putAndFlush(getSessionAttrNameKey(attributeName), attributeValue);
		}

		/**
		 * 删除属性
		 */
		@Override
		public void removeAttribute(String attributeName) {
			if (sessionAttrs != null && attributeName != null) {
				sessionAttrs.remove(attributeName);
				putAndFlush(getSessionAttrNameKey(attributeName), null);
			}
		}
		
		/**
		 * 请求结束后会销毁内容中的数据
		 */
		@Override
		public void destory() {
			this.id = null; this.principal = null;
			if (this.runAsPrincipals != null) {this.runAsPrincipals.clear();} this.runAsPrincipals = null;
			if (this.sessionAttrs != null) {sessionAttrs.clear();} this.sessionAttrs = null;
		}
		
		@Override
		public void onCommit() {
		}

		// 立即刷新数据(自动删除 v == null 的数据)
		private void putAndFlush(String a, Object v) {
			String key = getSessionKey(this.id);
			if (v == null) {
				RedisUtils.hDel(key, a);
			} else {
				RedisUtils.hSet(key, a, v);
			}
		}
		
		// 初始化才刷新数据
		public void saveDelta() {
			String key = getSessionKey(this.id);
			Map<String, Object> delta = Maps.newHashMap();
			delta.put(CREATION_TIME_ATTR, this.getCreationTime());
			delta.put(PRINCIPAL_ATTR, this.getPrincipal());
			delta.put(AUTHENTICATED_ATTR, this.isAuthenticated());
			RedisUtils.hMSet(key, delta); RedisUtils.expire(key, getSessionTimeout());
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