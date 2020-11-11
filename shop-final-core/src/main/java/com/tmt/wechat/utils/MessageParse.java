package com.tmt.wechat.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tmt.core.utils.XmlParse;
import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.message.EventMsgLocation;
import com.tmt.wechat.bean.message.EventMsgMass;
import com.tmt.wechat.bean.message.EventMsgTemplate;
import com.tmt.wechat.bean.message.EventMsgUserAttention;
import com.tmt.wechat.bean.message.MenuEventMsgClick;
import com.tmt.wechat.bean.message.MenuEventMsgLocationSelect;
import com.tmt.wechat.bean.message.MenuEventMsgPicWeuxin;
import com.tmt.wechat.bean.message.MenuEventMsgScancodePush;
import com.tmt.wechat.bean.message.MenuEventMsgView;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.ReqMsgImage;
import com.tmt.wechat.bean.message.ReqMsgLink;
import com.tmt.wechat.bean.message.ReqMsgLocation;
import com.tmt.wechat.bean.message.ReqMsgText;
import com.tmt.wechat.bean.message.ReqMsgVideo;
import com.tmt.wechat.bean.message.ReqMsgVoice;

/**
 * 消息解析
 * @author lifeng
 */
public class MessageParse {

	/**
	 * 直接利用 java dom 解析
	 * @param xml
	 * @return
	 */
	public static MsgHead parseXML(String xml) {
		Document doc = XmlParse.parse(xml);
		Element root = doc.getDocumentElement();
		
		// 基本信息
		MsgHead msgHead = new MsgHead();
		msgHead.read(root);
		
		// 返回的信息
		MsgHead msg = null;
		
		// 具体的信息
		if (Constants.ReqType.text.name().equals(msgHead.getMsgType())) {
			msg = new ReqMsgText();
		} else if (Constants.ReqType.image.name().equals(msgHead.getMsgType())) {
			msg = new ReqMsgImage();
		} else if (Constants.ReqType.link.name().equals(msgHead.getMsgType())) {
			msg = new ReqMsgLink();
		} else if (Constants.ReqType.location.name().equals(msgHead.getMsgType())) {
			msg = new ReqMsgLocation();
		} else if (Constants.ReqType.video.name().equals(msgHead.getMsgType())
				|| Constants.ReqType.shortvideo.name().equals(msgHead.getMsgType())) {
			msg = new ReqMsgVideo();
		} else if (Constants.ReqType.voice.name().equals(msgHead.getMsgType())) {
			msg = new ReqMsgVoice();
		} else if (Constants.ReqType.event.name().equals(msgHead.getMsgType())) {
			msg = eventMsg(msgHead);
		}
		
		// 获取数据
		if (msg != null) {
			msg.setHead(msgHead);
			msg.read(root);
		}
		return msg;
	}
	
	// 事件
	private static MsgHead eventMsg(MsgHead msgHead) {
		if (Constants.EventType.CLICK.name().equals(msgHead.getEvent())) {
			return new MenuEventMsgClick();
		} else if (Constants.EventType.VIEW.name().equals(msgHead.getEvent())) {
			return new MenuEventMsgView();
		} else if (Constants.EventType.LOCATION.name().equals(msgHead.getEvent())) {
			return new EventMsgLocation();
		} else if (Constants.EventType.pic_weixin.name().equals(msgHead.getEvent())
				|| Constants.EventType.pic_photo_or_album.name().equals(msgHead.getEvent())
				|| Constants.EventType.pic_sysphoto.name().equals(msgHead.getEvent())) {
			return new MenuEventMsgPicWeuxin();
		} else if (Constants.EventType.scancode_push.name().equals(msgHead.getEvent())
				|| Constants.EventType.scancode_waitmsg.name().equals(msgHead.getEvent())) {
			return new MenuEventMsgScancodePush();
		} else if (Constants.EventType.location_select.name().equals(msgHead.getEvent())) {
			return new MenuEventMsgLocationSelect();
		} else if (Constants.EventType.SCAN.name().equals(msgHead.getEvent())
				|| Constants.EventType.subscribe.name().equals(msgHead.getEvent())
				|| Constants.EventType.unsubscribe.name().equals(msgHead.getEvent())) {
			return new EventMsgUserAttention();
		} else if (Constants.EventType.TEMPLATESENDJOBFINISH.name().equals(msgHead.getEvent())) {
			return new EventMsgTemplate();
		} else if (Constants.EventType.MASSSENDJOBFINISH.name().equals(msgHead.getEvent())) {
			return new EventMsgMass();
		}
		
		// 其他的暂时不支持
		return null;
	}
}
