package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.BrandDao;
import com.tmt.shop.entity.Brand;

/**
 * 品牌管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopBrandService")
public class BrandService extends BaseService<Brand,Long> implements BrandServiceFacade{
	
	@Autowired
	private BrandDao brandDao;
	
	@Override
	protected BaseDao<Brand, Long> getBaseDao() {
		return brandDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Brand brand) {
		if( IdGen.isInvalidId(brand.getId()) ) {
			this.insert(brand);
		} else {
			this.update(brand);
		}
	}
	
	
    @Transactional
	public void updateSort(List<Brand> brands ) {
		this.batchUpdate("updateSort", brands);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Brand> brands) {
		this.batchDelete(brands);
	}
}