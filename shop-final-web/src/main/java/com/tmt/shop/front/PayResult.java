package com.tmt.shop.front;

import java.io.Serializable;

/**
 * 支付结果
 * @author root
 */
public class PayResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static int OK = 10000;
	public static int NEED_PAY = 10001;
	public static int NOT_PAY = 10002;
	public static int FAIL = 99999;
	
	private Integer returnFlag;
	private Object returnObj;
	private String remarks;
	
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Integer getReturnFlag() {
		return returnFlag;
	}
	public void setReturnFlag(Integer returnFlag) {
		this.returnFlag = returnFlag;
	}
	public Object getReturnObj() {
		return returnObj;
	}
	public void setReturnObj(Object returnObj) {
		this.returnObj = returnObj;
	}

	/**
	 * 正确返回
	 * @param answer
	 * @return
	 */
	public static PayResult ok(Object obj) {
		PayResult result = new PayResult();
		result.setReturnFlag(OK);
		result.setReturnObj(obj);
		return result;
	}
	
	/**
	 * 错误返回
	 * @param answer
	 * @return
	 */
	public static PayResult fail(String msg) {
		PayResult result = new PayResult();
		result.setReturnFlag(FAIL);
		result.setReturnObj(msg);
		return result;
	}
	
	/**
	 * 需要支付
	 * @param answer
	 * @return
	 */
	public static PayResult needPay(Object obj) {
		PayResult result = new PayResult();
		result.setReturnFlag(NEED_PAY);
		result.setReturnObj(obj);
		return result;
	}
	
	/**
	 * 需要支付
	 * @param answer
	 * @return
	 */
	public static PayResult notPay() {
		PayResult result = new PayResult();
		result.setReturnFlag(NOT_PAY);
		return result;
	}
}