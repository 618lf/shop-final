package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.PromotionExt;

/**
 * 多级促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Repository("shopPromotionExtDao")
public class PromotionExtDao extends BaseDaoImpl<PromotionExt,Long> {}
