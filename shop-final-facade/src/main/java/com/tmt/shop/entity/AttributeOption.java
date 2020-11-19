package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * 属性选项 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
public class AttributeOption extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long attributeId; // 编号
	private Integer sort; // 排序
	private String name; // 参数
	
    
    public Long getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(Long attributeId) {
		this.attributeId = attributeId;
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
