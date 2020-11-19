package com.tmt.cms.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 友情链接 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class FriendLink extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private String logo; // LOGO
	private Integer orders; // 排序
	private String url; // 地址
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
    public Integer getOrders() {
		return orders;
	}
	public void setOrders(Integer orders) {
		this.orders = orders;
	}
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}