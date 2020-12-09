package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.Mpage;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 页面 管理
 * @author 超级管理员
 * @date 2016-11-04
 */
@Repository("cmsMpageDao")
public class MpageDao extends BaseDaoImpl<Mpage,Long> {}
