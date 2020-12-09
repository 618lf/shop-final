package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Store;

/**
 * 店铺管理 管理
 * @author 超级管理员
 * @date 2017-01-10
 */
@Repository("shopStoreDao")
public class StoreDao extends BaseDaoImpl<Store,Long> {}
