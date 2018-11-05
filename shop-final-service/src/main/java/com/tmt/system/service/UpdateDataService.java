package com.tmt.system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.system.dao.UpdateDataDao;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;

/**
 * 数据更新 管理
 * @author 超级管理员
 * @date 2016-09-09
 */
@Service
public class UpdateDataService implements UpdateDataServiceFacade, UpdateServiceFacade{
	
	@Autowired
	private UpdateDataDao updateDataDao;
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(UpdateData updateData) {
		updateDataDao.insert(updateData);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<UpdateData> updateDatas) {
		updateDataDao.batchDelete(updateDatas);
	}
	
    /**
     * 获取前几个 (会更新状态)
     * @param qc
     * @param size   
     * @return
     */
    @Override
    @Transactional
    public List<UpdateData> queryUpdateAbles(int size) {
    	return updateDataDao.queryUpdateAbles(size);
    }

	@Override
	public List<UpdateData> queryModuleUpdateAbles(String modules, int size) {
		return updateDataDao.queryModuleUpdateAbles(modules, size);
	}
}