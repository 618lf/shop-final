package com.tmt.wechat.bean.device;

import java.io.Serializable;

/**
 * 设备相关消息
 * 
 * @author lifeng
 *
 */
public class DeviceMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	private String device_type;
	private String device_id;
	private String open_id;
	private String content;
	public String getDevice_type() {
		return device_type;
	}
	public void setDevice_type(String device_type) {
		this.device_type = device_type;
	}
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}