package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 发货管理 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Shipping extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sn; // 序号
	private Long orderId; // 编号
	private String orderSn; // 订单编号
	private Long areaId; // 送货的地区
	private String areaName; // 送货的地区
	private String address; // 送货的地址
	private String phone; // 联系方式
	private String zipCode; // 邮编
	private String consignee; // 收货人
	private Long shippingMethodId; // 送货方式
	private String shippingMethod; // 送货方式
	private Long deliveryCorpId; // 快递公司
	private String deliveryCorp; // 快递公司
	private String deliveryCorpCode; // 快递公司
	private String deliveryCorpUrl; // 快递公司
	private java.math.BigDecimal freight; // 运费
	private String trackingNo; // 单号
	private List<ShippingItem> items;

	private Integer productQuantity; // 商品数量
	private String productSn; // 商品编号

	public Integer getProductQuantity() {
		return productQuantity;
	}

	public void setProductQuantity(Integer productQuantity) {
		this.productQuantity = productQuantity;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public Long getShippingMethodId() {
		return shippingMethodId;
	}

	public void setShippingMethodId(Long shippingMethodId) {
		this.shippingMethodId = shippingMethodId;
	}

	public Long getDeliveryCorpId() {
		return deliveryCorpId;
	}

	public void setDeliveryCorpId(Long deliveryCorpId) {
		this.deliveryCorpId = deliveryCorpId;
	}

	public List<ShippingItem> getItems() {
		return items;
	}

	public void setItems(List<ShippingItem> items) {
		this.items = items;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(String shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getDeliveryCorp() {
		return deliveryCorp;
	}

	public void setDeliveryCorp(String deliveryCorp) {
		this.deliveryCorp = deliveryCorp;
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

	public java.math.BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(java.math.BigDecimal freight) {
		this.freight = freight;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	/**
	 * 单纯的商品价格
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public BigDecimal getPrice() {
		List<ShippingItem> items = this.getItems();
		if (items != null) {
			BigDecimal _price = BigDecimal.ZERO;
			for (ShippingItem item : items) {
				_price = BigDecimalUtil.add(_price, item.getTotal());
			}
			return _price;
		}
		return BigDecimal.ZERO;
	}
}