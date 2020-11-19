package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.Ints;
import com.tmt.core.utils.time.DateUtils;
/**
 * 优惠券 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class Coupon extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private java.util.Date beginDate; // 使用开始日期
	private java.util.Date endDate; // 使用结束日期
	private java.math.BigDecimal orderPrice; // 订单金额
	private Byte isPrice; // 是否价格限制 0 限制， 1 不限制
	private Byte isEnabled; // 是否启用，1默认启用，0 作废
	private Byte isExchange; // 是否允许积分兑换
	private Byte isFission; // 是否裂变优惠券
	private Integer fissionNum; // 裂变数
	private Byte expireType; // 过期方式：0 默认 1 指定过期天数，2 固定过期时间
	private Integer expireDays; // 从领取时间开始计算过期时间，超过总体过期时间
	private java.util.Date expireDate; // 固定过期时间，不得超过总体过期时间
	private Integer point; // 需要积分兑换
	private Integer val; // 面值
	private Byte getno; // 每人限领张数,默认1
	private Integer total; // 总数
	private Integer geted; // 领取
	private Integer used; // 使用
	private String introduction; // 
	private List<CouponProduct> products; // 优惠券关联商品
	private Set<Long> ps;//  关联商品
    
    public Set<Long> getPs() {
		return ps;
	}
	public void setPs(Set<Long> ps) {
		this.ps = ps;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
    public java.math.BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(java.math.BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
    public Byte getIsPrice() {
		return isPrice;
	}
	public void setIsPrice(Byte isPrice) {
		this.isPrice = isPrice;
	}
    public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
    public Byte getIsExchange() {
		return isExchange;
	}
	public void setIsExchange(Byte isExchange) {
		this.isExchange = isExchange;
	}
    public Byte getIsFission() {
		return isFission;
	}
	public void setIsFission(Byte isFission) {
		this.isFission = isFission;
	}
    public Integer getFissionNum() {
		return fissionNum;
	}
	public void setFissionNum(Integer fissionNum) {
		this.fissionNum = fissionNum;
	}
    public Byte getExpireType() {
		return expireType;
	}
	public void setExpireType(Byte expireType) {
		this.expireType = expireType;
	}
    public Integer getExpireDays() {
		return expireDays;
	}
	public void setExpireDays(Integer expireDays) {
		this.expireDays = expireDays;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
    public java.util.Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(java.util.Date expireDate) {
		this.expireDate = expireDate;
	}
    public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
    public Integer getVal() {
		return val;
	}
	public void setVal(Integer val) {
		this.val = val;
	}
    public Byte getGetno() {
		return getno;
	}
	public void setGetno(Byte getno) {
		this.getno = getno;
	}
    public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
    public Integer getGeted() {
		return geted;
	}
	public void setGeted(Integer geted) {
		this.geted = geted;
	}
	public Integer getResidue() {
		return Ints.subI(total, geted);
	}
    public Integer getUsed() {
		return used;
	}
	public void setUsed(Integer used) {
		this.used = used;
	}
    public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public List<CouponProduct> getProducts() {
		return products;
	}
	public void setProducts(List<CouponProduct> products) {
		this.products = products;
	}
	
	/**
	 * 返回状态
	 * 0  可用状态
	 * 1  过期（或设置为不可用）
	 * @return
	 */
	@JSONField(serialize=false)
	public Byte getStatus() {
		Date now = DateUtils.getTodayTime();
		if (this.isEnabled == null || this.isEnabled == 0
				|| this.getEndDate() == null || DateUtils.after(now, this.getEndDate())
				|| this.getBeginDate() == null || DateUtils.before(now, this.getBeginDate())) {
			return 1;
		}
		return 0;
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
	
	/**
	 * 得到过期的毫秒数
	 * @return
	 */
	@JSONField(serialize=false)
	public Long getExpirySeconds() {
		if (this.getEndDate() != null && this.getBeginDate() != null) {
			long end = this.getEndDate().getTime();
			long begin = DateUtils.getTodayTime().getTime();
			long seconds = end - begin;
			return seconds <= 0? ShopConstant.MIN_SECONDS_PROMOTIONS: seconds;
		}
		return ShopConstant.MIN_SECONDS_PROMOTIONS;
	}
}