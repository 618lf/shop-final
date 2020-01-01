package com.tmt.common.utils;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.tmt.common.utils.StringUtils;

/**
 * 扩展SavedRequest,可以在登录后跳转到指定的get页面，
 * 并验证域名
 * @author lifeng
 *
 */
public class SavedRequestEx implements Serializable{

	private static final long serialVersionUID = 7765958835026274088L;
	
	private String method;
    private String queryString;
    private String requestURI;
	
    public SavedRequestEx(){}
    public SavedRequestEx(HttpServletRequest request) {
    	this.method = request.getMethod();
        this.queryString = request.getQueryString();
        this.requestURI = request.getRequestURI();
    }
	public SavedRequestEx(String requestUrl) {
		this.method = WebUtils.GET_METHOD;
		this.queryString = null;
		this.requestURI = requestUrl;
	}

	public String getMethod() {
		return method;
	}
	public String getQueryString() {
		return queryString;
	}
	public String getRequestURI() {
		return requestURI;
	}
	
	//返回请求路径
	public String getRequestUrl() {
		String  _requestUrl = this.getRequestURI();
		if( StringUtils.isNotBlank(_requestUrl)) {
			StringBuilder requestUrl = new StringBuilder(_requestUrl);
	        if (getQueryString() != null) {
	            requestUrl.append("?").append(getQueryString());
	        }
	        _requestUrl = requestUrl.toString();
	        if (StringUtils.startsWith(_requestUrl, "//")) {
	        	_requestUrl = StringUtils.replace(_requestUrl, "//", "/");
			}
	        return _requestUrl;
		}
        return null;
    }
}