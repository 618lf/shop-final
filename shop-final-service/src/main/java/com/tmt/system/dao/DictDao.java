package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.Dict;

@Repository
public class DictDao extends BaseDaoImpl<Dict, Long>{}