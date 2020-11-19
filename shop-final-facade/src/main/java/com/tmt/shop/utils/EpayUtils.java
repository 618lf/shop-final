package com.tmt.shop.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.incrementer.UUIdGenerator;
import com.tmt.common.utils.BigDecimalUtil;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Epayment;
import com.tmt.shop.entity.Payable;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Refunds;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.exception.EpaymentErrorException;
import com.tmt.shop.service.EpayServiceFacade;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.base.WechatPayConfig;
import com.tmt.wechat.bean.pay.MchOrderquery;
import com.tmt.wechat.bean.pay.MchPayment;
import com.tmt.wechat.bean.pay.Refundorder;
import com.tmt.wechat.bean.pay.Refundquery;
import com.tmt.wechat.bean.pay.Unifiedorder;
import com.tmt.wechat.service.WechatPayService;
import com.tmt.wechat.utils.SignUtils;

/**
 * 支付相关
 * @author root
 *
 */
public class EpayUtils {

	private static EpayServiceFacade epayService = SpringContextHolder.getBean(EpayServiceFacade.class);
	
	/**
	 * 所有的支付方式
	 * @return
	 */
	public static List<Epay> getEpays() {
		List<Epay> epays = CacheUtils.get(ShopConstant.EPAY_CACHE);
		if (epays == null) {
			epays = epayService.queryUseableEpays();
			if (epays != null && epays.size() != 0) {
			    CacheUtils.put(ShopConstant.EPAY_CACHE, epays);
			}
		}
		return epays;
	}
	
	/**
	 * 指定
	 * @param id
	 * @return
	 */
	public static Epay get(Long id) {
	   String key = new StringBuilder(ShopConstant.EPAY_CACHE).append(id).toString();
	   Epay epay = CacheUtils.get(key);
	   if(epay == null) {
		  epay = epayService.get(id);
		  if(epay != null) {
			 CacheUtils.put(key, epay);
		  }
	   }
	   return epay;
	}
	
	/**
	 * 删除缓存
	 */
	public static void clearCache() {
		CacheUtils.evict(ShopConstant.EPAY_CACHE);
		CacheUtils.deletePattern(new StringBuilder(ShopConstant.EPAY_CACHE).append("*").toString());
	}
	
	/**
	 * 发起企业付款
	 * @param epayment
	 */
	public static Epayment pay(Epayment epayment) {
		Epay epay = get(epayment.getEpayId());
		
		// 发起微信付款（或者是不是可以直接设置取消订单）
		if (epay != null && epay.getType() == 1) {
			WechatPayService payService = SpringContextHolder.getBean(WechatPayService.class);
			// 支付参数
			MchPayment payment = new MchPayment();
			payment.setMch_appid(epay.getPartnerAccount());
			payment.setMchid(epay.getPartnerId());
			payment.setNonce_str(UUIdGenerator.uuid());
			payment.setPartner_trade_no(epayment.getPaySn());
			payment.setOpenid(epayment.getOpenid());
			payment.setAmount((int)(BigDecimalUtil.mul(epayment.getAmount(), BigDecimalUtil.valueOf(100)).doubleValue()));
			payment.setDesc(epayment.getRemarks());
			payment.setSpbill_create_ip(Globals.getSystemIP());
			payment.setCheck_name("NO_CHECK");
			Map<String, String> result = payService.pay(payment);
			if (result != null && WechatConstants.SUCCESS.equals(result.get("return_code"))
					&& WechatConstants.SUCCESS.equals(result.get("payment_no"))) {
				epayment.setPaymentNo(result.get("payment_no"));
				epayment.setPaymentTime(result.get("payment_time"));
				epayment.setPayResult((byte)1);
				epayment.setPayResultMsg("支付成功");
				return epayment;
			}
			if (result != null) {
				throw new EpaymentErrorException(StringUtil3.format("【%s】%s", result.get("return_msg"), result.get("err_code_des")));
			}
		}
		throw new EpaymentErrorException("不支持的付款方式");
	}
	
	/**
	 * 微信JS 支付
	 * @param epay
	 * @param pay
	 * @param notifyUrl
	 * @return
	 */
	public static AjaxResult generateWxPayJsRequestJson(Epay epay, String ip, String openId, Payable pay, String notifyUrl) {
		
		// 支付配置
		WechatPayConfig config = new WechatPayConfig();
		config.setAppId(epay.getPartnerAccount());
		config.setMchId(epay.getPartnerId());
		config.setMchKey(epay.getPartnerKey());
		config.setNotifyUrl(notifyUrl);
		config.setKeyPath(ShopConstant.PAY_CERT_KEYPATH);
		
		// 统一订单
		Unifiedorder pp = new Unifiedorder();
		pp.setBody(pay.getPayBody());
		pp.setOut_trade_no(pay.getJsSn());
		pp.setTotal_fee(String.valueOf(pay.getPayPrice()));
		pp.setSpbill_create_ip(ip);
		pp.setOpenid(openId);
		pp.setTrade_type("JSAPI");
		WechatPayService payService = SpringContextHolder.getBean(WechatPayService.class);
		Map<String, String> result = payService.bind(config).unifiedorder(pp);
		if (result != null && WechatConstants.SUCCESS.equals(result.get("return_code"))) {
			return AjaxResult.success(SignUtils.generateMchPayJsRequestJson(pp.getNonce_str(), result.get("prepay_id"), config.getMchId(), config.getMchKey()));
		}
		return AjaxResult.error("统一下单错误，请重新下单");
	}
	
	/**
	 * 微信NA 支付
	 * @param epay
	 * @param pay
	 * @param notifyUrl
	 * @return
	 */
	public static AjaxResult generateWxPayNaRequestJson(Epay epay, Payable pay, String notifyUrl) {
		
		// 支付配置
		WechatPayConfig config = new WechatPayConfig();
		config.setAppId(epay.getPartnerAccount());
		config.setMchId(epay.getPartnerId());
		config.setMchKey(epay.getPartnerKey());
		config.setNotifyUrl(notifyUrl);
		config.setKeyPath(ShopConstant.PAY_CERT_KEYPATH);
		
		// 统一订单
		Unifiedorder pp = new Unifiedorder();
		pp.setBody(pay.getPayBody());
		pp.setOut_trade_no(pay.getNaSn());
		pp.setTotal_fee(String.valueOf(pay.getPayPrice()));
		pp.setProduct_id(pay.getNaSn());
		pp.setSpbill_create_ip(Globals.getSystemIP());
		pp.setTrade_type("NATIVE");
		WechatPayService payService = SpringContextHolder.getBean(WechatPayService.class);
		Map<String, String> result = payService.bind(config).unifiedorder(pp);
		if (result != null && WechatConstants.SUCCESS.equals(result.get("return_code"))) {
		    return AjaxResult.success(result.get("code_url"));
		}
		return AjaxResult.error("统一下单错误，请重新下单");
	}
	
	/**
	 * 校验订单
	 * @param order
	 * @param payment
	 * @return
	 */
	public static Payment wxCheckPayment(Epay epay, Payable pay) {
		
		// 支付配置
		WechatPayConfig config = new WechatPayConfig();
		config.setAppId(epay.getPartnerAccount());
		config.setMchId(epay.getPartnerId());
		config.setMchKey(epay.getPartnerKey());
		
		// 查询订单
		MchOrderquery mchOrderquery = new MchOrderquery();
		mchOrderquery.setOut_trade_no(pay.getJsSn());
		
		WechatPayService payService = SpringContextHolder.getBean(WechatPayService.class);
		Map<String, String> result = payService.bind(config).queryOrder(mchOrderquery);
		if (!(result != null && WechatConstants.SUCCESS.equals(result.get("return_code"))
				&& WechatConstants.SUCCESS.equals(result.get("trade_state")))) {
			// 查询订单
			mchOrderquery.setOut_trade_no(pay.getNaSn());
			result = payService.bind(config).queryOrder(mchOrderquery);
		} 
		
		// 判断是否支付成功
		if (result != null && WechatConstants.SUCCESS.equals(result.get("return_code"))
				&& WechatConstants.SUCCESS.equals(result.get("trade_state"))) {
			Payment payment = new Payment();
			payment.setTransactionId(result.get("transaction_id"));
			payment.setAmount(BigDecimalUtil.div(BigDecimalUtil.valueOf(result.get("total_fee")), BigDecimal.valueOf(100)));
			payment.setPayer(result.get("openid"));
			payment.setPaymentDate(DateUtil3.getFormatDate(result.get("time_end"),"yyyyMMddHHmmss"));
			return payment;
		}
		
		// 尝试
		return null;
	}
	
	/**
	 * 发起退款申请，申请成功之后
	 * 不支持多次退款（一个订单一次性退款）
	 * @param payment
	 */
	public static AjaxResult refundsPayment(Refunds refunds) {
		Epay epay = get(refunds.getEpayId());
		
		WechatPayConfig config = new WechatPayConfig();
		config.setAppId(epay.getPartnerAccount());
		config.setMchId(epay.getPartnerId());
		config.setMchKey(epay.getPartnerKey());
		config.setKeyPath(ShopConstant.PAY_CERT_KEYPATH);
		
		Refundorder refund = new Refundorder();
		refund.setTransaction_id(refunds.getTransactionId());
		refund.setTotal_fee(String.valueOf(refunds.getPayPrice()));
		refund.setRefund_fee(String.valueOf(refunds.getPayPrice()));
		refund.setOut_refund_no(refunds.getSn());
		
		WechatPayService payService = SpringContextHolder.getBean(WechatPayService.class);
		Map<String, String> result = payService.bind(config).refundOrder(refund);
		if (result != null && WechatConstants.SUCCESS.equals(result.get("result_code"))) {
			return AjaxResult.success();
		}
		return AjaxResult.error(result.get("err_code_des"));
	}
	
	/**
	 * 发起退款申请，申请成功之后
	 * 不支持多次退款（一个订单一次性退款）
	 * @param payment
	 */
	public static AjaxResult refundsQuery(Refunds refunds) {
		Epay epay = get(refunds.getEpayId());
		
		WechatPayConfig config = new WechatPayConfig();
		config.setAppId(epay.getPartnerAccount());
		config.setMchId(epay.getPartnerId());
		config.setMchKey(epay.getPartnerKey());
		config.setKeyPath(ShopConstant.PAY_CERT_KEYPATH);
		
		Refundquery query = new Refundquery();
		query.setTransaction_id(refunds.getTransactionId());
		query.setOut_refund_no(refunds.getSn());
		
		WechatPayService payService = SpringContextHolder.getBean(WechatPayService.class);
		Map<String, String> result = payService.bind(config).refundQuery(query);
		if (result != null && WechatConstants.SUCCESS.equals(result.get("result_code"))) {
			return AjaxResult.success(result.get("refund_recv_accout_0"));
		}
		return AjaxResult.error(result.get("err_code_des"));
	}
}