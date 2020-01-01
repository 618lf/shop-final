package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 用户账户表
 * 
 * @author root
 */
public class UserAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // user_name, email, mobile, open_id, open_id, open_id, open_id
	private Long userId;
	private Byte type;// 1：用户名，2：邮箱，3：手机，4：微信（多个），5：微网站（多个），6：QQ，7：SINA
	private User user; // 用户信息

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

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

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}
}