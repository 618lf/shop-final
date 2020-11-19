package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tmt.shop.utils.PromotionTypes;
/**
 * 订单促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class OrderPromotion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long orders; // 订单
	private Long promotions; // 促销
	private String promotionName; // 促销名称
	private Byte promotionType;  // 促销类型
	private Long fissions; // 裂变
	private BigDecimal reduce; // 优惠的金额
	private Byte isGive; // 是否赠送
	private String coupons; // 获取的优惠券
	private Integer val;// 面值
    
	public Integer getVal() {
		return val;
	}
	public void setVal(Integer val) {
		this.val = val;
	}
	public String getCoupons() {
		return coupons == null?"":coupons;
	}
	public void setCoupons(String coupons) {
		this.coupons = coupons;
	}
	public Byte getIsGive() {
		return isGive;
	}
	public void setIsGive(Byte isGive) {
		this.isGive = isGive;
	}
	public BigDecimal getReduce() {
		return reduce;
	}
	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
	}
	public String getTypeName() {
		return PromotionTypes.getTypeName(promotionType);
	}
	public Byte getPromotionType() {
		return promotionType;
	}
	public void setPromotionType(Byte promotionType) {
		this.promotionType = promotionType;
	}
	public String getPromotionName() {
		return promotionName;
	}
	public void setPromotionName(String promotionName) {
		this.promotionName = promotionName;
	}
	public Long getOrders() {
		return orders;
	}
	public void setOrders(Long orders) {
		this.orders = orders;
	}
    public Long getPromotions() {
		return promotions;
	}
	public void setPromotions(Long promotions) {
		this.promotions = promotions;
	}
    public Long getFissions() {
		return fissions;
	}
	public void setFissions(Long fissions) {
		this.fissions = fissions;
	}
	
	/**
	 * 获取的优惠券, 不需记录分裂的优惠券
	 * @return
	 */
	public void addCoupon(CouponCode code) {
		if (code != null && code.getFissionId() == null) {
			StringBuilder coupons = new StringBuilder(this.getCoupons());
			coupons.append(",").append(code.getId());
			this.setCoupons(coupons.toString());
		}
	}
}