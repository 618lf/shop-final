package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

public class NoticeTask extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String msg; // 消息
	private String app; // APP ，或手机号
	private Byte type; // 状态：0未知，1：微信公众号, 2 短信
	private Byte state; // 状态：0初始，1：操作中
	private Byte reset; // 发送失败, 重置次数
	private String source; // 校验码

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Byte getReset() {
		return reset;
	}

	public void setReset(Byte reset) {
		this.reset = reset;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}