package com.tmt.cms.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.cms.dao.CategoryDao;
import com.tmt.cms.entity.Category;
import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Maps;

/**
 * 文章分类 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Service("cmsCategoryService")
public class CategoryService extends BaseService<Category,Long> implements CategoryServiceFacade{
	
	@Autowired
	private CategoryDao categoryDao;
	
	@Override
	protected BaseDao<Category, Long> getBaseDao() {
		return categoryDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Category category) {
        Category parent = this.get(category.getParentId());
        String oldParentIds = category.getParentIds();
		Integer oldLevel = category.getLevel(); 
		String oldPath = category.getPath();
		category.fillByParent(parent);
		if(IdGen.isInvalidId(category.getId())) {
			this.insert(category);
		} else {
			this.update(category);
            List<Category> children = this.findByParent(category);
			for( Category e : children ) {
				e.updateIdsByParent(category, oldParentIds, oldPath, oldLevel);
			}
			this.batchUpdate(children);
		}
	}
	
    public List<Category> findByParent(Category parent){
		Map<String,Object> params = Maps.newHashMap();
		params.put("PARENT_IDS",  parent.getId());
		return this.queryForList("findByCondition", params);
	}
    public List<Category> findByCondition(Map<String,Object> params) {
		return this.queryForList("findByCondition", params);
	}
	public List<TreeVO> findTreeList(Map<String,Object> params) {
		return this.queryForGenericsList("findTreeList", params);
	}
	public int deleteCategoryCheck(Category category){
		return this.countByCondition("deleteCategoryCheck", category);
	}
	
    @Transactional
	public void updateSort(List<Category> categorys ) {
		this.batchUpdate("updateSort", categorys);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Category> categorys) {
		this.batchDelete(categorys);
	}
	
}
