package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 产品参数 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
public class GoodsParameter extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Long parameterGroupId; // 参数
	private String parameterName; // 参数名称
	private String parameterValue; // 参数值
	private Integer sort; // 排序
    
    public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public Long getParameterGroupId() {
		return parameterGroupId;
	}
	public void setParameterGroupId(Long parameterGroupId) {
		this.parameterGroupId = parameterGroupId;
	}
    public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
    public String getParameterValue() {
		return parameterValue;
	}
	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}