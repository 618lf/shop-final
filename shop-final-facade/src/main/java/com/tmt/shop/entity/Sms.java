package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 短信配置 管理
 * 
 * @author 超级管理员
 * @date 2017-09-12
 */
public class Sms extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name; // 描述
	private Byte scene; // 状态：1 验证码、 2 订单通知
	private String appkey; // APPKEY
	private String secret; // SECRET
	private Byte sort; // 排序
	private String signName; // 签名
	private String template; // 模板

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getScene() {
		return scene;
	}

	public void setScene(Byte scene) {
		this.scene = scene;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Byte getSort() {
		return sort;
	}

	public void setSort(Byte sort) {
		this.sort = sort;
	}
}
