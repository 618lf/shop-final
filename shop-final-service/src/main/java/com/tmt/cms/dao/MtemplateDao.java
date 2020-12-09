package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.Mtemplate;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 页面模板 管理
 * @author 超级管理员
 * @date 2016-11-11
 */
@Repository("cmsMtemplateDao")
public class MtemplateDao extends BaseDaoImpl<Mtemplate,Long> {}
