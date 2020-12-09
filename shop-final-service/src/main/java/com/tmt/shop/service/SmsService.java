package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.SmsDao;
import com.tmt.shop.entity.Sms;

/**
 * 短信配置 管理
 * @author 超级管理员
 * @date 2017-09-12
 */
@Service("shopSmsService")
public class SmsService extends BaseService<Sms,Long> implements SmsServiceFacade {
	
	@Autowired
	private SmsDao smsDao;
	
	@Override
	protected BaseDao<Sms, Long> getBaseDao() {
		return smsDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Sms sms) {
		if(IdGen.isInvalidId(sms.getId())) {
			this.insert(sms);
		} else {
			this.update(sms);
		}
	}
	
	
    @Transactional
	public void updateSort(List<Sms> smss ) {
		this.batchUpdate("updateSort", smss);
	}
    
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Sms> smss) {
		this.batchDelete(smss);
	}
	
	/**
	 * 场景
	 * @param scene
	 * @return
	 */
	public Sms getByScene(Byte scene) {
		return this.queryForObject("getByScene", scene);
	}
}