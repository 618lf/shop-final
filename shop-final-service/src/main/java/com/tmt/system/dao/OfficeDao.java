package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.Office;

@Repository
public class OfficeDao extends BaseDaoImpl<Office, Long>{}