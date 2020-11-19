package com.tmt.bbs.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 回复点赞 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
public class ReplyHit extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long replyId; // 回复的ID
	private String createRank; // 用户等级图片
	private String createImage; // 用户图像
	private Byte isCancel; // 是否取消
    
    public Long getReplyId() {
		return replyId;
	}
	public void setReplyId(Long replyId) {
		this.replyId = replyId;
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
