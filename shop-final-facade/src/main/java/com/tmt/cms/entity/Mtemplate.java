package com.tmt.cms.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
/**
 * 页面模板 管理
 * @author 超级管理员
 * @date 2016-11-11
 */
public class Mtemplate extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private String title; // 标题
	private String color; // 背景颜色
	private String description; // 页面描述
	private Byte footerAble; // 显示底部
	private String thumbnail; // 缩略图
	private Byte isEnabled; // 上架
	private List<String> fields; // 组件
	private List<MpageField> mfields;// 真实的组件
    
    public Byte getFooterAble() {
		return footerAble;
	}
	public void setFooterAble(Byte footerAble) {
		this.footerAble = footerAble;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public List<MpageField> getMfields() {
		return mfields;
	}
	public void setMfields(List<MpageField> mfields) {
		this.mfields = mfields;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
    public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
}