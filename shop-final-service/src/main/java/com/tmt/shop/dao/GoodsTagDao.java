package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsTag;

/**
 * 商品标签 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopGoodsTagDao")
public class GoodsTagDao extends BaseDaoImpl<GoodsTag,Long> {}