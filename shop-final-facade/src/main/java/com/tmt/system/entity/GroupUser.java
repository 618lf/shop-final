package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 用户组-用户
 * @author lifeng
 */
public class GroupUser implements Serializable{
    
	private static final long serialVersionUID = 1L;
	private Long groupId;
    private Long userId;
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
}