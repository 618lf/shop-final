package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 订单事件 管理
 * 
 * @author 超级管理员
 * @date 2016-10-04
 */
public class OrderEvent extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId; // 订单ID
	private Byte opt; // 操作类型：见 OrderOpts
	private Byte state; // 状态：0初始，1：更新

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Byte getOpt() {
		return opt;
	}

	public void setOpt(Byte opt) {
		this.opt = opt;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}
}