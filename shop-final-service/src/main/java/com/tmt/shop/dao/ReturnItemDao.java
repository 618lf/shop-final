package com.tmt.shop.dao; 

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.ReturnItem;

/**
 * 退货明细 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopReturnItemDao")
public class ReturnItemDao extends BaseDaoImpl<ReturnItem,Long> {
	
	/**
	 * 根据货运信息查询详细的信息
	 * @return
	 */
	public List<ReturnItem> queryItemsByReturnsId(Long returnsId) {
		return this.queryForList("queryItemsByReturnsId", returnsId);
	}
}