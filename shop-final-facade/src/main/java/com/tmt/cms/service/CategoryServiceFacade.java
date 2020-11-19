package com.tmt.cms.service;

import java.util.List;
import java.util.Map;

import com.tmt.cms.entity.Category;
import com.tmt.core.entity.TreeVO;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 文章分类 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public interface CategoryServiceFacade extends BaseServiceFacade<Category,Long> {
	
	/**
	 * 保存
	 */
	public void save(Category category);
	
	/**
	 * 字节点
	 * @param parent
	 * @return
	 */
    public List<Category> findByParent(Category parent);
    
    /**
     * 条件查询
     * @param params
     * @return
     */
    public List<Category> findByCondition(Map<String,Object> params);
    
    /**
     * 树形展示
     * @param params
     * @return
     */
	public List<TreeVO> findTreeList(Map<String,Object> params);
	
	/**
	 * 删除校验
	 * @param category
	 * @return
	 */
	public int deleteCategoryCheck(Category category);
	
	/**
	 * 排序
	 * @param categorys
	 */
	public void updateSort(List<Category> categorys);
    
	/**
	 * 删除
	 */
	public void delete(List<Category> categorys);
}
