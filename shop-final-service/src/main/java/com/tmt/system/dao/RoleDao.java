package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.Role;

@Repository
public class RoleDao extends BaseDaoImpl<Role, Long> {}