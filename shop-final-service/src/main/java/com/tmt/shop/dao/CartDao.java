package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Cart;

/**
 * 购物车 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopCartDao")
public class CartDao extends BaseDaoImpl<Cart,Long> {}