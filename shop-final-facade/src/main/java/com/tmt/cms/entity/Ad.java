package com.tmt.cms.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 广告管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class Ad extends BaseEntity<Long> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private Integer orders; // 排序
	private String image; // 图片
	private String path; // 路径 -- 多余
	private String url; // 地址  -- 链接地址
	private Long positionId; // 广告位置
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    public Long getPositionId() {
		return positionId;
	}
	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}
}