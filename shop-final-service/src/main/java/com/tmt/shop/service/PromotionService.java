package com.tmt.shop.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.RegexpUtil;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.dao.PromotionCouponDao;
import com.tmt.shop.dao.PromotionDao;
import com.tmt.shop.dao.PromotionExtDao;
import com.tmt.shop.dao.PromotionProductDao;
import com.tmt.shop.dao.PromotionUseDao;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.GiveRedenvelopeResult;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderPromotion;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionCoupon;
import com.tmt.shop.entity.PromotionExt;
import com.tmt.shop.entity.PromotionProduct;
import com.tmt.shop.entity.PromotionUse;
import com.tmt.shop.enums.OrderStatus;
import com.tmt.shop.update.ShopModule;
import com.tmt.shop.utils.PromotionTypes;
import com.tmt.shop.utils.PromotionUtils;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.entity.User;
import com.tmt.system.service.UserServiceFacade;
import com.tmt.update.UpdateServiceFacade;

/**
 * 促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Service("shopPromotionService")
public class PromotionService extends BaseService<Promotion,Long> implements PromotionServiceFacade{
	
	@Autowired
	private PromotionDao promotionDao;
	@Autowired
	private PromotionProductDao productDao;
	@Autowired
	private PromotionExtDao extDao;
	@Autowired
	private PromotionCouponDao couponDao;
	@Autowired
	private PromotionUseDao useDao;
	@Autowired
	private CouponService couponService;
	@Autowired
	private UserServiceFacade userService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private UserRankServiceFacade userRankService;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateDataService;
	
	@Override
	protected BaseDao<Promotion, Long> getBaseDao() {
		return promotionDao;
	}
	
	/**
	 * 带出扩展属性
	 * @param id
	 * @return
	 */
	public Promotion getWithExt(Long id) {
		Promotion promotion = this.get(id);
		if (promotion != null) {
			if (promotion.getIsMulti() != null
					&& Promotion.YES == promotion.getIsMulti()) {
				promotion.setExt(this.extDao.get(id));
			}
		}
		return promotion;
	}
	
	/**
	 * 带出商品
	 * @param id
	 * @return
	 */
	public Promotion getWithProduct(Long id) {
		Promotion promotion = this.get(id);
		if (promotion != null) {
			promotion.setProducts(this.queryRichProductByPromotionId(id));
			
			// 多级促销
			if (promotion.getIsMulti() != null
					&& Promotion.YES == promotion.getIsMulti()) {
				promotion.setExt(this.extDao.get(id));
			}
			
			// 赠送
			if (promotion.getType() == Promotion.MZS || promotion.getType() == Promotion.XR
					|| promotion.getType() == Promotion.YQ) {
				promotion.setCoupons(this.queryRichCouponByPromotionId(id));
			}
		}
		return promotion;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Promotion promotion) {
		promotion.setRemarks(PromotionTypes.getRemarks(promotion));
		if(IdGen.isInvalidId(promotion.getId())) {
			this.insert(promotion);
		} else {
			this.update(promotion);
			PromotionUtils.clearCache(promotion.getId());
		}
		PromotionUtils.clearCache();
		this.saveProducts(promotion);
		this.saveExt(promotion);
		this.saveCoupon(promotion);
		
		this._update(promotion, (byte)0);
	}
	
	// 保存商品
	private void saveProducts(Promotion promotion) {
		List<PromotionProduct> olds = this.queryRichProductByPromotionId(promotion.getId());
	    List<PromotionProduct> products = promotion.getProducts();
	    if (products != null) {
	    	for(PromotionProduct product: products) {
	    		product.setPromotions(promotion.getId());
	    		PromotionUtils.clearCache(product.getProducts());
	    	}
	    }
	    this.productDao.batchDelete(olds);
	    if (products != null && products.size() != 0) {
	    	this.productDao.batchInsert(products);
	    }
	    
	    // 删除缓存
	    for(PromotionProduct product: olds) {
	    	PromotionUtils.clearCache(product.getProducts());
	    }
	}
	
	// 保存促销设置
	private void saveExt(Promotion promotion) {
		if (promotion.getIsMulti() != null 
				&& Promotion.YES == promotion.getIsMulti()
				&& promotion.getExt() != null) {
			PromotionExt ext = promotion.getExt();
			ext.setId(promotion.getId());
			extDao.insert(promotion.getExt());
		} else {
			PromotionExt ext = new PromotionExt();
			ext.setId(promotion.getId());
			extDao.delete(ext);
		}
	}
	
	// 保存赠送的优惠券
	private void saveCoupon(Promotion promotion) {
		List<PromotionCoupon> olds = this.queryCouponByPromotionId(promotion.getId());
	    List<PromotionCoupon> coupons = promotion.getCoupons();
	    if (coupons != null) {
	    	for(PromotionCoupon coupon: coupons) {
	    		coupon.setPromotions(promotion.getId());
	    	}
	    }
	    this.couponDao.batchDelete(olds);
	    if (coupons != null && coupons.size() != 0) {
	    	this.couponDao.batchInsert(coupons);
	    }
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Promotion> promotions) {
		this.batchDelete(promotions);
		List<PromotionProduct> products = Lists.newArrayList();
		List<PromotionCoupon> coupons = Lists.newArrayList();
		// 删除相关的商品、赠送、使用记录
		for(Promotion promotion: promotions) {
			products.addAll(this.queryProductByPromotionId(promotion.getId()));
			coupons.addAll(this.queryCouponByPromotionId(promotion.getId()));
			
			this._update(promotion, (byte)1);
		}
		this.productDao.batchDelete(products);
		this.couponDao.batchDelete(coupons);
		
		//删除缓存
		PromotionUtils.clearCache();
	    for(PromotionProduct product: products) {
	    	PromotionUtils.clearCache(product.getProducts());
	    }
	}
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<PromotionProduct> queryProductByPromotionId(Long promotionId) {
		return this.productDao.queryForList("queryProductByPromotionId", promotionId);
	}
	
	/**
	 * 查询优惠券关联的商品(包含商品信息)
	 * @param couponId
	 * @return
	 */
	public List<PromotionProduct> queryRichProductByPromotionId(Long promotionId) {
		return this.productDao.queryForList("queryRichProductByPromotionId", promotionId);
	}
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<PromotionCoupon> queryCouponByPromotionId(Long promotionId) {
		return this.couponDao.queryForList("queryCouponByPromotionId",promotionId);
	}
	
	/**
	 * 查询优惠券关联的商品(包含商品信息)
	 * @param couponId
	 * @return
	 */
	public List<PromotionCoupon> queryRichCouponByPromotionId(Long promotionId) {
		return this.couponDao.queryForList("queryRichCouponByPromotionId", promotionId);
	}
	
	/**
	 * 用户是否获取新人礼包
	 * @return
	 */
	public Boolean userFetchXrGift(User user) {
		PromotionUse use = new PromotionUse();
		use.setPromotionId(Promotion.XR_ID);
		use.setUserId(user.getId());
		int count = this.useDao.countByCondition("userFetchPromotion", use);
		return count >= 1;
	}
	
	/**
	 * 获取新人礼包
	 * 为保证数据的唯一性，并保证效率
	 * 需要锁住用户表
	 * @param user
	 * @param recommendId推荐人Id
	 * @return
	 */
	@Transactional
	public List<CouponCode> fetchXrGifts(User user, String recommendId) {
		
		// 是否可以领取(判断促销活动的有效期)
		Promotion promotion = PromotionUtils.getCachedPromotion(Promotion.XR_ID);
		if (promotion == null || promotion.getUseAble() != Promotion.NO) {
			return null;
		}
		
		// 锁住用户，防止并发，导致用户多领取优惠券
		userService.userResourceLock(user);
		Boolean flag = this.userFetchXrGift(user);
		
		// 不需要再领取
		if (flag) {
			return null;
		}
		
		// 领取新人礼包
		PromotionUse promotionUse = new PromotionUse();
		promotionUse.setPromotionId(Promotion.XR_ID);
		promotionUse.setUserId(user.getId());
		promotionUse.setUserName(user.getName());
		promotionUse.setCreateDate(DateUtils.getTimeStampNow());
		if (StringUtils.isNotBlank(recommendId) && RegexpUtil.isNumber(recommendId)
				&& !recommendId.equals(user.getId().toString())){
			promotionUse.setRecommendId(Long.parseLong(recommendId));
			promotionUse.setIsReturn(Promotion.NO);
		}
		this.useDao.insert(promotionUse);
		
		// 分配优惠券
		List<PromotionCoupon> promotionCoupons = this.queryCouponByPromotionId(Promotion.XR_ID);
		List<CouponCode> lockedCouponCodes = Lists.newArrayList();
		for(PromotionCoupon item : promotionCoupons){
			CouponCode couponCode = this.couponService.assignOneCode(item.getCoupons(), user);
		    if (couponCode != null) {
		    	lockedCouponCodes.add(couponCode);
		    }
		}
		
		// 分配用户为试用会员 -- 可以指定会员（现在不需要）
		userRankService.upgradeRank(user, null);
		
		// 返回分配的优惠券
		return lockedCouponCodes;
	}
	
	/**
	 * 发放邀请礼包
	 */
	@Transactional
	public void fetchYqGifts(Long userId) {
		
		// 判断促销活动的有效期
		Promotion promotion = PromotionUtils.getCachedPromotion(Promotion.YQ_ID);
		if (promotion == null || promotion.getUseAble() != Promotion.NO) {
			return;
		}
		
		// 锁住用户，防止并发，导致用户多领取优惠券
		User user = new User();
		user.setId(userId);
		userService.userResourceLock(user);
		
		// 新人礼包(没领取、没推荐人、已反现)
		PromotionUse promotionUse = new PromotionUse();
		promotionUse.setPromotionId(Promotion.XR_ID);
		promotionUse.setUserId(userId);
		promotionUse = this.useDao.queryForObject("getUserPromotion", promotionUse);
		if (promotionUse == null || promotionUse.getRecommendId() == null
				|| (promotionUse.getIsReturn() != null && promotionUse.getIsReturn() == 1) ) {
			return;
		}
        
		// 保存为已修改
		this.useDao.update("updateReturn", promotionUse);
		
		// 分配优惠券给推荐用户
		User rUser = userService.get(promotionUse.getRecommendId());
		if (rUser != null) {
			List<PromotionCoupon> promotionCoupons = this.queryCouponByPromotionId(Promotion.YQ_ID);
			for(PromotionCoupon item : promotionCoupons){
				this.couponService.assignOneCode(item.getCoupons(), rUser);
			}
		}
	}
	
	/**
	 * 没设置促销商品(需要查询出未开始的)
	 * 未开始的不参与缓存，但参与计算缓存的时间
	 * 不要直接调用这个
	 * @see com.tmt.shop.utils.PromotionUtils queryProductAllEnabledPromotions
	 */
	@Deprecated
	@Override
	public List<Promotion> queryGlobalEnabledPromotions() {
		Map<String, Object> params = Maps.newHashMap();
		params.put("now", DateUtils.getTimeStampNow());
		return this.queryForList("queryGlobalEnabledPromotions", params);
	}

	/**
	 * 设置了促销商品(需要查询出未开始的)
	 * 未开始的不参与缓存，但参与计算缓存的时间
	 * @see com.tmt.shop.utils.PromotionUtils queryProductAllEnabledPromotions
	 */
	@Deprecated
	@Override
	public List<Promotion> queryProductEnabledPromotions(Long productId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("now", DateUtils.getTimeStampNow());
		params.put("productId", productId);
	    List<Promotion> promotions = this.queryForList("queryProductEnabledPromotions", params);
	    if (!promotions.isEmpty()) {
	    	for(Promotion promotion: promotions) {
	    		List<PromotionProduct> products = this.queryProductByPromotionId(promotion.getId());
	    		Map<Long, PromotionProduct> ps = Maps.newHashMap();
				for(PromotionProduct p: products) {
					ps.put(p.getProducts(), p);
				}
	    		promotion.setPs(ps);
	    	}
	    }
		return promotions;
	}
	
	/**
	 * 发放红包(下单发放红包)
	 * @param id -- 订单ID
	 * @return
	 */
	@Transactional
	public GiveRedenvelopeResult giveRedenvelope(User user, Long orderId) {
		
		// 用户资源锁
		this.userService.userResourceLock(user);
				
		// 订单相关的满赠送 -- 已经赠送过了或其他问题(如果有多个则每次只处理一个)
		OrderPromotion op = this.orderService.getOrderGiveAbleMzsPromotion(orderId);
		if (op == null || (op.getIsGive() != null && op.getIsGive() == 1)) {
			return null;
		}
		
		// 真实的促销 -- 促销的可用性，有有效期的
		Promotion promotion = PromotionUtils.getCachedPromotion(op.getPromotions());
		if (promotion == null || promotion.getUseAble() != Promotion.NO) { // 状态为0 是可用
			return GiveRedenvelopeResult.fail("活动已结束");
		}
		
		// 订单的可获取红包(防止过期 -- 前端还需判断促销是否过期)
		Order order = this.orderService.get(orderId);
		if (order == null || !order.isGiveRedenvelopeable() || !user.getId().equals(order.getCreateId())) {
			return GiveRedenvelopeResult.fail("下单人才能领取红包！");
		}
		
		// 防止订单状态改变
		this.orderService.lock(order);
		
		// 可用性
		Byte enable = order.getOrderStatus() == OrderStatus.completed?Promotion.YES:Promotion.NO;
		
		// 分配优惠券(这些优惠券是暂时不能使用的)
		List<PromotionCoupon> promotionCoupons = this.queryCouponByPromotionId(promotion.getId());
		GiveRedenvelopeResult result = GiveRedenvelopeResult.fail("超过领取限制");
		for(PromotionCoupon item : promotionCoupons){
			CouponCode couponCode = this.couponService.assignOneCode_supportFission(item.getCoupons(), user, enable);
			result.addCoupon(couponCode);
			op.addCoupon(couponCode);
		}
		
		// 设置为已获取优惠
		op.setFissions(result.getFissions());
		op.setIsGive(Promotion.YES);
		op.setReduce(BigDecimal.valueOf(result.getPrice())); // 满增型的为赠送的优惠券的金额
		this.orderService.giveMzsPromotion(op);
		return result;
	}
	
	/**
	 * 前端显示需要
	 */
	@Override
	public Promotion getShowPromotion(Long id) {
		return getWithProduct(id);
	}
	
	// 修改
	private void _update(Promotion promotion, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(promotion.getId());
		updateData.setModule(ShopModule.PROMOTION);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
}