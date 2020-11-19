package com.tmt.cms.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 文章管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public class Article extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String title; // 标题
	private String author; // 作者
	private Long categoryId; // 栏目编号
	private String categoryName; // 栏目编号
	private String image; // 文章图片
	private String keywords; // 关键字
	private Integer weight; // 权重，越大越靠前: 精华:900~999、热门:500~599、新帖(默认):1~499
	private Integer hits; // 点击数
	private Integer comments; // 评论数
	private Byte isEnabled; // 是否启用
	private String content; //文章内容
	
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
    public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
    public Integer getWeight() {
		return weight;
	}
	public void setWeight(Integer weight) {
		this.weight = weight;
	}
    public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
    public Integer getComments() {
		return comments;
	}
	public void setComments(Integer comments) {
		this.comments = comments;
	}
    public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
	public ArticleData getData() {
		ArticleData data = new ArticleData();
		data.setContent(content);
		data.setId(this.getId());
		return data;
	}
}