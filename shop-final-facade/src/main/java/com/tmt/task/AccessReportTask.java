package com.tmt.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.report.ReportHandler;
import com.tmt.report.access.MpageLogHandler;
import com.tmt.report.access.PageLogHandler;
import com.tmt.report.access.ProductLogHandler;
import com.tmt.system.entity.Log;
import com.tmt.system.service.LogServiceFacade;

/**
 * 统计周期更具设置而定
 * @author root
 */
public class AccessReportTask extends AbstractReportTask {

	@Autowired
	private LogServiceFacade logService;
	private ReportHandler<Log> handler;
	
	public AccessReportTask() {
		handler = new PageLogHandler();
		ReportHandler<Log> mpageloghandler = new MpageLogHandler();
		ReportHandler<Log> productLogHandler = new ProductLogHandler();
		
		// 处理顺序
		handler.setNextHandler(mpageloghandler);
		mpageloghandler.setNextHandler(productLogHandler);
	}
	
	@Override
	public String getName() {
		return "访问统计";
	}

	@Override
	protected void doInner(Map<String, Date> dates) {
		Date start = dates.get("start_date");
		Date end = dates.get("end_date");
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andDateGreaterThanOrEqualTo("CREATE_DATE", start);
		c.andDateLessThan("CREATE_DATE", end);
		int iCount = logService.countByCondition(qc);
		PageParameters param = new PageParameters(1, 1000, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = logService.queryForPage(qc, param);
			List<Log> logs = page.getData();
			
			// 日结
			for(Log log: logs) {
				handler.doHandler(log);
			}
		}
	}
}