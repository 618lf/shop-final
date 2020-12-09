package com.tmt.shop.dao; 

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.utils.Lists;
import com.tmt.shop.entity.OrderEvent;

/**
 * 订单事件 管理
 * @author 超级管理员
 * @date 2016-10-04
 */
@Repository("shopOrderEventDao")
public class OrderEventDao extends BaseDaoImpl<OrderEvent,Long> {
	
	/**
	 * 批量新增 
	 * @Title: batchInsert 
	 * @Description: 批量新增 
	 * @return void    返回类型 
	 */
	@Override
	public void batchDelete(final List<OrderEvent> entitys){
		final List<OrderEvent> tempList = Lists.newArrayList();
    	for(OrderEvent t: entitys) {
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
	public void batchUpdate(final List<OrderEvent> entitys){
		final List<OrderEvent> tempList = Lists.newArrayList();
    	for(OrderEvent t: entitys) {
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
	public List<OrderEvent> queryUpdateAbles(int size) {
		List<OrderEvent> updates = this.queryForLimitList(FIND_BY_CONDITION, new Object(), size);
		this.batchUpdate(updates);
		return updates;
	}
}