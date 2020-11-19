package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.IdEntity;
import com.tmt.core.utils.time.DateUtils;
/**
 * 优惠码 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class CouponCode extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String code; // 条码，可输入条码领取
	private Byte isEnabled; // 是否可用，1默认启用，0 不可用
	private Byte isUsed; // 是否使用
	private java.util.Date usedDate; // 使用日期
	private Long coupon; // 优惠券
	private Long userId; // 用户
	private String userName; // 用户
	private String userImg; // 用户头像
	private java.util.Date beginDate; // 有效期
	private java.util.Date endDate; // 有效期
	private Long fissionId; // 裂变而来
	private Date createDate;//领取时间
	private Byte qtype; // 查询类型
	private Integer val; // 面值
	private String remarks; // 如果不能使用，则标注预计什么时候可以使用
	
	//查询字段
	private String name; // 优惠券名称
	private Byte isPrice; // 是否价格限制 0 限制， 1 不限制
	private java.math.BigDecimal orderPrice;
	
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Byte getIsPrice() {
		return isPrice;
	}
	public void setIsPrice(Byte isPrice) {
		this.isPrice = isPrice;
	}
	public java.math.BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(java.math.BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVal() {
		return val;
	}
	public void setVal(Integer val) {
		this.val = val;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Byte getQtype() {
		return qtype;
	}
	public void setQtype(Byte qtype) {
		this.qtype = qtype;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
    public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
    public Byte getIsUsed() {
		return isUsed;
	}
	public void setIsUsed(Byte isUsed) {
		this.isUsed = isUsed;
	}
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public java.util.Date getUsedDate() {
		return usedDate;
	}
	public void setUsedDate(java.util.Date usedDate) {
		this.usedDate = usedDate;
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
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public java.util.Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(java.util.Date beginDate) {
		this.beginDate = beginDate;
	}
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
    public Long getFissionId() {
		return fissionId;
	}
	public void setFissionId(Long fissionId) {
		this.fissionId = fissionId;
	}
	
	/**
	 * 获得领用日期
	 * @return
	 */
	@JSONField(format="MM.dd HH:mm")
	public java.util.Date getGiveDate() {
		return this.getCreateDate();
	}
	
	/**
	 * 返回状态
	 * 0  可用状态
	 * 1  过期（或设置为不可用）
	 * @return
	 */
	public Byte getStatus() {
		Date now = DateUtils.getTodayTime();
		if (this.isEnabled == null || this.isEnabled == 0
				|| (this.getEndDate() != null && DateUtils.after(now, this.getEndDate()))
				|| (this.getBeginDate() != null && DateUtils.before(now, this.getBeginDate()))) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * 初始化券码
	 * @param code
	 * @return
	 */
	public static CouponCode newCode(String code) {
		CouponCode _code = new CouponCode();
		_code.setCode(code);
		_code.setIsEnabled(Coupon.YES);
		_code.setIsUsed(Coupon.NO);
		return _code;
	}
	
	// 返回有效期
	public String getExpiryDate() {
		if (this.getBeginDate() != null && this.getEndDate() != null) {
			StringBuilder sb = new StringBuilder();
			sb.append(DateUtils.getFormatDate(this.getBeginDate(), "yyyy年MM月dd日"))
			  .append("-").append(DateUtils.getFormatDate(this.getEndDate(), "yyyy年MM月dd日"));
			return sb.toString();
		}
		return null;
	}
}
