package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tmt.core.entity.IdEntity;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 发货明细 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class ShippingItem extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long shippingId; // 编号
	private Long itemId;// 订单明细
	private String name; // 商品名称
	private String sn; // 商品编号
	private java.math.BigDecimal price; // 商品单价
	private Integer quantity; // 商品数量
	private String weight; // 商品重量
	private String unit; // 商品单位

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getShippingId() {
		return shippingId;
	}

	public void setShippingId(Long shippingId) {
		this.shippingId = shippingId;
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

	public java.math.BigDecimal getTotal() {
		return BigDecimalUtil.mul(this.getPrice(), BigDecimal.valueOf(this.getQuantity()));
	}
}