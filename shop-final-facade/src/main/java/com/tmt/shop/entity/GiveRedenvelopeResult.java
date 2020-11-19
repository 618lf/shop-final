package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.utils.Ints;

/**
 * 发放红包结果
 * @author root
 */
public class GiveRedenvelopeResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static int OK = 10000; // 成功
	public static int SHARE = 10001;// 需分享
	public static int FAIL = 99999; // 失败
	
	private Integer returnFlag;
	private Integer price = 0; // 红包金额
	private Long fissions; // 裂变号
	private String msg; // 消息
	
	public Integer getReturnFlag() {
		return returnFlag;
	}
	public void setReturnFlag(Integer returnFlag) {
		this.returnFlag = returnFlag;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Long getFissions() {
		return fissions;
	}
	public void setFissions(Long fissions) {
		this.fissions = fissions;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * 获取的优惠券
	 * @return
	 */
	public GiveRedenvelopeResult addCoupon(CouponCode code) {
		if (code != null) {
			this.setPrice(Ints.addI(this.getPrice(), code.getVal()));
			if (this.getPrice() != null) {
				this.setReturnFlag(OK);
			}
			if (code.getFissionId() != null) {
				this.setFissions(code.getFissionId());
				this.setReturnFlag(SHARE);
			}
		}
		return this;
	}
	
	/**
	 * 错误返回
	 * @param answer
	 * @return
	 */
	public static GiveRedenvelopeResult fail(String msg) {
		GiveRedenvelopeResult result = new GiveRedenvelopeResult();
		result.setMsg(msg);
		result.setReturnFlag(FAIL);
		return result;
	}
}