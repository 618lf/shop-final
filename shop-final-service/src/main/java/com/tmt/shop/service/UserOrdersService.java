package com.tmt.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.UserOrdersDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.UserOrders;

/**
 * 下单统计(需要统计消费的金额 -- 以后再统计)
 * @author 超级管理员
 * @date 2016-12-08
 */
@Service("shopUserOrdersService")
public class UserOrdersService extends BaseService<UserOrders,Long> {
	
	@Autowired
	private UserOrdersDao userOrdersDao;
	@Autowired
	private PromotionService promotionService;
	
	@Override
	protected BaseDao<UserOrders, Long> getBaseDao() {
		return userOrdersDao;
	}
	
	/**
	 * 用户的下单已完成
	 */
	@Transactional
	public void complete(Order order) {
		
		// 是否首次下单
		boolean notFirst = this.exists(order.getCreateId());
		
		// 是首次下单 -- 新人礼包发送推荐邀请
		if (!notFirst) {
			promotionService.fetchYqGifts(order.getCreateId());
		}
		
		// 累加数据
		UserOrders stat = new UserOrders();
		stat.setUserId(order.getCreateId());
		stat.setOrders(1);
		this.insert(stat);
	}
}