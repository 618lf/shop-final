package com.tmt.report.access;

import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.entity.Log;

public class MpageLogHandler extends AbstractLogHandler{

	// 插入数据
	private String pv_sql = "INSERT INTO REPORT_MPAGE_PV(MPAGE, NUMBER, REPORT_DATE) VALUES(:MPAGE, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	// IP 插入数据
	private String ip_sql = "INSERT INTO REPORT_MPAGE_IP(MPAGE, IP, NUMBER, REPORT_DATE) VALUES(:MPAGE, :IP, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	// UV 插入数据
	private String uv_sql = "INSERT INTO REPORT_MPAGE_UV(MPAGE, UV, NUMBER, REPORT_DATE) VALUES(:MPAGE, :UV, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	
	@Override
	public void doInner(Log log, String page) {
		if (StringUtils.startsWith(page, "mpage_")) {
			Map<String, Object> param = Maps.newHashMap();
			String product = StringUtils.removeStart(page,  "mpage_");
			param.put("MPAGE", product);
			param.put("NUMBER", 1);
			param.put("REPORT_DATE", DateUtils.getFormateDate(log.getCreateDate(), "yyyy-MM-dd HH"));
			JdbcSqlExecutor.insert(pv_sql, param);
			
			// ip
			param.put("IP", log.getRemoteAddr());
			JdbcSqlExecutor.insert(ip_sql, param);
			
			// uv
			if (log.getCreateId() != null) {
				param.put("UV", log.getCreateId());
				JdbcSqlExecutor.insert(uv_sql, param);
			}
		}
	}
}