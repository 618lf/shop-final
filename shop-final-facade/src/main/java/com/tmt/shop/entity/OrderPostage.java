package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 订单包邮 管理
 * 
 * @author 超级管理员
 * @date 2017-05-26
 */
public class OrderPostage extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long postages; // 包邮
	private String postagesAnme; // 包邮名称
	private java.math.BigDecimal reduce; // 优惠金额

	public Long getPostages() {
		return postages;
	}

	public void setPostages(Long postages) {
		this.postages = postages;
	}

	public String getPostagesAnme() {
		return postagesAnme;
	}

	public void setPostagesAnme(String postagesAnme) {
		this.postagesAnme = postagesAnme;
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
