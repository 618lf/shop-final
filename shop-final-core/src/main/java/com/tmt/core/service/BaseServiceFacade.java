package com.tmt.core.service;

import java.util.List;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;

/**
 * 基础服务
 * @author root
 *
 * @param <T>
 * @param <PK>
 */
public interface BaseServiceFacade<T, PK>{
	
	/**
	 * 获取单个值
	 * @param id
	 * @return
	 */
	public T get(final PK id);
	
	/**
	 * 获取所有值
	 * @return
	 */
    public List<T> getAll();
    
    /**
     * 条件查询
     * @param qc
     * @return
     */
    public List<T> queryByCondition(QueryCondition qc );
    
    /**
     * 分页查询
     * @param qc
     * @param param
     * @return
     */
    public Page queryForPage(QueryCondition qc, PageParameters param);
    
    /**
     * 条件查询个数
     * @param qc
     * @return
     */
    public Integer countByCondition(QueryCondition qc);
    
    /**
     * 查询指定的页数
     * @param qc
     * @param begin
     * @param end
     * @return
     */
    public List<T> queryForLimitList(QueryCondition qc, int end);
    
    /**
     * 是否存在
     * @param id
     * @return
     */
    public boolean exists(PK id);
}
