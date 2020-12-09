package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.CartItem;

/**
 * 购物车明细 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopCartItemDao")
public class CartItemDao extends BaseDaoImpl<CartItem,Long> {}