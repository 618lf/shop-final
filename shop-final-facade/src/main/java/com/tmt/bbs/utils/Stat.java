package com.tmt.bbs.utils;

import java.io.Serializable;

/**
 * 统计项目 栏目、标签
 * 
 * @author lifeng
 */
public class Stat implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Integer num;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
