package com.tmt.shop.enums;

/**
 * 订单状态
 * @author root
 */
public enum OrderStatus {
	unconfirmed("待审核"), confirmed("已审核"), completed("已完成"), cancelled("已取消");
	private String name;
	private OrderStatus(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}