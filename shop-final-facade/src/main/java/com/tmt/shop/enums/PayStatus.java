package com.tmt.shop.enums;

/**
 * 订单状态
 * @author root
 */
public enum PayStatus {
	unpaid("未付款"), partialpaid("部分支付"), paid("已支付"), refunds_request("退款申请"), refunds_process("退款处理"), partialrefunds("部分退款"), refunded("已退款");
	private String name;
	private PayStatus(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}