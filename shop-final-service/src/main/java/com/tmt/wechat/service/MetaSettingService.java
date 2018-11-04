package com.tmt.wechat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.wechat.dao.MetaSettingDao;
import com.tmt.wechat.entity.MetaSetting;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 回复配置 管理
 * @author 超级管理员
 * @date 2016-09-27
 */
@Service("wechatMetaSettingService")
public class MetaSettingService extends BaseService<MetaSetting,Long> implements MetaSettingServiceFacade {
	
	@Autowired
	private MetaSettingDao metaSettingDao;
	
	@Override
	protected BaseDao<MetaSetting, Long> getBaseDao() {
		return metaSettingDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(MetaSetting metaSetting) {
		if (IdGen.isInvalidId(metaSetting.getId())) {
			this.insert(metaSetting);
		} else {
			this.update(metaSetting);
		}
		
		// 删除缓存
		WechatUtils.clearCache();
	}
	
	/**
	 * 根据APP_ID 获取配置
	 * @param appId
	 * @return
	 */
	public MetaSetting getByAppId(String appId) {
		return this.queryForObject("getByAppId", appId);
	}
}