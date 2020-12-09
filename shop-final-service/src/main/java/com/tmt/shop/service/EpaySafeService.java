package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.EpaySafeDao;
import com.tmt.shop.entity.EpaySafe;

/**
 * 企业支付安全设置 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
@Service("shopEpaySafeService")
public class EpaySafeService extends BaseService<EpaySafe,Long> implements EpaySafeServiceFacade{
	
	@Autowired
	private EpaySafeDao epaySafeDao;
	
	@Override
	protected BaseDaoImpl<EpaySafe, Long> getBaseDao() {
		return epaySafeDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(EpaySafe epaySafe) {
		if( !this.exists(epaySafe.getId()) ) {
			this.insert(epaySafe);
		} else {
			this.update(epaySafe);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<EpaySafe> epaySafes) {
		this.batchDelete(epaySafes);
	}
	
}
