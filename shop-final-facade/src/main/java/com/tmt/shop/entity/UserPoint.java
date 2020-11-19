package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 积分明细 管理
 * 
 * @author 超级管理员
 * @date 2017-05-16
 */
public class UserPoint extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId; // 用户
	private String name; // 条目
	private Integer points; // 积分
	private Byte state; // 积分状态: 0添加积分，1减少积分

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
}
