package com.tmt.bbs.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.incrementer.IdGen;
/**
 * 主题 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
public class Topic extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long sectionId; // 所属板块
	private String sectionName; // 所属板块
	private Byte isTop; // 是否置顶
	private Byte isGood; // 是否精华
	private Byte isShow; // 是否显示
	private String mood; // 心情颜色
	private String tags; // 标签
	private String createRank; // 用户等级图片
	private String createImage; // 用户图像
	
	private Integer hits; // 点击数
	private Integer replys; // 评论数
	private String content; // 内容
	private String addContent; // 追评
	private String images; // 图片
	private TopicContent tcontent;

	public String getAddContent() {
		return addContent;
	}
	public void setAddContent(String addContent) {
		this.addContent = addContent;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public TopicContent getTcontent() {
		return tcontent;
	}
	public void setTcontent(TopicContent tcontent) {
		this.tcontent = tcontent;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImages() {
		return images;
	}
	public void setImages(String images) {
		this.images = images;
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
	public Long getSectionId() {
		return sectionId;
	}
	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}
    
    public Byte getIsTop() {
		return isTop;
	}
	public void setIsTop(Byte isTop) {
		this.isTop = isTop;
	}
    
    public Byte getIsGood() {
		return isGood;
	}
	public void setIsGood(Byte isGood) {
		this.isGood = isGood;
	}
    
    public Byte getIsShow() {
		return isShow;
	}
	public void setIsShow(Byte isShow) {
		this.isShow = isShow;
	}
    
    public String getMood() {
		return mood;
	}
	public void setMood(String mood) {
		this.mood = mood;
	}
    
    public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
    
    public String getCreateRank() {
		return createRank;
	}
	public void setCreateRank(String createRank) {
		this.createRank = createRank;
	}
    
    public String getCreateImage() {
		return createImage;
	}
	public void setCreateImage(String createImage) {
		this.createImage = createImage;
	}
	
	/**
	 * 如果指定了有效的id则不自动生成id
	 */
	@Override
	public Long prePersist() {
		if (IdGen.isInvalidId(this.getId())) {
			return super.prePersist();
		}
		return this.getId();
	}
}
