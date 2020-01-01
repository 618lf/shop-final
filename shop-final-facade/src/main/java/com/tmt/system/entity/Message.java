package com.tmt.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.common.entity.BaseEntity;
import com.tmt.common.utils.time.DateUtils;

/**
 * 站内信
 * @author lifeng
 */
public class Message extends BaseEntity<Long> implements Serializable{
   
	private static final long serialVersionUID = 1L;
	private String title;
    private String content;
    private Long sendUserId;
    private String sendUserName;
    private Date sendTime;
    private Long receiverUserId;
    private String receiverUserName;
    private Date viewTime;
    private Byte status;
    private MessageBox msgBox;
    private Byte delFalg;
    private Long own;
	private String relaTitle;
	private String relaUrl;

	public String getRelaTitle() {
		return relaTitle;
	}
	public void setRelaTitle(String relaTitle) {
		this.relaTitle = relaTitle;
	}
	public String getRelaUrl() {
		return relaUrl;
	}
	public void setRelaUrl(String relaUrl) {
		this.relaUrl = relaUrl;
	}
	public Long getOwn() {
		return own;
	}
	public void setOwn(Long own) {
		this.own = own;
	}
	public Long getSendUserId() {
		return sendUserId;
	}
	public Long getReceiverUserId() {
		return receiverUserId;
	}
	public void setSendUserId(Long sendUserId) {
		this.sendUserId = sendUserId;
	}
	public void setReceiverUserId(Long receiverUserId) {
		this.receiverUserId = receiverUserId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSendUserName() {
		return sendUserName;
	}
	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getReceiverUserName() {
		return receiverUserName;
	}
	public void setReceiverUserName(String receiverUserName) {
		this.receiverUserName = receiverUserName;
	}
	public Date getViewTime() {
		return viewTime;
	}
	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
	}
	public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public MessageBox getMsgBox() {
		return msgBox;
	}
	public void setMsgBox(MessageBox msgBox) {
		this.msgBox = msgBox;
	}
	public Byte getDelFalg() {
		return delFalg;
	}
	public void setDelFalg(Byte delFalg) {
		this.delFalg = delFalg;
	}
	public Message(){
		this.setDelFalg(BaseEntity.NO);
	}
	
	/**
	 * 根据模板生成一条收件箱信息
	 * @param from
	 * @param to
	 * @return
	 */
	public Message in(){
		Message msg = this.copy();
		msg.setMsgBox(MessageBox.IN);
		msg.setStatus(BaseEntity.NO);//未读
		msg.setOwn(this.getReceiverUserId());
		return msg;
	}
	
	/**
	 * 根据模板生成一条发件箱信息
	 * @param from
	 * @param to
	 * @return
	 */
	public Message out(){
		Message msg = this.copy();
		msg.setMsgBox(MessageBox.OUT);
		msg.setOwn(this.getSendUserId());
		return msg;
	}
	
	private Message copy(){
		Message msg = new Message();
		msg.setContent(this.getContent());
		msg.setTitle(this.getTitle());
		msg.setSendUserId(this.getSendUserId());
		msg.setSendUserName(this.getSendUserName());
		msg.setSendTime(DateUtils.getTimeStampNow());
		msg.setReceiverUserId(this.getReceiverUserId());
		msg.setReceiverUserName(this.getReceiverUserName());
		msg.setRelaTitle(this.getRelaTitle());
		msg.setRelaUrl(this.getRelaUrl());
		return msg;
	}
	
	/**
	 * 站内信
	 * @author lifeng
	 */
	public static enum MessageBox {
		IN, OUT, DRAFT, TRASH
	}
}