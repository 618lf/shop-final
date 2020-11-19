package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.utils.Ints;
import com.tmt.core.utils.time.DateUtils;

/**
 * 用户等级 管理
 * 
 * @author 超级管理员
 * @date 2017-02-18
 */
public class UserRank implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long userId; // 用户
	private String userNo;// 用户编号
	private String userName;// 昵称
	private String headimg;// 头像地址
	private Integer points; // 积分
	private Long rankId; // 等级
	private String rankName; // 等级
	private String rankImage; // 等级图片
	private Integer usePoints; // 已用积分
	private Integer shipping; // 包邮次数
	private Integer useShipping; // 已用包邮次数
	private java.util.Date effectDate; // 生效日期
	private java.util.Date expiryDate; // 失效日期
	private boolean isEnabled; // 是否可用 -- 通过这个字段来控制是否启用

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public String getRankImage() {
		return rankImage;
	}

	public void setRankImage(String rankImage) {
		this.rankImage = rankImage;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getUseAblePoints() {
		return Ints.subI(this.points, this.usePoints);
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public void addPoints(int points) {
		this.points = this.points + points;
	}

	public Long getRankId() {
		return rankId;
	}

	public void setRankId(Long rankId) {
		this.rankId = rankId;
	}

	public Integer getUsePoints() {
		return usePoints;
	}

	public void setUsePoints(Integer usePoints) {
		this.usePoints = usePoints;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public Integer getRemainShipping() {
		return Ints.subI(this.shipping, this.useShipping);
	}

	public Integer getUseShipping() {
		return useShipping;
	}

	public void setUseShipping(Integer useShipping) {
		this.useShipping = useShipping;
	}

	@JSONField(format = "yyyy-MM-dd")
	public java.util.Date getEffectDate() {
		return effectDate;
	}

	public void setEffectDate(java.util.Date effectDate) {
		this.effectDate = effectDate;
	}

	@JSONField(format = "yyyy-MM-dd")
	public java.util.Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(java.util.Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	/**
	 * 返回状态 0 失效 1 有效 2 永久有效
	 * 
	 * @return
	 */
	public Byte getUseAble() {
		Date now = DateUtils.getTodayDate();
		if (this.getExpiryDate() == null) {
			return 2;
		} else if (!DateUtils.after(now, this.getExpiryDate())) {
			return 1;
		}
		return 0;
	}
}