package com.tmt.cms.entity;

import java.io.Serializable;
import java.util.Map;

import com.tmt.core.entity.IdEntity;
/**
 * 页面组件 管理
 * @author 超级管理员
 * @date 2016-11-04
 */
public class MpageField extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long pageId; // 页面
	private Byte sort; // 排序
	private String config; // 配置
	private Map<String, Object> mconfig;
    
    public Map<String, Object> getMconfig() {
		return mconfig;
	}
	public void setMconfig(Map<String, Object> mconfig) {
		this.mconfig = mconfig;
	}
	public Long getPageId() {
		return pageId;
	}
	public void setPageId(Long pageId) {
		this.pageId = pageId;
	}
    
    public Byte getSort() {
		return sort;
	}
	public void setSort(Byte sort) {
		this.sort = sort;
	}
    
    public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
}
