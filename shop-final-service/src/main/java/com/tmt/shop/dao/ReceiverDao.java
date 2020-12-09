package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Receiver;

/**
 * 会员地址 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopReceiverDao")
public class ReceiverDao extends BaseDaoImpl<Receiver,Long> {}