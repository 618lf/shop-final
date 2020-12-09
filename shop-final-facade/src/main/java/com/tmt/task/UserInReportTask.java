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
import com.tmt.report.user.SignInHandler;
import com.tmt.system.entity.User;
import com.tmt.system.service.UserServiceFacade;

/**
 * 统计周期更具设置而定
 * @author lifeng
 */
public class UserInReportTask extends AbstractReportTask {

	@Autowired
	private UserServiceFacade userService;
	private ReportHandler<User> handler = new SignInHandler();
	
	@Override
	public String getName() {
		return "用户注册统计";
	}

	@Override
	protected void doInner(Map<String, Date> dates) {
		Date start = dates.get("start_date");
		Date end = dates.get("end_date");
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andDateGreaterThanOrEqualTo("CREATE_DATE", start);
		c.andDateLessThan("CREATE_DATE", end);
		int iCount = userService.countByCondition(qc);
		PageParameters param = new PageParameters(1, 1000, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = userService.queryForPage(qc, param);
			List<User> orders = page.getData();
			
			// 日结
			for(User order: orders) {
				handler.doHandler(order);
			}
		}
	}
}