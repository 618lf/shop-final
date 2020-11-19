package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.time.DateUtils;

/**
 * 订单出库 管理
 * 
 * @author 超级管理员
 * @date 2017-02-14
 */
public class ShippingTask extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId; // 订单编号
	private String orderSn; // 订单编号
	private String trackingNo; // 单号
	private Byte state; // 状态：0待出库，1：已出库，-1已取消

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	/**
	 * 不用自增主键
	 */
	@Override
	public Long prePersist() {
		this.createDate = DateUtils.getTimeStampNow();
		return this.getId();
	}
}