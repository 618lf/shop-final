package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsSpecificationOption;

/**
 * 产品规格选项 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
@Repository("shopGoodsSpecificationOptionDao")
public class GoodsSpecificationOptionDao extends BaseDaoImpl<GoodsSpecificationOption,Long> {}