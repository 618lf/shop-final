package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.DeliveryCorp;

/**
 * 物流公司 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Repository("shopDeliveryCorpDao")
public class DeliveryCorpDao extends BaseDaoImpl<DeliveryCorp,Long> {}
