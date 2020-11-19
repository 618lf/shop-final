package com.tmt.shop.check;

import com.tmt.core.utils.StringUtils;

/**
 * 订单校验结果 0 成功 1 商品下架或信息变动，请重新购买 2 商品库存不足 3 已超过商品限购数量
 * 
 * @author root
 */
public class OrderCheckResult {

	private byte code; // 校验结果码
	private String msg; // 校验结果描述
	private CheckItem item;

	public CheckItem getItem() {
		return item;
	}

	public void setItem(CheckItem item) {
		this.item = item;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 判断是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return code == 0;
	}

	/**
	 * 校验成功
	 * 
	 * @return
	 */
	public static OrderCheckResult success() {
		OrderCheckResult result = new OrderCheckResult();
		result.setCode((byte) 0);
		return result;
	}

	/**
	 * 优惠套装已失效
	 * 
	 * @return
	 */
	public static OrderCheckResult orderInvalid() {
		OrderCheckResult result = new OrderCheckResult();
		result.setCode((byte) 1);
		result.setMsg("订单已失效");
		return result;
	}

	/**
	 * 商品下架或信息变动，请重新购买
	 * 
	 * @return
	 */
	public static OrderCheckResult productModify(String name) {
		OrderCheckResult result = new OrderCheckResult();
		result.setCode((byte) 1);
		result.setMsg(StringUtils.format("商品'%s'下架或信息变动，请重新购买", name));
		return result;
	}

	/**
	 * 商品下架或信息变动，请重新购买
	 * 
	 * @return
	 */
	public static OrderCheckResult outOfStock(CheckItem item) {
		OrderCheckResult result = new OrderCheckResult();
		result.setCode((byte) 2);
		result.setMsg(StringUtils.format("商品'%s'库存不足", item.getName()));
		result.setItem(item);
		return result;
	}

	/**
	 * 已超过商品限购数量
	 * 
	 * @return
	 */
	public static OrderCheckResult beyondGoodsLimit(String name) {
		OrderCheckResult result = new OrderCheckResult();
		result.setCode((byte) 3);
		result.setMsg(new StringBuilder(name).append("已超过限购数量").toString());
		return result;
	}
}