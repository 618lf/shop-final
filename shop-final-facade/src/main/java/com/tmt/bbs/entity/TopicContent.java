package com.tmt.bbs.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;
/**
 * 主题内容 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
public class TopicContent extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String content; // 内容
	private String addContent; // 追加评价
	private String image; // 图片
	private String image2; // 图片
	private String image3; // 图片
	private String image4; // 图片
    
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    
    public String getAddContent() {
		return addContent;
	}
	public void setAddContent(String addContent) {
		this.addContent = addContent;
	}
    
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    
    public String getImage2() {
		return image2;
	}
	public void setImage2(String image2) {
		this.image2 = image2;
	}
    
    public String getImage3() {
		return image3;
	}
	public void setImage3(String image3) {
		this.image3 = image3;
	}
    
    public String getImage4() {
		return image4;
	}
	public void setImage4(String image4) {
		this.image4 = image4;
	}
	@Override
	public Long prePersist() {
		return this.id;
	}
}
