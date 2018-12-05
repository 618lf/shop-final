package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.Constants;
import com.tmt.common.lock.Handler;
import com.tmt.common.lock.impl.ResourceLockServiceFacade;
import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.CacheUtils;
import com.tmt.system.dao.DictDao;
import com.tmt.system.entity.Dict;

/**
 * @author lifeng
 */
@Service
public class DictService extends BaseService<Dict, Long> implements DictServiceFacade, ResourceLockServiceFacade {

	@Autowired
	private DictDao dictDao;

	@Override
	protected BaseDao<Dict, Long> getBaseDao() {
		return this.dictDao;
	}

	@Transactional
	public Long save(Dict dict) {
		if (IdGen.isInvalidId(dict.getId())) {
			this.insert(dict);
		} else {
			this.update(dict);
		}
		CacheUtils.getDictCache().clear();
		return dict.getId();
	}

	@Transactional
	public void delete(List<Dict> dicts) {
		this.batchDelete(dicts);
		CacheUtils.getDictCache().clear();
	}

	public Dict getDictByCode(String dictCode) {
		String key = new StringBuilder(Constants.C).append(dictCode).toString();
		Dict dict = CacheUtils.getDictCache().get(key);
		if (dict == null) {
			dict = this.dictDao.queryForObject("findByCode", dictCode);
			dict = dict == null ? new Dict() : dict;
			CacheUtils.getDictCache().put(key, dict);
		}
		return dict;
	}

	public int checkDictCode(Dict dict) {
		return this.countByCondition("checkDictCode", dict);
	}

	/**
	 * 最简单的资源锁 -- 但有时间的限制
	 */
	@Override
	@Transactional
	public <T> T lock(String resource, Handler handler) {
		Dict dict = new Dict();
		dict.setCode(resource);
		this.dictDao.lock(dict);
		if (handler != null) {
			return handler.doHandle();
		}
		return null;
	}
}