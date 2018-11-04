package com.tmt.wechat.bean.kefu;

public class KefuSession {

	/**
	 * kf_account 正在接待的客服，为空表示没有人在接待
	 */
	private String kf_account;

	/**
	 * createtime 会话接入的时间 ，UNIX时间戳
	 */
	private long createtime;

	public String getKf_account() {
		return kf_account;
	}

	public void setKf_account(String kf_account) {
		this.kf_account = kf_account;
	}

	public long getCreatetime() {
		return createtime;
	}

	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}

}
