package com.tmt.update.impl;

import java.util.List;

import com.tmt.shop.utils.StoreUtils;
import com.tmt.system.entity.UpdateData;

/**
 * 店铺更新
 * 
 * @author lifeng
 */
public abstract class StoreUpdateHandler extends AbstractUpdateHandler {

	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		if (datas == null || datas.size() == 0) {
			return Boolean.TRUE;
		}

		// 更新店铺的更新时间
		StoreUtils.updateStore();

		// 子类执行
		this.doInnerHandler(datas);

		return Boolean.TRUE;
	}

	protected abstract void doInnerHandler(List<UpdateData> datas);

	@Override
	protected Boolean doInnerHandler(UpdateData data) {
		return null;
	}
}
