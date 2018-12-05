package com.tmt.task;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.system.entity.Task;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.service.TaskExecutor;
import com.tmt.system.service.UpdateDataServiceFacade;
import com.tmt.update.UpdateHandler;

/**
 * 更新的任务
 * 
 * @author lifeng
 */
public class UpdateHandlerTask implements TaskExecutor {

	@Autowired
	private UpdateDataServiceFacade updateDataService;

	private final List<UpdateHandler> updates;

	public UpdateHandlerTask(List<UpdateHandler> updates) {
		this.updates = updates;
	}

	/**
	 * 根据handler 处理数据
	 * 
	 * @param task
	 * @return
	 */
	@Override
	public Boolean doTask(Task task) {
		List<UpdateData> datas = updateDataService.queryUpdateAbles(500);
		Map<Byte, List<UpdateData>> _updates = Maps.newHashMap();
		for (UpdateData data : datas) {
			List<UpdateData> _values = _updates.get(data.getModule());
			if (_values == null) {
				_values = Lists.newArrayList();
				_updates.put(data.getModule(), _values);
			}
			_values.add(data);
		}

		// 批量更新
		Iterator<Byte> keys = _updates.keySet().iterator();
		while (keys.hasNext()) {
			Byte moudle = keys.next();
			for (UpdateHandler handler : updates) {
				if (handler.handleFor(moudle)) {
					handler.doHandler(_updates.get(moudle));
					break;
				}
			}
		}

		// 处理之后删除数据
		updateDataService.delete(datas);
		return Boolean.TRUE;
	}

	@Override
	public String getName() {
		return "数据更新任务";
	}
}