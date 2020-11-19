package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 退货明细 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class ReturnItem extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long returnsId; // 编号
	private Long itemId; // 订单明细
	private String name; // 商品名称
	private String sn; // 商品编号
	private Integer quantity; // 商品数量

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getReturnsId() {
		return returnsId;
	}

	public void setReturnsId(Long returnsId) {
		this.returnsId = returnsId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}