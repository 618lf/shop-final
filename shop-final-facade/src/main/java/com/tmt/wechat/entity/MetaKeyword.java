package com.tmt.wechat.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;

/**
 * Meta 关键词
 * @author root
 */
public class MetaKeyword extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String keyword;
	private String hkeyword; 
	private String appId; // 对应公众号
	private Integer type; // 1 文本， 2 图文， 4 图片
	private String config; // 相关配置
	private String metaId; // 相关素材ID
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getHkeyword() {
		return hkeyword;
	}
	public void setHkeyword(String hkeyword) {
		this.hkeyword = hkeyword;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
	public String getMetaId() {
		return metaId;
	}
	public void setMetaId(String metaId) {
		this.metaId = metaId;
	}
}