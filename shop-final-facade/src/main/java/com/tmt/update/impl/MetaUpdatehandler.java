package com.tmt.update.impl;

import java.util.List;

import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.service.ShopStaticizerFacade;
import com.tmt.system.entity.UpdateData;
import com.tmt.wechat.service.MetaKeywordServiceFacade;

public class MetaUpdatehandler extends AbstractUpdateHandler {

	private MetaKeywordServiceFacade metaKeywordService;
	private ShopStaticizerFacade shopStaticizer;
	
	public MetaUpdatehandler() {
		shopStaticizer = SpringContextHolder.getBean(ShopStaticizerFacade.class);
		metaKeywordService = SpringContextHolder.getBean(MetaKeywordServiceFacade.class);
	}
	
	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		try {
			// 图文 - 静态化
			meta_static(datas);
		}catch(Exception e){e.printStackTrace();}
		
		try {
			// 图文 - 索引
			meta_lucene(datas);
		}catch(Exception e){e.printStackTrace();}
		return Boolean.TRUE;
	}
	
	// 图文静态化
	private void meta_static(List<UpdateData> categorys) {
		if (categorys == null || categorys.size() == 0) {
			return ;
		}
		for(UpdateData data: categorys) {
			// 删除
			if (data.getOpt() == 1) {
				shopStaticizer.meta_delete(data.getId());
			}
			
			// 更新
			else {
				shopStaticizer.meta_build(data.getId());
			}
		}
	}
	
	// 图文索引
	private void meta_lucene(List<UpdateData> metas) {
		if (metas == null || metas.size() == 0) {
			return ;
		}
		List<Long> updates = Lists.newArrayList();
		List<Long> deletes = Lists.newArrayList();
		for(UpdateData data: metas) {
			// 删除
			if (data.getOpt() == 1) {
				deletes.add(data.getId());
			}
			
			// 更新
			else {
				updates.add(data.getId());
			}
			
			if (deletes.size() >= 15) {
				metaKeywordService.deleteMetas(deletes);
				deletes.clear();
			}
			
			if (updates.size() >= 15) {
				metaKeywordService.refresh_rich(updates);
				updates.clear();
			}
		}
		
		// 主键删除
		if (deletes.size() != 0) {
			metaKeywordService.deleteMetas(deletes);
		}
		if (updates.size() != 0) {
			metaKeywordService.refresh_rich(updates);
		}
		
		// for gc
		updates.clear();
		deletes.clear();
		updates = null;
		deletes = null;
	}

	@Override
	protected Boolean doInnerHandler(UpdateData data) {
		return null;
	}
}