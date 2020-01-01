package com.tmt.common.web.security.session;

import com.tmt.common.security.principal.Session;
import com.tmt.common.security.subject.Subject;
import com.tmt.common.security.utils.SecurityUtils;

/**
 * 会话数据存储方案
 * @author lifeng
 */
public class SessionProvider {
   
   /**
    * 如果没有则，不会强制创建一个session
    * @return
    */
   public static Session getSession() {
	   Subject subject = SecurityUtils.getSubject();
	   return subject != null ? subject.getSession() : null;
   }
   
   /**
    * 调用 自定义的 session 
    * @param key
    * @param value
    */
	public static void setAttribute(String key, Object value) {
		Session session = getSession();
		if (session != null) {
			session.setAttribute(key, value);
		}
	}
	
   /**
    * 调用 自定义的 session 
    * @param key
    * @param value
    */
	@SuppressWarnings("unchecked")
	public static <T> T getAttribute(String key) {
		Session session = getSession();
		if (session != null) {
			return (T)(session.getAttribute(key));
		}
		return null;
	}
	
	/**
	 * 删除会话属性
	 * @param key
	 */
	public static void removeAttribute(String key) {
		Session session = getSession();
		if (session != null) {
			session.removeAttribute(key);
		}
	}
}