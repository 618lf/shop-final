package com.tmt.shop.dao; 

import java.util.List;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.utils.Lists;
import com.tmt.shop.entity.Product;

/**
 * 商品库存 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopProductDao")
public class ProductDao extends BaseDaoImpl<Product,Long> {
	
	/**
	 * 高效的批量删除
	 */
	@Override
	public void batchDelete(List<Product> entitys) {
		final List<Product> tempList = Lists.newArrayList();
    	for(Product t: entitys) {
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
}