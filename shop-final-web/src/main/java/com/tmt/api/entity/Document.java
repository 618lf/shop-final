package com.tmt.api.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 接口 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
public class Document extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long projectId; // 项目ID
	private Long groupId; // 分组ID
	private String groupName; // 分组名称
	private String name; // 接口名称
	private Byte status; // 状态：0-启用、1-维护、2-废弃
	private Byte starLevel; // 星标等级：0-普通接口、1-一星接口
	private String requestUrl; // REQUEST URL：相对地址
	private String requestMethod; // REQUEST METHOD：如POST、GET
	private String requestHeaders; // REQUEST HEADERS：MAP-JSON格式字符串
	private String queryParams; // QUERY STRING PARAMETERS：VO-JSON格式字符串
	private String responseParams; // RESPONSE PARAMETERS：VO-JSON格式字符串
	private String successRespType; // RESPONSE CONTENT-TYPE：成功接口，如JSON、XML、HTML、TEXT、JSONP
	private String successRespExample; // RESPONSE CONTENT：成功接口
	private String failRespType; // RESPONSE CONTENT-TYPE：失败接口
	private String failRespExample; // RESPONSE CONTENT：失败接口
    
    public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
    
    public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
    
    public Byte getStarLevel() {
		return starLevel;
	}
	public void setStarLevel(Byte starLevel) {
		this.starLevel = starLevel;
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
    
    public String getResponseParams() {
		return responseParams;
	}
	public void setResponseParams(String responseParams) {
		this.responseParams = responseParams;
	}
    
    public String getSuccessRespType() {
		return successRespType;
	}
	public void setSuccessRespType(String successRespType) {
		this.successRespType = successRespType;
	}
    
    public String getSuccessRespExample() {
		return successRespExample;
	}
	public void setSuccessRespExample(String successRespExample) {
		this.successRespExample = successRespExample;
	}
    
    public String getFailRespType() {
		return failRespType;
	}
	public void setFailRespType(String failRespType) {
		this.failRespType = failRespType;
	}
    
    public String getFailRespExample() {
		return failRespExample;
	}
	public void setFailRespExample(String failRespExample) {
		this.failRespExample = failRespExample;
	}
}
