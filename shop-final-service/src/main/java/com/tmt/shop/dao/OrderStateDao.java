package com.tmt.shop.dao; 

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.OrderState;

/**
 * 订单状态 管理
 * @author 超级管理员
 * @date 2016-10-06
 */
@Repository("shopOrderStateDao")
public class OrderStateDao extends BaseDaoImpl<OrderState,Long> {
	
	
	/**
	 * 批量新增 
	 * @Title: batchInsert 
	 * @Description: 批量新增 
	 * @return void    返回类型 
	 */
	public void updateEnabled(final List<OrderState> entitys){
		final List<OrderState> tempList = Lists.newArrayList();
    	for(OrderState t: entitys) {
    		tempList.add(t);
    		if (tempList.size() == 15) {
    		    this.getSqlRunner().update(getStatementName("updateEnabled"), tempList);
    		    tempList.clear();
    		}
    	}
    	if (tempList.size() > 0) {
    	    this.getSqlRunner().update(getStatementName("updateEnabled"), tempList);
    	}
	}
	
	/**
	 * 批量新增 
	 * @Title: batchInsert 
	 * @Description: 批量新增 
	 * @return void    返回类型 
	 */
	@Override
	public void batchUpdate(final List<OrderState> entitys){
		final List<OrderState> tempList = Lists.newArrayList();
    	for(OrderState t: entitys) {
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
	public List<OrderState> queryUpdateAbles(int size) {
		OrderState param = new OrderState();
		param.setExpire(DateUtils.getTimeStampNow());
		List<OrderState> updates = this.queryForLimitList("queryUpdateAbles", param, size);
		this.batchUpdate(updates);
		return updates;
	}
}
