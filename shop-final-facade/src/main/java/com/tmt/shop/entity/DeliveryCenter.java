package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 送货中心 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
public class DeliveryCenter extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private String areaId; // 地区
	private String areaName; // 地区
	private String address; // 详细地址
	private String contact; // 联系人
	private String mobile; // 手机
	private String phone; // 固定电话
	private String zipCode; // 邮编
	private Byte isDefault; // 是否默认
	
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
    public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
    public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
    public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
    public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
    
    public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
    public Byte getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}
}
