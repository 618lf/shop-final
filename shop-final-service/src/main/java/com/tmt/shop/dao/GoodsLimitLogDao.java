package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsLimitLog;

/**
 * 商品限购记录 管理
 * @author 超级管理员
 * @date 2016-11-05
 */
@Repository("shopGoodsLimitLogDao")
public class GoodsLimitLogDao extends BaseDaoImpl<GoodsLimitLog,Long> {}
