package com.sample.entity;

import com.tmt.core.entity.IdEntity;

/**
 * 订单
 * 
 * @author lifeng
 */
public class Order extends IdEntity<Long> {

	private static final long serialVersionUID = 1L;

	@Override
	public Long prePersist() {
		return this.id;
	}
}
