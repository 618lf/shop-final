package com.tmt.system.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseParentEntity;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.time.DateUtils;

/**
 * 地区
 * 
 * @author lifeng
 */
public class Area extends BaseParentEntity<Long> implements Serializable{

	private static final long serialVersionUID = 1L;

	private String code;
	private String type;
	private Integer sort = 0;
	private List<Area> children;
	
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Area> getChildren() {
		return children;
	}
	public void setChildren(List<Area> children) {
		this.children = children;
	}
	public void addChild(Area child) {
		if( this.children == null ) {
			this.children = Lists.newArrayList();
		}
		this.children.add(child);
	}
	
	/**
	 * 不需要自增长
	 */
	@Override
	public Long prePersist() {
		if (IdGen.isInvalidId(this.id)) {
			return super.prePersist();
		}
		this.createDate = DateUtils.getTimeStampNow();
		this.updateDate = this.createDate;
		return this.id;
	}
}