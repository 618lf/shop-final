package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsEvaluate;

/**
 * 产品评价 管理
 * @author 超级管理员
 * @date 2016-10-17
 */
@Repository("shopGoodsEvaluateDao")
public class GoodsEvaluateDao extends BaseDaoImpl<GoodsEvaluate,Long> {}
