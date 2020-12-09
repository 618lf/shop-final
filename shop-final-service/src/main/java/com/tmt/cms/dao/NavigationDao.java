package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.Navigation;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 导航管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Repository("cmsNavigationDao")
public class NavigationDao extends BaseDaoImpl<Navigation,Long> {}
