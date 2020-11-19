package com.tmt.update.impl;

import java.util.List;

import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.UpdateData;

public class TopicUpdateHandler extends AbstractUpdateHandler {

	private TopicSearcherFacade topicSearcher;

	public TopicUpdateHandler() {
		topicSearcher = SpringContextHolder.getBean(TopicSearcherFacade.class);
	}

	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		try {
			// 更新索引
			topic_lucene(datas);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// 商品索引
	private void topic_lucene(List<UpdateData> goods) {
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
				topicSearcher._delete(deletes);
				deletes.clear();
			}

			if (updates.size() >= 15) {
				topicSearcher.refresh(updates);
				updates.clear();
			}
		}

		// 主键删除
		if (deletes.size() != 0) {
			topicSearcher._delete(deletes);
		}
		if (updates.size() != 0) {
			topicSearcher.refresh(updates);
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
