package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.GiveRedenvelopeResult;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionCoupon;
import com.tmt.shop.entity.PromotionProduct;
import com.tmt.system.entity.User;

/**
 * 促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
public interface PromotionServiceFacade extends BaseServiceFacade<Promotion,Long> {
	
	/**
	 * 带出扩展属性
	 * @param id
	 * @return
	 */
	public Promotion getWithExt(Long id);
	
	/**
	 * 带出商品
	 * @param id
	 * @return
	 */
	public Promotion getWithProduct(Long id);
	
	/**
	 * 保存
	 */
	public void save(Promotion promotion);
	
	/**
	 * 删除
	 */
	public void delete(List<Promotion> promotions);
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<PromotionProduct> queryProductByPromotionId(Long promotionId);
	
	/**
	 * 查询优惠券关联的商品(包含商品信息)
	 * @param couponId
	 * @return
	 */
	public List<PromotionProduct> queryRichProductByPromotionId(Long promotionId);
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<PromotionCoupon> queryCouponByPromotionId(Long promotionId);
	
	/**
	 * 查询优惠券关联的商品(包含商品信息)
	 * @param couponId
	 * @return
	 */
	public List<PromotionCoupon> queryRichCouponByPromotionId(Long promotionId);
	
	/**
	 * 用户是否获取新人礼包
	 * @return
	 */
	public Boolean userFetchXrGift(User user);
	
	/**
	 * 获取新人礼包
	 * @param user
	 * @param 推荐人信息
	 * @return
	 */
	public List<CouponCode> fetchXrGifts(User user,String recommendId);
	
	
	// 前端需要
	/**
	 * 获取全局的可用促销（与具体商品无关的）
	 * 排除新人、邀请
	 * 不要直接调用这个
	 * @see com.tmt.shop.utils.PromotionUtils queryProductAllEnabledPromotions
	 */
	@Deprecated
	public List<Promotion> queryGlobalEnabledPromotions();
	
	/**
	 * 获取全局的可用促销（与具体商品无关的）
	 * 排除新人、邀请
	 * 不要直接调用这个
	 * @see com.tmt.shop.utils.PromotionUtils queryProductAllEnabledPromotions
	 */
	@Deprecated
	public List<Promotion> queryProductEnabledPromotions(Long productId);
	
	/**
	 * 分配红包
	 * @param user
	 * @param orderId
	 * @return
	 */
	public GiveRedenvelopeResult giveRedenvelope(User user, Long orderId);
	
	/**
	 * 带出相关数据
	 * @param id
	 * @return
	 */
	public Promotion getShowPromotion(Long id);
}