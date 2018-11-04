package com.tmt.wechat.bean.device;

import com.tmt.wechat.bean.base.BaseResult;

public class TransMessageResult extends BaseResult {

	private static final long serialVersionUID = 1L;
	private Integer ret;
	private String ret_info;
	public Integer getRet() {
		return ret;
	}
	public void setRet(Integer ret) {
		this.ret = ret;
	}
	public String getRet_info() {
		return ret_info;
	}
	public void setRet_info(String ret_info) {
		this.ret_info = ret_info;
	}
}
