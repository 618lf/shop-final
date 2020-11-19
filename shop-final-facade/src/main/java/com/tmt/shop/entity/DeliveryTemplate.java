package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 快递单模板 管理
 * @author 超级管理员
 * @date 2016-02-23
 */
public class DeliveryTemplate extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private String background; // 背景图片
	private String content; // 模板内容
	private Integer width = 1000; // 宽
	private Integer height = 400; // 高
	private String isDefault; // 是否默认
	
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public String getBackground() {
		return background;
	}
	public void setBackground(String background) {
		this.background = background;
	}
    
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
    
    public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	
     
}
