package com.tmt.system.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.Area;

/**
 * 区域的服务接口
 * @author root
 */
public interface AreaServiceFacade extends BaseServiceFacade<Area, Long>{

	/**
	 * 保存
	 * @param area
	 * @return
	 */
	public Long save(Area area);
	
	/**
	 * 根据code获取数据
	 * @param code
	 * @return
	 */
	public Area getByCode(String code);
	
	/**
	 * 通过父区域查询所有的子区域
	 * @param parent
	 * @return
	 */
	public List<Area> findByParent(Area parent);
	
	/**
	 * 通过父区域查询所有的子区域
	 * @param parent
	 * @return
	 */
	public List<Area> findAllParents(Long areaId);
	
	/**
	 * 删除前的校验
	 * @param entity
	 * @return
	 */
	public int deleteAreaCheck(Area entity);
	
	/**
	 * 删除
	 * @param areas
	 */
	public void delete(List<Area> areas);
	
	/**
	 * 查询当前层次下的数据
	 * @return
	 */
	public List<Area> queryAreasByLevel(Integer level, Long parentId, String name);
	
	/**
	 * 显示level层次下的数据
	 * @return
	 */
	public List<Area> queryAreasByBeforeLevel(Integer level, Long parentId);
	
	/**
	 * 批量导入
	 * @param areas
	 */
	public void batchImport(List<Area> areas);
}
