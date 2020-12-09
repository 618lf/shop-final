package com.tmt.shop.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Maps;
import com.tmt.shop.dao.CategoryBrandDao;
import com.tmt.shop.dao.CategoryDao;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.CategoryBrand;
import com.tmt.shop.update.ShopModule;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;

/**
 * 商品分类 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopCategoryService")
public class CategoryService extends BaseService<Category,Long> implements CategoryServiceFacade{
	
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CategoryBrandDao categoryBrandDao;
	@Autowired
	private UpdateServiceFacade updateDataService;
	
	@Override
	protected BaseDaoImpl<Category, Long> getBaseDao() {
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
		if( IdGen.isInvalidId(category.getId()) ) {
			this.insert(category);
		} else {
			this.update(category);
            List<Category> children = this.findByParent(category);
			for( Category e : children ) {
				e.updateIdsByParent(category, oldParentIds, oldPath, oldLevel);
			}
			this.batchUpdate(children);
		}
		this.saveBrands(category);
		this._update(category, (byte)0);
	}
	
	/**
	 * 保存品牌
	 */
	@Transactional
	public void saveBrands(Category category) {
		List<CategoryBrand> olds = this.queryRealBrandsByCategoryd(category.getId());
		List<CategoryBrand> brands = category.getBrands();
		for(CategoryBrand brand: brands) {
			brand.setCategoryId(category.getId());
		}
		this.categoryBrandDao.batchDelete(olds);
		this.categoryBrandDao.batchInsert(brands);
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
	
	/**
	 * 修改顺序
	 * @param categorys
	 */
    @Transactional
	public void updateSort(List<Category> categorys ) {
		this.batchUpdate("updateSort", categorys);
		
		// 更新其中一个
		this._update(categorys.get(0), (byte)0);
	}
    
	/**
	 * 删除
	 */
	@Transactional
	public boolean delete(List<Category> categorys) {
		for(Category category: categorys) {
			int count = this.countByCondition("deleteCategoryCheck", category);
			if(count > 0) {
			   return false;
			}
		}
		this.batchDelete(categorys);
		for(Category category: categorys) {
			this._update(category, (byte)1);
		}
		return true;
	}
	
	private void _update(Category category, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(category.getId());
		updateData.setModule(ShopModule.CATEGORY);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
	
	/**
	 * 根据配送方式查询关联的支付方式包括未选择的
	 * @return
	 */
	public List<CategoryBrand> queryBrandsByCategoryd(Long categoryd) {
		return this.categoryBrandDao.queryForList("queryByCategoryd", categoryd);
	}
	
	/**
	 * 根据配送方式查询关联的支付方式,实际选择的
	 * @return
	 */
	public List<CategoryBrand> queryRealBrandsByCategoryd(Long categoryd) {
		return this.categoryBrandDao.queryForList("queryRealsByCategoryd", categoryd);
	}
	
	/**
	 * 返回第一层
	 */
	public List<Category> queryUseAbleTopCategorys() {
		Map<String, Object> params = Maps.newHashMap();
		return this.queryForList("queryUseAbleTopCategorys", params);
	}
	
	/**
	 * 返回(所有)
	 */
	public List<Category> queryUseAbleCategorys() {
		Map<String, Object> params = Maps.newHashMap();
		return this.queryForList("queryUseAbleCategorys", params);
	}

	/**
	 * 根据层次查询
	 */
	@Override
	public List<Category> queryCategorysByLevel(Integer level, Long parentId, String name) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("PARENT_ID", parentId);
		params.put("LEVEL", level);
		params.put("NAME", name);
		return this.queryForList("queryCategorysByLevel", params);
	}
}