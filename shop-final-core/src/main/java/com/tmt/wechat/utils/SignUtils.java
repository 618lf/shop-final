package com.tmt.wechat.utils;

import java.security.MessageDigest;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.StringUtils;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.pay.PayJsRequest;

/**
 * 签名工具类
 * 
 * @author lifeng
 */
public class SignUtils {

	/**
	 * 生成签名
	 * 
	 * @param map
	 * @param paternerKey
	 * @return
	 * @throws Exception
	 */
	public static String generateSign(Object object, String signType, String paternerKey) throws Exception {
		Map<String, String> tmap = MapUtil.objectToMap(object);
		if (tmap.containsKey(WechatConstants.FIELD_SIGN)) {
			tmap.remove(WechatConstants.FIELD_SIGN);
		}
		String str = MapUtil.mapJoin(tmap, false, false);
		str = new StringBuilder(str).append("&key=").append(paternerKey).toString();
		if (StringUtils.isNotBlank(signType) && WechatConstants.MD5.equals(signType)) {
			return MD5(str).toUpperCase();
		} else if (StringUtils.isNotBlank(signType) && WechatConstants.HMACSHA256.equals(signType)) {
			return HMACSHA256(str, paternerKey);
		}
		return DigestUtils.md5Hex(str).toUpperCase();
	}

	/**
	 * 生成签名
	 * 
	 * @param map
	 * @param paternerKey
	 * @return
	 * @throws Exception
	 */
	public static String generateSign(Map<String, String> object, String signType, String paternerKey)
			throws Exception {
		Map<String, String> tmap = MapUtil.order(object);
		if (tmap.containsKey(WechatConstants.FIELD_SIGN)) {
			tmap.remove(WechatConstants.FIELD_SIGN);
		}
		String str = MapUtil.mapJoin(tmap, false, false);
		str = new StringBuilder(str).append("&key=").append(paternerKey).toString();
		if (StringUtils.isNotBlank(signType) && WechatConstants.MD5.equals(signType)) {
			return MD5(str).toUpperCase();
		} else if (StringUtils.isNotBlank(signType) && WechatConstants.HMACSHA256.equals(signType)) {
			return HMACSHA256(str, paternerKey);
		}
		return DigestUtils.md5Hex(str).toUpperCase();
	}

	/**
	 * 生成 MD5
	 *
	 * @param data 待处理数据
	 * @return MD5结果
	 */
	public static String MD5(String data) throws Exception {
		java.security.MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] array = md.digest(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 生成 HMACSHA256
	 * 
	 * @param data 待处理数据
	 * @param key  密钥
	 * @return 加密结果
	 * @throws Exception
	 */
	public static String HMACSHA256(String data, String key) throws Exception {
		Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
		SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
		sha256_HMAC.init(secret_key);
		byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
		StringBuilder sb = new StringBuilder();
		for (byte item : array) {
			sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * 校验签名
	 * 
	 * @param xmlBean
	 * @param signKey
	 * @return
	 * @throws Exception
	 */
	public static boolean checkSign(Object xmlBean, String signType, String paternerKey) throws Exception {
		Map<String, String> params = MapUtil.objectToMap(xmlBean);
		String sign = params.get(WechatConstants.FIELD_SIGN);
		return generateSign(params, signType, paternerKey).equals(sign);
	}

	/**
	 * (MCH)生成支付JS请求对象
	 * 
	 * @param prepay_id 预支付订单号
	 * @param appId
	 * @param key       商户支付密钥
	 * @return
	 * @throws Exception
	 */
	public static String generateMchPayJsRequestJson(String nonceStr, String prepay_id, String appId,
			String paternerKey) {
		String package_ = "prepay_id=" + prepay_id;
		PayJsRequest payJsRequest = new PayJsRequest();
		payJsRequest.setAppId(appId);
		payJsRequest.setNonceStr(nonceStr);
		payJsRequest.setPackage_(package_);
		payJsRequest.setSignType(WechatConstants.HMACSHA256);
		payJsRequest.setTimeStamp(System.currentTimeMillis() / 1000 + "");
		Map<String, String> mapS = MapUtil.objectToMap(payJsRequest);
		try {
			payJsRequest.setPaySign(SignUtils.generateSign(mapS, WechatConstants.HMACSHA256, paternerKey));
		} catch (Exception e) {
		}
		return JsonMapper.toJson(payJsRequest);
	}
}