package com.tmt.api.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 接口分组 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
public class Group extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long projectId; // 项目ID
	private String name; // 分组名称
	private Integer sort; // 排序（升序）
    
    public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
}
