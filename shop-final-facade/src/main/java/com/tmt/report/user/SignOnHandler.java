package com.tmt.report.user;

import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.report.ReportHandler;
import com.tmt.system.entity.User;

/**
 * 登录
 * 
 * @author lifeng
 *
 */
public class SignOnHandler implements ReportHandler<User> {

	// 插入数据
	private String sql = "INSERT INTO REPORT_USERA(REPORT_DATE, QUANTITY) VALUES(:REPORT_DATE, :QUANTITY) ON DUPLICATE KEY UPDATE QUANTITY = QUANTITY + 1";

	@Override
	public void doHandler(User object) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("QUANTITY", 1);
		param.put("REPORT_DATE", DateUtils.getFormateDate(object.getLoginDate(), "yyyy-MM-dd HH"));
		JdbcSqlExecutor.insert(sql, param);
	}

	@Override
	public void setNextHandler(ReportHandler<User> handler) {

	}
}