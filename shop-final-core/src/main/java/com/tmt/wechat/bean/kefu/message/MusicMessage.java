package com.tmt.wechat.bean.kefu.message;

import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.base.Music;

/**
 * 发送音乐
 * @author lifeng
 */
public class MusicMessage extends Message {

	private static final long serialVersionUID = 1L;
	private Music music;

	public MusicMessage(String touser, Music music) {
		this.music = music;
		this.setTouser(touser);
		this.setMsgtype(Constants.KefuType.music.name());
	}

	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}
}
