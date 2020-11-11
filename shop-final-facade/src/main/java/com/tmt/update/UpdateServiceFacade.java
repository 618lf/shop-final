package com.tmt.update;

import com.tmt.system.entity.UpdateData;

/**
 * 数据更新服务
 * @author lifeng
 */
public interface UpdateServiceFacade {

	/**
	 * 保存此更新
	 * @param updateData
	 */
	public void save(UpdateData updateData);
}