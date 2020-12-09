package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.UserRank;

/**
 * 用户等级 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
@Repository("shopUserRankDao")
public class UserRankDao extends BaseDaoImpl<UserRank,Long> {}
