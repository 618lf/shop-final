package com.tmt.core.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基本的filter
 * @author lifeng
 */
public abstract class AdviceFilter extends OncePerRequestFilter {

	/**
	 * 执行前
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return true;
	}

	/**
	 * 执行后(没啥用)
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	protected void postHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
	}

	/**
	 * 最后执行
	 * @param request
	 * @param response
	 * @param exception
	 * @throws Exception
	 */
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Exception exception) throws Exception {
	}

	/**
	 * 执行后面的过滤器
	 * 
	 * @param request
	 * @param response
	 * @param chain
	 * @throws Exception
	 */
	protected void executeChain(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws Exception {
		chain.doFilter(request, response);
	}

	/**
	 * 执行逻辑
	 * @param request
	 * @param response
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

		Exception exception = null;

		try {
			
			// 执行前返回是否需要执行后面的逻辑
			boolean continueChain = preHandle(request, response);
			if (continueChain) {
				executeChain(request, response, chain);
			}
			
			// 这个没用了
			postHandle(request, response);
		} catch (Exception e) {
			exception = e;
		} finally {
			cleanup(request, response, exception);
		}
	}
    
	/**
	 * 这个还不知到是干嘛的
	 * @param request
	 * @param response
	 * @param existing
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void cleanup(HttpServletRequest request, HttpServletResponse response,
			Exception existing) throws ServletException, IOException {
		Exception exception = existing;
		try {
			afterCompletion(request, response, exception);
		} catch (Exception e) {
			if (exception == null) {
				exception = e;
			}
		}
		if (exception != null) {
			if (exception instanceof ServletException) {
				throw (ServletException) exception;
			} else if (exception instanceof IOException) {
				throw (IOException) exception;
			} else {
				throw new ServletException(exception);
			}
		}
	}
}
