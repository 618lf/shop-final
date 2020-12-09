package com.tmt.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.Constants;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.CacheUtils;
import com.tmt.shop.dao.NoticeSettingDao;
import com.tmt.shop.entity.NoticeSetting;

/**
 * 通知设置 管理
 * @author 超级管理员
 * @date 2016-10-04
 */
@Service("shopNoticeSettingService")
public class NoticeSettingService extends BaseService<NoticeSetting,Byte> implements NoticeSettingServiceFacade{
	
	@Autowired
	private NoticeSettingDao noticeSettingDao;
	
	@Override
	protected BaseDao<NoticeSetting, Byte> getBaseDao() {
		return noticeSettingDao;
	}
	
	/**
	 * 修改配置
	 * @param setting
	 */
	@Transactional
	public void updateTmsg(NoticeSetting setting) {
		this.update("updateTmsg", setting);
		
		// 删除缓存
		String key = new StringBuilder(Constants.CACHE_MSG_CONFIG).append(setting.getId()).toString();
		CacheUtils.getSysCache().delete(key);
	}
	
	/**
	 * 修改配置
	 * @param setting
	 */
	@Transactional
	public void updateSitemsg(NoticeSetting setting) {
		this.update("updateSitemsg", setting);
		
		// 删除缓存
		String key = new StringBuilder(Constants.CACHE_MSG_CONFIG).append(setting.getId()).toString();
		CacheUtils.getSysCache().delete(key);
	}
	
	/**
	 * 修改配置
	 * @param setting
	 */
	@Transactional
	public void updateSmsg(NoticeSetting setting) {
		this.update("updateSmsg", setting);
		
		// 删除缓存
		String key = new StringBuilder(Constants.CACHE_MSG_CONFIG).append(setting.getId()).toString();
		CacheUtils.getSysCache().delete(key);
	}

	/**
	 * 得到消息配置缓存
	 */
	@Override
	public NoticeSetting getByType(Byte type) {
		String key = new StringBuilder(Constants.CACHE_MSG_CONFIG).append(type).toString();
		NoticeSetting setting = CacheUtils.getSysCache().get(key);
		if (setting == null) {
			setting = this.get(type);
		    CacheUtils.getSysCache().put(key, setting);
		}
		return setting;
	}

	/**
	 * 修改模板
	 */
	@Override
	public void updateTemplate(NoticeSetting setting) {
		this.update("updateTemplate", setting);
	}
}
