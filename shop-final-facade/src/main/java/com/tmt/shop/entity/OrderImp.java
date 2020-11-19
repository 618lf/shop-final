package com.tmt.shop.entity;

import java.io.Serializable;

/**
 * 商品导入
 * @author lifeng
 */
public class OrderImp implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String sn;
	private String createNo; //创建人名称
	private String createName;//创建人名称
	private String area; // 区域
	private String address; // 送货的地址 深圳市南山区海岸时代公寓（深南大道12069号）
	private String phone; // 联系方式
	private String consignee; // 收货人
	private String productSn; // 产品
	private String productName; // 产品
	private Integer quantity; // 数量
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getCreateNo() {
		return createNo;
	}
	public void setCreateNo(String createNo) {
		this.createNo = createNo;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getProductSn() {
		return productSn;
	}
	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	/**
	 *  按照这个去重复
	 */
	public String toString() {
		return new StringBuilder(this.getSn()).append("-").append(this.getCreateNo()).toString();
	}
}
