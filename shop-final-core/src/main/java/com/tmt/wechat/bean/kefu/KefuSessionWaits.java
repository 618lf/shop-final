package com.tmt.wechat.bean.kefu;

import java.util.List;

public class KefuSessionWaits {

	/**
	 * count 未接入会话数量
	 */
	private Long count;

	/**
	 * waitcaselist 未接入会话列表，最多返回100条数据
	 */
	private List<KefuSession> waitcaselist;

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public List<KefuSession> getWaitcaselist() {
		return waitcaselist;
	}

	public void setWaitcaselist(List<KefuSession> waitcaselist) {
		this.waitcaselist = waitcaselist;
	}
}
