package com.tmt.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.ExceptionUtil;
import com.tmt.core.utils.WebUtils;

/**
 * 这里不需要打印异常信息，最前面会统一处理. 这里只需要处理异常后怎么跳转的问题 简单处理 异常信息处理类,默认是不会打印 springmvc
 * 内部的错误信息， 统一打印出来 2016-11-30 加入错误地址
 * 
 * @author liFeng 2014年6月23日
 */
public class DefaultExceptionHandler implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex);
		ModelAndView mv = new ModelAndView();
		HandlerMethod method = (HandlerMethod) handler;
		if (method != null) {
			ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
			if (responseBody != null) {
				WebUtils.sendJson(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
						AjaxResult.error("服务器异常").toJson());
			} else {
				WebUtils.sendError(response, ExceptionUtil.getMessage(ex));
			}
		}
		return mv;
	}
}