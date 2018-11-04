package com.tmt.system.entity;

import java.io.Serializable;
/**
 * 用户会话 管理
 * @author 超级管理员
 * @date 2017-05-27
 */
public class UserSession implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long userId; // 用户
	private String sessionId; // 会话
	private java.util.Date updateTime; // 登录时间
    
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
    public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
    public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
}