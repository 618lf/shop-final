package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 商品规格选项 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class SpecificationOption extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long specificationId; // 编号
	private Integer sort; // 排序
	private String name; // 名称
	private String image; // 图片

	public Long getSpecificationId() {
		return specificationId;
	}

	public void setSpecificationId(Long specificationId) {
		this.specificationId = specificationId;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}