package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;

/**
 * 等级设置 管理
 * 
 * @author 超级管理员
 * @date 2017-02-18
 */
public class Rank extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;
	public static Byte GRADE_NORMAL = 0; // 普通会员
	public static Byte GRADE_TRY = 1; // 试用会员
	public static Byte GRADE_GOLD = 2; // 金牌会员
	public static Byte GRADE_DIAMOND = 3; // 钻石会员

	private Byte grade; // 等级
	private String name; // 等级名称
	private String image; // 等级图片
	private String bigImage; // 大图
	private String sn; // 编号
	private java.math.BigDecimal price; // 价格
	private Integer minPoints; // 积分
	private Integer maxPoints; // 积分
	private Byte autoUpgrade; // 状态：0 否，1 是
	private Double discount; // 折扣
	private Integer shipping; // 包邮次数，0不包邮，-1永久包邮费
	private Double points; // 购物赠送积分
	private String birthdayRight; // 生日特权
	private String memberRight; // 会员沙龙
	private List<RankCoupon> coupons; // 关联优惠券
	private String couponsStr; // 关联优惠券
	private Integer validDays;// 有效天数，过了有效期会自动退回到 grade = 0 的会员

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public void setCouponsStr(String couponsStr) {
		this.couponsStr = couponsStr;
	}

	public String getCouponsStr() {
		return couponsStr;
	}

	public String getBigImage() {
		return bigImage;
	}

	public void setBigImage(String bigImage) {
		this.bigImage = bigImage;
	}

	public List<RankCoupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<RankCoupon> coupons) {
		this.coupons = coupons;
	}

	public Byte getGrade() {
		return grade;
	}

	public void setGrade(Byte grade) {
		this.grade = grade;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public Integer getMinPoints() {
		return minPoints;
	}

	public void setMinPoints(Integer minPoints) {
		this.minPoints = minPoints;
	}

	public Integer getMaxPoints() {
		return maxPoints;
	}

	public void setMaxPoints(Integer maxPoints) {
		this.maxPoints = maxPoints;
	}

	public Byte getAutoUpgrade() {
		return autoUpgrade;
	}

	public void setAutoUpgrade(Byte autoUpgrade) {
		this.autoUpgrade = autoUpgrade;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Integer getShipping() {
		return shipping;
	}

	public void setShipping(Integer shipping) {
		this.shipping = shipping;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public String getBirthdayRight() {
		return birthdayRight;
	}

	public void setBirthdayRight(String birthdayRight) {
		this.birthdayRight = birthdayRight;
	}

	public String getMemberRight() {
		return memberRight;
	}

	public void setMemberRight(String memberRight) {
		this.memberRight = memberRight;
	}

	public void initCoupons() {
		if (coupons != null && coupons.size() > 0) {
			StringBuilder couponsStr = new StringBuilder();
			for (RankCoupon coupon : coupons) {
				if (coupon.getCoupon() != null) {
					couponsStr.append(coupon.getCoupon().getName()).append("<b>1张</b>").append(";");
				}
			}
			this.couponsStr = couponsStr.deleteCharAt(couponsStr.length() - 1).toString();
		}
	}
}