package com.tmt.wechat.bean.message;

import org.w3c.dom.Element;

import com.tmt.core.utils.XmlParse;

/**
 * 图片请求消息
 * 
 * @author rikky.cai
 * @qq:6687523
 * @Email:6687523@qq.com
 * 
 */
public class ReqMsgImage extends ReqMsgMedia {
	
	private static final long serialVersionUID = 1L;
	
	private String picUrl;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	
	@Override
	public void read(Element element) {
		super.read(element);
		this.picUrl = XmlParse.elementText(element, "PicUrl");
	}

	@Override
	public String toString() {
		StringBuilder msg = new StringBuilder();
		msg.append("msgId:").append(this.getMsgId()).append("\n");
		msg.append("mediaId:").append(this.getMediaId()).append("\n");
		msg.append("picUrl:").append(this.getPicUrl()).append("\n");
		return msg.toString();
	}

	@Override
	public String getShowMessage() {
		StringBuilder msg = new StringBuilder();
		msg.append("<a data-type='pic' data-id='").append(this.getMediaId())
				.append("' data-href='" + this.getPicUrl() + "'>图片预览</a>");
		return msg.toString();
	}
}
