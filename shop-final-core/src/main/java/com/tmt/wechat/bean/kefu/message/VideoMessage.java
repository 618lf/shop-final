package com.tmt.wechat.bean.kefu.message;

import com.tmt.wechat.bean.base.Constants;
import com.tmt.wechat.bean.base.Video;

/**
 * 视频
 * @author lifeng
 */
public class VideoMessage extends Message {

	private static final long serialVersionUID = 1L;
	public Video video;

	public VideoMessage(String touser, Video video) {
		this.video = video;
		this.setTouser(touser);
		this.setMsgtype(Constants.KefuType.video.name());
	}

	public Video getVideo() {
		return video;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
}
