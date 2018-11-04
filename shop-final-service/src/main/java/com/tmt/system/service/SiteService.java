package com.tmt.system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.email.EmailParam;
import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.dao.SiteDao;
import com.tmt.system.entity.Site;
import com.tmt.system.entity.SystemConstant;

/**
 * 站点设置 管理
 * @author 超级管理员
 * @date 2016-01-18
 */
@Service
public class SiteService extends BaseService<Site,Long> implements SiteServiceFacade{
	
	@Autowired
	private SiteDao siteDao;
	
	@Override
	protected BaseDao<Site, Long> getBaseDao() {
		return siteDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void baseSave(Site site) {
		this.update("updateBase", site);
		CacheUtils.evict(SystemConstant.CACHE_SITE);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void safeSave(Site site) {
		this.update("updateSafe", site);
		CacheUtils.evict(SystemConstant.CACHE_SITE);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void emailSave(Site site) { 
		Site _site = this.getSite();
		if(!StringUtil3.isNotBlank(site.getSmtpPassword())) {
		   site.setSmtpPassword(_site.getSmtpPassword());
		}
		this.update("updateEmail", site);
		CacheUtils.evict(SystemConstant.CACHE_SITE);
		
		//Email 配置
		CacheUtils.put(EmailParam.EMAIL_KEY, _site.getEmailParam());
	}
	
	/**
	 * 得到站点配置
	 * @return
	 */
	public Site getSite() {
		Site site = CacheUtils.get(SystemConstant.CACHE_SITE);
		if(site == null) {
		   site = this.get(IdGen.ROOT_ID);
		   if(site != null) {
			  CacheUtils.put(SystemConstant.CACHE_SITE, site);
		   }
		}
		return site;
	}
}