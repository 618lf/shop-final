package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Rank;

/**
 * 等级设置 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
@Repository("shopRankDao")
public class RankDao extends BaseDaoImpl<Rank,Long> {}
