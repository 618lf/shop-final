package com.tmt.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.cms.dao.MpageDao;
import com.tmt.cms.dao.MpageFieldDao;
import com.tmt.cms.entity.Mpage;
import com.tmt.cms.entity.MpageField;
import com.tmt.cms.update.CmsModule;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;

/**
 * 页面 管理
 * @author 超级管理员
 * @date 2016-11-04
 */
@Service("cmsMpageService")
public class MpageService extends BaseService<Mpage,Long> implements MpageServiceFacade{
	
	@Autowired
	private MpageDao mpageDao;
	@Autowired
	private MpageFieldDao mpageFieldDao;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateDataService;
	
	@Override
	protected BaseDao<Mpage, Long> getBaseDao() {
		return mpageDao;
	}
	
	/**
	 * 带出组件
	 * @param id
	 * @return
	 */
	public Mpage getWithFields(Long id) {
		Mpage page = this.get(id);
		if (page != null) {
			page.setMfields(this.queryFieldsByPageId(id));
		}
		return page;
	}
	
	/**
	 * 复制的
	 */
	@Transactional
	public void copy(Mpage mpage) {
		this.insert(mpage);
		List<MpageField> inserts = mpage.getMfields();
		for(MpageField field: inserts) {
			field.setPageId(mpage.getId());
			field.setId(IdGen.INVALID_ID);
		}
		this.mpageFieldDao.batchInsert(inserts);
		
		// 记录数据更新
		_update(mpage, (byte)0);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Mpage mpage) {
		if (IdGen.isInvalidId(mpage.getId())) {
			this.insert(mpage);
		} else {
			this.update(mpage);
		}
		
		// 保存组件
		this.saveFields(mpage);
		
		// 记录数据更新
		_update(mpage, (byte)0);
	}
	
	/**
	 * 保存组件
	 */
	private void saveFields(Mpage mpage) {
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
		List<MpageField> deletes = this.queryFieldsByPageId(mpage.getId());
		this.mpageFieldDao.batchDelete(deletes);
		this.mpageFieldDao.batchInsert(inserts);
	}
	
	/**
	 * 修改排序
	 * @param mpages
	 */
    @Transactional
	public void updateSort(List<Mpage> mpages ) {
		this.batchUpdate("updateSort", mpages);
	}
    
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Mpage> mpages) {
		this.batchDelete(mpages);
		List<MpageField> fields = Lists.newArrayList();
		for(Mpage page: mpages) {
			fields.addAll(this.queryFieldsByPageId(page.getId()));
			
			// 记录数据更新
			_update(page, (byte)1);
		}
		this.mpageFieldDao.batchDelete(fields);
	}
	
	private void _update(Mpage mpage, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(mpage.getId());
		updateData.setModule(CmsModule.MPAGE);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
	
	/**
	 * 查询配置项
	 * @param pageId
	 * @return
	 */
	public List<MpageField> queryFieldsByPageId(Long pageId) {
		return this.mpageFieldDao.queryForList("queryByPageId", pageId);
	}
}