package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 关联品牌 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public class CategoryBrand implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long categoryId; // 编号
	private Long brandId; // 编号
	private String brandName;// 名称
	private Byte support;//是否支持0 不支持， 1支持
	
	public Byte getSupport() {
		return support;
	}
	public void setSupport(Byte support) {
		this.support = support;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    public Long getBrandId() {
		return brandId;
	}
	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}
}