package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 产品规格选项 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
public class GoodsSpecificationOption implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Long specificationsId; // 编号
	private Long specificationsOptionId; // 编号
	private String specificationsOptionName; // 规格选项名称
	private Integer sort;// 排序
	private int support; //是否支持
	
    public Long getSpecificationsId() {
		return specificationsId;
	}
	public void setSpecificationsId(Long specificationsId) {
		this.specificationsId = specificationsId;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public int getSupport() {
		return support;
	}
	public void setSupport(int support) {
		this.support = support;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public Long getSpecificationsOptionId() {
		return specificationsOptionId;
	}
	public void setSpecificationsOptionId(Long specificationsOptionId) {
		this.specificationsOptionId = specificationsOptionId;
	}
    public String getSpecificationsOptionName() {
		return specificationsOptionName;
	}
	public void setSpecificationsOptionName(String specificationsOptionName) {
		this.specificationsOptionName = specificationsOptionName;
	}
}