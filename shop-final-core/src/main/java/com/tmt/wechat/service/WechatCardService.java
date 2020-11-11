package com.tmt.wechat.service;

import com.tmt.wechat.bean.card.CardResult;
import com.tmt.wechat.bean.signature.CardSignature;
import com.tmt.wechat.exception.WechatErrorException;

/**
 * 卡券相关接口
 * 
 * @author lifeng
 */
public interface WechatCardService {

	/**
	 * 获得卡券api_ticket，不强制刷新卡券api_ticket
	 * 
	 * @return
	 * @throws WxErrorException
	 */
	String getCardApiTicket() throws WechatErrorException;

	/**
	 * 获得卡券api_ticket 获得时会检查卡券apiToken是否过期，如果过期了，那么就刷新一下，否则就什么都不干
	 * 
	 * @param forceRefresh
	 * @return
	 * @throws WechatErrorException
	 */
	String getCardApiTicket(boolean forceRefresh) throws WechatErrorException;

	/**
	 * 创建调用卡券api时所需要的签名
	 * 
	 * @param params
	 * @return
	 * @throws WechatErrorException
	 */
	CardSignature createCardSignature(String... params)
			throws WechatErrorException;

	/**
	 * 卡券Code解码
	 * 
	 * @param encryptCode
	 * @return
	 * @throws WechatErrorException
	 */
	String decryptCardCode(String encryptCode) throws WechatErrorException;

	/**
	 * 卡券Code查询
	 * 
	 * @param cardId
	 * @param code
	 * @param checkConsume
	 * @return
	 * @throws WechatErrorException
	 */
	CardResult queryCardCode(String cardId, String code, boolean checkConsume)
			throws WechatErrorException;

	/**
	 * 卡券Code核销。核销失败会抛出异常
	 * 
	 * @param code
	 * @return
	 * @throws WechatErrorException
	 */
	String consumeCardCode(String code) throws WechatErrorException;

	/**
	 * 卡券Code核销。核销失败会抛出异常
	 * 
	 * @param code
	 * @param cardId
	 * @return
	 * @throws WxErrorException
	 */
	String consumeCardCode(String code, String cardId)
			throws WechatErrorException;

	/**
	 * 卡券Mark接口。
	 * 
	 * @param code
	 * @param cardId
	 * @param openId
	 * @param isMark
	 * @throws WechatErrorException
	 */
	void markCardCode(String code, String cardId, String openId, boolean isMark)
			throws WechatErrorException;

	/**
	 * 查看卡券详情接口
	 * 
	 * @param cardId
	 * @return
	 * @throws WechatErrorException
	 */
	String getCardDetail(String cardId) throws WechatErrorException;
}
