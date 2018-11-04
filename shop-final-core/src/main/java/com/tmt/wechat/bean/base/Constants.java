package com.tmt.wechat.bean.base;

/**
 * 请求、响应、事件 消息类型
 * 
 * @author lifeng
 */
public class Constants {

	/**
	 * 用户请求消息类型
	 */
	public static enum ReqType {
		text, //
		image, //
		link, //
		location, //
		video, //
		shortvideo, //
		voice, //
		event, //
		device_text, //
		device_event, //
		device_status, //
		hardware, //
		transfer_customer_service//
	}

	/**
	 * 事件消息类型包括，菜单上的的
	 */
	public static enum EventType {
		subscribe, // 关注
		unsubscribe, // 取消关注
		SCAN, // 扫码
		LOCATION, // 定位
		CLICK, // 菜单点击
		VIEW, // 菜单浏览
		scancode_waitmsg, //
		scancode_push, // 扫码推送
		location_select, //
		pic_weixin, //
		pic_photo_or_album, //
		pic_sysphoto, //
		MASSSENDJOBFINISH, // 高级群发接口
		TEMPLATESENDJOBFINISH, // 模板消息发送接口
		enter_agent, //
		card_pass_check, //
		card_not_pass_check, //
		user_get_card, //
		user_del_card, //
		user_consume_card, //
		user_pay_from_pay_cell, //
		user_view_card, //
		user_enter_session_from_card, //
		card_sku_remind, // 库存报警
		kf_create_session, // 客服接入会话
		kf_close_session, // 客服关闭会话
		kf_switch_session, // 客服转接会话
		poi_check_notify, // 门店审核事件推送
		submit_membercard_user_info // 接收会员信息事件推送
	}

	/**
	 * 应答消息类型定义。
	 * 
	 * @author cailx
	 * 
	 */
	public static enum RespType {
		image, // 图片消息
		music, // 音乐消息
		news, // 图文消息（点击跳转到外链）
		text, // 文本消息
		video, // 视频消息
		voice, // 语音消息
		mpnews, // 图文消息（点击跳转到图文消息页面）
		file, // 发送文件（CP专用）
		wxcard, // 卡券消息
		transfer_customer_service // 客服
	}

	/**
	 * 群发接口消息类型
	 * 
	 * @author lifeng
	 */
	public static enum MassType {
		mpnews, //
		text, //
		voice, //
		image, //
		mpvideo, //
	}
	
	/**
	 * 应答消息类型定义。
	 * 
	 * @author cailx
	 * 
	 */
	public static enum KefuType {
		image, // 图片消息
		music, // 音乐消息
		news, // 图文消息（点击跳转到外链）
		text, // 文本消息
		video, // 视频消息
		voice, // 语音消息
		wxcard, // 卡券消息
	}
	
	/**
	 * 临时素材类型
	 * 
	 * @author lifeng
	 */
	public static enum MediaType {
		image, // 图片消息
		video, // 视频消息
		voice, // 语音消息
		thumb, // 缩略图
	}
}
