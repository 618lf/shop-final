package com.tmt.wechat.bean.kefu.message;

import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.base.Voice;

/**
 * 语音
 * @author lifeng
 */
public class VoiceMessage extends Message {

	private static final long serialVersionUID = 1L;

	public VoiceMessage(String touser, String mediaId) {
		this.voice = new Voice();
		this.voice.setMedia_id(mediaId);
		this.setTouser(touser);
		this.setMsgtype(Constants.KefuType.voice.name());
	}

	public Voice voice;

	public Voice getVoice() {
		return voice;
	}

	public void setVoice(Voice voice) {
		this.voice = voice;
	}
}