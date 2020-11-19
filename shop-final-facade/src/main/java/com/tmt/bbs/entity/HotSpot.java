package com.tmt.bbs.entity;

import com.tmt.core.entity.IdEntity;

/**
 * 热点
 * 
 * @author lifeng
 */
public class HotSpot extends IdEntity<Long> {

	private static final long serialVersionUID = 1L;

	private Byte type;
	private Integer hits;
	private Integer replys;
    private Byte isShow; // 是否显示
    
	public Byte getIsShow() {
		return isShow;
	}
	public void setIsShow(Byte isShow) {
		this.isShow = isShow;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
	public Integer getReplys() {
		return replys;
	}
	public void setReplys(Integer replys) {
		this.replys = replys;
	}
}
