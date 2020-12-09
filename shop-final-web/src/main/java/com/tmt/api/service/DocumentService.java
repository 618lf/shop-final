package com.tmt.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.api.dao.DocumentDao;
import com.tmt.api.entity.Document;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 接口 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Service("apiDocumentService")
public class DocumentService extends BaseService<Document,Long> {
	
	@Autowired
	private DocumentDao documentDao;
	
	@Override
	protected BaseDao<Document, Long> getBaseDao() {
		return documentDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Document document) {
		if(IdGen.isInvalidId(document.getId())) {
			this.insert(document);
		} else {
			this.update(document);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Document> documents) {
		this.batchDelete(documents);
	}
	
}
