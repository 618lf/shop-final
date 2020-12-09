package com.tmt.update.impl;

import java.util.List;

import com.tmt.core.utils.SpringContextHolder;
import com.tmt.shop.service.ShopStaticizerFacade;
import com.tmt.system.entity.UpdateData;

public class PromotionUpdateHandler extends AbstractUpdateHandler {

	private ShopStaticizerFacade shopStaticizer;

	public PromotionUpdateHandler() {
		shopStaticizer = SpringContextHolder.getBean(ShopStaticizerFacade.class);
	}

	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		try {
			// 静态化
			promotion_static(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Boolean.TRUE;
	}

	// 促销静态化
	private void promotion_static(List<UpdateData> goods) {
		if (goods == null || goods.size() == 0) {
			return;
		}
		for (UpdateData data : goods) {
			// 删除
			if (data.getOpt() == 1) {
				shopStaticizer.promotion_delete(data.getId());
			}

			// 更新
			else {
				shopStaticizer.promotion_build(data.getId());
			}
		}
	}

	@Override
	protected Boolean doInnerHandler(UpdateData data) {
		return null;
	}
}