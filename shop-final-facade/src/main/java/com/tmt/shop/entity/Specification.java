package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.incrementer.IdGen;

/**
 * 商品规格 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Specification extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long categoryId; // 分类
	private String categoryName; // 分类
	private Integer sort; // 排序
	private String name; // 参数
	private String type = "TEXT"; // 类型
	private List<SpecificationOption> options;

	public List<SpecificationOption> getOptions() {
		return options;
	}

	public void setOptions(List<SpecificationOption> options) {
		this.options = options;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void space() {
		this.setCategoryId(null);
		this.setCategoryName(null);
		this.setId(IdGen.INVALID_ID);
		if (options != null && options.size() != 0) {
			for (SpecificationOption option : options) {
				option.setId(IdGen.INVALID_ID);
				option.setSpecificationId(IdGen.INVALID_ID);
			}
		}
	}
}