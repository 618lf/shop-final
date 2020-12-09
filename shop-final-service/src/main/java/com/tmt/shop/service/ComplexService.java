package com.tmt.shop.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.shop.dao.ComplexDao;
import com.tmt.shop.dao.ComplexProductDao;
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.ComplexProduct;
import com.tmt.shop.utils.ComplexUtils;
import com.tmt.shop.utils.ProductUtils;

/**
 * 商品组合 管理
 * @author 超级管理员
 * @date 2016-11-28
 */
@Service("shopComplexService")
public class ComplexService extends BaseService<Complex,Long> implements ComplexServiceFacade{
	
	@Autowired
	private ComplexDao complexDao;
	@Autowired
	private ComplexProductDao productDao;
	
	@Override
	protected BaseDao<Complex, Long> getBaseDao() {
		return complexDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Complex complex) {
		if (complex.getType() == Complex.DEL_FLAG_AUDIT) {
			complex.setPrefer(BigDecimal.ZERO);
		}
		if(IdGen.isInvalidId(complex.getId())) {
			this.insert(complex);
		} else {
			this.update(complex);
		}
		this.saveProduct(complex);
		ComplexUtils.clearComplex(complex.getId());
	}
	
	private void saveProduct(Complex complex) {
		List<ComplexProduct> olds = this.queryProductsByComplexId(complex.getId());
		List<ComplexProduct> products = complex.getProducts();
		List<ComplexProduct> copys = Lists.newArrayList();
		for(ComplexProduct product: products) {
			product.setComplexId(complex.getId());
			if (product.getProductId() != null) {
				copys.add(product);
			}
		}
		this.productDao.batchDelete(olds);
		this.productDao.batchInsert(copys);
		
		// 清除所有相关的缓存
		for(ComplexProduct p: olds) {
			ComplexUtils.clearProduct(p.getProductId());
		}
		for(ComplexProduct p: products) {
			ComplexUtils.clearProduct(p.getProductId());
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Complex> complexs) {
		this.batchDelete(complexs);
	}
	
	/**
	 * 查询商品
	 * @param complexId
	 * @return
	 */
	public List<ComplexProduct> queryProductsByComplexId(Long complexId) {
		return this.productDao.queryForList("queryProductsByComplexId", complexId);
	}
	
	/**
	 * 查询商品(商品信息)
	 * @param complexId
	 * @return
	 */
	public List<ComplexProduct> queryRichProductsByComplexId(Long complexId) {
		List<ComplexProduct> products = this.queryProductsByComplexId(complexId);
		for(ComplexProduct product: products) {
			product.setProduct(ProductUtils.getProduct(product.getProductId()));
		}
		return products;
	}

	/**
	 * 商品相关的优惠套装
	 */
	@Override
	public List<Long> queryProductComplexs(Long productId) {
		return this.productDao.queryForGenericsList("queryProductComplexs", productId);
	}
}
