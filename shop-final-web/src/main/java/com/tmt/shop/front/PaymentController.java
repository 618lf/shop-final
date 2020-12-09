package com.tmt.shop.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.qrcode.QrcodeGen;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.PaymentServiceFacade;
import com.tmt.shop.utils.EpayUtils;
import com.tmt.system.utils.UserUtils;

/**
 * 基本的支付逻辑
 * @author lifeng
 *
 */
public abstract class PaymentController {

	@Autowired
	protected PaymentServiceFacade paymentService;
	
	/**
	 * 确定付款 -- 支持异步支付
	 * @param id
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/payment/plugin_ajax_submit.json", method = RequestMethod.POST)
	public PayResult paymentAjaxSubmit(Long id) {
		
		// PC 版的不支持公众号支付
		if (WebUtils.isWeixinPc(null)) {
		    return PayResult.fail("微信PC客户端暂不支持JS支付!");
		}
		
		// 发起支付
		Payment payment = paymentService.get(id);
		if (payment == null) {
			return PayResult.fail("数据错误");
		} else if(payment.getPayFlag() == Payment.YES) {
			return PayResult.ok(true);
		}
		
		// 支付插件
		Epay epay = EpayUtils.get(payment.getEpayId());
		String openId = UserUtils.getUserWechatOpenId(epay.getPartnerAccount());
		String notifyUrl = new StringBuilder(ShopConstant.WX_PAY_NOTIFY_GLOBAL).append(payment.getId()).toString();
		AjaxResult _result = EpayUtils.generateWxPayJsRequestJson(epay, WebUtils.getRemoteAddr(null), openId, payment, notifyUrl);
		if (_result.getSuccess()) {
			return PayResult.needPay(_result.getObj());
		}
	    return PayResult.fail(_result.getMsg());
	}
	
	/**
	 * 二维码支付 - 支持异步支付
	 * @param id
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/payment/plugin_ajax_submit/native.json", method = RequestMethod.POST)
	public PayResult paymentAjaxSubmit_native(Long id) {
		
		// 发起支付
		Payment payment = paymentService.get(id);
		if (payment == null) {
			return PayResult.fail("数据错误");
		} else if(payment.getPayFlag() == Payment.YES) {
			return PayResult.ok(true);
		}
		
		// 支付插件
		Epay epay = EpayUtils.get(payment.getEpayId());
		String notifyUrl = new StringBuilder(ShopConstant.WX_PAY_NOTIFY_GLOBAL).append(payment.getId()).toString();
		AjaxResult _result = EpayUtils.generateWxPayNaRequestJson(epay, payment, notifyUrl);
		if (_result.getSuccess()) {
			String remarks = StringUtils.format("收款方：%s, 金额：￥%s元", epay.getPartnerName(), payment.getAmount().toPlainString());
			String image = QrcodeGen.of(_result.getObj()).asString();
			PayResult result = PayResult.needPay(new StringBuilder("data:image/png;base64,").append(image).toString());
			result.setRemarks(remarks);
			return result;
		}
	    return PayResult.fail(_result.getMsg());
	}
	
	/**
	 * 支付之后主动查询订单
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/payment/touch.json", method = RequestMethod.POST)
	public PayResult paymentTouch(Long id) {
		
		if(id == null) {return PayResult.notPay();}
		
		Payment payment = paymentService.get(id);
		if (payment == null) {
			return PayResult.fail("数据错误");
		} else if(payment.getPayFlag() == Payment.YES) {
			return PayResult.ok(true);
		}
		
		// 支付插件
		Epay epay = EpayUtils.get(payment.getEpayId());
		
		// 支付结果
		Payment _payment = EpayUtils.wxCheckPayment(epay, payment);
		if (_payment != null) {
		    this.paymentService.confirmPay(_payment);
		    return PayResult.ok(null);
	    }
		
		// 没识别到则提示，支付失败
		return PayResult.notPay();
	}
}
