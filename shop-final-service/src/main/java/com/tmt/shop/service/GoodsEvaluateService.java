package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.GoodsEvaluateDao;
import com.tmt.shop.entity.GoodsEvaluate;

/**
 * 产品评价 管理
 * @author 超级管理员
 * @date 2016-10-17
 */
@Service("shopGoodsEvaluateService")
public class GoodsEvaluateService extends BaseService<GoodsEvaluate,Long> {
	
	@Autowired
	private GoodsEvaluateDao goodsEvaluateDao;
	
	@Override
	protected BaseDao<GoodsEvaluate, Long> getBaseDao() {
		return goodsEvaluateDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(GoodsEvaluate goodsEvaluate) {
		if(IdGen.isInvalidId(goodsEvaluate.getId())) {
			this.insert(goodsEvaluate);
		} else {
			this.update(goodsEvaluate);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<GoodsEvaluate> goodsEvaluates) {
		this.batchDelete(goodsEvaluates);
	}
	
}
