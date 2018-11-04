package com.tmt.wechat.bean.base;

import java.io.Serializable;


/**
 * 微信请求状态数据
 * @author lifeng
 *
 */
public class BaseResult implements Serializable{

	private static final long serialVersionUID = -3936043437027443795L;
	
	private String errcode;
	private String errmsg;
	
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
