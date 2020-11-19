package com.tmt.cms.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 文章内容 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class ArticleData extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String content; // 文章内容
	
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public Long prePersist() {
		return this.id;
	}
	@Override
	public void preUpdate() {
		super.preUpdate();
	}
}