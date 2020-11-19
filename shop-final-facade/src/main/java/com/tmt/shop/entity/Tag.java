package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 商品标签 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Tag extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer sort; // 排序
	private String icon; // 图标
	private String name; // 名称

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}