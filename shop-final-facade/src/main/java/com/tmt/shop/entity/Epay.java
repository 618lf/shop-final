package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 支付账户 管理
 * @author 超级管理员
 * @date 2015-09-23
 */
public class Epay extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Byte type; // 类型
	private String icon; // 商标
	private String website; // 官网
	private String name; // 名称
	private String partnerId; // 商户号
	private String partnerName; // 收款方
	private String partnerKey; // 安全密钥
	private String partnerAccount; // 签名或账户
	private String payDomain; // 绑定域名
	private String payUrl;// 支付的链接地址
	private String method;// 提交的方法
	
    public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
    public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
    public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public String getPartnerKey() {
		return partnerKey;
	}
	public void setPartnerKey(String partnerKey) {
		this.partnerKey = partnerKey;
	}
    public String getPartnerAccount() {
		return partnerAccount;
	}
	public void setPartnerAccount(String partnerAccount) {
		this.partnerAccount = partnerAccount;
	}
    public String getPayDomain() {
		return payDomain;
	}
	public void setPayDomain(String payDomain) {
		this.payDomain = payDomain;
	}
}