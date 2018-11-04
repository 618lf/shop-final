package com.tmt.wechat.bean.base;

import java.io.Serializable;
import java.util.Date;

import com.tmt.common.utils.DateUtil3;

/**
 * Token
 * @author lifeng
 */
public class Token extends BaseResult implements Serializable {

	private static final long serialVersionUID = 4854663775452644338L;
	
	private String access_token;
	private Date addTime;
	private int expires_in;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expiresIn) {
		expires_in = expiresIn;
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
		if(this.addTime != null && (DateUtil3.getTimeStampNow().getTime() - this.addTime.getTime() <= this.expires_in * 1000) ) {
			return Boolean.FALSE;
		} else if(this.addTime != null) {
			return Boolean.TRUE;
		}
		return null; //无法判断
	}
}
