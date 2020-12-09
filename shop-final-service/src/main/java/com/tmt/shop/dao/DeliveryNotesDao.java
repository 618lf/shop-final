package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.DeliveryNotes;

/**
 * 物流信息 管理
 * @author 超级管理员
 * @date 2016-03-12
 */
@Repository("shopDeliveryNotesDao")
public class DeliveryNotesDao extends BaseDaoImpl<DeliveryNotes,Long> {}
