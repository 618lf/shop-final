package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.UserPoint;

/**
 * 积分明细 管理
 * @author 超级管理员
 * @date 2017-05-16
 */
@Repository("shopUserPointDao")
public class UserPointDao extends BaseDaoImpl<UserPoint,Long> {}
