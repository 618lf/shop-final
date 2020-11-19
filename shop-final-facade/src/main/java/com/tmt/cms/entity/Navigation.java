package com.tmt.cms.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 导航管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class Navigation extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 图片
	private Integer orders; // 排序
	private String url; // 地址
	private Byte position; // 位置
	private Byte isTarget; // 打开方式
	
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
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    public Byte getPosition() {
		return position;
	}
	public void setPosition(Byte position) {
		this.position = position;
	}
    public Byte getIsTarget() {
		return isTarget;
	}
	public void setIsTarget(Byte isTarget) {
		this.isTarget = isTarget;
	}
}