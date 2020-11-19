package com.tmt.shop.utils;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionProduct;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.promotion.PromotionHandler;
import com.tmt.shop.promotion.PromotionWrap;
import com.tmt.shop.promotion.impl.ByPromotionHandler;
import com.tmt.shop.promotion.impl.ComplexPromotionHandler;
import com.tmt.shop.promotion.impl.InitpromotionHandler;
import com.tmt.shop.promotion.impl.MjPromotionHandler;
import com.tmt.shop.promotion.impl.MzPromotionHandler;
import com.tmt.shop.promotion.impl.MzsPromotionHandler;
import com.tmt.shop.promotion.impl.QgPromotionHandler;
import com.tmt.shop.promotion.impl.TgPromotionHandler;
import com.tmt.shop.promotion.impl.ZjPromotionHandler;
import com.tmt.shop.promotion.impl.ZkPromotionHandler;
import com.tmt.shop.service.PromotionServiceFacade;
import com.tmt.system.entity.User;

/**
 * 促销相关
 * 
 * @author root
 */
public class PromotionUtils {

	/**
	 * 返回一个商品可用的促销，其实就是下面两个的合计
	 * 排序： 先取直接关联商品的，在取全局的，按照时间排序，但两部分是没按时间排序
	 * @param productId
	 * @return
	 */
	public static Map<Long, Promotion> queryProductAllEnabledPromotions(Long productId) {
		//System.out.println("当前时间：" + DateUtil3.getTodayStr("yyyy-MM-dd HH:mm:ss"));
		Map<Long, Promotion> promotions = PromotionUtils.queryProductEnabledPromotions(productId);
		promotions.putAll(PromotionUtils.queryGlobalEnabledPromotions());
		return promotions;
	}

	/**
	 * 获取全局的可用促销（与具体商品无关的） 设置缓存的时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Map<Long, Promotion> queryGlobalEnabledPromotions() {
		String key = ShopConstant.GLOBAL_PROMOTIONS;
		Map<Long, Promotion> promotions = CacheUtils.getSysCache().get(key);
		if (promotions == null) {
			promotions = Maps.newOrderMap();
			PromotionServiceFacade promotionService = SpringContextHolder.getBean(PromotionServiceFacade.class);
			List<Promotion> _promotions = promotionService.queryGlobalEnabledPromotions();

			// 缓存并获取数据
			cachePromotions(promotions, key, _promotions);
			
		}
		return promotions;
	}

	/**
	 * 获取商品相关的可用缓存（与具体商品有关的） 设置缓存的时间
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static Map<Long, Promotion> queryProductEnabledPromotions(Long productId) {
		String key = new StringBuilder(ShopConstant.GOODS_PROMOTIONS).append(productId).toString();
		Map<Long, Promotion> promotions = CacheUtils.getSysCache().get(key);
		if (promotions == null) {
			promotions = Maps.newOrderMap();
			PromotionServiceFacade promotionService = SpringContextHolder.getBean(PromotionServiceFacade.class);
			List<Promotion> _promotions = promotionService.queryProductEnabledPromotions(productId);

			// 缓存并获取数据
			cachePromotions(promotions, key, _promotions);
		}
		return promotions;
	}
	
	// 缓存
	private static void cachePromotions(Map<Long, Promotion> promotions, String key, List<Promotion> datas) {
		// 设置一个最小的过期时间
		long min = ShopConstant.MAX_SECONDS_PROMOTIONS;
		for (Promotion promotion : datas) {
			
			long _min = promotion.getExpirySeconds();
			
			// 所有取出来的数据参与时间的比较
			if (min > _min) {
				min = _min;
			}
			
			// 但只存储可用的
			if (promotion.getUseAble() == 0) {
				promotions.put(promotion.getId(), promotion);
			}
		}
		
		// （也没有即将开始的数据）没有数据 -- 缓存10秒(修改缓存机制后无数据可以缓存时间长一点)
		if (datas.size() == 0) {
			min = ShopConstant.MAX_SECONDS_PROMOTIONS;
		}

		// System.out.println("当前时间：" + DateUtil3.getTodayStr("yyyy-MM-dd HH:mm:ss") + "缓存：" + min + "秒");
		// 设置最长的生效时间
		CacheUtils.getSysCache().put(key, promotions, (int) min);
	}
	
	/**
	 * 获取缓存中数据
	 * @param promotionId
	 * @return
	 */
	public static Promotion getCachedPromotion(Long promotionId) {
		String key = new StringBuilder(ShopConstant.GOODS_PROMOTIONS).append(promotionId).toString();
		Promotion promotion = CacheUtils.getSysCache().get(key);
		if (promotion == null) {
			PromotionServiceFacade promotionService = SpringContextHolder.getBean(PromotionServiceFacade.class);
			promotion = promotionService.getWithExt(promotionId);
			if (promotion != null) {
				List<PromotionProduct> products = promotionService.queryProductByPromotionId(promotion.getId());
				Map<Long, PromotionProduct> ps = Maps.newHashMap();
				for(PromotionProduct p: products) {
					ps.put(p.getProducts(), p);
				}
				promotion.setPs(ps);
				
				long min = promotion.getExpirySeconds();
				
				min = ShopConstant.MAX_SECONDS_PROMOTIONS > min? min: ShopConstant.MAX_SECONDS_PROMOTIONS;
				
				// 设置最长的生效时间
				CacheUtils.getSysCache().put(key, promotion, (int) min);
			}
		}
		return promotion;
	}
	
	/**
	 * 获取缓存中数据
	 * @param promotionId
	 * @return
	 */
	public static Promotion getComplexPromotion(Long promotionId) {
		Complex complex  =ComplexUtils.getComplex(promotionId);
		if (complex != null) {
			Promotion promotion = new Promotion();
			promotion.setId(complex.getId());
			promotion.setName(complex.getName());
			promotion.setType(Promotion.TZ);
			promotion.setReduce(complex.getPrefer());
			Date today = DateUtil3.getTodayDate();
			promotion.setBeginDate(today);
			promotion.setEndDate(DateUtil3.getDateByOffset(today, 1));
			promotion.setIsEnabled(Promotion.YES);
			promotion.setRemarks(StringUtil3.format("优惠%s元", complex.getPrefer()));
			return promotion;
		}
		return null;
	}

	/**
	 * 删除缓存
	 */
	public static void clearCache() {
		String key = ShopConstant.GLOBAL_PROMOTIONS;
		CacheUtils.getSysCache().delete(key);
	}

	/**
	 * 删除指定商品相关的产品
	 * 或指定促销
	 * @param productId
	 */
	public static void clearCache(Long productId) {
		String key = new StringBuilder(ShopConstant.GOODS_PROMOTIONS).append(productId).toString();
		CacheUtils.getSysCache().delete(key);
	}

	// 折扣处理器
	private static PromotionHandler handler;
	
	static {
		handler = new InitpromotionHandler();
		PromotionHandler zj = new ZjPromotionHandler(); // 直减
		PromotionHandler mj = new MjPromotionHandler(); // 满减
		PromotionHandler mz = new MzPromotionHandler(); // 满折
		PromotionHandler zk = new ZkPromotionHandler(); // 折扣
		PromotionHandler by = new ByPromotionHandler(); // 包邮
		PromotionHandler tg = new TgPromotionHandler(); // 团购
		PromotionHandler qg = new QgPromotionHandler(); // 抢购
		PromotionHandler mzs = new MzsPromotionHandler();// 满赠送
		PromotionHandler tz = new ComplexPromotionHandler();// 套装
		handler.setNextHandler(zj);
		zj.setNextHandler(mj);
		mj.setNextHandler(mz);
		mz.setNextHandler(zk);
		zk.setNextHandler(by);
		by.setNextHandler(tg);
		tg.setNextHandler(qg);
		qg.setNextHandler(mzs);
		mzs.setNextHandler(tz);
	}

	/**
	 * 计算订单
	 * 
	 * @param order
	 * @return
	 */
	public static Order calcula(Order order, User user) {
		if (order == null) {return order;}
		PromotionWrap wrap = new PromotionWrap();
		wrap.setOrder(order); wrap.setUser(user); order.setPromotions(Maps.newOrderMap());
		List<OrderItem> items = order.getItems();
		if (items != null) {
			for(OrderItem item : items) {
				
				// 是否有优惠信息
				Long promotionId = item.getPromotions();
				if (promotionId == null) {
					continue;
				}
				
				// 获取优惠
                Promotion promotion = null;
				if (item.getGoodsId() == null) {
					promotion = PromotionUtils.getComplexPromotion(promotionId);
				} else {
					promotion = PromotionUtils.getCachedPromotion(promotionId);
				}
				
				// 没选择优惠信息
				if (promotion == null) {
					continue;
				}
				
				wrap.setPromotion(promotion);
				wrap.setItem(item);
				handler.doHandler(wrap);
			}
		}
		return order;
	}
}