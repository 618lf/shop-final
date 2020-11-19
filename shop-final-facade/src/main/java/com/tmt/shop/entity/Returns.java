package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;

/**
 * 退货管理 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Returns extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId; // 编号
	private String orderSn; // 订单编号
	private Long areaId; // 区域
	private String areaName; // 区域
	private String address; // 详细地址
	private String shippingMethod; // 送货方式
	private Long shippingMethodId; // 送货方式
	private Long deliveryCorpId; // 快递公司
	private String deliveryCorp; // 快递公司
	private String deliveryCorpCode; // 快递公司
	private String deliveryCorpUrl; // 快递公司
	private java.math.BigDecimal freight; // 运费
	private String phone; // 联系方式
	private String shipper; // 发货人
	private String sn; // 编号
	private String trackingNo; // 单号
	private String zipCode; // 邮编
	private String isStore;// 是否入库（暂时没用到）
	private List<ReturnItem> items;// 退货明细

	public Long getDeliveryCorpId() {
		return deliveryCorpId;
	}

	public void setDeliveryCorpId(Long deliveryCorpId) {
		this.deliveryCorpId = deliveryCorpId;
	}

	public Long getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(Long shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}

	public String getIsStore() {
		return isStore;
	}

	public void setIsStore(String isStore) {
		this.isStore = isStore;
	}

	public List<ReturnItem> getItems() {
		return items;
	}

	public void setItems(List<ReturnItem> items) {
		this.items = items;
	}

	public String getDeliveryCorpCode() {
		return deliveryCorpCode;
	}

	public void setDeliveryCorpCode(String deliveryCorpCode) {
		this.deliveryCorpCode = deliveryCorpCode;
	}

	public String getDeliveryCorpUrl() {
		return deliveryCorpUrl;
	}

	public void setDeliveryCorpUrl(String deliveryCorpUrl) {
		this.deliveryCorpUrl = deliveryCorpUrl;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
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

	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
	}

	public java.math.BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(java.math.BigDecimal freight) {
		this.freight = freight;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getShipper() {
		return shipper;
	}

	public void setShipper(String shipper) {
		this.shipper = shipper;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}