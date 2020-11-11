package com.tmt.wechat.bean.qrcode;

import com.tmt.wechat.bean.base.BaseResult;

/**
 * 二维码
 * @author lifeng
 */
public class QrCodeTicket extends BaseResult{

	private static final long serialVersionUID = 4743499258775735829L;
	private String ticket;
	private Integer expire_seconds;
	private String url;
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Integer getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(Integer expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}