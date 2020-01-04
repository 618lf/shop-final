package com.tmt.system.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Maps;
import com.tmt.system.dao.LogDao;
import com.tmt.system.entity.Log;

/**
 * 访问记录服务 - 不需要事务
 * 
 * @author root
 */
@Service
public class LogService extends BaseService<Log, Long> implements LogServiceFacade {

	@Autowired
	private LogDao logDao;

	@Override
	protected BaseDao<Log, Long> getBaseDao() {
		return this.logDao;
	}

	/**
	 * 指定分钟之前的数据
	 * 
	 * @param second
	 * @param step
	 * @return
	 */
	public int countLogsAfterDate(int second, int step) {
		Long datetime = new Date().getTime() - second * 1000;
		Map<String, Date> params = Maps.newHashMap();
		params.put("start", new Date(datetime));
		params.put("end", new Date(datetime + step * 1000));
		return this.countByCondition("countLogsAfterDate", params);
	}

	/**
	 * 指定分钟之前的数据
	 * 
	 * @param second
	 * @param step
	 * @return
	 */
	@Override
	public int countUserAfterDate(int second, int step) {
		Long datetime = new Date().getTime() - second * 1000;
		Map<String, Date> params = Maps.newHashMap();
		params.put("start", new Date(datetime));
		params.put("end", new Date(datetime + step * 1000));
		return this.countByCondition("countUserAfterDate", params);
	}
}