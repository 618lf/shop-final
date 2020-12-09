package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.EpaySafe;

/**
 * 企业支付安全设置 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
@Repository("shopEpaySafeDao")
public class EpaySafeDao extends BaseDaoImpl<EpaySafe,Long> {}
