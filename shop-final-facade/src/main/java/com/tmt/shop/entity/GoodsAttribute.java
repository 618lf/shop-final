package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 产品属性 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
public class GoodsAttribute implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Long attributeId; // 编号
	private String attributeName;// 名称
	private String attributeOptionKey; // 属性值编号
	private Attribute attribute;// 选择项
    
    public Attribute getAttribute() {
		return attribute;
	}
	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public Long getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
	}
    public String getAttributeOptionKey() {
		return attributeOptionKey;
	}
	public void setAttributeOptionKey(String attributeOptionKey) {
		this.attributeOptionKey = attributeOptionKey;
	}
}