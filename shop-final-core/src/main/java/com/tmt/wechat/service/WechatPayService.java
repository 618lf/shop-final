package com.tmt.wechat.service;

import java.util.Map;

import com.tmt.wechat.bean.base.WechatPayConfig;
import com.tmt.wechat.bean.pay.MchOrderquery;
import com.tmt.wechat.bean.pay.MchPayment;
import com.tmt.wechat.bean.pay.Refundorder;
import com.tmt.wechat.bean.pay.Refundquery;
import com.tmt.wechat.bean.pay.Unifiedorder;

/**
 * 支付相关
 * @author lifeng
 */
public interface WechatPayService {
	
	/**
	 * 执行栈中绑定此配置
	 * @param config
	 * @return
	 */
	WechatPayService bind(WechatPayConfig config);
	
	/**
	 * 执行栈中绑定此配置
	 * @param config
	 * @return
	 */
	void unbind();
	
	/**
     * 作用：统一下单<br>
     * 场景：公共号支付、扫码支付、APP支付
     * @param reqData 向wxpay post的请求数据
     * @return API返回数据
     * @throws Exception
     */
	Map<String, String> unifiedorder(Unifiedorder unifiedorder);
	
	
	/**
	 * 支付回调的结果
	 * @param xml
	 * @return
	 */
	Map<String, String> payNotifyResult(String xml);
	
	
	/**
	 * 查询订单
	 * @param mchOrderquery
	 * @return
	 */
	Map<String, String> queryOrder(MchOrderquery mchOrderquery);
	
	
	/**
	 * 退款订单
	 * @param mchOrderquery
	 * @return
	 */
	Map<String, String> refundOrder(Refundorder refund);
	
	/**
	 * 退款订单
	 * @param mchOrderquery
	 * @return
	 */
	Map<String, String> refundQuery(Refundquery query);

	/**
	 * 企业付款
	 * @return
	 */
	Map<String, String> pay(MchPayment payment);
}