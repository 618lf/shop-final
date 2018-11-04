package com.tmt.wechat.entity;

import java.io.Serializable;
/**
 * 子图文 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
public class MetaRichRela implements Serializable { 
	
	private static final long serialVersionUID = 1L;
	
	private Long richId; // 主图文
	private Long relaId; // 相关图文
	private String relaName; // 相关图文名称
	private String relaImage; // 相关图文图片
	private Byte sort; // 排序
    
    public String getRelaName() {
		return relaName;
	}
	public void setRelaName(String relaName) {
		this.relaName = relaName;
	}
	public Long getRichId() {
		return richId;
	}
	public void setRichId(Long richId) {
		this.richId = richId;
	}
    public Long getRelaId() {
		return relaId;
	}
	public void setRelaId(Long relaId) {
		this.relaId = relaId;
	}
    public Byte getSort() {
		return sort;
	}
	public void setSort(Byte sort) {
		this.sort = sort;
	}
	public String getRelaImage() {
		return relaImage;
	}
	public void setRelaImage(String relaImage) {
		this.relaImage = relaImage;
	}
}