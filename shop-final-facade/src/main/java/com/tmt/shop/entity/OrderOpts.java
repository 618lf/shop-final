package com.tmt.shop.entity;

/**
 * 订单操作动作
 * @author lifeng
 */
public abstract class OrderOpts {

	// 旧版： 1、下单 11、审核 2、付款 3、发货 4、收货 5、申请退货 6、退货 7、退款
	// orderState 和 orderEvent 使用新版（如下定义）
	public static Byte BOOK = 1; // 下单
	public static Byte CONFIRM = 2; // 审核
	public static Byte PAY = 3; // 支付
	public static Byte SHIPPING = 4; // 发货
	public static Byte UN_SHIPPING = 5; // 取消发货
	public static Byte RECEIPT = 6; // 收货
	public static Byte APPLY_RETURNS = 7; // 申请退货
	public static Byte RETURNS = 8;// 退货
	public static Byte APPLY_REFUND = 9; // 申请退款
	public static Byte REFUND_PROCESS = 10; // 退款处理
	public static Byte REFUNDS = 11; // 退款
	public static Byte CANCEL = 12; // 取消
	public static Byte COMPLETE = 13; // 完成
}