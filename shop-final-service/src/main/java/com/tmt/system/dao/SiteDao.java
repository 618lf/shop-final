package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.Site;

@Repository
public class SiteDao extends BaseDaoImpl<Site,Long> {}