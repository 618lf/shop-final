package com.tmt.shop.front;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.core.config.Globals;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.JaxbMapper;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.service.PaymentServiceFacade;
import com.tmt.shop.utils.EpayUtils;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.base.WechatPayConfig;
import com.tmt.wechat.bean.pay.PayNotifyReply;
import com.tmt.wechat.service.WechatPayService;

/**
 * 支付回调
 * 待改进的项目
 * 需要改进的点： 支付相关的统一提取出来作为单独的模块来使用
 * 统一支付，和统一支付回调
 * 
 * 现在将 Payment 和 RankPayment混在一起使用
 * 建议将这两个部分合起来统一使用
 * 
 * 加一个统一支付页面(解决微信只能呢个允许三个支付地址的问题)，现在还只需要用两个
 * @author root
 */
@Controller("paymentNotifyController")
@RequestMapping(value = "${frontPath}/shop/payment/pay-notify")
public class PaymentNotifyController {
	
	protected static Logger logger = LoggerFactory.getLogger(PaymentNotifyController.class);
	
	@Autowired
	private TaskExecutor taskExecutor;
	@Autowired
	private PaymentServiceFacade paymentService;
	@Autowired
	private WechatPayService wechatPayService;
	
	/**
	 * 微信支付回调
	 * @param id -- 支付主体的ID payment 中的ID
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("wx/{id}")
	public void wx_callback(@PathVariable Long id, HttpServletRequest request, HttpServletResponse response) { 
		try {
			// 支付记录
			Payment payment = paymentService.get(id);
			if (payment == null) {
				WebUtils.sendXml(response, JaxbMapper.toXml(PayNotifyReply.fail()));
				return;
			}
			
			// 读取xml数据
			String result = new String(IOUtils.toByteArray(request.getInputStream()), Globals.DEFAULT_ENCODING);
			
			// 支付方式
			Epay epay = EpayUtils.get(payment.getEpayId());
			
			// 支付参数
			WechatPayConfig config = new WechatPayConfig();
			config.setAppId(epay.getPartnerAccount());
			config.setMchId(epay.getPartnerId());
			config.setMchKey(epay.getPartnerKey());
			
			Map<String, String> _result = wechatPayService.bind(config).payNotifyResult(result);
			if (_result != null && WechatConstants.SUCCESS.equals(_result.get("return_code"))) {
				
				// 支付回调的参数
				payment.setPayFlag(Payment.YES);
				payment.setTransactionId(_result.get("transaction_id"));
	   			payment.setAmount(BigDecimalUtil.div(BigDecimalUtil.valueOf(_result.get("total_fee")), BigDecimal.valueOf(100)));
	   			payment.setPayer(_result.get("openid"));
	   			payment.setPaymentDate(DateUtils.getFormatDate(_result.get("time_end"),"yyyyMMddHHmmss"));
	   			taskExecutor.execute(new Callabled(payment));
			    WebUtils.sendXml(response, JaxbMapper.toXml(PayNotifyReply.success()));
			    return;
			}
			logger.error("【订单支付回调】微信支付回调错误，参数错误");
		} catch (Exception e) {
			logger.error("【订单支付回调】微信支付回调错误，处理错误", e);
		}
	    WebUtils.sendXml(response, JaxbMapper.toXml(PayNotifyReply.fail()));
	}
	
	//任务执行
	private class Callabled implements Runnable {
		Payment payment;
		Callabled(Payment payment) {
			this.payment = payment;
		}
		@Override
		public void run() {
			try{
				paymentService.confirmPay(payment);
			}catch(Exception e) {}
		}
	}
}