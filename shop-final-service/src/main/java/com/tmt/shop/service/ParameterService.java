package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.shop.dao.ParameterDao;
import com.tmt.shop.dao.ParameterOptionDao;
import com.tmt.shop.entity.Parameter;
import com.tmt.shop.entity.ParameterOption;

/**
 * 商品参数 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopParameterService")
public class ParameterService extends BaseService<Parameter,Long> implements ParameterServiceFacade{
	
	@Autowired
	private ParameterDao parameterDao;
	@Autowired
	private ParameterOptionDao optionDao;
	
	@Override
	protected BaseDaoImpl<Parameter, Long> getBaseDao() {
		return parameterDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Parameter parameter) {
		if( IdGen.isInvalidId(parameter.getId()) ) {
			this.insert(parameter);
		} else {
			this.update(parameter);
		}
		this.saveOptions(parameter); 
	}
	
	/**
	 * 分为添加和修改删除
	 * @param parameter
	 */
	@Transactional
	public void saveOptions(Parameter parameter) {
		List<ParameterOption> options = parameter.getOptions();
		List<ParameterOption> olds = this.queryByParameterId(parameter.getId());
		List<ParameterOption> inserts = Lists.newArrayList();
		List<ParameterOption> updates = Lists.newArrayList();
		List<ParameterOption> deletes = Lists.newArrayList();
		//设置序号
		int i = 1;
		for(ParameterOption option: options) {
			option.setParameterId(parameter.getId());
			option.setSort(i++);
		}
		//修改的和删除的
		for(ParameterOption old: olds) {
			Boolean found = Boolean.FALSE;
			for(ParameterOption option: options) {
				if(old.getId().equals(option.getId())) {
				   updates.add(option);  found = Boolean.TRUE;
				   break;
				}
			}
			if(!found) {
			   deletes.add(old);
			}
		}
		//新增的
		for(ParameterOption option: options) {
			Boolean found = Boolean.FALSE;
			for(ParameterOption old: olds) {
				if(old.getId().equals(option.getId())) {
				   found = Boolean.TRUE; break;
				}
			}
			if(!found) {
			   inserts.add(option);
			}
		}
		this.optionDao.batchDelete(deletes);
		this.optionDao.batchInsert(inserts);
		this.optionDao.batchUpdate(updates);
	}
	
    @Transactional
	public void updateSort(List<Parameter> parameters ) {
		this.batchUpdate("updateSort", parameters);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Parameter> parameters) {
		this.batchDelete(parameters);
	}
	
	/**
	 * 得到选项
	 * @param id
	 * @return
	 */
	public Parameter getWithOptions(Long id) {
		Parameter parameter = this.get(id);
		if( parameter != null) {
			List<ParameterOption> options = this.queryByParameterId(id);
			parameter.setOptions(options);
		}
		return parameter;
	}
	
	public List<ParameterOption> queryByParameterId(Long id) {
		return this.optionDao.queryForList("queryByParameterId", id);
	}
	
	/**
	 * 根据分类查询参数列表
	 * @param categoryId
	 * @return
	 */
	public List<Parameter> queryByCategoryId(Long categoryId) {
		return this.queryForList("queryByCategoryId", categoryId);
	}
}