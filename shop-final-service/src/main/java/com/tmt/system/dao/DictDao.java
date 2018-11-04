package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.Dict;

@Repository
public class DictDao extends BaseDaoImpl<Dict, Long>{}