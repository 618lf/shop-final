package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.Group;

@Repository
public class GroupDao extends BaseDaoImpl<Group, Long>{}