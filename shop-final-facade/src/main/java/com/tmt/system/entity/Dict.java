package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.common.entity.BaseEntity;

/**
 * 数据字典
 * @author lifeng
 */
public class Dict extends BaseEntity<Long> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String code;
    private String value;
    private Integer sort;
    
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}