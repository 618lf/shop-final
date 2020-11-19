package com.tmt.bbs.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.incrementer.IdGen;
/**
 * 主题分类 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
public class Section extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long pid; // 产品ID -- 临时字段
	private String name; // 栏目名称
	private String description; // 描述
	private Integer sort; // 排序（升序）
	private Byte isShow; // 是否显示
	private String tags; // 标签
    
    public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
    
    public Byte getIsShow() {
		return isShow;
	}
	public void setIsShow(Byte isShow) {
		this.isShow = isShow;
	}
    
    public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	/**
	 * 如果指定了有效的id则不自动生成id
	 */
	@Override
	public Long prePersist() {
		if (IdGen.isInvalidId(this.getId())) {
			return super.prePersist();
		}
		return this.getId();
	}
}
