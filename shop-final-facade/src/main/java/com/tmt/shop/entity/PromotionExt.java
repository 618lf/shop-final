package com.tmt.shop.entity;

import java.io.Serializable;
/**
 * 多级促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class PromotionExt implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private java.math.BigDecimal orderPrice; // 订单金额
	private Integer orderQuantity; // 购买数量
	private java.math.BigDecimal reduce; // 金额
	private Double discount; // 折扣
	private java.math.BigDecimal orderPrice1; // 订单金额
	private Integer orderQuantity1; // 购买数量
	private java.math.BigDecimal reduce1; // 金额
	private Double discount1; // 折扣
	private java.math.BigDecimal orderPrice2; // 订单金额
	private Integer orderQuantity2; // 购买数量
	private java.math.BigDecimal reduce2; // 金额
	private Double discount2; // 折扣
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public java.math.BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(java.math.BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
	public java.math.BigDecimal getReduce() {
		return reduce;
	}
	public void setReduce(java.math.BigDecimal reduce) {
		this.reduce = reduce;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public java.math.BigDecimal getOrderPrice1() {
		return orderPrice1;
	}
	public void setOrderPrice1(java.math.BigDecimal orderPrice1) {
		this.orderPrice1 = orderPrice1;
	}
    public Integer getOrderQuantity1() {
		return orderQuantity1;
	}
	public void setOrderQuantity1(Integer orderQuantity1) {
		this.orderQuantity1 = orderQuantity1;
	}
    public java.math.BigDecimal getReduce1() {
		return reduce1;
	}
	public void setReduce1(java.math.BigDecimal reduce1) {
		this.reduce1 = reduce1;
	}
    public Double getDiscount1() {
		return discount1;
	}
	public void setDiscount1(Double discount1) {
		this.discount1 = discount1;
	}
    public java.math.BigDecimal getOrderPrice2() {
		return orderPrice2;
	}
	public void setOrderPrice2(java.math.BigDecimal orderPrice2) {
		this.orderPrice2 = orderPrice2;
	}
    public Integer getOrderQuantity2() {
		return orderQuantity2;
	}
	public void setOrderQuantity2(Integer orderQuantity2) {
		this.orderQuantity2 = orderQuantity2;
	}
    public java.math.BigDecimal getReduce2() {
		return reduce2;
	}
	public void setReduce2(java.math.BigDecimal reduce2) {
		this.reduce2 = reduce2;
	}
    public Double getDiscount2() {
		return discount2;
	}
	public void setDiscount2(Double discount2) {
		this.discount2 = discount2;
	}
}
