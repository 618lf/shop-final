package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.EmailTask;

/**
 * 邮件任务
 * @author lifeng
 */
@Repository
public class EmailTaskDao extends BaseDaoImpl<EmailTask, Long>{}