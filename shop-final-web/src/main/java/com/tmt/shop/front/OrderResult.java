package com.tmt.shop.front;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tmt.shop.entity.Order;

/**
 * 订单结果 -- 用于前端交互
 * @author root
 */
public class OrderResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private BigDecimal price = BigDecimal.ZERO; // 商品总价
	private BigDecimal discount = BigDecimal.ZERO; // 折扣
	private BigDecimal freight = BigDecimal.ZERO; // 运费
	private BigDecimal amount = BigDecimal.ZERO;  // 订单价格
	private String detail; // 折扣明细
	
	// 设置order的数据
	public void setOrder(Order order) {
		if (order != null) {
			this.setPrice(order.getPrice());
			this.setAmount(order.getAmount());
			this.setFreight(order.getFreight());
			this.setDiscount(order.getDiscount());
		}
	}
	
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getFreight() {
		return freight;
	}
	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
}