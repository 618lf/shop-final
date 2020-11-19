package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tmt.core.utils.BigDecimalUtil;
/**
 * 商品组合 管理
 * @author 超级管理员
 * @date 2016-11-28
 */
public class ComplexProduct implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long complexId; // 主键
	private Long productId; // 产品
	private Integer quantity; // 数量
	private Product product; // 产品
    
    public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Long getComplexId() {
		return complexId;
	}
	public void setComplexId(Long complexId) {
		this.complexId = complexId;
	}
    public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
    public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public java.math.BigDecimal getPrice() {
		if (product != null) {
			return BigDecimalUtil.mul(product.getPrice(), BigDecimal.valueOf(quantity));
		}
		return BigDecimal.ZERO;
	}
	public Double getWeight() {
		if (product != null) {
			return BigDecimalUtil.mul(quantity, Double.valueOf(product.getWeight()));
		}
		return 0.0;
	}
}