package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Brand;

/**
 * 品牌管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface BrandServiceFacade extends BaseServiceFacade<Brand,Long> {
	
	/**
	 * 保存
	 */
	public void save(Brand brand);
	
	/**
	 * 排序
	 * @param brands
	 */
	public void updateSort(List<Brand> brands );
    
	/**
	 * 删除
	 */
	public void delete(List<Brand> brands);
	
}
