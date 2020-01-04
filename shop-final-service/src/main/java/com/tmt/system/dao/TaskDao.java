package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.Task;

@Repository
public class TaskDao extends BaseDaoImpl<Task, Long> {}