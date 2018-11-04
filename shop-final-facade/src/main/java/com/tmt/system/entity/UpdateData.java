package com.tmt.system.entity;

import java.io.Serializable;
/**
 * 数据更新 管理
 * @author 超级管理员
 * @date 2016-09-09
 */
public class UpdateData implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String msg; // 消息
	private Byte module; // 模块
	private Byte opt; // 操作类型：0更新，1：删除
	private Byte state; // 状态： 0 未操作， 1 操作中
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Byte getModule() {
		return module;
	}
	public void setModule(Byte module) {
		this.module = module;
	}
    public Byte getOpt() {
		return opt;
	}
	public void setOpt(Byte opt) {
		this.opt = opt;
	}
	public Byte getState() {
		return state;
	}
	public void setState(Byte state) {
		this.state = state;
	}
}