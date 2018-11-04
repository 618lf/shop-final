package com.tmt.wechat.bean.kefu;

import java.util.List;

public class KefuRecords {

	private List<KefuRecord> records;
	private Integer number;
	private Long msgid;

	public List<KefuRecord> getRecords() {
		return records;
	}

	public void setRecords(List<KefuRecord> records) {
		this.records = records;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getMsgid() {
		return msgid;
	}

	public void setMsgid(Long msgid) {
		this.msgid = msgid;
	}
}
