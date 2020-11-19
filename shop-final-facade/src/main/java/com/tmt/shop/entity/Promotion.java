package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.utils.PromotionTypes;
/**
 * 促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public class Promotion extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public static Byte ZJ = 1;
	public static Byte MJ = 2;
	public static Byte MZ = 3;
	public static Byte ZK = 4;
	public static Byte BY = 5;
	public static Byte TG = 6;
	public static Byte QG = 7;
	public static Byte MZS = 8;
	public static Byte XR = 9;
	public static Byte YQ = 10;
	public static Byte TZ = 11; // 套装
	public static long XR_ID = 1;
	public static long YQ_ID = 2;
	
	private String name; // 名称
	private String title; // 标题
	private String image; // 图片
	private java.util.Date beginDate; // 开始使用日期
	private java.util.Date endDate; // 结束使用日期
	private Byte type; // 类型：1直降、2满减、3满折、4折扣、5包邮、6团购、7抢购、8满赠、9新人礼包（ID为1，且只能建一个）、10邀请有礼（ID为2，且只能建一个）
	private Byte isEnabled; // 是否启用，1默认启用，0 作废
	private Byte isPrice; // 是否价格限制
	private Byte isQuantity; // 是否数量限制
	private java.math.BigDecimal orderPrice; // 订单金额
	private Integer orderQuantity; // 购买数量
	private Byte getno; // 限购数量：对团购抢购有效
	private java.math.BigDecimal reduce; // 金额
	private Double discount; // 折扣
	private Byte isCouponAllowed; // 是否允许使用优惠券
	private Byte isMulti; // 是否多级促销
	private Byte isShare; // 赠送的是否需要分享，现在只能赠送优惠券 --- 冒失无效
	private Integer sort; // 排序越大，越优先使用，相同优先级最新的优先使用
	private String introduction; // 描述
	private List<PromotionProduct> products; // 关联商品
	private PromotionExt ext; // 扩展数据
	private List<PromotionCoupon> coupons; // 关联优惠券
	private Map<Long, PromotionProduct> ps; // 关联商品
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Map<Long, PromotionProduct> getPs() {
		return ps;
	}
	public void setPs(Map<Long, PromotionProduct> ps) {
		this.ps = ps;
	}
	public List<PromotionCoupon> getCoupons() {
		return coupons;
	}
	public void setCoupons(List<PromotionCoupon> coupons) {
		this.coupons = coupons;
	}
	public PromotionExt getExt() {
		return ext;
	}
	public void setExt(PromotionExt ext) {
		this.ext = ext;
	}
	public void initExt(PromotionExt ext) {
		if (ext != null) {
			this.setOrderPrice(ext.getOrderPrice());
			this.setOrderQuantity(ext.getOrderQuantity());
			this.setDiscount(ext.getDiscount());
			this.setReduce(ext.getReduce());
			
			// 多级促销
			if (ext.getOrderPrice1() != null || ext.getOrderQuantity1() != null) {
				this.setIsMulti(Promotion.YES);
			}
		}
		this.ext = ext;
	}
	public List<PromotionProduct> getProducts() {
		return products;
	}
	public void setProducts(List<PromotionProduct> products) {
		this.products = products;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
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
	public String getTypeName() {
		return PromotionTypes.getTypeName(type);
	}
    public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
    public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
    public Byte getIsPrice() {
		return isPrice;
	}
	public void setIsPrice(Byte isPrice) {
		this.isPrice = isPrice;
	}
    public Byte getIsQuantity() {
		return isQuantity;
	}
	public void setIsQuantity(Byte isQuantity) {
		this.isQuantity = isQuantity;
	}
    public java.math.BigDecimal getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(java.math.BigDecimal orderPrice) {
		this.orderPrice = orderPrice;
	}
    public Integer getOrderQuantity() {
		return orderQuantity;
	}
	public void setOrderQuantity(Integer orderQuantity) {
		this.orderQuantity = orderQuantity;
	}
    public Byte getGetno() {
		return getno;
	}
	public void setGetno(Byte getno) {
		this.getno = getno;
	}
    public java.math.BigDecimal getReduce() {
		return reduce;
	}
	public void setReduce(java.math.BigDecimal reduce) {
		this.reduce = reduce;
	}
    public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
    public Byte getIsCouponAllowed() {
		return isCouponAllowed;
	}
	public void setIsCouponAllowed(Byte isCouponAllowed) {
		this.isCouponAllowed = isCouponAllowed;
	}
    public Byte getIsMulti() {
		return isMulti;
	}
	public void setIsMulti(Byte isMulti) {
		this.isMulti = isMulti;
	}
    public Byte getIsShare() {
		return isShare;
	}
	public void setIsShare(Byte isShare) {
		this.isShare = isShare;
	}
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
    public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	/**
	 * 返回状态
	 * 0  可用状态
	 * 1  过期（或设置为不可用）
	 * 2  未开始
	 * @return
	 */
	@JSONField(serialize=false)
	public Byte getUseAble() {
		// 用这个时间避免临界点的问题
		Date now = DateUtils.getTimeStampNow();
		if (this.isEnabled == null || this.isEnabled == 0
				|| this.getEndDate() == null || this.getBeginDate() == null
				|| DateUtils.after(now, this.getEndDate())) {
			return 1; // 过期或设置为不可用
		} else if(DateUtils.before(now, this.getBeginDate())) {
			return 2; // 没开始
		}
		// 之前将前两种状态统一返回为1
//		if (this.isEnabled == null || this.isEnabled == 0
//				|| this.getEndDate() == null || DateUtils.after(now, this.getEndDate())
//				|| this.getBeginDate() == null || DateUtils.before(now, this.getBeginDate())) {
//			return 1;
//		}
		return 0;
	}
	
	/**
	 * 得到过期的毫秒数
	 * bug 时间计算错误，应该是据当前时间
	 * @return
	 */
	@JSONField(serialize=false)
	public Long getExpirySeconds() {
		
		// 没设置时间则不缓存
		if (this.getEndDate() == null || this.getBeginDate() == null) {
			return ShopConstant.MIN_SECONDS_PROMOTIONS;
		}
		
		// 默认值不能设置为 MIN_SECONDS_PROMOTIONS， 如果 status = -1 则被认为为计算的10秒
		// seconds / 1000 会导致计算结果缩减为0
		// 是因为给的初始值和 status = 2 或 0 时计算的结果的单位不一致。
		long seconds = 0;
		Date now = DateUtils.getTodayTime();
		Byte status = this.getUseAble();
		
		// 没开始
		if (status == 2) {
			long end = this.getBeginDate().getTime();
			long begin = now.getTime();
			seconds = end - begin;
		} 
		
		// 有效
		else if(status == 0) {
			long end = this.getEndDate().getTime();
			long begin = now.getTime();
			seconds = end - begin;
		}
		// 不能为0秒
		return seconds <= 0? ShopConstant.MIN_SECONDS_PROMOTIONS: (seconds / 1000);
	}
}