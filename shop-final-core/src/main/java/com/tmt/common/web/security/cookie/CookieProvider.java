package com.tmt.common.web.security.cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.CookieUtils;
import com.tmt.common.utils.StringUtil3;

/**
 * cookie cache
 * key 放入cookie ，value 放入缓存
 * cookie 是与用户相关的信息
 * @author root
 *
 */
public class CookieProvider {

	
   //------------cache cookie session--------------------------
	/**
	 * 存储格式 key: SESS#0#XXXX ，其中0为用户id,XXXX为实际的缓存key
	 * @param key
	 * @param value
	 * @see com.tmt.system.security.session.SessionProvider
	 */
    public static void setAttribute(HttpServletResponse response, String key, Object value) {
    	CookieProvider.setAttribute(null, response, key, value);
    }
   /**
	 * 存储格式 key: SESS#0#XXXX ，其中0为用户id,XXXX为实际的缓存key
	 * @param key
	 * @param value
	 * @see com.tmt.system.security.session.SessionProvider
	 */
	public static void setAttribute(HttpServletRequest request, HttpServletResponse response, String key, Object value) {
		request = request==null?ContextHolderUtils.getRequest():request;
		String validateCodeKey = CookieUtils.getCookie(request, key);
		if (StringUtil3.isBlank(validateCodeKey) ) {
			validateCodeKey = IdGen.stringKey();
			CookieUtils.setCookie(response, key, validateCodeKey, -1);
		}
		CacheUtils.getSessCache().put(validateCodeKey, value);
	}
	
	/**
	 * 获取用户属性
	 * @param key
	 * @return
	 * @see com.tmt.system.security.session.SessionProvider
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAttribute(HttpServletRequest request, String key) {
		try{
			String _key = CookieUtils.getCookie(request, key);
			if( StringUtil3.isNotBlank(_key)) {
				Object obj =  CacheUtils.getSessCache().get(_key);
				return (T)(obj);
			}
			return null;
		}catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * 得到值，并清除相应值
	 * @param response
	 * @param key
	 * @return
	 */
	public static <T> T getAndClearAttribute(HttpServletRequest request, HttpServletResponse response, String key) {
		T t = CookieProvider.getAttribute(request, key);
		CookieProvider.removeAttribute(request, response, key);
		return t;
	}
	
	/**
	 * 删除用户属性
	 * @param key
	 */
	public static void removeAttribute(HttpServletRequest request, HttpServletResponse response, String key) {
		String _key = CookieUtils.getCookie(request, key);
		if( StringUtil3.isNotBlank(_key)) {
			CookieUtils.remove(ContextHolderUtils.getRequest(), response, key);
			CacheUtils.getSessCache().delete(_key);
		}
	}
}
