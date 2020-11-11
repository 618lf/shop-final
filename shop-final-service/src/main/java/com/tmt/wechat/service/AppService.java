package com.tmt.wechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.wechat.dao.AppDao;
import com.tmt.wechat.entity.App;

/**
 * 微信公众号 管理
 * @author 超级管理员
 * @date 2016-09-04
 */
@Service("wechatAppService")
public class AppService extends BaseService<App,String> implements AppServiceFacade{
	
	@Autowired
	private AppDao appDao;
	
	@Override
	protected BaseDao<App, String> getBaseDao() {
		return appDao;
	}
	
	/**
	 * 保存(ID不能修改)
	 */
	@Transactional
	public void save(App app) {
		App _app = this.get(app.getId());
		if (_app != null) {
			this.update(app);
		} else {
			this.insert(app);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<App> apps) {
		this.batchDelete(apps);
	}
	
	/**
	 * 根据域名获取
	 */
	@Override
	public App getDomain(String domain) {
		App app = this.queryForObject("getDomain", domain);
		if (app == null) {
			app = this.queryForObject("getDefault", "");
		}
		return app;
	}

	/**
	 * 根据微信号获取
	 */
	@Override
	public App getEvent(String wxnum) {
		App app = this.queryForObject("getEvent", wxnum);
		if (app == null) {
			app = this.queryForObject("getDefault", "");
		}
		return app;
	}
}
