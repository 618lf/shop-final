package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 商品标签 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public class GoodsTag implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Long tagId; // 编号
	private String tagName;// 名称
	private Byte support;//是否支持0 不支持， 1支持
	
    public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public Byte getSupport() {
		return support;
	}
	public void setSupport(Byte support) {
		this.support = support;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
}