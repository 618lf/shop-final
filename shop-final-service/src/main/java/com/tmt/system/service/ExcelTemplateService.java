package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.Lists;
import com.tmt.system.dao.ExcelTemplateDao;
import com.tmt.system.entity.ExcelItem;
import com.tmt.system.entity.ExcelTemplate;
import com.tmt.system.service.ExcelTemplateServiceFacade;

@Service
public class ExcelTemplateService extends BaseService<ExcelTemplate,Long> implements ExcelTemplateServiceFacade{

	@Autowired
	private ExcelTemplateDao excelTemplateDao;
	@Autowired
	private ExcelItemService excelItemService;
	
	@Override
	public BaseDao<ExcelTemplate,Long> getBaseDao() {
		return this.excelTemplateDao;
	}
	
	@Transactional(readOnly = false)
	public Long save(ExcelTemplate template) {
		if(IdGen.isInvalidId(template.getId())) {
			this.insert(template);
		} else {
			this.update(template);
		}
		this.excelItemService.save(template);
		return template.getId();
	}
	
	@Transactional(readOnly = false)
	public void delete(List<ExcelTemplate> templates){
		List<ExcelItem> allItems  = Lists.newArrayList();
		for( ExcelTemplate template: templates){
			List<ExcelItem> oldItems = excelItemService.findByTempleteId(template.getId());
			allItems.addAll(oldItems);
		}
		this.batchDelete(templates);
		this.excelItemService.delete(allItems);
	}
	
	/**
	 * 查询模版和详情
	 * @param templateId
	 * @return
	 */
	public ExcelTemplate getWithItems(Long templateId){
		ExcelTemplate template = this.get(templateId);
		if(template != null) {
		   List<ExcelItem> items = excelItemService.findByTempleteId(templateId);
		   template.setItems(items);
 		}
		return template;
	}
	
	/**
	 * 通过目标类,查询模版列表
	 * @param className
	 * @return
	 */
	public List<ExcelTemplate> queryByTargetClass(String className){
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("ET.TARGET_CLASS", className);
		return this.queryByCondition(qc);
	}
	
	/**
	 * 通过类型，查询模板列表
	 * @param className
	 * @return
	 */
	public List<ExcelTemplate> queryByType(String type) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("ET.TYPE", type);
		return this.queryByCondition(qc);
	}
}
