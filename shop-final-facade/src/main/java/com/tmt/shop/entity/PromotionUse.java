package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;

import com.tmt.core.entity.IdEntity;

/**
 * 促销使用 管理
 * 
 * @author 超级管理员
 * @date 2016-11-26
 */
public class PromotionUse extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long promotionId; // 促销
	private Long userId; // 用户
	private String userName; // 用户
	private Date createDate;// 创建时间
	private Byte isReturn; // 是否返还推荐人
	private Long recommendId;// 推荐人

	public Byte getIsReturn() {
		return isReturn;
	}

	public void setIsReturn(Byte isReturn) {
		this.isReturn = isReturn;
	}

	public Long getRecommendId() {
		return recommendId;
	}

	public void setRecommendId(Long recommendId) {
		this.recommendId = recommendId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}