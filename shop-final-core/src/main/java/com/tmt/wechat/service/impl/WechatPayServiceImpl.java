package com.tmt.wechat.service.impl;

import java.util.Map;

import com.tmt.core.persistence.incrementer.UUIdGenerator;
import com.tmt.core.utils.JaxbMapper;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.base.WechatPayConfig;
import com.tmt.wechat.bean.pay.MchOrderquery;
import com.tmt.wechat.bean.pay.MchPayment;
import com.tmt.wechat.bean.pay.Refundorder;
import com.tmt.wechat.bean.pay.Refundquery;
import com.tmt.wechat.bean.pay.SandboxSignKey;
import com.tmt.wechat.bean.pay.Unifiedorder;
import com.tmt.wechat.service.WechatPayService;
import com.tmt.wechat.utils.MapUtil;
import com.tmt.wechat.utils.SignUtils;

/**
 * 基本的支付操作
 * @author lifeng
 */
public class WechatPayServiceImpl implements WechatPayService{

	// 线程栈
	private static final ThreadLocal<WechatPayConfig> RESOURCES = new InheritableThreadLocal<WechatPayConfig>();
		
	@Override
	public WechatPayService bind(WechatPayConfig config) {
		RESOURCES.set(config);
		return this;
	}

	@Override
	public void unbind() {
		RESOURCES.remove();
	}
	
	/**
	 * 获得沙箱测试密钥
	 * @return
	 * @throws Exception 
	 */
	private String getSandboxSignKey(String sign_type) throws Exception {
		WechatPayConfig config = RESOURCES.get();
		SandboxSignKey data = new SandboxSignKey();
		data.setMch_id(config.getMchId());
		data.setNonce_str(UUIdGenerator.uuid());
		data.setSign_type(sign_type);
		data.setSign(SignUtils.generateSign(data, data.getSign_type(), config.getMchKey()));
		
		// xml 请求参数
		String reqBody = JaxbMapper.toXml(data);
		
		// 发起请求
		String resp = config.doRequest(WechatConstants.MCH_URI_DOMAIN_API, WechatConstants.SANDBOX_GET_SIGNKEY_SUFFIX, reqBody, 
					config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), false);
		return MapUtil.xmlToMap(resp).get("sandbox_signkey");
	}

	/**
	 * 统一下单
	 */
	@Override
	public Map<String, String> unifiedorder(Unifiedorder unifiedorder) {
        try {
			
			WechatPayConfig config = RESOURCES.get();
			
			String url; 
			if (config.isUseSandbox()) {
				url = WechatConstants.SANDBOX_UNIFIEDORDER_URL_SUFFIX;
				unifiedorder.setSign_type(WechatConstants.MD5);
				config.setMchKey(this.getSandboxSignKey(unifiedorder.getSign_type()));
			} else {
				url = WechatConstants.UNIFIEDORDER_URL_SUFFIX;
				unifiedorder.setSign_type(WechatConstants.HMACSHA256);
			}
			
			// 签名
			unifiedorder.checkAndSign(config);
			
			// xml 请求参数
			String reqBody = JaxbMapper.toXml(unifiedorder);
			
			// 发起请求
			String resp = config.doRequest(WechatConstants.MCH_URI_DOMAIN_API, url, reqBody, 
					config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), false);
			
			// 校验请求
			return config.processResponse(resp, unifiedorder.getSign_type(), config.getMchKey());
		} catch (Exception e) {
			return null;
		} finally {
			this.unbind();
		}
	}

	/**
	 * 支付回调仅仅校验下
	 */
	@Override
	public Map<String, String> payNotifyResult(String xml) {
         try {
			
			WechatPayConfig config = RESOURCES.get();
			
			// 校验请求
			return config.processResponse(xml, WechatConstants.HMACSHA256, config.getMchKey());
		} catch (Exception e) {
			return null;
		} finally {
			this.unbind();
		}
	}

	/**
	 * 支付查询
	 */
	@Override
	public Map<String, String> queryOrder(MchOrderquery mchOrderquery) {
         try {
			
			WechatPayConfig config = RESOURCES.get();
			
			String url; 
			if (config.isUseSandbox()) {
				url = WechatConstants.SANDBOX_ORDERQUERY_URL_SUFFIX;
				mchOrderquery.setSign_type(WechatConstants.MD5);
				config.setMchKey(this.getSandboxSignKey(mchOrderquery.getSign_type()));
			} else {
				url = WechatConstants.ORDERQUERY_URL_SUFFIX;
				mchOrderquery.setSign_type(WechatConstants.HMACSHA256);
			}
			
			// 签名
			mchOrderquery.checkAndSign(config);
			
			// xml 请求参数
			String reqBody = JaxbMapper.toXml(mchOrderquery);
			
			// 发起请求
			String resp = config.doRequest(WechatConstants.MCH_URI_DOMAIN_API, url, reqBody, 
					config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), false);
			
			// 校验请求
			return config.processResponse(resp, mchOrderquery.getSign_type(), config.getMchKey());
		} catch (Exception e) {
			return null;
		} finally {
			this.unbind();
		}
	}
	
	/**
	 * 退款
	 */
	@Override
	public Map<String, String> refundOrder(Refundorder refund) {
        try {
			
			WechatPayConfig config = RESOURCES.get();
			
			String url; 
			if (config.isUseSandbox()) {
				url = WechatConstants.SANDBOX_REFUND_URL_SUFFIX;
				refund.setSign_type(WechatConstants.MD5);
				config.setMchKey(this.getSandboxSignKey(refund.getSign_type()));
			} else {
				url = WechatConstants.REFUND_URL_SUFFIX;
				refund.setSign_type(WechatConstants.HMACSHA256);
			}
			
			// 签名
			refund.checkAndSign(config);
			
			// xml 请求参数
			String reqBody = JaxbMapper.toXml(refund);
			
			// 发起请求
			String resp = config.doRequest(WechatConstants.MCH_URI_DOMAIN_API, url, reqBody, 
					config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), true);
			
			// 校验请求
			return config.processResponse(resp, refund.getSign_type(), config.getMchKey());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			this.unbind();
		}
	}

	/**
	 * 退款申请查询
	 */
	@Override
	public Map<String, String> refundQuery(Refundquery query) {
         try {
			
			WechatPayConfig config = RESOURCES.get();
			
			String url; 
			if (config.isUseSandbox()) {
				url = WechatConstants.SANDBOX_REFUNDQUERY_URL_SUFFIX;
				query.setSign_type(WechatConstants.MD5);
				config.setMchKey(this.getSandboxSignKey(query.getSign_type()));
			} else {
				url = WechatConstants.REFUNDQUERY_URL_SUFFIX;
				query.setSign_type(WechatConstants.HMACSHA256);
			}
			
			// 签名
			query.checkAndSign(config);
			
			// xml 请求参数
			String reqBody = JaxbMapper.toXml(query);
			
			// 发起请求
			String resp = config.doRequest(WechatConstants.MCH_URI_DOMAIN_API, url, reqBody, 
					config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs(), true);
			
			// 校验请求
			return config.processResponse(resp, query.getSign_type(), config.getMchKey());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			this.unbind();
		}
	}

	/**
	 * 企业付款
	 */
	@Override
	public Map<String, String> pay(MchPayment payment) {
		return null;
	}
}