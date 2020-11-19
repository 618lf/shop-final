package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tmt.core.entity.IdEntity;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 送货区域 管理
 * 
 * @author 超级管理员
 * @date 2017-02-06
 */
public class ShippingMethodArea extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long shippingMethodId; // 编号
	private java.math.BigDecimal firstPrice; // 默认首重价格
	private Double firstWeight; // 首重量
	private java.math.BigDecimal continuePrice; // 默认续重价格
	private Double continueWeight; // 续重量
	private String areaIds; // 地区
	private String areaNames; // 地区
	private Byte sort; // 排序

	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	public String getAreaNames() {
		return areaNames;
	}

	public void setAreaNames(String areaNames) {
		this.areaNames = areaNames;
	}

	public Long getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(Long shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}

	public java.math.BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(java.math.BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public Double getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(Double firstWeight) {
		this.firstWeight = firstWeight;
	}

	public java.math.BigDecimal getContinuePrice() {
		return continuePrice;
	}

	public void setContinuePrice(java.math.BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}

	public Double getContinueWeight() {
		return continueWeight;
	}

	public void setContinueWeight(Double continueWeight) {
		this.continueWeight = continueWeight;
	}

	public Byte getSort() {
		return sort;
	}

	public void setSort(Byte sort) {
		this.sort = sort;
	}

	/**
	 * 计算税费
	 * 
	 * @param weight
	 * @return
	 */
	public BigDecimal calcula(Double weight) {
		if (weight.compareTo((double) this.firstWeight) <= 0) {
			return this.firstPrice;
		}
		Double continueWeight = BigDecimalUtil.sub(weight, this.firstWeight);
		return BigDecimalUtil.add(this.firstPrice, BigDecimalUtil.mul(
				BigDecimalUtil.valueOf(BigDecimalUtil.div(continueWeight, this.continueWeight)), this.continuePrice));
	}
}