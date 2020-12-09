package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsImage;

/**
 * 商品图片 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopGoodsImageDao")
public class GoodsImageDao extends BaseDaoImpl<GoodsImage,Long> {}