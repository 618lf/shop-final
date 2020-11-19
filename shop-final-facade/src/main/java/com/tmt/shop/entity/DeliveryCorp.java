package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 物流公司 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
public class DeliveryCorp extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private String code; // 代码
	private Integer sort; // 排序
	private String url; // 网址
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}