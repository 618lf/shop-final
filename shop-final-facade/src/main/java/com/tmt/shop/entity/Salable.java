package com.tmt.shop.entity;

import java.math.BigDecimal;

/**
 * 
 * @author lifeng
 */
public interface Salable {

	/**
	 * 名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 价格
	 * 
	 * @return
	 */
	BigDecimal getPrice();
	
	/**
	 * 重量
	 * @return
	 */
	String getWeight();

	/**
	 * 是否上架
	 * @return
	 */
	Byte getIsMarketable();
	
	/**
	 * 是否可售
	 * @return
	 */
	Byte getIsSalestate();
}