package com.tmt.shop.entity;

import java.io.Serializable;
/**
 * 商品限购 管理
 * @author 超级管理员
 * @date 2016-11-05
 */
public class GoodsLimit implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Byte buyLimit; // 限购数量
    
    public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public Byte getBuyLimit() {
		return buyLimit;
	}
	public void setBuyLimit(Byte buyLimit) {
		this.buyLimit = buyLimit;
	}
}