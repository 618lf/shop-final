package com.tmt.task;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.bbs.update.BbsModule;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.service.TaskExecutor;
import com.tmt.system.service.UpdateDataServiceFacade;
import com.tmt.update.UpdateHandler;
import com.tmt.update.impl.HotspotUpdateHandler;
import com.tmt.update.impl.ReplyUpdateHandler;
import com.tmt.update.impl.TopicUpdateHandler;

/**
 * 单独提取出来，加快处理速度
 * @author lifeng
 */
public class BbsUpdateDataTask implements TaskExecutor{

	@Autowired
	private UpdateDataServiceFacade updateDataService;
	private UpdateHandler replyUpdateHandler;
	private UpdateHandler topicHotspotUpdateHandler;
	private UpdateHandler replyHotspotUpdateHandler;
	private UpdateHandler topicUpdateHandler;
	
	public BbsUpdateDataTask() {
		replyUpdateHandler = new ReplyUpdateHandler();
		topicHotspotUpdateHandler = new HotspotUpdateHandler();
		replyHotspotUpdateHandler = new HotspotUpdateHandler();
		topicUpdateHandler = new TopicUpdateHandler();
	}
	
	@Override
	public Boolean doTask(Task task) {
		// 每次更新500条数据(批量的更新)
		List<UpdateData> updates = updateDataService.queryModuleUpdateAbles("8,9,10,11", 500);
		Map<Byte, List<UpdateData>> _updates = Maps.newHashMap();
		for(UpdateData data: updates) {
			List<UpdateData> _values = _updates.get(data.getModule());
			if (_values == null) {
				_values = Lists.newArrayList();
				_updates.put(data.getModule(), _values);
			}
			_values.add(data);
		}
		// 8
		topicUpdateHandler.doHandler(_updates.get(BbsModule.TOPIC));
		
		// 9
		replyUpdateHandler.doHandler(_updates.get(BbsModule.REPLY));
		
		// 10
		topicHotspotUpdateHandler.doHandler(_updates.get(BbsModule.TOPIC_HOTSPOT));
		
		// 11
		replyHotspotUpdateHandler.doHandler(_updates.get(BbsModule.REPLY_HOTSPOT));
		
		// 处理完成之后删除
		updateDataService.delete(updates);
		// for gc
		updates.clear();
		updates = null;
		return Boolean.TRUE;
	}

	@Override
	public String getName() {
		return "社区数据定时更新";
	}

}
