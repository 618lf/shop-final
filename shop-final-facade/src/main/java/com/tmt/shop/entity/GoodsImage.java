package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 商品图片 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class GoodsImage extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long goodsId; // 编号
	private String large; // 大图
	private String medium; // 中图
	private String thumbnail; // 小图
	private Integer sort; // 排序
	private String title; // 标题

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getLarge() {
		return large;
	}

	public void setLarge(String large) {
		this.large = large;
	}

	public String getMedium() {
		return medium;
	}

	public void setMedium(String medium) {
		this.medium = medium;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

}
