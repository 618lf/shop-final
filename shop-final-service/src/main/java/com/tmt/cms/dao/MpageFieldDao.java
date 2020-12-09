package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.MpageField;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 页面组件 管理
 * @author 超级管理员
 * @date 2016-11-04
 */
@Repository("cmsMpageFieldDao")
public class MpageFieldDao extends BaseDaoImpl<MpageField,Long> {}
