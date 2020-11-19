package com.tmt.report.access;

import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.entity.Log;

/**
 * 分类处理
 * @author root
 */
public class ProductLogHandler extends AbstractLogHandler {

	// 插入数据
	private String pv_sql = "INSERT INTO REPORT_PRODUCT_PV(PRODUCT, NUMBER, REPORT_DATE) VALUES(:PRODUCT, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	// IP 插入数据
	private String ip_sql = "INSERT INTO REPORT_PRODUCT_IP(PRODUCT, IP, NUMBER, REPORT_DATE) VALUES(:PRODUCT, :IP, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	// UV 插入数据
	private String uv_sql = "INSERT INTO REPORT_PRODUCT_UV(PRODUCT, UV, NUMBER, REPORT_DATE) VALUES(:PRODUCT, :UV, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	
	
	@Override
	public void doInner(Log log, String page) {
		if (StringUtils.startsWith(page, "product_")) {
			Map<String, Object> param = Maps.newHashMap();
			String product = StringUtils.removeStart(page,  "product_");
			param.put("PRODUCT", product);
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