package com.tmt.bbs.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 主题点赞 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
public class TopicHit extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long topicId; // 主题
	private String createRank; // 用户等级图片
	private String createImage; // 用户图像
	private Byte isCancel; // 是否取消
    
    public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
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
    
    public Byte getIsCancel() {
		return isCancel;
	}
	public void setIsCancel(Byte isCancel) {
		this.isCancel = isCancel;
	}
}
