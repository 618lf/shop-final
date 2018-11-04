package com.tmt.wechat;

import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.message.EventMsgUserAttention;
import com.tmt.wechat.bean.message.MenuEventMsgClick;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.ReqMsgImage;
import com.tmt.wechat.bean.message.ReqMsgText;
import com.tmt.wechat.bean.message.RespMsg;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.Menu;
import com.tmt.wechat.entity.MetaSetting;
import com.tmt.wechat.entity.Qrcode;
import com.tmt.wechat.handler.Handler;
import com.tmt.wechat.handler.impl.ImageHandler;
import com.tmt.wechat.handler.impl.KeywordHandler;
import com.tmt.wechat.handler.impl.NoneHandler;
import com.tmt.wechat.handler.impl.NoticeHandler;
import com.tmt.wechat.handler.impl.RichHandler;
import com.tmt.wechat.handler.impl.TextHandler;
import com.tmt.wechat.service.QrcodeServiceFacade;
import com.tmt.wechat.service.impl.WechatOptionServiceImpl;
import com.tmt.wechat.utils.MessageParse;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 基于 domain 的微信消息处理服务
 * @author lifeng
 */
public class DomainWechatOptionService extends WechatOptionServiceImpl {

	// 是否初始化
	private boolean inited = false;
	
	// 处理器入口
	private Handler messageHandler1 = null;
	private Handler messageHandler2 = null;
	
	// 二维码服务
	private QrcodeServiceFacade qrcodeServiceFacade = null;
	
	/**
	 * 初始化处理器
	 */
	protected synchronized void init() {
		if (!inited) {
			
			// 初始化处理流程
			initHandler();
			
			// 二维码服务
			qrcodeServiceFacade = SpringContextHolder.getBean(QrcodeServiceFacade.class);
			
			// 设置为已经初始化
			inited = true;
		}
	}
	
	/**
	 * 子类可以实现该怎么处理
	 */
	protected void initHandler() {
		messageHandler1 = new NoticeHandler();
		messageHandler2 = new NoticeHandler();
		NoneHandler noneHandler = new NoneHandler();
		TextHandler textHandler = new TextHandler();
		RichHandler richHandler = new RichHandler();
		ImageHandler imageHandler = new ImageHandler();
		KeywordHandler keywordHandler = new KeywordHandler();
		
		// 默认的处理线
		messageHandler1.setNextHandler(noneHandler);
		noneHandler.setNextHandler(textHandler);
		textHandler.setNextHandler(richHandler);
		richHandler.setNextHandler(imageHandler);
		imageHandler.setNextHandler(keywordHandler);
		keywordHandler.setNextHandler(textHandler);
		
		// 关键词包含两种处理方式(1、文本；2、图文)
		messageHandler2.setNextHandler(keywordHandler);
	}
	
	//==========================================================
	//                消息处理
	//==========================================================

	@Override
	public MsgHead onMessage(String req) {
		
		// 初始化校验
		this.init();
		
		// 消息
		MsgHead msg = MessageParse.parseXML(req);
		
		// 暂时不支持的消息
		if (msg == null) {
			return null;
		}
		
		// 关注事件，取消关注事件
		if (msg instanceof EventMsgUserAttention) {
			return onUserAttention(msg);
		}
		
		// 菜单点击事件
		if (msg instanceof MenuEventMsgClick) {
			return onClick((MenuEventMsgClick)msg);
		}
		
		// 发送关键字事件
		if (msg instanceof ReqMsgText) {
			return onText((ReqMsgText)msg);
		}
		
		// 图片处理
		if (msg instanceof ReqMsgImage) {
			return onImage((ReqMsgImage)msg);
		}
		
		// 其他事件暂不处理
		return null;
	}
	
	/**
	 * 执行关注事件
	 * @param msg
	 * @return
	 */
	protected MsgHead onUserAttention(MsgHead msg) {
		final EventMsgUserAttention _msg = (EventMsgUserAttention)msg;
		
		//关注
		if (Constants.EventType.SCAN.name().equals(msg.getEvent())
					|| Constants.EventType.subscribe.name().equals(msg.getEvent())) {
			return _onUserAttentionMsg(_msg);
		}
		
		//取消关注(暂时不用做什么)
		else if(Constants.EventType.unsubscribe.name().equals(msg.getEvent())) {
			
		}
		
		return null;
	}
	
	/**
	 * 执行关注事件
	 * 如果扫码关注 -- 会返回二维码对应的消息
	 * @param _msg
	 * @return
	 */
	protected MsgHead _onUserAttentionMsg(EventMsgUserAttention msg) {
		
		String qrscene = msg.getQrscene();
		
		// 二维码相关的事件(暂不处理) -- 找到二维码（和下面一样的处理方式）
		if (StringUtil3.isNotBlank(qrscene)) {
			return _onUserScanMsg(msg, qrscene);
		}
		
		// 默认的事件支持
		App app = WechatUtils.getEventApp(msg.getToUserName());
		if (app == null || app.getSetting() == null) {
			return null;
		}
		
		// 公众号事件配置
		MetaSetting setting = app.getSetting();
		Byte type = setting.getAttentionType();
		String config = setting.getAttentionConfig();
		return messageHandler1.doHandler(msg, app, type, config);
	}
	
	/**
	 * 执行关注事件
	 * 如果扫码关注 -- 会返回二维码对应的消息
	 * @param _msg
	 * @return
	 */
	protected RespMsg _onUserScanMsg(EventMsgUserAttention msg, String qrscene) {
		Qrcode qrcode = WechatUtils.getCacheQrcode(qrscene);
		if (qrcode == null) {
			qrcode = qrcodeServiceFacade.getBySceneStr(qrscene);
			WechatUtils.cachedQrcode(qrscene, qrcode);
		}
		if (qrcode == null) {
			return null;
		}
		
		App app = WechatUtils.getEventApp(msg.getToUserName());
		
		// 二维码配置
		Byte type = qrcode.getType();
		String config = qrcode.getConfig();
		return messageHandler1.doHandler(msg, app, type, config);
	}
	
	/**
	 * 菜单点击事件，根据不同的菜单ID推送不同的数据
	 * @param msg
	 * @return
	 */
	protected RespMsg onClick(MenuEventMsgClick msg) {
		String menuKey = msg.getEventKey();
		App app = WechatUtils.getEventApp(msg.getToUserName());
		Menu menu = app.getMenu(menuKey);
		
		if (menu == null) {
			return null;
		}
		
		// 菜单配置
		Byte type = menu.getType();
		String config = menu.getConfig();
		return messageHandler1.doHandler(msg, app, type, config);
	}
	
	/**
	 * 关键词的处理方式
	 * 怎么找到那个关键词（按照关键词做一个词库记录在Lucene中）
	 * @param msg
	 * @return
	 */
	protected RespMsg onText(ReqMsgText msg) {
		String command = msg.getShowMessage();
		
		// 关键词
		if (StringUtil3.isBlank(command)) {
			return null;
		}
		
		// 传入匹配关键词的类型
		App app = WechatUtils.getEventApp(msg.getToUserName());
		RespMsg respMsg = messageHandler2.doHandler(msg, app, WechatConstants.HANDLER_keyword, command);
		
		if (respMsg != null) {
			return respMsg;
		}
		
		// 默认的事件支持
		MetaSetting setting = app.getSetting();
		Byte type = setting.getDefaultType();
		String config = setting.getDefaultConfig();
		return messageHandler1.doHandler(msg, app, type, config);
	}
	
	/**
	 * 图片的处理方式
	 * @param image
	 * @return
	 */
	protected RespMsg onImage(ReqMsgImage image) {
		return null;
	}
}