package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Specification;
import com.tmt.shop.entity.SpecificationOption;

/**
 * 商品规格 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface SpecificationServiceFacade extends BaseServiceFacade<Specification,Long> {
	
	/**
	 * 保存
	 */
	public void save(Specification specification);
	
	/**
	 * 分为添加和修改删除
	 * @param Specification
	 */
	public void saveOptions(Specification specification);
	
	/**
	 * 排序
	 * @param specifications
	 */
	public void updateSort(List<Specification> specifications);
	
	/**
	 * 删除
	 */
	public void delete(List<Specification> specifications);
	
	/**
	 * 得到选项
	 * @param id
	 * @return
	 */
	public Specification getWithOptions(Long id);
	
	/**
	 * 查询选择项
	 * @param id
	 * @return
	 */
	public List<SpecificationOption> queryBySpecificationId(Long id);
	
	/**
	 * 判断分类是否友规格
	 * @param categoryId
	 * @return
	 */
	public Boolean hasSpecification(Long categoryId);
}
