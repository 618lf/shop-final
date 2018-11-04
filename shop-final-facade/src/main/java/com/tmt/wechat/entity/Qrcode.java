package com.tmt.wechat.entity;

import java.io.Serializable;

import com.tmt.common.entity.BaseEntity;
/**
 * 二维码 管理
 * @author 超级管理员
 * @date 2016-10-01
 */
public class Qrcode extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String appId; // 公众号
	private String appName; // 公众号名称
	private String actionName; // 二维码类型
	private String sceneStr; // 二维码唯一标识
	private String ticket; // 唯一凭证
	private String image; // 二维码图片
	private Byte type; // 0：什么都不做, 1：文本回复，2：图片回复，3：语音回复，4：图片回复，5：/，6：/，7：匹配关键字
	private String config; // 对应的配置内容
    
    public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
    public String getSceneStr() {
		return sceneStr;
	}
	public void setSceneStr(String sceneStr) {
		this.sceneStr = sceneStr;
	}
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}