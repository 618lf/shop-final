package com.tmt.shop.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.utils.Lists;
import com.tmt.shop.entity.NoticeTask;

/**
 * 通知任务 管理
 * @author 超级管理员
 * @date 2016-10-05
 */
@Repository("shopNoticeTaskDao")
public class NoticeTaskDao extends BaseDaoImpl<NoticeTask,Long> {
	
	/**
	 * 批量新增 
	 * @Title: batchInsert 
	 * @Description: 批量新增 
	 * @return void    返回类型 
	 */
	@Override
	public void batchDelete(final List<NoticeTask> entitys){
		final List<NoticeTask> tempList = Lists.newArrayList();
    	for(NoticeTask t: entitys) {
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
	public void batchUpdate(final List<NoticeTask> entitys){
		final List<NoticeTask> tempList = Lists.newArrayList();
    	for(NoticeTask t: entitys) {
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
	 * 批量新增 
	 * @Title: batchInsert 
	 * @Description: 批量新增 
	 * @return void    返回类型 
	 */
	public void updateReset(final List<NoticeTask> entitys){
		final List<NoticeTask> tempList = Lists.newArrayList();
    	for(NoticeTask t: entitys) {
    		tempList.add(t);
    		if (tempList.size() == 15) {
    		    this.getSqlRunner().update(getStatementName("updateReset"), tempList);
    		    tempList.clear();
    		}
    	}
    	if (tempList.size() > 0) {
    	    this.getSqlRunner().update(getStatementName("updateReset"), tempList);
    	}
	}
	
	/**
	 * 需要更新的数据
	 * @param sql
	 * @param size
	 * @return
	 */
	public List<NoticeTask> queryUpdateAbles(int size) {
		List<NoticeTask> updates = this.queryForLimitList(FIND_BY_CONDITION, new Object(), size);
		this.batchUpdate(updates);
		return updates;
	}
}
