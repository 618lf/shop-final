package com.tmt.wechat.bean.message;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.tmt.wechat.bean.base.Constants.RespType;
import com.tmt.wechat.bean.base.Music;

/**
 * 回复音乐消息
 * 
 * @author lifeng
 *
 */
@XmlRootElement(name="xml")
public class RespMsgMusic extends RespMsg {
	private static final long serialVersionUID = 1L;
	private Music music;
	public RespMsgMusic() {}
	public RespMsgMusic(MsgHead req, Music music) {
		super(req, RespType.music.name());
		this.music = music;
	}
	@XmlElement(name="Music")
	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}
}
