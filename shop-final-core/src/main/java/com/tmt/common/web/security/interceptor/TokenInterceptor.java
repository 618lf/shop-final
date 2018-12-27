package com.tmt.common.web.security.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.web.security.session.SessionProvider;

/**
 * 场景1.打开一个页面（get页面）申请token 这个页面的ajax请求或form提交，提交时会将token带到后台
 * 
 * @author lifeng
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

	private Logger logger = LoggerFactory.getLogger(TokenInterceptor.class);
	private final String TOKEN_KEY = "token";
	private final String ACCESS_DENIED_MSG = "Bad or missing token!";

	/**
	 * 执行验证
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();

			// 是否需要验证token
			Token token = method.getAnnotation(Token.class);

			// 没设置则不需要验证
			if (token == null) {
				return Boolean.TRUE;
			}

			// 存储token
			if (token.save()) {
				this.createToken(request, response);
				return Boolean.TRUE;
			}

			Boolean flag = Boolean.TRUE;

			// 客户端token
			String clinetToken = this.getClinetToken(request);
			
			// 验证token
			if (token.validate() && isRepeatSubmit(clinetToken)) {
				flag = Boolean.FALSE;
			}

			// 是否删除
			if (token.remove()) {
				SessionProvider.removeAttribute(clinetToken);
			}

			// token 失效
			if (!flag) {
				try {
					response.sendError(403, ACCESS_DENIED_MSG);
				} catch (Exception e) {
					logger.error("token error", e);
				}
			}
			return flag;
		}
		return Boolean.TRUE;
	}

	// 创建token
	private void createToken(HttpServletRequest request, HttpServletResponse response) {
		String token = IdGen.stringKey();

		// 方便ajax请求来获取
		response.setHeader(TOKEN_KEY, token);

		// 放入request scope -- 方便页面端获取
		request.setAttribute(TOKEN_KEY, token);

		// 服务器存放
		SessionProvider.setAttribute(token, TOKEN_KEY);
	}

	/**
	 * 是否重复提交了
	 * 
	 * @return
	 */
	private Boolean isRepeatSubmit(String clinetToken) {

		// 获取客户端的token
		if (StringUtil3.isBlank(clinetToken)) {
			return Boolean.TRUE;
		}

		// 服务器端的 token
		String serverToken = SessionProvider.getAttribute(clinetToken);

		// 服务器端清空了，则视为重复提交
		if (serverToken == null || !TOKEN_KEY.equals(serverToken)) {
			return Boolean.TRUE;
		}

		// 不是重复提交
		return Boolean.FALSE;
	}

	/**
	 * 获取客户端提交的token
	 * 
	 * @param request
	 * @return
	 */
	private String getClinetToken(HttpServletRequest request) {
		String clinetToken = request.getParameter(TOKEN_KEY);
		if (StringUtil3.isBlank(clinetToken)) {
			clinetToken = request.getHeader(TOKEN_KEY);
		}
		return clinetToken;
	}
}