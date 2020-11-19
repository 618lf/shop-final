package com.tmt.cms.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 文章标签 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class ArticleTag extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long articleId; // 文章
	private Long tagId; // 标签
	
    public Long getArticleId() {
		return articleId;
	}
	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}
    public Long getTagId() {
		return tagId;
	}
	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}
}