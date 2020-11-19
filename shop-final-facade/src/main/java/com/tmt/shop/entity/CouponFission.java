package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;

import com.tmt.core.entity.IdEntity;
import com.tmt.core.utils.Ints;

/**
 * 优惠券裂变 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class CouponFission extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long coupon; // 优惠券
	private Long userId; // 用户
	private String userName; // 用户
	private Date createDate;//创建时间
	private Integer fissionNum; // 裂变数
	private Integer val; // 面值
	private Integer geted; // 领取
	private Byte isEnabled; // 是否不可用，1可用，0 不可用 -- 订单完成之后置为可用
    
	// 其中一个
	private CouponCode oneCode;
	
    public Integer getVal() {
		return val;
	}
	public void setVal(Integer val) {
		this.val = val;
	}
	public CouponCode getOneCode() {
		return oneCode;
	}
	public void setOneCode(CouponCode oneCode) {
		this.oneCode = oneCode;
	}
	public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Integer getFissionNum() {
		return fissionNum;
	}
	public void setFissionNum(Integer fissionNum) {
		this.fissionNum = fissionNum;
	}
	public Integer getGeted() {
		return geted;
	}
	public void setGeted(Integer geted) {
		this.geted = geted;
	}
	public Integer getResidue() {
		return Ints.subI(fissionNum, geted);
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCoupon() {
		return coupon;
	}
	public void setCoupon(Long coupon) {
		this.coupon = coupon;
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