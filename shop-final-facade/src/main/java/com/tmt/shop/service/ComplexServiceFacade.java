package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.ComplexProduct;

/**
 * 商品组合 管理
 * @author 超级管理员
 * @date 2016-11-28
 */
public interface ComplexServiceFacade extends BaseServiceFacade<Complex,Long> {
	
	/**
	 * 保存
	 */
	public void save(Complex complex);
	
	/**
	 * 删除
	 */
	public void delete(List<Complex> complexs);
	
	/**
	 * 查询商品
	 * @param complexId
	 * @return
	 */
	public List<ComplexProduct> queryProductsByComplexId(Long complexId);
	
	/**
	 * 查询商品(商品信息)
	 * @param complexId
	 * @return
	 */
	public List<ComplexProduct> queryRichProductsByComplexId(Long complexId);
	
	/**
	 * 商品相关的优惠套装
	 * @param productId
	 * @return
	 */
	public List<Long> queryProductComplexs(Long productId);
}
