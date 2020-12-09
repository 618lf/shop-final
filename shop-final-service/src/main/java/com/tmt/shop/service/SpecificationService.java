package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.shop.dao.SpecificationDao;
import com.tmt.shop.dao.SpecificationOptionDao;
import com.tmt.shop.entity.Specification;
import com.tmt.shop.entity.SpecificationOption;

/**
 * 商品规格 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopSpecificationService")
public class SpecificationService extends BaseService<Specification,Long> implements SpecificationServiceFacade{
	
	@Autowired
	private SpecificationDao specificationDao;
	@Autowired
	private SpecificationOptionDao optionDao;
	
	@Override
	protected BaseDao<Specification, Long> getBaseDao() {
		return specificationDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Specification specification) {
		if( IdGen.isInvalidId(specification.getId()) ) {
			this.insert(specification);
		} else {
			this.update(specification);
		}
		this.saveOptions(specification); 
	}
	
	/**
	 * 分为添加和修改删除
	 * @param Specification
	 */
	@Transactional
	public void saveOptions(Specification specification) {
		List<SpecificationOption> options = specification.getOptions();
		List<SpecificationOption> olds = this.queryBySpecificationId(specification.getId());
		List<SpecificationOption> inserts = Lists.newArrayList();
		List<SpecificationOption> updates = Lists.newArrayList();
		List<SpecificationOption> deletes = Lists.newArrayList();
		//设置序号
		int i = 1;
		for(SpecificationOption option: options) {
			option.setSpecificationId(specification.getId());
			option.setSort(i++);
		}
		//修改的和删除的
		for(SpecificationOption old: olds) {
			Boolean found = Boolean.FALSE;
			for(SpecificationOption option: options) {
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
		for(SpecificationOption option: options) {
			Boolean found = Boolean.FALSE;
			for(SpecificationOption old: olds) {
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
	public void updateSort(List<Specification> specifications ) {
		this.batchUpdate("updateSort", specifications);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Specification> specifications) {
		this.batchDelete(specifications);
	}
	
	/**
	 * 得到选项
	 * @param id
	 * @return
	 */
	public Specification getWithOptions(Long id) {
		Specification specification = this.get(id);
		if( specification != null) {
			List<SpecificationOption> options = this.queryBySpecificationId(id);
			specification.setOptions(options);
		}
		return specification;
	}
	
	public List<SpecificationOption> queryBySpecificationId(Long id) {
		return this.optionDao.queryForList("queryBySpecificationId", id);
	}
	
	/**
	 * 判断分类是否友规格
	 * @param categoryId
	 * @return
	 */
	public Boolean hasSpecification(Long categoryId) {
		int count = this.countByCondition("countByCategoryId", categoryId);
		return count>0;
	}
}
