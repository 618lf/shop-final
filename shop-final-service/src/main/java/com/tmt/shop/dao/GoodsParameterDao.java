package com.tmt.shop.dao; 

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsParameter;

/**
 * 产品参数 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
@Repository("shopGoodsParameterDao")
public class GoodsParameterDao extends BaseDaoImpl<GoodsParameter,Long> {
	
	public List<GoodsParameter> queryGoodsParameterByGroupId(Long groupId) {
		return this.queryForList("queryGoodsParameterByGroupId", groupId);
	}
}