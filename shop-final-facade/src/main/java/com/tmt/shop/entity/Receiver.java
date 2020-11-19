package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 会员地址 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Receiver extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String areaId; // 地区
	private String areaName; // 地区
	private String address; // 送货的地址
	private String location;// 经维度
	private String phone; // 联系方式
	private String houseNumber; // 门牌号 101
	private String consignee; // 收货人
	private Byte sex; // 性别 0 1
	private Byte isDefault; // 是否默认
	private String tag; // 标签
	private Byte isVerifiedPhone; // 是否验证通过的手机号(0否，1是)

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public Byte getIsVerifiedPhone() {
		return isVerifiedPhone;
	}

	public void setIsVerifiedPhone(Byte isVerifiedPhone) {
		this.isVerifiedPhone = isVerifiedPhone;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public Byte getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 得到全地址
	 * 
	 * @return
	 */
	public String getArea() {
		StringBuilder full = new StringBuilder();
		return full.append(this.getAreaName()).append(this.getAddress()).toString();
	}

	/**
	 * 得到全地址
	 * 
	 * @return
	 */
	public String getFullAddress() {
		StringBuilder full = new StringBuilder();
		return full.append(this.getAreaName()).append(this.getAddress()).append(this.getHouseNumber()).toString();
	}
}