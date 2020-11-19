package com.tmt.report.access;

import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.entity.Log;

/**
 * pv 处理器
 * @author root
 */
public class PageLogHandler extends AbstractLogHandler {

	// 插入数据
	private String pv_sql = "INSERT INTO REPORT_PV(PAGE, NUMBER, REPORT_DATE) VALUES(:PAGE, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	// IP 插入数据
	private String ip_sql = "INSERT INTO REPORT_IP(PAGE, IP, NUMBER, REPORT_DATE) VALUES(:PAGE, :IP, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
	// UV 插入数据
	private String uv_sql = "INSERT INTO REPORT_UV(PAGE, UV, NUMBER, REPORT_DATE) VALUES(:PAGE, :UV, :NUMBER, :REPORT_DATE) ON DUPLICATE KEY UPDATE NUMBER = NUMBER + 1";
		
	@Override
	public void doInner(Log log, String page) {
		Map<String, Object> param = Maps.newHashMap();
		if ("index".equals(page)) {
			param.put("PAGE", "首页");
		} else if("search_goods".equals(page)) {
			param.put("PAGE", "搜索");
		} else if(StringUtils.startsWith(page, "category_")) {
			param.put("PAGE", "分类");
		} else if(StringUtils.startsWith(page, "product_")) {
			param.put("PAGE", "商品");
		} else if(StringUtils.startsWith(page, "mpage_")) {
			param.put("PAGE", "微页面");
		} else {
			param.put("PAGE", "其它");
		}
		param.put("NUMBER", 1);
		param.put("REPORT_DATE", DateUtils.getFormateDate(log.getCreateDate(), "yyyy-MM-dd HH"));
		
		// pv
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