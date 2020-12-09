package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsLimit;

/**
 * 商品限购 管理
 * @author 超级管理员
 * @date 2016-11-05
 */
@Repository("shopGoodsLimitDao")
public class GoodsLimitDao extends BaseDaoImpl<GoodsLimit,Long> {}
