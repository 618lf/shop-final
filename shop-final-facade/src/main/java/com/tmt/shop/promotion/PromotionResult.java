package com.tmt.shop.promotion;

import java.math.BigDecimal;

/**
 * 处理结果
 * @author lifeng
 */
public class PromotionResult {

	private boolean result;// -1、0、1
	private BigDecimal reduce;
	private String reason; // reduce == null 的原因
	
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public BigDecimal getReduce() {
		return reduce;
	}
	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	/**
	 * 处理失败，继续下一个处理
	 * @return
	 */
	public static PromotionResult fail() {
		PromotionResult result = new PromotionResult();
		result.setResult(false);
		return result;
	}
	
	/**
	 * 处理成功
	 * 1. 不满足条件 -- 不用继续处理
	 * @return
	 */
	public static PromotionResult success(String reason) {
		PromotionResult result = new PromotionResult();
		result.setResult(true);
		result.setReduce(null);
		result.setReason(reason);
		return result;
	}
	
	/**
	 * 处理成功
	 * 1. 满足条件 -- 不用继续处理
	 * @return
	 */
	public static PromotionResult success(BigDecimal reduce) {
		PromotionResult result = new PromotionResult();
		result.setResult(true);
		result.setReduce(reduce);
		return result;
	}
}