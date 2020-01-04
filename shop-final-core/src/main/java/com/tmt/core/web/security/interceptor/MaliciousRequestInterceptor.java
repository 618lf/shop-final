package com.tmt.core.web.security.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tmt.core.security.principal.Session;
import com.tmt.core.web.security.session.SessionProvider;

/**
 * 恶意请求拦截
 * 
 * @author lifeng
 */
public class MaliciousRequestInterceptor extends HandlerInterceptorAdapter {

	// 上次请求的相关记录
    public String PRE_REQUEST_TIME = "PRE_REQUEST_TIME";
    public String MALICIOUS_REQUEST_TIMES = "MALICIOUS_REQUEST_TIMES";
    
	private Long minIntervalTime = 1000L; // 允许的最小请求间隔
	private Integer maxTimes = 3; // 允许的最大恶意请求次数
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Session session = SessionProvider.getSession();
		Long preRequestTime = (Long) session.getAttribute(PRE_REQUEST_TIME);
		if (preRequestTime != null) { // 过滤频繁操作
			if (System.currentTimeMillis() - preRequestTime < minIntervalTime) {
				Integer mTimes = (Integer) session.getAttribute(MALICIOUS_REQUEST_TIMES);
				        mTimes = mTimes == null?0:mTimes;
				session.setAttribute(MALICIOUS_REQUEST_TIMES, ++mTimes);
				if (mTimes > maxTimes) { // 频繁访问
					response.setStatus(207);
					return false;
				}
			} else {
				session.setAttribute(MALICIOUS_REQUEST_TIMES, 0);
			}
		}
		session.setAttribute(PRE_REQUEST_TIME, System.currentTimeMillis());
		return super.preHandle(request, response, handler);
	}
}