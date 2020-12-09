package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.shop.dao.OrderPostageDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderPostage;
import com.tmt.shop.entity.UserRank;

/**
 * 订单包邮 管理
 * @author 超级管理员
 * @date 2017-05-26
 */
@Service("shopOrderPostageService")
public class OrderPostageService extends BaseService<OrderPostage,Long> {
	
	@Autowired
	private OrderPostageDao orderPostageDao;
	
	@Override
	protected BaseDao<OrderPostage, Long> getBaseDao() {
		return orderPostageDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(UserRank rank, Order order) {
		if (BigDecimalUtil.biggerThenZERO(order.getPostageDiscount())) {
			OrderPostage postage = new OrderPostage();
			postage.setId(order.getId());
			postage.setPostages(rank.getUserId()); // 会员id
			postage.setPostagesAnme(rank.getRankName()); // 会员等级
			postage.setReduce(order.getPostageDiscount());
			this.insert(postage);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<OrderPostage> orderPostages) {
		this.batchDelete(orderPostages);
	}
	
}
