package com.tmt.system.service;

import java.util.List;

import com.tmt.system.entity.UpdateData;

/**
 * 数据更新 管理
 * @author 超级管理员
 * @date 2016-09-09
 */
public interface UpdateDataServiceFacade {
	
	/**
	 * 保存
	 */
	public void save(UpdateData updateData);
	
	/**
	 * 删除
	 */
	public void delete(List<UpdateData> updateDatas);
	
	/**
	 * 查询更新列表
	 * @param sql
	 * @param size
	 * @return
	 */
	public List<UpdateData> queryUpdateAbles(int size);
	
	/**
	 * 查询更新列表 -- 指定模块
	 * @param sql
	 * @param size
	 * @return
	 */
	public List<UpdateData> queryModuleUpdateAbles(String modules, int size);
}