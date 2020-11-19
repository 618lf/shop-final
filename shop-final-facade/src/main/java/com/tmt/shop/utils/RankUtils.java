package com.tmt.shop.utils;

import java.math.BigDecimal;
import java.util.List;

import com.tmt.common.utils.BigDecimalUtil;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.service.RankServiceFacade;
import com.tmt.shop.service.UserRankServiceFacade;
import com.tmt.system.entity.User;

/**
 * 等级计算
 * @author lifeng
 */
public class RankUtils {

	
	/**
	 * 通过等级标识获取等级
	 * @param grade
	 * @return
	 */
	public static Rank getRank(Byte grade) {
		List<Rank> ranks = RankUtils.getRanks();
		for(Rank rank: ranks) {
			if (rank.getGrade() == grade) {
				return rank;
			}
		}
		return null;
	}
	
	
	/**
	 * 所有的等级
	 * @return
	 */
	public static Rank getRank(Long rankId) {
		if(rankId == null) {return null;}
		String key = new StringBuilder(ShopConstant.SHOP_RANKS).append(rankId).toString();
		Rank rank = CacheUtils.getSysCache().get(key);
		if (rank == null) {
			RankServiceFacade rankService = SpringContextHolder.getBean(RankServiceFacade.class);
			rank = rankService.get(rankId);
			if (rank != null) {
				rank.setCoupons(rankService.queryRichCouponByRankId(rank.getId()));
				rank.initCoupons();
				CacheUtils.getSysCache().put(key, rank);
			}
		}
		return rank;
	}
	
	
	/**
	 * 所有的等级
	 * @return
	 */
	public static List<Rank> getRanks() {
		String key = new StringBuilder(ShopConstant.SHOP_RANKS).append("ALL").toString();
		List<Rank> ranks = CacheUtils.getSysCache().get(key);
		if (ranks == null) {
			RankServiceFacade rankService = SpringContextHolder.getBean(RankServiceFacade.class);
			ranks = rankService.getAll();
			if (ranks != null) {
				for(Rank rank: ranks) {
					rank.setCoupons(rankService.queryRichCouponByRankId(rank.getId()));
					rank.initCoupons();
				}
				CacheUtils.getSysCache().put(key, ranks);
			}
		}
		return ranks;
	}
	
	/**
	 * 清除缓存
	 */
	public static void clearCache() {
		String key = new StringBuilder(ShopConstant.SHOP_RANKS).append("*").toString();
		CacheUtils.getSysCache().delete(key);
	}
	
	/**
	 * 计算等级
	 * @return
	 */
	public static Rank calculateRank(int points) {
		List<Rank> ranks = RankUtils.getRanks();
		// 满足积分的升级方式，
		for(Rank rank: ranks) {
			if (rank.getAutoUpgrade() == Rank.YES && rank.getMinPoints() != null && rank.getMaxPoints() != null
				    && rank.getMinPoints().compareTo(points) <=0 && rank.getMaxPoints().compareTo(points) > 0) {
				return rank;
			}
		}
		
		// 或者是免费且不过期的 --- 不支持自动升级
		for(Rank rank: ranks) {
			if (rank.getAutoUpgrade() == Rank.NO && BigDecimalUtil.equalZERO(rank.getPrice())
					&& rank.getValidDays() == null) {
				return rank;
			}
		}
		
		// 没有可用的等级
		return null;
	}
	
	/**
	 * 计算折扣
	 * @return
	 */
	public static void calcula(Order order, User user) {
		// 商品的总金额(销售价格) * rank
		BigDecimal amount = order.getPrice();
		
		// 等级
		Rank rank = null;
		
		// 用户折扣
		UserRankServiceFacade userRankService = SpringContextHolder.getBean(UserRankServiceFacade.class);
		UserRank userRank = userRankService.getSimple(user.getId());
		if (userRank != null && userRank.getRankId() != null
				&& (rank = RankUtils.getRank(userRank.getRankId())) != null
				&& rank.getDiscount() != null && rank.getDiscount().compareTo(1.0) < 0) { // 1 没啥意义
			BigDecimal rankDiscount = BigDecimalUtil.mul(amount, BigDecimal.valueOf(1- rank.getDiscount()));
			order.setRankDiscount(rankDiscount);
			order.setRank(rank);
		}
	}
}
