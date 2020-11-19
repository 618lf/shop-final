package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 产品规格 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
public class GoodsSpecificationValue implements Serializable{

	private static final long serialVersionUID = -6874471372740209663L;
	
	private Long id;
	private String value;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}