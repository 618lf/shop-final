package com.tmt.shop.dao; 

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.GoodsSpecification;
import com.tmt.shop.entity.GoodsSpecificationOption;

/**
 * 产品规格 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
@Repository("shopGoodsSpecificationDao")
public class GoodsSpecificationDao extends BaseDaoImpl<GoodsSpecification,Long> {
	
	@Autowired
	private GoodsSpecificationOptionDao specificationOptionDao;
	
	/**
	 * 判断是否有规格
	 * @param categoryId
	 * @return
	 */
	public Boolean hasSpecification(Long goodsId) {
		int count = this.countByCondition("countByGoodsId", goodsId);
		return count>0;
	}
	
	/**
	 * 保存规格
	 * @param specifications
	 */
	public void save(Goods goods) {
		List<GoodsSpecificationOption> options = Lists.newArrayList();
		List<GoodsSpecification> specifications = goods.getSpecifications();
		int i = 1;
		for(GoodsSpecification specification : specifications) {
			specification.setGoodsId(goods.getId());
			specification.setSort(i++);
			List<GoodsSpecificationOption> _options = specification.getOptions();
			int j = 1;
			for(GoodsSpecificationOption option: _options) {
				if(option.getSupport() == 1) {
				   option.setGoodsId(goods.getId());
				   option.setSpecificationsId(specification.getSpecificationsId());
				   option.setSort(j++);
				   options.add(option);
				}
			}
		}
		this.batchInsert(specifications);
		this.specificationOptionDao.batchInsert(options);
	}
	
	/**
	 * 获取分类规格
	 * @param categoryId
	 * @param goodsId
	 * @return
	 */
	public List<GoodsSpecification> queryByCategoryId(Long categoryId) {
	    List<GoodsSpecification> specifications = this.queryForList("queryByCategoryId", categoryId);
	    for(GoodsSpecification specification: specifications) {
	    	List<GoodsSpecificationOption> options = specificationOptionDao.queryForList("queryBySpecificationsId", specification.getSpecificationsId());
	    	specification.setOptions(options);
	    }
	    return specifications;
	}
	
	/**
	 * 获取商品规格
	 * @param categoryId
	 * @param goodsId
	 * @return
	 */
	public List<GoodsSpecification> queryByGoodsId(Long goodsId) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("GOODS_ID", goodsId);
	    List<GoodsSpecification> specifications = this.queryForList("queryByGoodsId", goodsId);
	    for(GoodsSpecification specification: specifications) {
	    	params.put("SPECIFICATIONS_ID", specification.getSpecificationsId());
	    	List<GoodsSpecificationOption> options = specificationOptionDao.queryForList("queryBySpecificationsIdAndGoodsId", params);
	    	specification.setOptions(options);
	    }
	    return specifications;
	}
}
