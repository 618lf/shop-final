package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Goods;

/**
 * 商品管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopGoodsDao")
public class GoodsDao extends BaseDaoImpl<Goods,Long> {}
