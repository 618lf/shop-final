package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 订单记录 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class OrderLog extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId; // 订单
	private Type type; // 类型
	private String content; // 内容

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public enum Type {
		create("新建"), modify("修改"), confirm("确认"), payment("付款"), apply_refund("申请退款"), refunds_process("退款处理"),
		refunds("退款"), shipping("发货"), unshipping("取消发货"), receipt("签收"), apply_returns("申请退货"), returns("退货"),
		complete("完成"), cancel("取消"), other("其他");

		private String name;

		private Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 从订单信息中获取用户信息
	 */
	public void userOrder(Order user) {
		this.createId = user.getCreateId();
		this.createName = user.getCreateName();
	}
}