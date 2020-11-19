package com.tmt.cms.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
/**
 * 广告管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class AdPosition extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private Integer width; // 宽
	private Integer height; // 高
	private String template; // 模板
	private List<Ad> ads;
	
    public List<Ad> getAds() {
		return ads;
	}
	public void setAds(List<Ad> ads) {
		this.ads = ads;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}
    public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
    public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
}