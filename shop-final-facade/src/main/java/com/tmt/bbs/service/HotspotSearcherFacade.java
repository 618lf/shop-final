package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.HotSpot;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;

/**
 * 动态热点
 * @author lifeng
 */
public interface HotspotSearcherFacade {

	/**
	 * 得到指定内容的HotSpot
	 * @param id
	 * @return
	 */
	public HotSpot get(Long id);
	
	/**
	 * 动态热点
	 * @param query
	 * @return
	 */
	public Page page(ScorePage page);
	
	
	/**
	 * 刷新索引(批量更新，效率更高)
	 */
	public void refresh(List<Long> updates, Byte module);
	
	/**
	 * 重建索引
	 */
	public void hotspots_build();
	
	/**
	 * 删除
	 * @param deletes
	 */
	public void _delete(List<Long> deletes);
	
}
