package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 产品规格 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
public class GoodsSpecification implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Long specificationsId; // 编号
	private String specificationsName; // 规格名称
	private Integer sort;// 排序
	private List<GoodsSpecificationOption> options;
	
	public List<GoodsSpecificationOption> getOptions() {
		return options;
	}
	public void setOptions(List<GoodsSpecificationOption> options) {
		this.options = options;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public Long getSpecificationsId() {
		return specificationsId;
	}
	public void setSpecificationsId(Long specificationsId) {
		this.specificationsId = specificationsId;
	}
    public String getSpecificationsName() {
		return specificationsName;
	}
	public void setSpecificationsName(String specificationsName) {
		this.specificationsName = specificationsName;
	}
}
