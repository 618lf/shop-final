package com.tmt.update.impl;

import java.util.List;

import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.bbs.service.ReplySearcherFacade;
import com.tmt.bbs.update.BbsModule;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.UpdateData;

public class ReplyUpdateHandler extends AbstractUpdateHandler {

	private ReplySearcherFacade replySearcher;
	private HotspotSearcherFacade hotspotSearcher;

	public ReplyUpdateHandler() {
		replySearcher = SpringContextHolder.getBean(ReplySearcherFacade.class);
		hotspotSearcher = SpringContextHolder.getBean(HotspotSearcherFacade.class);
	}

	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		try {
			// 更新索引
			reply_lucene(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			// 更新索引
			hot_lucene(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 商品索引
	private void reply_lucene(List<UpdateData> goods) {
		if (goods == null || goods.size() == 0) {
			return;
		}
		List<Long> updates = Lists.newArrayList();
		List<Long> deletes = Lists.newArrayList();
		for (UpdateData data : goods) {
			// 删除
			if (data.getOpt() == 1) {
				deletes.add(data.getId());
			}

			// 更新
			else {
				updates.add(data.getId());
			}

			if (deletes.size() >= 15) {
				replySearcher._delete(deletes);
				deletes.clear();
			}

			if (updates.size() >= 15) {
				replySearcher.refresh(updates);
				updates.clear();
			}
		}

		// 主键删除
		if (deletes.size() != 0) {
			replySearcher._delete(deletes);
		}
		if (updates.size() != 0) {
			replySearcher.refresh(updates);
		}

		// for gc
		updates.clear();
		deletes.clear();
		updates = null;
		deletes = null;
	}
	
	// 商品索引
	private void hot_lucene(List<UpdateData> goods) {
		if (goods == null || goods.size() == 0) {
			return;
		}
		List<Long> updates = Lists.newArrayList();
		List<Long> deletes = Lists.newArrayList();
		for (UpdateData data : goods) {
			// 删除
			if (data.getOpt() == 1) {
				deletes.add(data.getId());
			}

			// 更新
			else {
				updates.add(data.getId());
			}

			if (deletes.size() >= 15) {
				hotspotSearcher._delete(deletes);
				deletes.clear();
			}

			if (updates.size() >= 15) {
				hotspotSearcher.refresh(updates, BbsModule.TOPIC_HOTSPOT);
				updates.clear();
			}
		}

		// 主键删除
		if (deletes.size() != 0) {
			hotspotSearcher._delete(deletes);
		}
		if (updates.size() != 0) {
			hotspotSearcher.refresh(updates, BbsModule.TOPIC_HOTSPOT);
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
