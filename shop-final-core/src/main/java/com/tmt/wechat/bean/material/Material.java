package com.tmt.wechat.bean.material;

import java.io.InputStream;
import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 非图文永久素材
 * @author root
 */
public class Material implements Serializable {

	private static final long serialVersionUID = 1L;

	private String title;
	private String introduction;
	private InputStream upload;
	
	@JSONField(serialize=false)
	public InputStream getUpload() {
		return upload;
	}
	public void setUpload(InputStream	 upload) {
		this.upload = upload;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
}