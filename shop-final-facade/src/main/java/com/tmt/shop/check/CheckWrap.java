package com.tmt.shop.check;

import java.util.List;

import com.tmt.shop.entity.Order;
import com.tmt.system.entity.User;

public class CheckWrap {

	private Order order;
	private User user;
	private List<CheckItem> items;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	public List<CheckItem> getItems() {
		return items;
	}

	public void setItems(List<CheckItem> items) {
		this.items = items;
	}
}