package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 商品参数选项 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class ParameterOption extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long parameterId; // 编号
	private Integer sort; // 排序
	private String name; // 参数

	public Long getParameterId() {
		return parameterId;
	}

	public void setParameterId(Long parameterId) {
		this.parameterId = parameterId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}