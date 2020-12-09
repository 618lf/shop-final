package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.AdPosition;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 广告管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Repository("cmsAdPositionDao")
public class AdPositionDao extends BaseDaoImpl<AdPosition,Long> {}
