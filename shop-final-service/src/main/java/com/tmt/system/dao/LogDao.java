package com.tmt.system.dao;

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.Log;

/**
 * 使用特定语言的优势来批量插入数据
 * 
 * @author lifeng
 */
@Repository
public class LogDao extends BaseDaoImpl<Log, Long> {
}