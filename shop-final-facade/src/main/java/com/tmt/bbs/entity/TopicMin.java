package com.tmt.bbs.entity;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class TopicMin {

	private Long id;
	private String mood;
	private Long sectionId;
	private String sectionName;
	private String content;
	private String addContent;
	private Date createDate;
	private String createName;
	private String createImage;
	private String images;
	private Integer hits;
	private Integer replys;

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getAddContent() {
		return addContent;
	}

	public void setAddContent(String addContent) {
		this.addContent = addContent;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Integer getReplys() {
		return replys;
	}

	public void setReplys(Integer replys) {
		this.replys = replys;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCreateImage() {
		return createImage;
	}

	public void setCreateImage(String createImage) {
		this.createImage = createImage;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
}
