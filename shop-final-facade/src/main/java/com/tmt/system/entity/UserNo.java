package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 用户编号表
 * @author root
 */
public class UserNo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String id;
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