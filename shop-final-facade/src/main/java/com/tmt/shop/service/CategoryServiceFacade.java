package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import com.tmt.core.entity.TreeVO;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.CategoryBrand;

/**
 * 商品分类 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface CategoryServiceFacade extends BaseServiceFacade<Category,Long> {
	
	public static String CACHE_NAME = "SHOP#C#";
		
	/**
	 * 保存
	 */
	public void save(Category category);
	
	/**
	 * 保存品牌
	 */
	public void saveBrands(Category category);
	
	/**
	 * 得到子项目
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
     * 树形结构显示
     * @param params
     * @return
     */
	public List<TreeVO> findTreeList(Map<String,Object> params);
	
	/**
	 * 排序
	 * @param categorys
	 */
	public void updateSort(List<Category> categorys);
    
	/**
	 * 删除
	 */
	public boolean delete(List<Category> categorys);
	
	/**
	 * 根据配送方式查询关联的支付方式包括未选择的
	 * @return
	 */
	public List<CategoryBrand> queryBrandsByCategoryd(Long categoryd);
	
	/**
	 * 根据配送方式查询关联的支付方式,实际选择的
	 * @return
	 */
	public List<CategoryBrand> queryRealBrandsByCategoryd(Long categoryd);
	
	/**
	 * 查询可用的分类 (所有)
	 * @return
	 */
	public List<Category> queryUseAbleCategorys();
	
	/**
	 * 查询可用的分类（第一层）
	 * @return
	 */
	public List<Category> queryUseAbleTopCategorys();
	
	/**
	 * 查询当前层次下的数据
	 * @return
	 */
	public List<Category> queryCategorysByLevel(Integer level, Long parentId, String name);
	
}