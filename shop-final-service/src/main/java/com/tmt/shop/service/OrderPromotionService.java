package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.dao.OrderPromotionDao;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.CouponFission;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderPromotion;
import com.tmt.shop.entity.PromotionMini;
import com.tmt.system.entity.User;

/**
 * 订单相关优惠
 * @author root
 */
@Service
public class OrderPromotionService extends BaseService<OrderPromotion,Long> implements OrderPromotionServiceFacade{

	@Autowired
	private OrderPromotionDao promotionDao;
	@Autowired
	private CouponService couponService;
	
	@Override
	protected BaseDao<OrderPromotion, Long> getBaseDao() {
		return promotionDao;
	}
	
	/**
	 * 保存
	 * @param order
	 */
	@Transactional
	public void save(Order order) {
		if (order.getPromotions() != null && !order.getPromotions().isEmpty()) {
			List<PromotionMini> minis = Lists.newArrayList(order.getPromotions().values());
			List<OrderPromotion> promotions = Lists.newArrayList();
			for(PromotionMini item: minis) {
				if (item.getPromotionValid())  {
					OrderPromotion promotion = new OrderPromotion();
					promotion.setPromotions(item.getId());
					promotion.setOrders(order.getId());
					promotion.setReduce(item.getReduce());
					promotion.setPromotionName(item.getName());
					promotion.setPromotionType(item.getType());
					promotions.add(promotion);
				}
			}
			this.batchInsert(promotions);
		}
	}
	
	/**
	 * 相关的促销活动
	 * @param orderId
	 * @return
	 */
	public List<OrderPromotion> queryOrderPromotions(Long orderId) {
		return this.queryForList("queryOrderPromotions", orderId);
	}
	
	/**
	 * 可赠送的促销活动， 每次只获取一个
	 * @return
	 */
	public int hasOrderGiveAbleQgPromotion(User user, Long promotionId) {
		Map<String, Long> params = Maps.newHashMap();
		params.put("users", user.getId());
		params.put("promotions", promotionId);
		return this.countByCondition("hasOrderGiveAbleQgPromotion", params);
	}
	
	/**
	 * 可赠送的促销活动， 每次只获取一个
	 * @return
	 */
	public int hasOrderGiveAbleMzsPromotion(Long orderId) {
		return this.countByCondition("hasOrderGiveAbleMzsPromotion", orderId);
	}
	
	/**
	 * 可赠送的促销活动， 每次只获取一个
	 * @return
	 */
	public OrderPromotion getOrderGiveAbleMzsPromotion(Long orderId) {
		return this.queryForObject("getOrderGiveAbleMzsPromotion", orderId);
	}
	
	/**
	 * 返回订单已领取的促销
	 * @param orderId
	 * @return
	 */
	public List<OrderPromotion> queryOrderGivedMzsPromotions(Long orderId) {
		return this.queryForList("queryOrderGivedMzsPromotions", orderId);
	}
	
	/**
	 * 返回订单已领取的分享促销
	 * @param orderId
	 * @return
	 */
	public List<OrderPromotion> queryOrderGivedFissionPromotion(Long orderId) {
		return this.queryForList("queryOrderGivedFissionPromotion", orderId);
	}
	
	/**
	 * 设置为已经发放
	 */
	@Transactional
	public void updateGiveMzsPromotion(OrderPromotion op) {
		this.update("updateGiveMzsPromotion", op);
	}
	
	/**
	 * 用户的下单已完成
	 */
	@Transactional
	public void complete(Order order) {
		List<OrderPromotion> giveds = this.queryOrderGivedMzsPromotions(order.getId());
		List<CouponFission> fissions = Lists.newArrayList();
		List<CouponCode> coupons = Lists.newArrayList();
		for(OrderPromotion give: giveds) {
			
			// 分裂相关
			if (give.getFissions() != null) {
				CouponFission newFission = new CouponFission();
				newFission.setId(give.getFissions());
				fissions.add(newFission);
			}
			
			// 直送相关
			if (StringUtils.isNotBlank(give.getCoupons())) {
				String[] ids = give.getCoupons().split(",");
				for(String id: ids) {
					if (StringUtils.isNotBlank(id)) {
						CouponCode code = new CouponCode();
						code.setId(Long.parseLong(id));
						coupons.add(code);
					}
				}
			}
		}
		
		// 设置为可使用
		couponService.enableFissions(fissions);
		couponService.enableCoupons(coupons);
	}
}