package com.tmt.api.dao;

import org.springframework.stereotype.Repository;

import com.tmt.api.entity.Document;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 接口 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Repository("apiDocumentDao")
public class DocumentDao extends BaseDaoImpl<Document,Long> {}
