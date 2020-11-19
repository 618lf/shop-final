package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 配送方式 管理
 * 
 * @author 超级管理员
 * @date 2016-01-20
 */
public class ShippingMethod extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name; // 名称
	private Integer sort; // 排序
	private java.math.BigDecimal firstPrice; // 默认首重价格
	private Float firstWeight; // 首重量
	private java.math.BigDecimal continuePrice; // 默认续重价格
	private Float continueWeight; // 续重量
	private String icon; // 图标
	private Long deliveryCorpId; // 默认物流公司
	private String deliveryCorpName; // 默认物流公司
	private List<PaymentShiopingMethod> paymentMethods;// 支持的支付方式
	private List<ShippingMethodArea> areas; // 区域配置
	private Map<String, ShippingMethodArea> mareas; // 区域配置

	public Map<String, ShippingMethodArea> getMareas() {
		return mareas;
	}

	public void setMareas(Map<String, ShippingMethodArea> mareas) {
		this.mareas = mareas;
	}

	public List<ShippingMethodArea> getAreas() {
		return areas;
	}

	public void setAreas(List<ShippingMethodArea> areas) {
		this.areas = areas;
	}

	public List<PaymentShiopingMethod> getPaymentMethods() {
		return paymentMethods;
	}

	public void setPaymentMethods(List<PaymentShiopingMethod> paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public java.math.BigDecimal getFirstPrice() {
		return firstPrice;
	}

	public void setFirstPrice(java.math.BigDecimal firstPrice) {
		this.firstPrice = firstPrice;
	}

	public Float getFirstWeight() {
		return firstWeight;
	}

	public void setFirstWeight(Float firstWeight) {
		this.firstWeight = firstWeight;
	}

	public java.math.BigDecimal getContinuePrice() {
		return continuePrice;
	}

	public void setContinuePrice(java.math.BigDecimal continuePrice) {
		this.continuePrice = continuePrice;
	}

	public Float getContinueWeight() {
		return continueWeight;
	}

	public void setContinueWeight(Float continueWeight) {
		this.continueWeight = continueWeight;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getDeliveryCorpId() {
		return deliveryCorpId;
	}

	public void setDeliveryCorpId(Long deliveryCorpId) {
		this.deliveryCorpId = deliveryCorpId;
	}

	public String getDeliveryCorpName() {
		return deliveryCorpName;
	}

	public void setDeliveryCorpName(String deliveryCorpName) {
		this.deliveryCorpName = deliveryCorpName;
	}

	/**
	 * 是否支持
	 * 
	 * @param paymentMethod
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isSupport(PaymentMethod paymentMethod) {
		List<PaymentShiopingMethod> methods = this.getPaymentMethods();
		if (methods != null) {
			for (PaymentShiopingMethod method : methods) {
				if (method.getSupport() == 1 && paymentMethod.getId().equals(method.getPaymentMethods())) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * 计算税费
	 * 
	 * @param weight
	 * @return
	 */
	public BigDecimal calcula(Double weight) {
		if (weight.compareTo((double) this.firstWeight) <= 0) {
			return this.firstPrice;
		}
		Double continueWeight = BigDecimalUtil.sub(weight, this.firstWeight);
		return BigDecimalUtil.add(this.firstPrice, BigDecimalUtil.mul(
				BigDecimalUtil.valueOf(BigDecimalUtil.div(continueWeight, this.continueWeight)), this.continuePrice));
	}
}