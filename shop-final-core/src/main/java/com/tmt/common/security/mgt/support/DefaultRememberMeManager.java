package com.tmt.common.security.mgt.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.SerializationUtils;

import com.tmt.common.codec.Encodes;
import com.tmt.common.security.context.AuthenticationToken;
import com.tmt.common.security.cookie.Cookie;
import com.tmt.common.security.cookie.SimpleCookie;
import com.tmt.common.security.mgt.RememberMeManager;
import com.tmt.common.security.principal.Principal;
import com.tmt.common.security.subject.Subject;

/**
 * 默认的记住我， 先放入cookie中
 * @author lifeng
 */
public class DefaultRememberMeManager implements RememberMeManager {

	private int maxAge = 60 * 60* 24 * 10;
	private String domain;
	private String path;
	
	public int getMaxAge() {
		return maxAge;
	}

	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
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
	 * 如何获取身份信息
	 */
	@Override
	public Principal getPrincipal(HttpServletRequest request, HttpServletResponse response) {
		Principal principal = null;
		try {
			byte[] _principal = this.getRememberedSerializedIdentity(request);
			if (_principal != null) {
				principal = (Principal) SerializationUtils.deserialize(_principal);
			}
		}catch(Exception e) {
			this.forgetIdentity(request, response);
		}
		return principal;
	}
	
	private byte[] getRememberedSerializedIdentity(HttpServletRequest request) {
		
		// 找我设置的身份
		String base64 = this.readCookie(request);
		
		// 设置为删除我
		if (Cookie.DELETED_COOKIE_VALUE.equals(base64)) return null;
		if (base64 != null) {
			base64 = ensurePadding(base64);
			byte[] decoded = Encodes.decodeBase64(base64);
			return decoded;
		}
		return null;
	}
	
	/**
	 * 选择了记住我才会调用
	 */
	@Override
	public void onLoginSuccess(Subject subject, HttpServletRequest request,
			HttpServletResponse response) {
		
		//always clear any previous identity:
		forgetIdentity(request, response);
		
		// 设置记住我
		this.rememberIdentity(subject, request, response);
	}

	@Override
	public void onLoginFailure(HttpServletRequest request,
			HttpServletResponse response) {
		forgetIdentity(request, response);
	}

	@Override
	public void onLogout(HttpServletRequest request,
			HttpServletResponse response) {
		forgetIdentity(request, response);
	}
	
	// 从cookie 中获取数据
	private String readCookie(HttpServletRequest request) {
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (javax.servlet.http.Cookie cookie : cookies) {
				if (AuthenticationToken.rememberMe.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
	
	// 转为base64
	private String ensurePadding(String base64) {
        int length = base64.length();
        if (length % 4 != 0) {
            StringBuilder sb = new StringBuilder(base64);
            for (int i = 0; i < length % 4; ++i) {
                sb.append('=');
            }
            base64 = sb.toString();
        }
        return base64;
    }
	
	/**
	 * 删除记住我的定义
	 * @param request
	 * @param response
	 */
	private void forgetIdentity(HttpServletRequest request, HttpServletResponse response) {
		Cookie sessionCookie = new SimpleCookie(AuthenticationToken.rememberMe);
		sessionCookie.removeFrom(request, response);
	}
	
	/**
	 * 记住我
	 * @param request
	 * @param response
	 */
	private void rememberIdentity(Subject subject, HttpServletRequest request, HttpServletResponse response) {
		Object principal = subject.getPrincipal();
		byte[] serialized = SerializationUtils.serialize(principal);
		String base64 = Encodes.encodeBase64(serialized);
		Cookie cookie = new SimpleCookie(AuthenticationToken.rememberMe);
		cookie.setDomain(this.getDomain());
		cookie.setPath(this.getPath());
		cookie.setMaxAge(this.getMaxAge());
		cookie.setValue(base64);
		cookie.saveTo(request, response);
	}
}