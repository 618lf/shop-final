package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.ProductNotify;

/**
 * 到货通知 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopProductNotifyDao")
public class ProductNotifyDao extends BaseDaoImpl<ProductNotify,Long> {}