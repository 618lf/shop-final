package com.tmt.common.security.filter.authc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.security.filter.PathMatchingFilter;

/**
 * 匿名用户
 * @author lifeng
 */
public class AnonymousFilter extends PathMatchingFilter {

	@Override
	protected boolean onPreHandle(HttpServletRequest request, HttpServletResponse response, Object mappedValue) {
		return true;
	}
}
