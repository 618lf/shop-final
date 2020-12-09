package com.tmt.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.cms.dao.MpageFieldDao;
import com.tmt.cms.dao.MtemplateDao;
import com.tmt.cms.entity.MpageField;
import com.tmt.cms.entity.Mtemplate;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;

/**
 * 页面模板 管理
 * @author 超级管理员
 * @date 2016-11-11
 */
@Service("cmsMtemplateService")
public class MtemplateService extends BaseService<Mtemplate,Long> implements MtemplateServiceFacade{
	
	@Autowired
	private MtemplateDao mtemplateDao;
	@Autowired
	private MpageFieldDao mpageFieldDao;
	
	@Override
	protected BaseDao<Mtemplate, Long> getBaseDao() {
		return mtemplateDao;
	}
	
	/**
	 * 带出组件
	 * @param id
	 * @return
	 */
	public Mtemplate getWithFields(Long id) {
		Mtemplate page = this.get(id);
		if (page != null) {
			page.setMfields(this.queryFieldsByTemplateId(id));
		}
		return page;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Mtemplate mtemplate) {
		if(IdGen.isInvalidId(mtemplate.getId())) {
			this.insert(mtemplate);
		} else {
			this.update(mtemplate);
		}
		
		// 保存组件
		this.saveFields(mtemplate);
	}
	
	/**
	 * 保存组件
	 */
	public void saveFields(Mtemplate mpage) {
		List<String> fields = mpage.getFields();
		List<MpageField> inserts = Lists.newArrayList();
		byte iSort = 1;
		for(String field: fields) {
			MpageField _field = new MpageField();
			_field.setConfig(field);
			_field.setPageId(mpage.getId());
			_field.setSort(iSort ++);
			inserts.add(_field);
		}
		List<MpageField> deletes = this.queryFieldsByTemplateId(mpage.getId());
		this.mpageFieldDao.batchDelete(deletes);
		this.mpageFieldDao.batchInsert(inserts);
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Mtemplate> mtemplates) {
		this.batchDelete(mtemplates);
		List<MpageField> fields = Lists.newArrayList();
		for(Mtemplate page: mtemplates) {
			fields.addAll(this.queryFieldsByTemplateId(page.getId()));
		}
		this.mpageFieldDao.batchDelete(fields);
	}
	
	/**
	 * 查询配置项
	 * @param pageId
	 * @return
	 */
	public List<MpageField> queryFieldsByTemplateId(Long templateId) {
		return this.mpageFieldDao.queryForList("queryByTemplateId", templateId);
	}
}