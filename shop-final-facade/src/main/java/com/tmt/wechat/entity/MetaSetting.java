package com.tmt.wechat.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 回复配置 管理
 * @author 超级管理员
 * @date 2016-09-27
 */
public class MetaSetting extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String appId; // 对应公众号APP_ID
	private Byte defaultType; // see com.tmt.wechat.entity.WechatConstant
	private String defaultConfig; // 对应的配置内容
	private Byte attentionType; // see com.tmt.wechat.entity.WechatConstant
	private String attentionConfig; // 对应的配置内容
    
    public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    
    public Byte getDefaultType() {
		return defaultType;
	}
	public void setDefaultType(Byte defaultType) {
		this.defaultType = defaultType;
	}
    
    public String getDefaultConfig() {
		return defaultConfig;
	}
	public void setDefaultConfig(String defaultConfig) {
		this.defaultConfig = defaultConfig;
	}
    
    public Byte getAttentionType() {
		return attentionType;
	}
	public void setAttentionType(Byte attentionType) {
		this.attentionType = attentionType;
	}
    
    public String getAttentionConfig() {
		return attentionConfig;
	}
	public void setAttentionConfig(String attentionConfig) {
		this.attentionConfig = attentionConfig;
	}
}
