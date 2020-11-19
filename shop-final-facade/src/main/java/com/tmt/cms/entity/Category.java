package com.tmt.cms.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseTreeEntity;
/**
 * 文章分类 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class Category extends BaseTreeEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 栏目名称
	private String code; // 别名
	private String description; // 描述
	private String keywords; // 关键字
	private Integer sort; // 排序（升序）
	private Byte isShow; // 是否显示
	private Byte inMenu; // 是否在导航中显示
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
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
    public Byte getInMenu() {
		return inMenu;
	}
	public void setInMenu(Byte inMenu) {
		this.inMenu = inMenu;
	}
}