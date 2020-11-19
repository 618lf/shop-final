package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;

/**
 * 订单评价 管理
 * 
 * @author 超级管理员
 * @date 2017-04-10
 */
public class OrderAppraise extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sn; // 序列号
	private Byte state; // 状态：0待评价，1：待追评, 2：已评价
	private List<OrderItem> items;
	private List<ProductAppraise> appraises; // 商品评价

	public List<ProductAppraise> getAppraises() {
		return appraises;
	}

	public void setAppraises(List<ProductAppraise> appraises) {
		this.appraises = appraises;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	@Override
	public Long prePersist() {
		return this.getId();
	}
}