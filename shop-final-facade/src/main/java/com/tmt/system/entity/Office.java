package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.common.entity.BaseParentEntity;

/**
 * 机构
 * @author lifeng
 */
public class Office extends BaseParentEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private String areaId;
    private String code;
    private Byte type;// 机构类型（1：公司；2：部门；3：小组）
    private String address;
    private String zipCode;
    private String master;
    private String phone;
    private String fax;//传真
    private String email;
    private String areaName;
    
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}