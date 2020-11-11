package com.tmt.wechat.bean.kefu.message;

import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.base.Text;

/**
 * 文本消息
 * @author lifeng
 */
public class TextMessage extends Message {

	private static final long serialVersionUID = 1L;
	private Text text;

	public TextMessage(String touser, String content) {
		this.text = new Text();
		this.text.setContent(content);
		this.setTouser(touser);
		this.setMsgtype(Constants.KefuType.text.name());
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
}
