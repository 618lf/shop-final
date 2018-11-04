package com.tmt.wechat.bean.kefu.message;

import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.base.Image;

/**
 * 图片消息
 * @author lifeng
 *
 */
public class ImageMessage extends Message {

	private static final long serialVersionUID = 1L;

	public ImageMessage(String touser, String mediaId) {
		this.image = new Image();
		this.image.setMedia_id(mediaId);
		this.setTouser(touser);
		this.setMsgtype(Constants.KefuType.image.name());
	}

	private Image image;

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
