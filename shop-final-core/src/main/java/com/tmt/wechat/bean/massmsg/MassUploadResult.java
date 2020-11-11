package com.tmt.wechat.bean.massmsg;

import java.io.Serializable;

/**
 * 上传群发用的素材的结果 -- 视频和图文消息需要在群发前上传素材
 * @author lifeng
 */
public class MassUploadResult implements Serializable {
	
	private static final long serialVersionUID = 6568157943644994029L;
	private String type;
	private String mediaId;
	private long createdAt;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
}