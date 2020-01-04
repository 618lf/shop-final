package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.system.dao.ExcelItemDao;
import com.tmt.system.entity.ExcelItem;
import com.tmt.system.entity.ExcelTemplate;

@Service
public class ExcelItemService extends BaseService<ExcelItem, Long> {

	@Autowired
	private ExcelItemDao excelItemDao;

	@Override
	protected BaseDao<ExcelItem, Long> getBaseDao() {
		return this.excelItemDao;
	}

	@Transactional
	public void save(ExcelTemplate template) {
		List<ExcelItem> oldItems = this.findByTempleteId(template.getId());
		if (oldItems != null) {
			this.batchDelete(oldItems);
		}
		if (template.getItems() != null) {
			for (ExcelItem item : template.getItems()) {
				item.setTemplateId(template.getId());
			}
			this.batchInsert(template.getItems());
		}
	}

	@Transactional(readOnly = false)
	public void delete(List<ExcelItem> allItems) {
		this.batchDelete(allItems);
	}

	public List<ExcelItem> findByTempleteId(Long templateId) {
		return this.queryForList("findByTempleteId", templateId);
	}
}
