package com.tmt.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.config.Globals;
import com.tmt.common.utils.StringUtils;

/**
 * Cookie 操作类
 * 
 * @author lifeng
 */
public class CookieUtils {

	/**
	 * 设置 Cookie（生成时间为1天）
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 */
	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, 60 * 60 * 24);
	}

	/**
	 * 设置 Cookie（生成时间为1天）
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, Integer maxAge) {
		setCookie(response, name, value, maxAge, "/", Globals.domain, null);
	}

	/**
	 * 设置 Cookie
	 * 
	 * @param name
	 *            名称
	 * @param value
	 *            值
	 * @param maxAge
	 *            生存时间（单位秒）
	 * @param uri
	 *            路径
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, Integer maxAge, String path,
			String domain, Boolean secure) {
		Cookie cookie = new Cookie(name, null);
		if (maxAge != null)
			cookie.setMaxAge(maxAge.intValue());
		if (StringUtils.isNotEmpty(path))
			cookie.setPath(path);
		if (StringUtils.isNotEmpty(domain))
			cookie.setDomain(domain);
		if (secure != null)// 为true时 https才会转递到服务器
			cookie.setSecure(secure);
		try {
			cookie.setValue(URLEncoder.encode(value, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		response.addCookie(cookie);
	}

	/**
	 * 获得指定Cookie的值
	 * 
	 * @param name
	 *            名称
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		return getCookie(request, null, name, false);
	}

	/**
	 * 获得指定Cookie的值，并删除。
	 * 
	 * @param name
	 *            名称
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		return getCookie(request, response, name, true);
	}

	/**
	 * 获得指定Cookie的值
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param name
	 *            名字
	 * @param isRemove
	 *            是否移除
	 * @return 值
	 */
	public static String getCookie(HttpServletRequest request, HttpServletResponse response, String name,
			boolean isRemove) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						value = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (isRemove) {
						cookie.setMaxAge(0);
						if (StringUtils.isNotEmpty(Globals.domain))
							cookie.setDomain(Globals.domain);
						response.addCookie(cookie);
					}
				}
			}
		}
		return value;
	}

	/**
	 * 移除
	 * 
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void remove(HttpServletRequest request, HttpServletResponse response, String name, String path,
			String domain) {
		try {
			name = URLEncoder.encode(name, "UTF-8");
			Cookie localCookie = new Cookie(name, "deleteMe");
			localCookie.setMaxAge(0);
			if (StringUtils.isNotEmpty(path))
				localCookie.setPath(path);
			if (StringUtils.isNotEmpty(domain))
				localCookie.setDomain(domain);
			response.addCookie(localCookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移除
	 * 
	 * @param request
	 * @param response
	 * @param name
	 */
	public static void remove(HttpServletRequest request, HttpServletResponse response, String name) {
		CookieUtils.remove(request, response, name, null, Globals.domain);
	}
}
