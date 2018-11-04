package com.tmt.wechat.service;

import com.tmt.wechat.bean.qrcode.QrCodeTicket;

/**
 * 二维码服务
 * @author lifeng
 *
 */
public interface WechatQrCodeService {

	/**
	 * 创建永久二维码
	 * @param scene_str
	 * @return
	 */
	QrCodeTicket qrcodeCreateFinal(String scene_str);
	
	/**
	 * 显示二维码图片
	 * @param ticket
	 * @return
	 */
	String qrcodeShowUrl(String ticket);
}
