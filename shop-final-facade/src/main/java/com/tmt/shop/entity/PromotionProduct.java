package com.tmt.shop.entity;

import java.io.Serializable;
/**
 * 促销商品 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class PromotionProduct implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long promotions; // 促销
	private Long products; // 产品
	private String productName; // 名称
	private Double discount; // 折扣：折扣促销才有意义
	private Byte scale; // 小数位数: 2 保留两位小数, 1 摸分, 0 摸分角
	private Product product; // 具体的商品信息
    
    public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Long getPromotions() {
		return promotions;
	}
	public void setPromotions(Long promotions) {
		this.promotions = promotions;
	}
    public Long getProducts() {
		return products;
	}
	public void setProducts(Long products) {
		this.products = products;
	}
    public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
    public Byte getScale() {
		return scale;
	}
	public void setScale(Byte scale) {
		this.scale = scale;
	}
}
