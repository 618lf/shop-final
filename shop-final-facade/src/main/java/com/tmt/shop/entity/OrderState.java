package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;

import com.tmt.core.entity.IdEntity;

/**
 * 订单状态 管理
 * 
 * @author 超级管理员
 * @date 2016-10-06
 */
public class OrderState extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Byte opt; // 操作类型：见新版
	private Byte state; // 状态：0初始，1：更新
	private Date createDate; // 创建日期
	private Date expire; // 过期时间
	private String ostate; // 订单当前状态
	private Integer ecount; // 执行次数

	public Integer getEcount() {
		return ecount;
	}

	public void setEcount(Integer ecount) {
		this.ecount = ecount;
	}

	public String getOstate() {
		return ostate;
	}

	public void setOstate(String ostate) {
		this.ostate = ostate;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	@Override
	public Long prePersist() {
		return this.id;
	}
}
