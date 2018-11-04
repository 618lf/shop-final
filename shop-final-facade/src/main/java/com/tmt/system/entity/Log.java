package com.tmt.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.common.entity.IdEntity;
import com.tmt.common.utils.DateUtil3;

/**
 * 日志
 * @author lifeng
 */
public class Log extends IdEntity<Long> implements Serializable{
  
	private static final long serialVersionUID = 1L;
	private Byte type;        // 日志类型
	private String remoteAddr; 	// 操作用户的IP地址
	private String requestUri; 	// 操作的URI
	private String method; 		// 操作的方式
	private String userAgent;	// 操作用户代理信息
	private String exception; 	// 异常信息
	private Integer dealTime;    // 请求操作时长(毫秒)
	private String clientType;  //客户类型优先wixin, qq, 不再这两个再看具体的浏览器版本
	private String referer;//上一个地址
	private Long createId;//创建人ID
	private String createName;//创建人名称
	private Date createDate;//创建时间
	private Date updateDate; //修改时间
	
	//全部的异常信息
	private String fullException;
	
	public Long getCreateId() {
		return createId;
	}
	public void setCreateId(Long createId) {
		this.createId = createId;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public String getFullException() {
		return fullException;
	}
	public void setFullException(String fullException) {
		this.fullException = fullException;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public Integer getDealTime() {
		return dealTime;
	}
	public void setDealTime(Integer dealTime) {
		this.dealTime = dealTime;
	}
	public String getException() {
		return exception;
	}
	public void setException(String exception) {
		this.exception = exception;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getRequestUri() {
		return requestUri;
	}
	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public void logOptions(){
		this.createDate = DateUtil3.getTimeStampNow();
	}
}