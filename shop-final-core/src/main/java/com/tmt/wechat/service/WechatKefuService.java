package com.tmt.wechat.service;

import java.util.Date;

import com.tmt.wechat.bean.kefu.KefuOnlines;
import com.tmt.wechat.bean.kefu.KefuRecords;
import com.tmt.wechat.bean.kefu.KefuRequest;
import com.tmt.wechat.bean.kefu.KefuSession;
import com.tmt.wechat.bean.kefu.KefuSessionWaits;
import com.tmt.wechat.bean.kefu.KefuSessions;
import com.tmt.wechat.bean.kefu.Kefus;
import com.tmt.wechat.bean.kefu.message.Message;
import com.tmt.wechat.exception.WechatErrorException;

/**
 * 客服接口
 * 
 * @author lifeng
 *
 */
public interface WechatKefuService {

	/**
	 * 发送客服消息
	 * 
	 * @param message
	 * @return
	 * @throws WechatErrorException
	 */
	boolean sendKefuMessage(Message message) throws WechatErrorException;

	/**
	 * 获取客服基本信息
	 * 
	 * @return
	 * @throws WechatErrorException
	 */
	Kefus kefus() throws WechatErrorException;

	/**
	 * 获取在线客服基本信息
	 * 
	 * @return
	 * @throws WechatErrorException
	 */
	KefuOnlines kefuOnlines() throws WechatErrorException;

	/**
	 * 添加客服账号
	 * 
	 * @param request
	 * @return
	 * @throws WechatErrorException
	 */
	boolean kefuAdd(KefuRequest request) throws WechatErrorException;

	/**
	 * 设置客服信息（即更新客服信息）
	 * 
	 * @param request
	 * @return
	 * @throws WechatErrorException
	 */
	boolean kefuUpdate(KefuRequest request) throws WechatErrorException;

	/**
	 * 设置客服信息（即更新客服信息）
	 * 
	 * @param request
	 * @return
	 * @throws WechatErrorException
	 */
	boolean kefuInviteWorker(KefuRequest request) throws WechatErrorException;

	/**
	 * 上传客服图像
	 * 
	 * @param kfAccount
	 * @param imgFile
	 * @return
	 * @throws WechatErrorException
	 */
	boolean kfAccountUploadHeadImg(String kfAccount, String imgFile)
			throws WechatErrorException;

	/**
	 * 删除客服账号
	 * 
	 * @param kfAccount
	 * @return
	 * @throws WechatErrorException
	 */
	boolean kfAccountDel(String kfAccount) throws WechatErrorException;

	/**
	 * 创建会话 此接口在客服和用户之间创建一个会话，如果该客服和用户会话已存在，则直接返回0。指定的客服帐号必须已经绑定微信号且在线。
	 * 
	 * @param openid
	 * @param kfAccount
	 * @return
	 * @throws WechatErrorException
	 */
	boolean kfSessionCreate(String openid, String kfAccount)
			throws WechatErrorException;

	/**
	 * 关闭会话 开发者可以使用本接口，关闭一个会话。
	 * 
	 * @param openid
	 * @param kfAccount
	 * @return
	 * @throws WechatErrorException
	 */
	boolean kfSessionClose(String openid, String kfAccount)
			throws WechatErrorException;

	/**
	 * 获取客户的会话状态
	 * 
	 * @param openid
	 * @return
	 * @throws WechatErrorException
	 */
	KefuSession kfSessionGet(String openid) throws WechatErrorException;

	/**
	 * 获取客服的会话列表
	 * 
	 * @param kfAccount
	 * @return
	 * @throws WechatErrorException
	 */
	KefuSessions kfSessionList(String kfAccount) throws WechatErrorException;

	/**
	 * 获取未接入会话列表
	 * 
	 * @return
	 * @throws WechatErrorException
	 */
	KefuSessionWaits kfSessionGetWaitCase() throws WechatErrorException;

	/**
	 * 获取聊天记录（原始接口）
	 * 
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @param msgId
	 *            消息id顺序从小到大，从1开始
	 * @param number
	 *            每次获取条数，最多10000条
	 * @return 聊天记录对象
	 * @throws WechatErrorException
	 */
	KefuRecords kfRecords(Date startTime, Date endTime, Long msgId,
			Integer number) throws WechatErrorException;

	/**
	 * 获取聊天记录（优化接口，返回指定时间段内所有的聊天记录）
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws WechatErrorException
	 */
	KefuRecords kfMsgList(Date startTime, Date endTime)
			throws WechatErrorException;
}