package com.tmt.wechat.service;

import com.tmt.wechat.bean.massmsg.MassNews;
import com.tmt.wechat.bean.massmsg.MassOpenIdMessage;
import com.tmt.wechat.bean.massmsg.MassPreviewMessage;
import com.tmt.wechat.bean.massmsg.MassSendResult;
import com.tmt.wechat.bean.massmsg.MassTagMessage;
import com.tmt.wechat.bean.massmsg.MassUploadResult;
import com.tmt.wechat.bean.massmsg.MassVideo;
import com.tmt.wechat.exception.WechatErrorException;

/**
 * 群发服务
 * @author lifeng
 */
public interface WechatMassService {

	/**
	 * 上传群发用的图文消息，上传后才能群发图文消息
	 * 
	 * @param news
	 * @return
	 * @throws WechatErrorException
	 */
	MassUploadResult massNewsUpload(MassNews news) throws WechatErrorException;

	/**
	 * 上传群发用的视频，上传后才能群发视频消息
	 * 
	 * @param video
	 * @return
	 * @throws WechatErrorException
	 */
	MassUploadResult massVideoUpload(MassVideo video)
			throws WechatErrorException;

	/**
	 * 分组群发消息 如果发送图文消息 massNewsUpload 如果发送视频消息 massVideoUpload
	 * 
	 * @return
	 * @throws WechatErrorException
	 */
	MassSendResult massGroupMessageSend(MassTagMessage message)
			throws WechatErrorException;

	/**
	 * 按openId列表群发消息
	 * 
	 * @return
	 * @throws WechatErrorException
	 */
	MassSendResult massOpenIdsMessageSend(MassOpenIdMessage message)
			throws WechatErrorException;

	/**
	 * 群发消息预览接口
	 * 开发者可通过该接口发送消息给指定用户，在手机端查看消息的样式和排版。为了满足第三方平台开发者的需求，在保留对openID预览能力的同时
	 * 增加了对指定微信号发送预览的能力，但该能力每日调用次数有限制（100次），请勿滥用。
	 * 
	 * @param message
	 * @return
	 * @throws WechatErrorException
	 */
	MassSendResult massMessagePreview(MassPreviewMessage message)
			throws WechatErrorException;
}
