package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.Site;

@Repository
public class SiteDao extends BaseDaoImpl<Site,Long> {}