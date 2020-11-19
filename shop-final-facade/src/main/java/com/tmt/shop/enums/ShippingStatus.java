package com.tmt.shop.enums;

/**
 * 发货状态
 * @author root
 */
public enum ShippingStatus {
	unshipped("未发货"), partialshipment("部分发货"), shipped("已发货"),  receipted("已收货"), return_request("退货申请"), partialreturns("部分退货"), returned("已退货");
	private String name;
	private ShippingStatus(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}