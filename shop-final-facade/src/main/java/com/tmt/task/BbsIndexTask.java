package com.tmt.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.bbs.service.ReplySearcherFacade;
import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;

/**
 * bbs 相关的任务
 * @author lifeng
 *
 */
public class BbsIndexTask implements TaskExecutor {

	@Autowired
	private TopicSearcherFacade topicSearcher;
	@Autowired
	private ReplySearcherFacade replySearcher;
	@Autowired
	private HotspotSearcherFacade hotspotSearcher;
	
	@Override
	public Boolean doTask(Task task) {
		
		// 动态索引重建
		topicSearcher.topics_build();
		
		// 回复索引重建
		replySearcher.replys_build();
		
		// 热点索引重建
		hotspotSearcher.hotspots_build();
		
		return null;
	}

	@Override
	public String getName() {
		return "社区索引更新";
	}
}
