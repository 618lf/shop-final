package com.tmt.update.impl;

import java.util.List;

import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.UpdateData;
import com.tmt.wechat.service.MetaKeywordServiceFacade;

public class MetaTextUpdateHandler extends AbstractUpdateHandler {

	private MetaKeywordServiceFacade metaKeywordService;
	
	public MetaTextUpdateHandler() {
		metaKeywordService = SpringContextHolder.getBean(MetaKeywordServiceFacade.class);
	}
	
	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		
		try {
			// 文本 - 索引
			meta_text_lucene(datas);
		}catch(Exception e){e.printStackTrace();}
		
		return null;
	}
	
	// 图文索引
	private void meta_text_lucene(List<UpdateData> metas) {
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
				metaKeywordService.refresh_text(updates);
				updates.clear();
			}
		}
		
		// 主键删除
		if (deletes.size() != 0) {
			metaKeywordService.deleteMetas(deletes);
		}
		if (updates.size() != 0) {
			metaKeywordService.refresh_text(updates);
		}
		
		// for gc
		updates.clear();
		deletes.clear();
		updates = null;
		deletes = null;
	}

	@Override
	protected Boolean doInnerHandler(UpdateData data) {
		// TODO Auto-generated method stub
		return null;
	}

}
