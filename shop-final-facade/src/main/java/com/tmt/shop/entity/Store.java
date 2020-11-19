package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.IdEntity;
import com.tmt.core.staticize.DomainSer;
import com.tmt.core.utils.time.DateUtils;

/**
 * 店铺管理 管理(单店铺默认只有一条数据)
 * 
 * @author 超级管理员
 * @date 2017-01-10
 */
public class Store extends IdEntity<Long> implements Serializable, DomainSer {

	private static final long serialVersionUID = 1L;
	public static final long DEFAULT_STORE = 1L;

	private String sn; // 店铺编号， 二级域名
	private String name; // 店铺名称
	private String image; // 店铺图标
	private String phone; // 店铺电话
	private String introduce; // 介绍
	private String storeIndex; // 店铺首页
	private String storeProduct; // 商品页
	private Byte orderLimit; // 限制下单
	private String wxApp; // 绑定的微信
	private String domain; // 唯一确定的域名
	private Long updateTime; // 店铺更新时间

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 唯一确定的域名
	 */
	@Override
	public String getDomain() {
		return this.domain;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getWxApp() {
		return wxApp;
	}

	public void setWxApp(String wxApp) {
		this.wxApp = wxApp;
	}

	public String getStoreProduct() {
		return storeProduct;
	}

	public void setStoreProduct(String storeProduct) {
		this.storeProduct = storeProduct;
	}

	public Byte getOrderLimit() {
		return orderLimit;
	}

	public void setOrderLimit(Byte orderLimit) {
		this.orderLimit = orderLimit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStoreIndex() {
		return storeIndex;
	}

	public void setStoreIndex(String storeIndex) {
		this.storeIndex = storeIndex;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	@Override
	public Long prePersist() {
		return this.id;
	}

	@Override
	public Long getUpdateTime() {
		return updateTime == null ? DateUtils.getTimeStampNow().getTime() : updateTime;
	}
}