package com.tmt.shop.entity;

/**
 * 支付基础类(提供基础的支付功能)
 * 有支付能力的对象
 * @author root
 */
public interface Payable {

	/**
	 * 支付的实体主键
	 * @return
	 */
	public Long getId();
	
	/**
	 * JS 支付的凭证
	 * @return
	 */
	public String getJsSn();
	
	/**
	 * 扫码支付的凭证
	 * @return
	 */
	public String getNaSn();
	
	/**
	 * 支付的价格
	 * @return
	 */
	public String getPayPrice();
	
	/**
	 * 支付的描述
	 * @return
	 */
	public String getPayBody();
}
