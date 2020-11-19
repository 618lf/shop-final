package com.tmt.update.impl;

import java.util.List;

import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.service.ShopStaticizerFacade;
import com.tmt.shop.utils.StoreUtils;
import com.tmt.system.entity.UpdateData;

public class CategoryUpdateHandler extends StoreUpdateHandler{

	private ShopStaticizerFacade shopStaticizer;
	
	public CategoryUpdateHandler() {
		shopStaticizer = SpringContextHolder.getBean(ShopStaticizerFacade.class);
	}
	
	@Override
	public void doInnerHandler(List<UpdateData> datas) {
		try {
			// 更新索引
			category_static(datas);
		}catch(Exception e){e.printStackTrace();}
	}
	
	// 分类索引
	private void category_static(List<UpdateData> categorys) {
		if (categorys == null || categorys.size() == 0) {
			return ;
		}
		
		// 修改一次就好了
		shopStaticizer.categorys_build(categorys.get(0).getId());
		shopStaticizer.index_build();
		StoreUtils.updateStore();
	}
}