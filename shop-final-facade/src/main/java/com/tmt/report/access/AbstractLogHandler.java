package com.tmt.report.access;

import com.tmt.core.utils.StringUtils;
import com.tmt.report.ReportHandler;
import com.tmt.system.entity.Log;

public abstract class AbstractLogHandler implements ReportHandler<Log> {

	ReportHandler<Log> handler;

	@Override
	public void doHandler(Log log) {
		String url = log.getRequestUri();
		if (StringUtils.isNotBlank(url) && StringUtils.startsWith(url, "/f/statistics/")) {
			String page = StringUtils.removeStart(url, "/f/statistics/");
			page = StringUtils.removeEnd(page, ".json");

			// 执行内部操作
			this.doInner(log, page);

			// 下一个处理器
			if (handler != null) {
				handler.doHandler(log);
			}
		}
	}

	/**
	 * 执行内部操作
	 * 
	 * @param log
	 * @param page
	 */
	public abstract void doInner(Log log, String page);

	/**
	 * 下一个处理器
	 */
	@Override
	public void setNextHandler(ReportHandler<Log> handler) {
		this.handler = handler;
	}
}