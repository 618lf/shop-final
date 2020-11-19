package com.tmt.update.impl;

import java.util.List;

import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.entity.UpdateData;

public class HotspotUpdateHandler extends AbstractUpdateHandler {

	private HotspotSearcherFacade hotspotSearcher;

	public HotspotUpdateHandler() {
		hotspotSearcher = SpringContextHolder.getBean(HotspotSearcherFacade.class);
	}

	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		try {
			// 更新索引
			hotspot_lucene(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	// 减位
	private Long reducedPost(Long id) {
		String _id = StringUtil3.removeEnd(id.toString(), "0");
		return Long.parseLong(_id);
	}

	// 商品索引
	private void hotspot_lucene(List<UpdateData> goods) {
		if (goods == null || goods.size() == 0) {
			return;
		}
		Byte module = goods.get(0).getModule();
		List<Long> updates = Lists.newArrayList();
		List<Long> deletes = Lists.newArrayList();
		for (UpdateData data : goods) {
			
			// id 转换
			Long id = this.reducedPost(data.getId());
			
			// 删除
			if (data.getOpt() == 1) {
				deletes.add(id);
			}

			// 更新
			else {
				updates.add(id);
			}

			if (deletes.size() >= 15) {
				hotspotSearcher._delete(deletes);
				deletes.clear();
			}

			if (updates.size() >= 15) {
				hotspotSearcher.refresh(updates, module);
				updates.clear();
			}
		}

		// 主键删除
		if (deletes.size() != 0) {
			hotspotSearcher._delete(deletes);
		}
		if (updates.size() != 0) {
			hotspotSearcher.refresh(updates, module);
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
