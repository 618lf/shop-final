package com.tmt.system.dao; 

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.system.entity.UpdateData;

/**
 * 数据更新 管理
 * @author 超级管理员
 * @date 2016-09-09
 */
@Repository("systemUpdateDataDao")
public class UpdateDataDao extends BaseDaoImpl<UpdateData,Long> {
	
	/**
	 * 批量新增 
	 * @Title: batchInsert 
	 * @Description: 批量新增 
	 * @return void    返回类型 
	 */
	@Override
	public void batchDelete(final List<UpdateData> entitys){
		final List<UpdateData> tempList = Lists.newArrayList();
    	for(UpdateData t: entitys) {
    		tempList.add(t);
    		if (tempList.size() == 15) {
    		    this.getSqlRunner().delete(getStatementName(DELETE), tempList);
    		    tempList.clear();
    		}
    	}
    	if (tempList.size() > 0) {
    	    this.getSqlRunner().delete(getStatementName(DELETE), tempList);
    	}
	}
	
	/**
	 * 批量新增 
	 * @Title: batchInsert 
	 * @Description: 批量新增 
	 * @return void    返回类型 
	 */
	@Override
	public void batchUpdate(final List<UpdateData> entitys){
		final List<UpdateData> tempList = Lists.newArrayList();
    	for(UpdateData t: entitys) {
    		tempList.add(t);
    		if (tempList.size() == 15) {
    		    this.getSqlRunner().update(getStatementName(UPDATE), tempList);
    		    tempList.clear();
    		}
    	}
    	if (tempList.size() > 0) {
    	    this.getSqlRunner().update(getStatementName(UPDATE), tempList);
    	}
	}
	
	/**
	 * 需要更新的数据
	 * @param sql
	 * @param size
	 * @return
	 */
	public List<UpdateData> queryUpdateAbles(int size) {
		List<UpdateData> updates = this.queryForLimitList(FIND_BY_CONDITION, new Object(), size);
		this.batchUpdate(updates);
		return updates;
	}
	
	/**
	 * 需要更新的数据 -- 指定模块
	 * @param sql
	 * @param size
	 * @return
	 */
	public List<UpdateData> queryModuleUpdateAbles(String modules, int size) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("MODULES", modules);
		List<UpdateData> updates = this.queryForLimitList("queryByModule", params, size);
		this.batchUpdate(updates);
		return updates;
	}
}