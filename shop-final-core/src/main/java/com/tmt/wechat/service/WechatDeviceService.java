package com.tmt.wechat.service;

import com.tmt.wechat.bean.device.DeviceMessage;
import com.tmt.wechat.bean.device.TransMessageResult;
import com.tmt.wechat.exception.WechatErrorException;

/**
 * 设备相关
 * @author lifeng
 */
public interface WechatDeviceService {

	/**
	 * 主动发送消息给设备
	 * @param message
	 * @return
	 * @throws WechatErrorException
	 */
	TransMessageResult transMsg(DeviceMessage message) throws WechatErrorException;
}
