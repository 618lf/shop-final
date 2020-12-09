package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.PromotionUse;

/**
 * 促销使用 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopPromotionUseDao")
public class PromotionUseDao extends BaseDaoImpl<PromotionUse,Long> {}
