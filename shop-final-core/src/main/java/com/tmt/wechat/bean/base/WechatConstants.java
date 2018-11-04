package com.tmt.wechat.bean.base;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;

/**
 * 微信 相关的常量
 * @author lifeng
 */
public class WechatConstants {

	// 基本
	public static final String BASE_URI = "https://api.weixin.qq.com";
	public static final String MEDIA_URI = "http://file.api.weixin.qq.com";
	public static final String QRCODE_DOWNLOAD_URI = "https://mp.weixin.qq.com";
	public static final Header jsonHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,ContentType.APPLICATION_JSON.toString());
	public static final Header xmlHeader = new BasicHeader(HttpHeaders.CONTENT_TYPE,ContentType.APPLICATION_XML.toString());
	
	// 支付
	public static final String MCH_URI_DOMAIN_API = "api.mch.weixin.qq.com";
    public static final String MCH_URI_DOMAIN_API2 = "api2.mch.weixin.qq.com";
    public static final String FAIL     = "FAIL";
    public static final String SUCCESS  = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";
    
    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String MICROPAY_URL_SUFFIX     = "/pay/micropay";
    public static final String UNIFIEDORDER_URL_SUFFIX = "/pay/unifiedorder";
    public static final String ORDERQUERY_URL_SUFFIX   = "/pay/orderquery";
    public static final String REVERSE_URL_SUFFIX      = "/secapi/pay/reverse";
    public static final String CLOSEORDER_URL_SUFFIX   = "/pay/closeorder";
    public static final String REFUND_URL_SUFFIX       = "/secapi/pay/refund";
    public static final String REFUNDQUERY_URL_SUFFIX  = "/pay/refundquery";
    public static final String DOWNLOADBILL_URL_SUFFIX = "/pay/downloadbill";
    public static final String REPORT_URL_SUFFIX       = "/payitil/report";
    public static final String SHORTURL_URL_SUFFIX     = "/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL_SUFFIX = "/tools/authcodetoopenid";
    
    // sandbox
    public static final String SANDBOX_GET_SIGNKEY_SUFFIX     = "/sandboxnew/pay/getsignkey";
    public static final String SANDBOX_MICROPAY_URL_SUFFIX     = "/sandboxnew/pay/micropay";
    public static final String SANDBOX_UNIFIEDORDER_URL_SUFFIX = "/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL_SUFFIX   = "/sandboxnew/pay/orderquery";
    public static final String SANDBOX_REVERSE_URL_SUFFIX      = "/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL_SUFFIX   = "/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL_SUFFIX       = "/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL_SUFFIX  = "/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL_SUFFIX = "/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL_SUFFIX       = "/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL_SUFFIX     = "/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL_SUFFIX = "/sandboxnew/tools/authcodetoopenid";
    
    // 微信回复处理消息类型
 	public static Byte HANDLER_none = 0; // 什么都不做
 	public static Byte HANDLER_text = 1; // 文本回复
 	public static Byte HANDLER_rich = 2; // 图文回复
 	public static Byte HANDLER_voice = 3; // 语音回复
 	public static Byte HANDLER_pic = 4; // 图片回复
 	public static Byte HANDLER_view = 5; // 跳转网页
 	public static Byte HANDLER_site_view = 6; // 业务模块
 	public static Byte HANDLER_keyword = 7; // 匹配关键字
 	
 	
 	// 缓存
 	public static String APP_CACHE = "APP#"; // 公众号
 	public static String META_CACHE = "META#"; // 素材
 	public static String QRCODE_CACHE = "QRCODE#"; // 二维码
 	public static String LOCK_FOR_ACCESS_TOKEN = "LOCK_FOR_ACCESS_TOKEN";
 	public static String PUB_ACCOUNT_ACCESS_TOKEN = "WECHAT#PUB_AAAT_CODE#";
 	public static String PUB_ACCOUNT_JS_TICKET = "WECHAT#PUB_AJT_CODE#";
}