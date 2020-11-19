package com.tmt.bbs.entity;

import java.io.Serializable;
/**
 * 回复点赞数 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
public class ReplyHits implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer hits; // 点击量
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getHits() {
		return hits;
	}
	public void setHits(Integer hits) {
		this.hits = hits;
	}
}
