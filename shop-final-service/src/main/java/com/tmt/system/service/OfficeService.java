package com.tmt.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.Constants;
import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.CacheUtils;
import com.tmt.system.dao.OfficeDao;
import com.tmt.system.entity.Office;

@Service
public class OfficeService extends BaseService<Office, Long> implements OfficeServiceFacade {

	@Autowired
	private OfficeDao officeDao;

	@Override
	protected BaseDao<Office, Long> getBaseDao() {
		return this.officeDao;
	}

	@Transactional
	public Long save(Office office) {
		Office parent = this.get(office.getParentId());
		String oldParentIds = office.getParentIds();
		String oldPath = office.getPath();
		Integer oldLevel = office.getLevel();
		office.fillByParent(parent);
		if (IdGen.isInvalidId(office.getId())) {
			this.insert(office);
		} else {
			this.update(office);
			List<Office> children = this.findByParent(office);
			for (Office o : children) {
				o.updateIdsByParent(office, oldParentIds, oldPath, oldLevel);
			}
			this.batchUpdate(children);
			removeCache(office);
		}
		return office.getId();
	}

	/**
	 * 查询子节点
	 * 
	 * @param parent
	 * @return
	 */
	public List<Office> findByParent(Office parent) {
		return this.findByParent(parent.getId());
	}

	/**
	 * 查询子节点
	 * 
	 * @param parent
	 * @return
	 */
	public List<Office> findByParent(Long parentId) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("PARENT_ID", parentId);
		return this.queryByCondition(qc);
	}

	/**
	 * 查询子节点
	 * 
	 * @param parent
	 * @return
	 */
	public List<Office> findAllByParent(Long parentId) {
		return this.queryForList("findAllChildrenByParent", parentId);
	}

	public List<TreeVO> findTreeList(Map<String, Object> params) {
		return this.queryForGenericsList("findTreeList", params);
	}

	/**
	 * 获取office 通过code
	 * 
	 * @param code
	 * @return
	 */
	public Office getByCode(String code) {
		return this.queryForObject("findByCode", code);
	}

	/**
	 * 删除
	 */
	@Transactional
	public Boolean delete(List<Office> offices) {
		Boolean bFalg = Boolean.TRUE;
		for (Office office : offices) {
			int iCount = this.countByCondition("deleteOfficeCheck", office);
			if (iCount > 0) {
				bFalg = Boolean.FALSE;
				break;
			}
		}
		if (!bFalg) {
			return Boolean.FALSE;
		}
		this.batchDelete(offices);
		return Boolean.TRUE;
	}

	/**
	 * 添加缓存
	 * 
	 * @param key
	 * @param value
	 */
	public void putCache(Office office) {
		String key = new StringBuilder(Constants.CACHE_OFFICE).append(office.getCode()).toString();
		CacheUtils.put(key, office);
	}

	/**
	 * 删除缓存
	 * 
	 * @param key
	 * @param value
	 */
	public void removeCache(Office office) {
		String key = new StringBuilder(Constants.CACHE_OFFICE).append(office.getCode()).toString();
		CacheUtils.evict(key);
	}
}