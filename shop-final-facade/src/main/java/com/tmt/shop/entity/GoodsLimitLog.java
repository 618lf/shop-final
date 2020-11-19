package com.tmt.shop.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
/**
 * 商品限购记录 管理
 * @author 超级管理员
 * @date 2016-11-05
 */
public class GoodsLimitLog implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Long userId; // 创建者
	private java.util.Date buyDate; // 购买时间
	private Integer buyNum; // 购买数量
    
    public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
    public java.util.Date getBuyDate() {
		return buyDate;
	}
	public void setBuyDate(java.util.Date buyDate) {
		this.buyDate = buyDate;
	}
    
    public Integer getBuyNum() {
		return buyNum;
	}
	public void setBuyNum(Integer buyNum) {
		this.buyNum = buyNum;
	}
}
