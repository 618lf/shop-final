package com.tmt.bbs.entity;

import java.io.Serializable;

/**
 * 点击
 * 
 * @author lifeng
 *
 */
public class Hit implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private Byte hited = 0;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getHited() {
		return hited;
	}
	public void setHited(Byte hited) {
		this.hited = hited;
	}
}
