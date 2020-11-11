package com.tmt.wechat.bean.kefu;

import java.io.Serializable;

public class KefuRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * kf_account 完整客服账号，格式为：账号前缀@公众号微信号
	 */
	private String kf_account;

	/**
	 * nickname 客服昵称，最长6个汉字或12个英文字符
	 */
	private String nickname;

	/**
	 * invite_wx 接收绑定邀请的客服微信号
	 */
	private String invite_wx;

	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getInvite_wx() {
		return invite_wx;
	}

	public void setInvite_wx(String invite_wx) {
		this.invite_wx = invite_wx;
	}
}
