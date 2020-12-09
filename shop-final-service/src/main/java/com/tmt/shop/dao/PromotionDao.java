package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Promotion;

/**
 * 促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopPromotionDao")
public class PromotionDao extends BaseDaoImpl<Promotion,Long> {}
