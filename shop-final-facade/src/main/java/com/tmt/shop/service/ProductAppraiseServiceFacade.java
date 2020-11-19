package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.ProductAppraise;
import com.tmt.shop.entity.ProductAppraiseContent;

/**
 * 商品评价 管理
 * @author 超级管理员
 * @date 2017-04-10
 */
public interface ProductAppraiseServiceFacade extends BaseServiceFacade<ProductAppraise,Long> {
	
	/**
	 * 得到明细
	 * @param orderId
	 * @return
	 */
	public ProductAppraiseContent getContent(Long orderId);
	
	/**
	 * 保存
	 */
	public void save(ProductAppraise productAppraise);
	
	
	/**
	 * 删除
	 */
	public void delete(List<ProductAppraise> productAppraises);
	
	/**
	 * 查询订单的已评价商品信息
	 * @param orderId
	 * @return
	 */
	public List<ProductAppraise> queryByOrder(Long orderId);
	
	/**
	 * 赠送积分
	 * @param appraise
	 */
	public Boolean addPoints(ProductAppraise appraise, Boolean mode);
}