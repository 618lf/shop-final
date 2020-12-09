package com.tmt.api.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * 项目 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
public class Project extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 项目名称
	private Byte permission; // 访问权限：0-公开、1-私有
	private String baseUrlProduct; // 跟地址：线上环境
	private String baseUrlPpe; // 跟地址：预发布环境
	private String baseUrlQa; // 跟地址：测试环境
    
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    
    public Byte getPermission() {
		return permission;
	}
	public void setPermission(Byte permission) {
		this.permission = permission;
	}
    
    public String getBaseUrlProduct() {
		return baseUrlProduct;
	}
	public void setBaseUrlProduct(String baseUrlProduct) {
		this.baseUrlProduct = baseUrlProduct;
	}
    
    public String getBaseUrlPpe() {
		return baseUrlPpe;
	}
	public void setBaseUrlPpe(String baseUrlPpe) {
		this.baseUrlPpe = baseUrlPpe;
	}
    
    public String getBaseUrlQa() {
		return baseUrlQa;
	}
	public void setBaseUrlQa(String baseUrlQa) {
		this.baseUrlQa = baseUrlQa;
	}
}
