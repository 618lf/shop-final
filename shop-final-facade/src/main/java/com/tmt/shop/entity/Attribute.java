package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.incrementer.IdGen;

/**
 * 商品属性 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Attribute extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long categoryId; // 分类
	private String categoryName; // 分类
	private String name; // 名称
	private Integer sort; // 排序
	private List<AttributeOption> options;
    
    public List<AttributeOption> getOptions() {
		return options;
	}
	public void setOptions(List<AttributeOption> options) {
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
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public void space() {
		this.setCategoryId(null);
		this.setCategoryName(null);
		this.setId(IdGen.INVALID_ID);
		if(options != null && options.size() != 0) {
		   for(AttributeOption option: options) {
			   option.setId(IdGen.INVALID_ID);
			   option.setAttributeId(IdGen.INVALID_ID);
		   }
		}
	}
}
