package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 订单折扣 管理
 * 
 * @author 超级管理员
 * @date 2017-02-19
 */
public class OrderRank extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long ranks; // 折扣
	private String ranksAnme; // 折扣名称
	private Double discount; // 折扣
	private java.math.BigDecimal reduce; // 优惠金额

	public Long getRanks() {
		return ranks;
	}

	public void setRanks(Long ranks) {
		this.ranks = ranks;
	}

	public String getRanksAnme() {
		return ranksAnme;
	}

	public void setRanksAnme(String ranksAnme) {
		this.ranksAnme = ranksAnme;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public java.math.BigDecimal getReduce() {
		return reduce;
	}

	public void setReduce(java.math.BigDecimal reduce) {
		this.reduce = reduce;
	}

	@Override
	public Long prePersist() {
		return this.id;
	}
}
