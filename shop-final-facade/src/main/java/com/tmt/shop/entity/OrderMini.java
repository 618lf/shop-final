package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.utils.Ints;

/**
 * 订单结果
 * 
 * @author lifeng
 *
 */
public class OrderMini implements Serializable {

	private static final long serialVersionUID = 1L;
	private String productName;
	private Integer quantity;
	private String unit;

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void addQuantity(Integer quantity) {
		this.quantity = Ints.addI(this.quantity, quantity);
	}
}
