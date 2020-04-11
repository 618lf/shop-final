package com.sample.dao;

import com.sample.entity.Order;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.mapper.Mapper;

/**
 * 定义接口
 * 
 * @author lifeng
 */
@Mapper
public interface OrderDao extends BaseDao<Order, Long> {

}