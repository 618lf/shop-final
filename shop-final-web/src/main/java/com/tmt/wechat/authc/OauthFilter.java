package com.tmt.wechat.authc;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.entity.Result;
import com.tmt.core.exception.ErrorCode;
import com.tmt.core.security.filter.authc.UserFilter;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.SecurityUtils;
import com.tmt.core.utils.SavedRequestEx;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.service.WechatService;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 自动重定向到授权页面
 * 
 * @author lifeng
 */
public class OauthFilter extends UserFilter {

	/**
	 * 微信服务
	 */
	@Autowired
	private WechatService wechatService;

	/**
	 * 是否需要自定义的登录 如果需要自定义登录： 1. ajax 请求过来的返回json 2. 非ajax 请求过来的重定向登录页面 3.
	 * 需要配置一个过滤器对应到前台的登录页面，可以使用后台的过滤器 authc
	 * 
	 * 如果不需要自定义登录： 1. ajax 请求过来的返回json 2. 非ajax 重定向到对应的微信授权页面（自动登录）
	 */
	private String customLogin;

	/**
	 * 和 OauthenFilter 不一样
	 */
	@Override
	protected boolean onAccessDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!WebUtils.isAjax(request)) {
			this.saveRequestAndRedirectToLogin(request, response);
		} else {
			Subject subject = SecurityUtils.getSubject();
			ErrorCode code = ErrorCode.NO_USER;
			if (StringUtils.isNotBlank(subject.getReason())) {
				code = code.clone();
				code.setReason(subject.getReason());
			}
			WebUtils.sendJson(response, Result.error(code).toJson());
		}
		return false;
	}

	/**
	 * 改进： 如果是微信浏览器者直接吧转向地址写到url中 --- 所有请求登录走微信
	 */
	@Override
	protected void saveRequestAndRedirectToLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 回调的地址
		SavedRequestEx savedRequest = new SavedRequestEx(request);
		String requestUrl = savedRequest.getRequestUrl();
		// 请求的域名
		String domain = request.getServerName();

		// 需要调转的地址
		String url = "";
		if (StringUtils.isNotBlank(customLogin)) {
			url = new StringBuilder(domain).append(customLogin).append("?to=").append(requestUrl).toString();
			url = URLEncoder.encode(url, "UTF-8");
		} else {
			// 根据 domain 找到具体的app
			App app = WechatUtils.getDomainApp(domain);
			url = wechatService.bind(app).getAuthorizeURL(domain, requestUrl);
		}
		WebUtils.issueRedirect(request, response, url);
	}

	public void setCustomLogin(String customLogin) {
		this.customLogin = customLogin;
	}
}