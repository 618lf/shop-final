package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.Task;

@Repository
public class TaskDao extends BaseDaoImpl<Task, Long> {}