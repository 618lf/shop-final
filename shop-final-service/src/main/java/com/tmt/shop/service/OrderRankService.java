package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.OrderRankDao;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderRank;
import com.tmt.shop.entity.Rank;

/**
 * 订单折扣 管理
 * @author 超级管理员
 * @date 2017-02-19
 */
@Service("shopOrderRankService")
public class OrderRankService extends BaseService<OrderRank,Long> {
	
	@Autowired
	private OrderRankDao orderRankDao;
	
	@Override
	protected BaseDao<OrderRank, Long> getBaseDao() {
		return orderRankDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Order order) {
		Rank rank = order.getRank();
		if (rank != null) {
			OrderRank _rank = new OrderRank();
			_rank.setId(order.getId());
			_rank.setRanks(rank.getId());
			_rank.setRanksAnme(rank.getName());
			_rank.setDiscount(rank.getDiscount());
			_rank.setReduce(order.getRankDiscount());
			this.insert(_rank);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<OrderRank> orderRanks) {
		this.batchDelete(orderRanks);
	}
	
}
