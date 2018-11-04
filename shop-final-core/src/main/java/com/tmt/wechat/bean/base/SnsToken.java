package com.tmt.wechat.bean.base;

import java.util.Date;

import com.tmt.common.utils.DateUtil3;

public class SnsToken extends BaseResult{
	
	private static final long serialVersionUID = -3958198538240647148L;
	
	private String access_token;
	private Integer expires_in;
	private String refresh_token;
	private String openid;
	private String unionid;
	private String scope;
	private String appId;
	private Date createDate;

	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getUnionid() {
		return unionid;
	}
	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String accessToken) {
		access_token = accessToken;
	}
	public Integer getExpires_in() {
		return expires_in;
	}
	public void setExpires_in(Integer expiresIn) {
		expires_in = expiresIn;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refreshToken) {
		refresh_token = refreshToken;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	
	/**
	 * 判断token 是否过期
	 * @return
	 */
	public Boolean isExpired(){
		try{
			int expires_in = this.getExpires_in();
			if (this.getCreateDate() != null && (DateUtil3.getTimeStampNow().getTime() - this.getCreateDate().getTime() <= expires_in * 1000) ) {
				return Boolean.FALSE;
			} else if(this.getCreateDate() != null) {
				return Boolean.TRUE;
			}
		}catch(Exception e) {}
		return null;
	}
}