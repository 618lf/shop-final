package com.tmt.shop.entity;

import com.tmt.core.config.Globals;

/**
 * 系统常量
 * 
 * @author root
 *
 */
public class ShopConstant {

	// 店铺
	public static final String STORE_CACHE = "STORE#";

	// 支付插件字典项
	public static String EPAY_CACHE = "SHOP#EPAYS#";
	public static String SHOP_PAYMENT_METHODS = "SHOP#PMS#";
	public static String SHOP_SHIPPING_METHODS = "SHOP#SMS#";
	public static String DELIVERY_CENTER_KEY = "SHOP#DE_C#";// 送货中心
	public static String SHOP_RANKS = "SHOP#RANKS#";

	// 回调的域名应该是全局的域名
	public static String WX_PAY_NOTIFY_GLOBAL = new StringBuilder(Globals.domain).append(Globals.frontPath)
			.append("/payment/pay-notify/wx/").toString();

	// 二维码支付的存储目录
	public static String PAY_QRCODE_STORAGE_PATH = "/pay_qrcode/${data}/${datatime}${rand}.jpg";

	// 支付的证书路径
	public static String PAY_CERT_KEYPATH = "/home/cert/apiclient_cert.p12";

	// 支付前缀
	public static String JS_PAY_PREFIX = "JS-";
	public static String NA_PAY_PREFIX = "NA-";

	// 收货期限(发货之后自动完成的期限)
	public static int RECEIPT_DAYS = 7;

	// 临时缓存
	public static String CART_QUANTITY = "cartQ"; // 购物车数量
	public static String CART_KEY = "cartKey";
	public static String ORDER_KEY = "orderKey";
	public static String XR_KEY = "xrKey";
	public static String COUPON_KEY = "SHOP#CP#";
	public static String GLOBAL_PROMOTIONS = "SHOP#GPOS";
	public static String GOODS_PROMOTIONS = "SHOP#PPOS#";
	public static String GOODS_COMPLEXS = "SHOP#PCPXS#";
	public static String SHOP_COMPLEXS = "SHOP#CPXS#";
	public static Long MAX_SECONDS_PROMOTIONS = 1 * 24 * 60 * 60L; // 最长缓存1天
	public static Long MIN_SECONDS_PROMOTIONS = 10L; // 最小缓存的时间 10秒 针对过期的数据

	// 订单缓存
	public static String SHOP_USER_ORDER_CACHE = "SHOP#U_OR#"; // 用户临时订单
	public static String SHOP_PRE_ORDER_CACHE = "SHOP#PREO#"; // 预支付订单

	// 短信
	public static String SMS_CACHE_NAME = "SMS#";
	public static Byte SCENE_CODE = 1; // 验证码
	public static Byte SCENE_NOTICE = 2;// 订单通知
	public static int VERIFICATION_TIMETOIDLE = 600; // 10分钟能有效 -- 一个验证码10分钟内有限
	public static int VERIFICATION_TIMES = 3; // 一个号码10分钟之内只能发送三次验证码

	// 商品
	public static String PRODUCTS = "PRODUCTS#"; // 货品
	public static String GOODS = "GOODS#"; // 商品
}