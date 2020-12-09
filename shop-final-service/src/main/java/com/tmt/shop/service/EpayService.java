package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.EpayDao;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.utils.EpayUtils;

/**
 * 支付账户 管理
 * @author 超级管理员
 * @date 2015-09-23
 */
@Service("shopEpayService")
public class EpayService extends BaseService<Epay,Long> implements EpayServiceFacade{
	
	@Autowired
	private EpayDao epayDao;
	
	@Override
	protected BaseDaoImpl<Epay, Long> getBaseDao() {
		return epayDao;
	}
	
	/**
	 * 保存 -- 只能更新，不能添加
	 */
	@Transactional
	public void save(Epay epay) {
		if (IdGen.isInvalidId(epay.getId())) {
			this.insert(epay);
		} else {
			this.update(epay);
		}
		EpayUtils.clearCache();
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Epay> epays) {
		this.batchDelete(epays);
		EpayUtils.clearCache();
	}
	
	/**
	 * 查询前端可用的在线支付方式
	 * @return
	 */
	public List<Epay> queryUseableEpays() {
		return this.queryForList("queryUseableEpays", "");
	}
}
