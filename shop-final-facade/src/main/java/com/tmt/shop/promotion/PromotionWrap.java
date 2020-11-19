package com.tmt.shop.promotion;

import java.math.BigDecimal;

import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.system.entity.User;

/**
 * 促销的壳
 * @author root
 */
public class PromotionWrap {

	private Promotion promotion;
	private Order order;
	private OrderItem item;
	private User user;
	
	// 中间计算结果
	private BigDecimal amount = BigDecimal.ZERO;
	private int quantity = 0;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public Promotion getPromotion() {
		return promotion;
	}
	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	public OrderItem getItem() {
		return item;
	}
	public void setItem(OrderItem item) {
		this.item = item;
	}
	public Byte getType() {
		return this.getPromotion().getType();
	}
}