package com.tmt.system.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.Maps;
import com.tmt.system.dao.AreaDao;
import com.tmt.system.entity.Area;

/**
 * 区域管理
 * 
 * @author root
 */
@Service
public class AreaService extends BaseService<Area, Long> implements AreaServiceFacade {

	@Autowired
	private AreaDao areaDao;

	@Override
	protected BaseDao<Area, Long> getBaseDao() {
		return areaDao;
	}

	@Override
	@Transactional
	public Long save(Area area) {
		String oldParentIds = area.getParentIds();
		String oldPath = area.getPath();
		Integer oldLevel = area.getLevel();
		Area parent = this.get(area.getParentId());
		area.fillByParent(parent);
		if (IdGen.isInvalidId(area.getId())) {
			this.insert(area);
		} else {
			this.update(area);
			// 应该得到所有的子节点
			List<Area> children = this.findByParent(area);
			for (Area o : children) {
				o.updateIdsByParent(area, oldParentIds, oldPath, oldLevel);
			}
			this.batchUpdate(children);
		}
		return area.getId();
	}

	/**
	 * 根据code获取数据
	 * 
	 * @param code
	 * @return
	 */
	@Override
	public Area getByCode(String code) {
		return this.queryForObject("getByCode", code);
	}

	/**
	 * 通过父区域查询所有的子区域
	 * 
	 * @param parent
	 * @return
	 */
	@Override
	public List<Area> findByParent(Area parent) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andLike("PARENT_IDS", parent.getId());
		return this.queryByCondition(qc);
	}

	/**
	 * 通过父区域查询所有的子区域
	 * 
	 * @param parent
	 * @return
	 */
	@Override
	public List<Area> findAllParents(Long areaId) {
		return this.queryForList("queryAllParents", areaId);
	}

	/**
	 * 删除前的校验
	 * 
	 * @param entity
	 * @return
	 */
	@Override
	public int deleteAreaCheck(Area entity) {
		return this.countByCondition("deleteAreaCheck", entity);
	}

	/**
	 * 删除
	 * 
	 * @param areas
	 */
	@Override
	@Transactional
	public void delete(List<Area> areas) {
		this.batchDelete(areas);
	}

	/**
	 * 查询当前层次下的数据
	 * 
	 * @return
	 */
	@Override
	public List<Area> queryAreasByLevel(Integer level, Long parentId, String name) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("PARENT_ID", parentId);
		params.put("LEVEL", level);
		params.put("NAME", name);
		return this.queryForList("queryAreasByLevel", params);
	}

	/**
	 * 查询当前层次下的数据
	 * 
	 * @return
	 */
	@Override
	public List<Area> queryAreasByBeforeLevel(Integer level, Long parentId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("PARENT_ID", parentId);
		params.put("LEVEL", level);
		return this.queryForList("queryAreasByBeforeLevel", params);
	}

	/**
	 * 导入 id, pid, name 1 0 北京 code level
	 * 
	 * @param area
	 */
	@Override
	@Transactional
	public void batchImport(List<Area> areas) {
		for (Area area : areas) {
			area.setCode(String.valueOf(area.getId()));
			if (area.getParentId() == null) {
				area.setParentId(IdGen.ROOT_ID);
			}
			Area _area = this.getByCode(area.getCode());
			if (_area != null) {
				this._update(area);
			} else {
				this._insert(area);
			}
		}
	}

	// 插入
	private void _insert(Area area) {
		Area parent = this.get(area.getParentId());
		area.fillByParent(parent);
		this.insert(area);
	}

	// 修改
	private void _update(Area area) {
		String oldParentIds = area.getParentIds();
		String oldPath = area.getPath();
		Integer oldLevel = area.getLevel();
		Area parent = this.get(area.getParentId());
		area.fillByParent(parent);
		this.update(area);
		// 应该得到所有的子节点
		List<Area> children = this.findByParent(area);
		for (Area o : children) {
			o.updateIdsByParent(area, oldParentIds, oldPath, oldLevel);
		}
		this.batchUpdate(children);
	}
}