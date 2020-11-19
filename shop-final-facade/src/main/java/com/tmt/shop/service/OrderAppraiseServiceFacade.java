package com.tmt.shop.service;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.OrderAppraise;
import com.tmt.system.entity.User;

/**
 * 订单评价 管理
 * 
 * @author 超级管理员
 * @date 2017-04-10
 */
public interface OrderAppraiseServiceFacade extends BaseServiceFacade<OrderAppraise, Long> {

	/**
	 * 用户待评价列表
	 * @param userId
	 * @param param
	 * @return
	 */
	public Page queryUserAppraiseTask(Long userId, PageParameters param);
	
	/**
	 * 用户待追评列表
	 * @param userId
	 * @param param
	 * @return
	 */
	public Page queryUserRappraiseTask(Long userId, PageParameters param);
	
	/**
	 * 用户待已评价列表
	 * @param userId
	 * @param param
	 * @return
	 */
	public Page queryUserAppraisedTask(Long userId, PageParameters param);
	
	
	/**
	 * 用户待评价列表
	 * @param userId
	 * @param param
	 * @return
	 */
	public int countUserAppraiseTask(Long userId);
	
	/**
	 * 用户待追评列表
	 * @param userId
	 * @param param
	 * @return
	 */
	public int countUserRappraiseTask(Long userId);
	
	/**
	 * 用户已评价列表
	 * @param userId
	 * @param param
	 * @return
	 */
	public int countUserAppraisedTask(Long userId);
	
	/**
	 * 评价
	 * @param appraise
	 */
	public void appraise(User user, OrderAppraise appraise);
	
	/**
	 * 评价
	 * @param appraise
	 */
	public void rappraise(User user, OrderAppraise appraise);
}