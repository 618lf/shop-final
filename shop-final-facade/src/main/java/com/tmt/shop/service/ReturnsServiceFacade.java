package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Returns;
import com.tmt.system.entity.User;

/**
 * 退货管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface ReturnsServiceFacade extends BaseServiceFacade<Returns,Long> {
	
	/**
	 * 保存
	 */
	public void save(Order order, User user, Returns returns);
	
	/**
	 * 删除
	 */
	public void delete(List<Returns> returnss);
	
	/**
	 * 查询订单相关的退货记录
	 * @param orderId
	 * @return
	 */
	public List<Returns> queryReturnsByOrderId(Long orderId);
	
	/**
	 * 并查询明细
	 * @param id
	 * @return
	 */
	public Returns getWithItems(Long id);
}
