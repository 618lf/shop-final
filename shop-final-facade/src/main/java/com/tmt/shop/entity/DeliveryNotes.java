package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 物流信息 管理
 * @author 超级管理员
 * @date 2016-03-12
 */
public class DeliveryNotes extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String trackingNo; // 单号
	private String corpCode; // 快递公司代码
	private String corpName; // 快递公司名称
	private String noteTime; // 记录时间
	private String noteMessage; // 记录信息
	
    public String getTrackingNo() {
		return trackingNo;
	}
	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}
    public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
    public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
    public String getNoteTime() {
		return noteTime;
	}
	public void setNoteTime(String noteTime) {
		this.noteTime = noteTime;
	}
    public String getNoteMessage() {
		return noteMessage;
	}
	public void setNoteMessage(String noteMessage) {
		this.noteMessage = noteMessage;
	}
	@Override
	public Long prePersist() {
		return this.id;
	}
}