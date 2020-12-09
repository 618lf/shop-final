package com.tmt.api.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 测试 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
public class Test extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long documentId; // 接口ID
	private String documentName; // 接口名称
	private String requestUrl; // REQUEST URL：绝对地址
	private String requestMethod; // REQUEST METHOD：如POST、GET
	private String requestHeaders; // REQUEST HEADERS：MAP-JSON格式字符串
	private String queryParams; // QUERY STRING PARAMETERS：VO-JSON格式字符串
	private String respType; // RESPONSE CONTENT-TYPE：如JSON
    
    public String getDocumentName() {
		return documentName;
	}
	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}
	public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
    public String getRequestUrl() {
		return requestUrl;
	}
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}
    public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
    public String getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
    public String getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(String queryParams) {
		this.queryParams = queryParams;
	}
    public String getRespType() {
		return respType;
	}
	public void setRespType(String respType) {
		this.respType = respType;
	}
}