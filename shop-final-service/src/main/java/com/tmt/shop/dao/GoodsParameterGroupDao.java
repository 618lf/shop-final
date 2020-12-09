package com.tmt.shop.dao; 

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.utils.Lists;
import com.tmt.shop.entity.GoodsParameter;
import com.tmt.shop.entity.GoodsParameterGroup;

/**
 * 产品参数组 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
@Repository("shopGoodsParameterGroupDao")
public class GoodsParameterGroupDao extends BaseDaoImpl<GoodsParameterGroup,Long> {
	
	@Autowired
	private GoodsParameterDao parameterDao;

	/**
	 * 查询商品参数组
	 * @param goodsId
	 * @return
	 */
	public List<GoodsParameterGroup> queryParameterGroupsByGoodsId(Long goodsId) {
		List<GoodsParameterGroup> groups = this.queryForList("queryParameterGroupsByGoodsId", goodsId);
		for(GoodsParameterGroup group: groups) {
			List<GoodsParameter> parameters = this.parameterDao.queryGoodsParameterByGroupId(group.getId());
			group.setParameters(parameters);
		}
		return groups;
	}
	
	/**
	 * 根据商品删除参数
	 * @param goodsId
	 */
	public void delete(Long goodsId) {
		//删除参数组
		GoodsParameterGroup group = new GoodsParameterGroup();
		group.setGoodsId(goodsId);
		this.delete("deleteByGoodsId", group);
		//删除参数
		GoodsParameter parameter  = new GoodsParameter(); 
		parameter.setGoodsId(goodsId);
		this.parameterDao.delete("deleteByGoodsId", parameter);
	}
	
	/**
	 * 保存
	 * @param groups
	 */
	public void save(List<GoodsParameterGroup> groups) {
		this.batchInsert(groups);
		//设置ID
		List<GoodsParameter> parameters = Lists.newArrayList();
		for(GoodsParameterGroup group: groups) {
			List<GoodsParameter> _parameters = group.getParameters();
			int i = 1;
			for(GoodsParameter parameter: _parameters) {
				parameter.setGoodsId(group.getGoodsId());
				parameter.setParameterGroupId(group.getId());
				parameter.setSort(i++);
			}
			parameters.addAll(group.getParameters());
		}
		this.parameterDao.batchInsert(parameters);
	}
}
