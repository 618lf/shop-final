package com.tmt.system.entity;

import java.io.Serializable;


/**
 * 用户切换 管理
 * @author 超级管理员
 * @date 2016-02-19
 */
public class UserRunas implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long userId; // 用户ID
	private String userName;// 用户名称
	private Long grantUserId; // 授权用户ID
	private String grantUserName;//授权用户名称
	
    public String getGrantUserName() {
		return grantUserName;
	}
	public void setGrantUserName(String grantUserName) {
		this.grantUserName = grantUserName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
    public Long getGrantUserId() {
		return grantUserId;
	}
	public void setGrantUserId(Long grantUserId) {
		this.grantUserId = grantUserId;
	}
}