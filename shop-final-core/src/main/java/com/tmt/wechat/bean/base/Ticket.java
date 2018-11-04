package com.tmt.wechat.bean.base;

import java.util.Date;

import com.tmt.common.utils.DateUtil3;

/**
 * 票据 js - sdk
 * @author root
 *
 */
public class Ticket extends BaseResult{

	private static final long serialVersionUID = -5982436805501081006L;
	
	private String ticket;
	private Integer expires_in;
	private Date addTime;
	
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Integer getExpires_in() {
		return expires_in == null?0:expires_in;
	}
	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	/**
	 * 判断是否过期
	 * @param expiresIn 过期的秒
	 * @return
	 */
	public Boolean isExpired(){
		if(this.addTime != null && (DateUtil3.getTimeStampNow().getTime() - this.addTime.getTime() <= this.getExpires_in() * 1000) ) {
			return Boolean.FALSE;
		} else if(this.addTime != null) {
			return Boolean.TRUE;
		}
		return null; //无法判断
	}
}
