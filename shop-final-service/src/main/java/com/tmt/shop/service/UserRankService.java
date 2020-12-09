package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.Ints;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.dao.UserRankDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderPostage;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.RankCoupon;
import com.tmt.shop.entity.UserPoint;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.enums.PayStatus;
import com.tmt.shop.exception.OrderErrorException;
import com.tmt.shop.utils.RankUtils;
import com.tmt.system.entity.User;
import com.tmt.system.service.UserServiceFacade;

/**
 * 用户等级 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
@Service("shopUserRankService")
public class UserRankService extends BaseService<UserRank,Long> implements UserRankServiceFacade {
	
	@Autowired
	private UserRankDao userRankDao;
	@Autowired
	private CouponService couponService;
	@Autowired
	private UserServiceFacade userService;
	@Autowired
	private UserPointService pointService;
	@Autowired
	private OrderPostageService postageService;
	
	@Override
	protected BaseDao<UserRank, Long> getBaseDao() {
		return userRankDao;
	}
	
	/**
	 * 会初始化等级(前台获取)会自动回退过期的等级
	 */
	@Transactional
	public UserRank touch(Long userId) {
		UserRank rank = this.get(userId);
		if (rank == null) {
			this.initRank(userId);
			return this.get(userId);
		}
		// 过期了自动回到“普通会员”
		if (rank.getUseAble() == 0) { // 已过期
			this.backRank(userId);
			return this.get(userId);
		}
		return rank;
	}
	
	/**
	 * 初始化等级
	 * @return
	 */
	private void initRank(Long userId) {
		UserRank rank = this.queryForObject("getRankandLock", userId);
		if (rank != null) {return;}
		rank = new UserRank(); rank.setUserId(userId); rank.setPoints(0); this.insert(rank);
		// 升级到此等级
		Rank _rank = RankUtils.calculateRank(rank.getPoints());
		if (_rank != null && this.upgradeAble(null, _rank)) {
			this.upgradeRank(rank, _rank);
		}
	}
	
	/**
	 *  回退等级
	 * @param userId
	 */
	private void backRank(Long userId) {
		UserRank rank = this.queryForObject("getRankandLock", userId);
		Rank _rank = RankUtils.calculateRank(rank.getPoints());
		if (_rank != null && (rank.getRankId() == null || rank.getRankId().compareTo(_rank.getId()) != 0) 
			  && this.upgradeAble(null, _rank)) {
			// 只是改变等级，不发放福利
			rank.setRankId(_rank.getId());
			rank.setShipping(0);
			this.update("updateUpgrade", rank);
		}
	}
	
	/**
	 * 保存（后台赠送）
	 */
	@Transactional
	public void save(UserRank userRank) {
		// 用户当前的等级
		UserRank rank = this.getRankandLock(userRank.getUserId());
		Rank prank = null;
		if (rank != null) {
			prank = RankUtils.getRank(rank.getRankId());
		}
		
		// 修改等级信息
		if (rank == null) {
			this.insert(userRank);
		} else {
			this.update(userRank);
		}
		
		// 用户修改后的等级, 可以降级
		Rank mrank = RankUtils.getRank(userRank.getRankId());
		if (mrank != null && (prank == null || (prank.getId().compareTo(mrank.getId()) != 0))) {
			this.upgradeRank(userRank, mrank);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<UserRank> userRanks) {
		this.batchDelete(userRanks);
	}
	
	/**
	 * 订单完成时统计积分(付款了才赠送积分)
	 */
	@Transactional
	public void complete(Order order) {
		if (order.getPaymentStatus() == PayStatus.paid) {
			// 获得积分 
			int points = order.getRewardPoint();
			
			// 用户等级
			UserRank rank = this.getRankandLock(order.getCreateId());
			if (rank == null) {
				rank = new UserRank();
				rank.setUserId(order.getCreateId());
				rank.setPoints(0);
				this.insert(rank);
			}
			
			// 我的等级
			Rank myRank = RankUtils.getRank(rank.getRankId());
			
			// 当前按照旧等级计算(且在有效期内)
			if (myRank != null && rank.getUseAble() != 0) {
				points = (int)((myRank.getPoints() != null ? myRank.getPoints() : 1.0) * points);
			}
			// 添加积分
			rank.addPoints(points);
			
			// 是否可以升级的等级(设置需要购买的是不能自动升级的)
			Rank _rank = RankUtils.calculateRank(rank.getPoints());
			if (_rank != null && this.upgradeAble(myRank, _rank)) {
				this.upgradeRank(rank, _rank);
			}
			
			// 修改积分
			this.update("updatePoints", rank);
			
			// 积分明细服务
			UserPoint point = new UserPoint();
			point.setUserId(rank.getUserId());
			point.setPoints(points);
			point.setState(UserPoint.NO); // 进
			point.setName("购买商品");
			pointService.save(point);
		}
	}
	
	/**
	 * 评价结束时增加积分（也会触发等级的升级）
	 */
	@Transactional
	public void appraise(Long userId, Integer points) {
		UserRank rank = this.touch(userId);
		rank.addPoints(points);
		this.update("updatePoints", rank);
		
		// 积分明细服务
		UserPoint point = new UserPoint();
		point.setUserId(rank.getUserId());
		point.setPoints(points);
		point.setState(UserPoint.NO); // 进
		point.setName("评价商品");
		pointService.save(point);
		
		// 我的等级
		Rank myRank = RankUtils.getRank(rank.getRankId());
		
		// 是否可以升级的等级(设置需要购买的是不能自动升级的)
		Rank _rank = RankUtils.calculateRank(rank.getPoints());
		if (_rank != null && this.upgradeAble(myRank, _rank)) {
			this.upgradeRank(rank, _rank);
		}
	}
	
	/**
	 * 确认支付（购买）
	 */
	@Transactional
	public boolean confirmPay(Payment payment) {
		UserRank rank = new UserRank();
		rank.setUserId(payment.getCreateId());
		rank.setRankId(payment.getOrderId());
		Rank myRank = RankUtils.getRank(rank.getRankId());
		this.upgradeRank(rank, myRank);
		if (Ints.nullToZero(myRank.getValidDays()) >= 0) {
			rank.setEffectDate(DateUtils.getTodayDate());
			rank.setExpiryDate(DateUtils.addDays(DateUtils.getTodayDate(), myRank.getValidDays()));
		} else {
			rank.setEffectDate(null);
			rank.setExpiryDate(null);
		}
		this.update("updateUpgrade", rank);
		return true;
	}
	
	// 是否可以升级（不能降，只能升）
	private boolean upgradeAble(Rank myRank, Rank _rank) {
		// 之前没有等级，或有升级的等级
		if (myRank == null || (myRank.getId().compareTo(_rank.getId()) != 0
				&& myRank.getGrade() < _rank.getGrade())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	// 升级等级
	private void upgradeRank(UserRank rank, Rank _rank) {
		
		// 升级到此等级
		rank.setRankId(_rank.getId());
		
		// 设置包邮次数
		Integer shipping = _rank.getShipping();
		rank.setShipping(Ints.nullToZero(shipping));
		
		// 当前用户
		User user = userService.get(rank.getUserId());
		
		// 赠送优惠券
		List<RankCoupon> coupons = _rank.getCoupons();
		if (coupons != null && coupons.size() != 0) {
			for(RankCoupon coupon: coupons) {
				couponService.assignOneCode(coupon.getCoupons(), user);
			}
		}
		
		// 修改等级，并设置等级有效期
		if (_rank.getAutoUpgrade() == Rank.YES 
				|| (_rank.getAutoUpgrade() == Rank.NO && !BigDecimalUtil.biggerThenZERO(_rank.getPrice()))) {
			rank.setEffectDate(null);
			rank.setExpiryDate(null);
		} else if (Ints.nullToZero(_rank.getValidDays()) >= 0) {
			rank.setEffectDate(DateUtils.getTodayDate());
			rank.setExpiryDate(DateUtils.addDays(DateUtils.getTodayDate(), Ints.nullToZero(_rank.getValidDays())));
		} else { // 如果出现这种情况表示永久有效
			rank.setEffectDate(null);
			rank.setExpiryDate(null);
		}
		this.update("updateUpgrade", rank);
	}
	
	/**
	 * 简单模式
	 */
	@Override
	public UserRank getSimple(Long userId) {
		return this.queryForObject("getSimple", userId);
	}

	/**
	 * 得到并锁定
	 */
	@Override
	public UserRank getRankandLock(Long userId) {
		return this.queryForObject("getRankandLock", userId);
	}

	/**
	 * 升级到此等级（新人礼包赠送）
	 */
	@Override
	@Transactional
	public void upgradeRank(User user, Rank rank) {
		// 如果等级为空，则升级到试用会员
		if (rank == null) {
			rank = RankUtils.getRank(Rank.GRADE_TRY);
		}
		
		// 是否有等级
		UserRank urank = this.getRankandLock(user.getId());
		if (urank == null) {
			urank = new UserRank();
			urank.setUserId(user.getId());
			urank.setPoints(0);
			this.insert(urank);
		}
		
		// 我的等级
		Rank myRank = RankUtils.getRank(urank.getRankId());
		
		// 是否可以升级(只能升级)
		if (rank != null && this.upgradeAble(myRank, rank)) {
			this.upgradeRank(urank, rank);
		}
	}
	
	/**
	 * 下订单时
	 */
	@Transactional
	public void book(Order order) {
		UserRank rank = this.get(order.getCreateId());
		boolean hasRight = (order != null && BigDecimalUtil.biggerThenZERO(order.getFreight()) 
				&& rank != null && rank.getUseAble() != 0 && Ints.nullToZero(rank.getShipping()) != 0
				&& (Ints.nullToZero(rank.getShipping()) == -1 || rank.getRemainShipping() >= 1 ));
		if (hasRight && BigDecimalUtil.biggerThenZERO(order.getPostageDiscount())) {
			rank.setUseShipping(1);
			this.update("updateUseShipping", rank);
			this.postageService.save(rank, order);
		} else if(!hasRight && BigDecimalUtil.biggerThenZERO(order.getPostageDiscount())) {
			throw new OrderErrorException("会员包邮错误");
		}
	}
	
	/**
	 * 取消订单时
	 */
	@Transactional
	public void unUseShipping(Order order) {
		UserRank rank = this.getSimple(order.getCreateId());
		// 是当前用户使用的包邮
		OrderPostage postage = this.postageService.get(order.getId());
		if (rank != null && postage != null && postage.getPostages() != null 
				&& postage.getPostages().compareTo(order.getCreateId()) == 0) {
			rank.setUseShipping(-1);
			this.update("updateUseShipping", rank);
		}
	}
}