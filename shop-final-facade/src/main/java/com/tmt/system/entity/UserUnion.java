package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 用户统一表
 * 针对微信的Union_id
 * @author root
 */
public class UserUnion implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id; // UNION_ID
	private Long userId;
	
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
}
