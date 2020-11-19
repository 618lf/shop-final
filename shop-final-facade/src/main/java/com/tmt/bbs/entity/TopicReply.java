package com.tmt.bbs.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 主题回复 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
public class TopicReply extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long topicId; // 话题
	private String content; // 评论内容
	private String createRank; // 用户等级图片
	private String createImage; // 用户图像
	private String replyUser; // 回复用户
	private Long replyId; // 回复评论
	private Integer hits;
	
    public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
	public Long getTopicId() {
		return topicId;
	}
	public void setTopicId(Long topicId) {
		this.topicId = topicId;
	}
    
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
    
    public String getReplyUser() {
		return replyUser;
	}
	public void setReplyUser(String replyUser) {
		this.replyUser = replyUser;
	}
    
    public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
	}
}
