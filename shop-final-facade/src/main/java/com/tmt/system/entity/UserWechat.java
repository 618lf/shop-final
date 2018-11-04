package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 用户微信
 * @author root
 */
public class UserWechat implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id; // UserId + appId MD5
	private Long userId;
	private String appId;
	private String openId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
}