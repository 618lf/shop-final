package com.tmt.shop.utils;

import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.service.NumberGeneratorFacade;

public class SnUtils {

	private static NumberGeneratorFacade numberGenerator = SpringContextHolder.getBean(NumberGeneratorFacade.class);
	private static String SHOP_PRODUCT_SN = "SHOP_PRODUCT_SN";
	private static String SHOP_ORDER_SN = "SHOP_ORDER_SN";
	private static String SHOP_PAYMENT_SN = "SHOP_PAYMENT_SN";
	private static String SHOP_SHIPPING_SN = "SHOP_SHIPPING_SN";
	private static String SHOP_RETUENS_SN = "SHOP_RETUENS_SN";
	private static String SHOP_REFUNDS_SN = "SHOP_REFUNDS_SN";
	
	/**
	 * 创建全局的订单号
	 * @return
	 */
	public static String createPaymentSn() {
		String nextSeq = numberGenerator.generateNumber(SHOP_PAYMENT_SN).toString();
		return new StringBuilder(DateUtils.getTodayStr("yyyyMMdd")).append(StringUtils.leftPad(nextSeq, 8, "0")).toString();
	}
	
	
	/**
	 * 全局产品编号
	 * @return
	 */
	public static String createProductSn() {
		String nextSeq = numberGenerator.generateNumber(SHOP_PRODUCT_SN).toString();
		return new StringBuilder(DateUtils.getTodayStr("yyyyMMdd")).append(StringUtils.leftPad(nextSeq, 8, "0")).toString();
	}
	
	/**
	 * 创建全局的订单号
	 * @return
	 */
	public static String createOrderSn() {
		String nextSeq = numberGenerator.generateNumber(SHOP_ORDER_SN).toString();
		return new StringBuilder(DateUtils.getTodayStr("yyyyMMdd")).append(StringUtils.leftPad(nextSeq, 8, "0")).toString();
	}
	
	/**
	 * 创建全局的订单号
	 * @return
	 */
	public static String createShippingSn() {
		String nextSeq = numberGenerator.generateNumber(SHOP_SHIPPING_SN).toString();
		return new StringBuilder(DateUtils.getTodayStr("yyyyMMdd")).append(StringUtils.leftPad(nextSeq, 8, "0")).toString();
	}
	
	/**
	 * 创建全局的订单号
	 * @return
	 */
	public static String createReturnsSn() {
		String nextSeq = numberGenerator.generateNumber(SHOP_RETUENS_SN).toString();
		return new StringBuilder(DateUtils.getTodayStr("yyyyMMdd")).append(StringUtils.leftPad(nextSeq, 8, "0")).toString();
	}
	
	/**
	 * 创建全局的订单号
	 * @return
	 */
	public static String createRefundsSn() {
		String nextSeq = numberGenerator.generateNumber(SHOP_REFUNDS_SN).toString();
		return new StringBuilder(DateUtils.getTodayStr("yyyyMMdd")).append(StringUtils.leftPad(nextSeq, 8, "0")).toString();
	}
}