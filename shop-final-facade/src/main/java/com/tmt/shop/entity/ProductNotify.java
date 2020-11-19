package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 到货通知 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class ProductNotify extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long productId; // 商品
	private String productName; // 商品

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
}