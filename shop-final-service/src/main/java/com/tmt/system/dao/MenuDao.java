package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.Menu;

@Repository
public class MenuDao extends BaseDaoImpl<Menu, Long>{}