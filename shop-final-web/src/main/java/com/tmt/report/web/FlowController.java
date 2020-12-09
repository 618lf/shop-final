package com.tmt.report.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.tree.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.cms.entity.Mpage;
import com.tmt.cms.service.MpageServiceFacade;
import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.Product;
import com.tmt.shop.service.ProductServiceFacade;

/**
 * 订单统计
 * @author lifeng
 */
@Controller("reportFlowController")
@RequestMapping(value = "${adminPath}/report/flow")
public class FlowController {

	@Autowired
	private ProductServiceFacade productService;
	@Autowired
	private MpageServiceFacade pageService;
	
	/**
	 * 网站流量
	 * @return
	 */
	@RequestMapping("site")
	public String site(Model model) {
		
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(NUMBER) C FROM REPORT_PV")
				.append(" UNION ALL ")
				.append("SELECT COUNT(IP) C FROM (SELECT IP FROM REPORT_IP GROUP BY IP) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(UV) C FROM (SELECT UV FROM REPORT_UV GROUP BY UV) A").toString();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 获得查询结果
		List<Integer> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("C");
			}
		});
		
		Date today = DateUtils.getTodayTime();
		model.addAttribute("stats", stats);
		model.addAttribute("start_date", DateUtils.getDayFirstTime(today));
		model.addAttribute("end_date", DateUtils.getDayLastTime(today));
		return "report/SiteFlow";
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("site/pv")
	public List<Stat> sitePv(String start_date, String end_date) {
		Date s1 = DateUtils.getFormatDate(start_date, "yyyy-MM-dd");
		Date s2 = DateUtils.getFormatDate(end_date, "yyyy-MM-dd");
		
		// 执行的sql
		String sql = null;
		
		// 执行的查询条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S1", DateUtils.getDayFirstTime(s1));
		param.put("E1", DateUtils.getDayLastTime(s2));
		
		// 今天的数据
		if (s2.compareTo(s1) == 0) {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%H') REPORT_DATE, SUM(NUMBER) QUANTITY FROM REPORT_PV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%H')").toString();
		} else {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d') REPORT_DATE, SUM(NUMBER) QUANTITY FROM REPORT_PV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d')").toString();
		}
		
		// 查询结果
		List<Stat> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Stat>() {
			@Override
			public Stat mapRow(ResultSet rs, int rowNum) throws SQLException {
				Stat vals = new Stat();
				vals.setX(rs.getString("REPORT_DATE"));
				vals.setY(rs.getInt("QUANTITY"));
				return vals;
			}
		});
		
		// 模拟数据
		if (s2.compareTo(s1) == 0) { 
			for(int i= 0; i< 24; i++) {
				String time = String.valueOf(i);
				if (i != 0) {
					time = StringUtils.leftPad(String.valueOf(i), 2, "0");
				}
				Boolean found = false;
				for(Stat stat: stats) {
					if (time.equals(stat.getX())) {
						found = true;
						break;
					}
				}
				if (!found) {
					Stat stat = new Stat();
					stat.setX(time);
					stat.setY(0);
					stats.add(stat);
				}
			}
		} else {
			int days = (int) ((s2.getTime() - s1.getTime()) / (1000*3600*24));
			for(int i= 0; i<= days; i++) {
				String time = DateUtils.getFormatDate(DateUtils.getDateByOffset(s1, i), "yyyy-MM-dd");
				Boolean found = false;
				for(Stat stat: stats) {
					if (time.equals(stat.getX())) {
						found = true;
						break;
					}
				}
				if (!found) {
					Stat stat = new Stat();
					stat.setX(time);
					stat.setY(0);
					stats.add(stat);
				}
			}
		}
		
		// 排序
		Collections.sort(stats);
		return stats;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("site/ip")
	public List<Stat> siteIp(String start_date, String end_date) {
		Date s1 = DateUtils.getFormatDate(start_date, "yyyy-MM-dd");
		Date s2 = DateUtils.getFormatDate(end_date, "yyyy-MM-dd");
		
		// 执行的sql
		String sql = null;
		
		// 执行的查询条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S1", DateUtils.getDayFirstTime(s1));
		param.put("E1", DateUtils.getDayLastTime(s2));
		
		// 今天的数据
		if (s2.compareTo(s1) == 0) {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%H') REPORT_DATE, COUNT(DISTINCT IP) QUANTITY FROM REPORT_IP A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%H')").toString();
		} else {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d') REPORT_DATE, COUNT(DISTINCT IP) QUANTITY FROM REPORT_IP A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d')").toString();
		}
		
		// 查询结果
		List<Stat> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Stat>() {
			@Override
			public Stat mapRow(ResultSet rs, int rowNum) throws SQLException {
				Stat vals = new Stat();
				vals.setX(rs.getString("REPORT_DATE"));
				vals.setY(rs.getInt("QUANTITY"));
				return vals;
			}
		});
		
		// 模拟数据
		if (s2.compareTo(s1) == 0) { 
			for(int i= 0; i< 24; i++) {
				String time = String.valueOf(i);
				if (i != 0) {
					time = StringUtils.leftPad(String.valueOf(i), 2, "0");
				}
				Boolean found = false;
				for(Stat stat: stats) {
					if (time.equals(stat.getX())) {
						found = true;
						break;
					}
				}
				if (!found) {
					Stat stat = new Stat();
					stat.setX(time);
					stat.setY(0);
					stats.add(stat);
				}
			}
		} else {
			int days = (int) ((s2.getTime() - s1.getTime()) / (1000*3600*24));
			for(int i= 0; i<= days; i++) {
				String time = DateUtils.getFormatDate(DateUtils.getDateByOffset(s1, i), "yyyy-MM-dd");
				Boolean found = false;
				for(Stat stat: stats) {
					if (time.equals(stat.getX())) {
						found = true;
						break;
					}
				}
				if (!found) {
					Stat stat = new Stat();
					stat.setX(time);
					stat.setY(0);
					stats.add(stat);
				}
			}
		}
		
		// 排序
		Collections.sort(stats);
		return stats;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("site/uv")
	public List<Stat> siteUv(String start_date, String end_date) {
		Date s1 = DateUtils.getFormatDate(start_date, "yyyy-MM-dd");
		Date s2 = DateUtils.getFormatDate(end_date, "yyyy-MM-dd");
		
		// 执行的sql
		String sql = null;
		
		// 执行的查询条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S1", DateUtils.getDayFirstTime(s1));
		param.put("E1", DateUtils.getDayLastTime(s2));
		
		// 今天的数据
		if (s2.compareTo(s1) == 0) {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%H') REPORT_DATE, COUNT(DISTINCT UV) QUANTITY FROM REPORT_UV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%H')").toString();
		} else {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d') REPORT_DATE, COUNT(DISTINCT UV) QUANTITY FROM REPORT_UV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d')").toString();
		}
		
		// 查询结果
		List<Stat> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Stat>() {
			@Override
			public Stat mapRow(ResultSet rs, int rowNum) throws SQLException {
				Stat vals = new Stat();
				vals.setX(rs.getString("REPORT_DATE"));
				vals.setY(rs.getInt("QUANTITY"));
				return vals;
			}
		});
		
		// 模拟数据
		if (s2.compareTo(s1) == 0) { 
			for(int i= 0; i< 24; i++) {
				String time = String.valueOf(i);
				if (i != 0) {
					time = StringUtils.leftPad(String.valueOf(i), 2, "0");
				}
				Boolean found = false;
				for(Stat stat: stats) {
					if (time.equals(stat.getX())) {
						found = true;
						break;
					}
				}
				if (!found) {
					Stat stat = new Stat();
					stat.setX(time);
					stat.setY(0);
					stats.add(stat);
				}
			}
		} else {
			int days = (int) ((s2.getTime() - s1.getTime()) / (1000*3600*24));
			for(int i= 0; i<= days; i++) {
				String time = DateUtils.getFormatDate(DateUtils.getDateByOffset(s1, i), "yyyy-MM-dd");
				Boolean found = false;
				for(Stat stat: stats) {
					if (time.equals(stat.getX())) {
						found = true;
						break;
					}
				}
				if (!found) {
					Stat stat = new Stat();
					stat.setX(time);
					stat.setY(0);
					stats.add(stat);
				}
			}
		}
		
		// 排序
		Collections.sort(stats);
		return stats;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@RequestMapping("product_pv")
	public String productPv(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(NUMBER) C FROM REPORT_PRODUCT_PV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
				.append(" UNION ALL ")
				.append("SELECT SUM(NUMBER) C FROM REPORT_PRODUCT_PV A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
				.append(" UNION ALL ")
				.append("SELECT SUM(NUMBER) C FROM REPORT_PRODUCT_PV A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3")
				.append(" UNION ALL ")
				.append("SELECT SUM(NUMBER) C FROM REPORT_PRODUCT_PV A").toString();
		
		Date today = DateUtils.getTodayTime();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S3", DateUtils.getMonthFirstDate(today));
		param.put("E3", DateUtils.getMonthLastDate(today));
		param.put("S2", DateUtils.getWeekFirstDate(today));
		param.put("E2", DateUtils.getWeekLastDate(today));
		param.put("S1", DateUtils.getDayFirstTime(today));
		param.put("E1", DateUtils.getDayLastTime(today));
		
		// 获得查询结果
		List<Integer> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("C");
			}
		});
		// 查询的条件
		model.addAttribute("stats", stats);
		model.addAttribute("start_date", DateUtils.getDayFirstTime(today));
		model.addAttribute("end_date", DateUtils.getDayLastTime(today));
		return "report/ProductFlowPv"; 
	}
	
	/**
	 * 下单数(分页)
	 */
	@ResponseBody
	@RequestMapping("product_pv/page")
	public Page product_pv_page(String start_date, String end_date, Page page) {
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 执行的 SQL
		String sql = null; String count_sql = null;
		if (StringUtils.isNotBlank(start_date)
				&& StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT A.PRODUCT, A.QUANTITY FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY  FROM REPORT_PRODUCT_PV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY  FROM REPORT_PRODUCT_PV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT A.PRODUCT, A.QUANTITY FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY  FROM REPORT_PRODUCT_PV WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY  FROM REPORT_PRODUCT_PV WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT A.PRODUCT, A.QUANTITY FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY  FROM REPORT_PRODUCT_PV WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY  FROM REPORT_PRODUCT_PV WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT A.PRODUCT, A.QUANTITY FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY  FROM REPORT_PRODUCT_PV GROUP BY PRODUCT) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, SUM(NUMBER) QUANTITY FROM REPORT_PRODUCT_PV GROUP BY PRODUCT) A").toString();
		}
		
		// 排序条件
		String order = page.getParam().orderBy("QUANTITY");
		sql = new StringBuilder(sql).append(" ORDER BY ").append(order).append(" DESC").toString();
		
		// 分页参数
		Integer pageIndex = page.getParam().getPageIndex();
		Integer pageSize = page.getParam().getPageSize();
		sql = new StringBuilder(sql).append(" LIMIT ").append((pageIndex -1)* pageSize).append(", ").append(pageSize).toString();
		
		// 总数
	    Integer count = JdbcSqlExecutor.count(count_sql, param);
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("PRODUCT", rs.getObject("PRODUCT"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				Long id = rs.getLong("PRODUCT");
				Product product = productService.get(id);
				if (product != null) {
					vals.put("PRODUCT_NAME", product.getName() + product.getTip());
				}
				return vals;
			}
		});
		
		// 构造page对象
		page.setData(stats);
		PageParameters pageParameters = new PageParameters(pageIndex, pageSize, count);
		page.setPage(pageParameters);
		return page;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@RequestMapping("product_ip")
	public String productIp(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_PRODUCT_IP A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY IP) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_PRODUCT_IP A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2 GROUP BY IP) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_PRODUCT_IP A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3 GROUP BY IP) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_PRODUCT_IP GROUP BY IP) A").toString();
		
		Date today = DateUtils.getTodayTime();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S3", DateUtils.getMonthFirstDate(today));
		param.put("E3", DateUtils.getMonthLastDate(today));
		param.put("S2", DateUtils.getWeekFirstDate(today));
		param.put("E2", DateUtils.getWeekLastDate(today));
		param.put("S1", DateUtils.getDayFirstTime(today));
		param.put("E1", DateUtils.getDayLastTime(today));
		
		// 获得查询结果
		List<Integer> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("C");
			}
		});
		// 查询的条件
		model.addAttribute("stats", stats);
		model.addAttribute("start_date", DateUtils.getDayFirstTime(today));
		model.addAttribute("end_date", DateUtils.getDayLastTime(today));
		return "report/ProductFlowIp"; 
	}
	
	/**
	 * 下单数(分页)
	 */
	@ResponseBody
	@RequestMapping("product_ip/page")
	public Page product_ip_page(String start_date, String end_date, Page page) {
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 执行的 SQL
		String sql = null; String count_sql = null;
		if (StringUtils.isNotBlank(start_date)
				&& StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(IP) QUANTITY FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT, IP) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, COUNT(IP) QUANTITY FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT, IP) A GROUP BY PRODUCT) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(IP) QUANTITY FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT, IP) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, COUNT(IP) QUANTITY FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT, IP) A GROUP BY PRODUCT) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(IP) QUANTITY FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT, IP) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, COUNT(IP) QUANTITY FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT, IP) A GROUP BY PRODUCT) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(IP) QUANTITY FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP GROUP BY PRODUCT, IP) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) FROM (SELECT PRODUCT, COUNT(IP) FROM (SELECT PRODUCT, IP FROM REPORT_PRODUCT_IP GROUP BY PRODUCT, IP) A GROUP BY PRODUCT) A").toString();
		}
		
		// 排序条件
		String order = page.getParam().orderBy("QUANTITY");
		sql = new StringBuilder(sql).append(" ORDER BY ").append(order).append(" DESC").toString();
		
		// 分页参数
		Integer pageIndex = page.getParam().getPageIndex();
		Integer pageSize = page.getParam().getPageSize();
		sql = new StringBuilder(sql).append(" LIMIT ").append((pageIndex -1)* pageSize).append(", ").append(pageSize).toString();
		
		// 总数
	    Integer count = JdbcSqlExecutor.count(count_sql, param);
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("PRODUCT", rs.getObject("PRODUCT"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				Long id = rs.getLong("PRODUCT");
				Product product = productService.get(id);
				if (product != null) {
					vals.put("PRODUCT_NAME", product.getName() + product.getTip());
				}
				return vals;
			}
		});
		
		// 构造page对象
		page.setData(stats);
		PageParameters pageParameters = new PageParameters(pageIndex, pageSize, count);
		page.setPage(pageParameters);
		return page;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@RequestMapping("product_uv")
	public String productUv(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_PRODUCT_UV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY UV) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_PRODUCT_UV A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2 GROUP BY UV) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_PRODUCT_UV A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3 GROUP BY UV) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_PRODUCT_UV GROUP BY UV) A").toString();
		
		Date today = DateUtils.getTodayTime();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S3", DateUtils.getMonthFirstDate(today));
		param.put("E3", DateUtils.getMonthLastDate(today));
		param.put("S2", DateUtils.getWeekFirstDate(today));
		param.put("E2", DateUtils.getWeekLastDate(today));
		param.put("S1", DateUtils.getDayFirstTime(today));
		param.put("E1", DateUtils.getDayLastTime(today));
		
		// 获得查询结果
		List<Integer> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("C");
			}
		});
		// 查询的条件
		model.addAttribute("stats", stats);
		model.addAttribute("start_date", DateUtils.getDayFirstTime(today));
		model.addAttribute("end_date", DateUtils.getDayLastTime(today));
		return "report/ProductFlowUv"; 
	}
	
	/**
	 * 下单数(分页)
	 */
	@ResponseBody
	@RequestMapping("product_uv/page")
	public Page product_uv_page(String start_date, String end_date, Page page) {
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 执行的 SQL
		String sql = null; String count_sql = null;
		if (StringUtils.isNotBlank(start_date)
				&& StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(UV) QUANTITY FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT, UV) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, COUNT(UV) QUANTITY FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT, UV) A GROUP BY PRODUCT) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(UV) QUANTITY FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT, UV) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, COUNT(UV) QUANTITY FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT, UV) A GROUP BY PRODUCT) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(UV) QUANTITY FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT, UV) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT PRODUCT, COUNT(UV) QUANTITY FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT, UV) A GROUP BY PRODUCT) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT PRODUCT, COUNT(UV) QUANTITY FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV GROUP BY PRODUCT, UV) A GROUP BY PRODUCT").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) FROM (SELECT PRODUCT, COUNT(UV) FROM (SELECT PRODUCT, UV FROM REPORT_PRODUCT_UV GROUP BY PRODUCT, UV) A GROUP BY PRODUCT) A").toString();
		}
		
		// 排序条件
		String order = page.getParam().orderBy("QUANTITY");
		sql = new StringBuilder(sql).append(" ORDER BY ").append(order).append(" DESC").toString();
		
		// 分页参数
		Integer pageIndex = page.getParam().getPageIndex();
		Integer pageSize = page.getParam().getPageSize();
		sql = new StringBuilder(sql).append(" LIMIT ").append((pageIndex -1)* pageSize).append(", ").append(pageSize).toString();
		
		// 总数
	    Integer count = JdbcSqlExecutor.count(count_sql, param);
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("PRODUCT", rs.getObject("PRODUCT"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				Long id = rs.getLong("PRODUCT");
				Product product = productService.get(id);
				if (product != null) {
					vals.put("PRODUCT_NAME", product.getName() + product.getTip());
				}
				return vals;
			}
		});
		
		// 构造page对象
		page.setData(stats);
		PageParameters pageParameters = new PageParameters(pageIndex, pageSize, count);
		page.setPage(pageParameters);
		return page;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@RequestMapping("mpage_pv")
	public String mpagePv(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(NUMBER) C FROM REPORT_MPAGE_PV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
				.append(" UNION ALL ")
				.append("SELECT SUM(NUMBER) C FROM REPORT_MPAGE_PV A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
				.append(" UNION ALL ")
				.append("SELECT SUM(NUMBER) C FROM REPORT_MPAGE_PV A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3")
				.append(" UNION ALL ")
				.append("SELECT SUM(NUMBER) C FROM REPORT_MPAGE_PV A").toString();
		
		Date today = DateUtils.getTodayTime();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S3", DateUtils.getMonthFirstDate(today));
		param.put("E3", DateUtils.getMonthLastDate(today));
		param.put("S2", DateUtils.getWeekFirstDate(today));
		param.put("E2", DateUtils.getWeekLastDate(today));
		param.put("S1", DateUtils.getDayFirstTime(today));
		param.put("E1", DateUtils.getDayLastTime(today));
		
		// 获得查询结果
		List<Integer> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("C");
			}
		});
		// 查询的条件
		model.addAttribute("stats", stats);
		model.addAttribute("start_date", DateUtils.getDayFirstTime(today));
		model.addAttribute("end_date", DateUtils.getDayLastTime(today));
		return "report/MpageFlowPv"; 
	}
	
	/**
	 * 下单数(分页)
	 */
	@ResponseBody
	@RequestMapping("mpage_pv/page")
	public Page mpage_pv_page(String start_date, String end_date, Page page) {
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 执行的 SQL
		String sql = null; String count_sql = null;
		if (StringUtils.isNotBlank(start_date)
				&& StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT A.MPAGE, A.QUANTITY FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY  FROM REPORT_MPAGE_PV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY MPAGE) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY  FROM REPORT_MPAGE_PV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY MPAGE) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT A.MPAGE, A.QUANTITY FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY  FROM REPORT_MPAGE_PV WHERE REPORT_DATE >= :S1 GROUP BY MPAGE) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY  FROM REPORT_MPAGE_PV WHERE REPORT_DATE >= :S1 GROUP BY MPAGE) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT A.MPAGE, A.QUANTITY FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY  FROM REPORT_MPAGE_PV WHERE REPORT_DATE <= :E1 GROUP BY MPAGE) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY  FROM REPORT_MPAGE_PV WHERE REPORT_DATE <= :E1 GROUP BY MPAGE) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT A.MPAGE, A.QUANTITY FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY  FROM REPORT_MPAGE_PV GROUP BY MPAGE) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, SUM(NUMBER) QUANTITY FROM REPORT_MPAGE_PV GROUP BY MPAGE) A").toString();
		}
		
		// 排序条件
		String order = page.getParam().orderBy("QUANTITY");
		sql = new StringBuilder(sql).append(" ORDER BY ").append(order).append(" DESC").toString();
		
		// 分页参数
		Integer pageIndex = page.getParam().getPageIndex();
		Integer pageSize = page.getParam().getPageSize();
		sql = new StringBuilder(sql).append(" LIMIT ").append((pageIndex -1)* pageSize).append(", ").append(pageSize).toString();
		
		// 总数
	    Integer count = JdbcSqlExecutor.count(count_sql, param);
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("MPAGE", rs.getObject("MPAGE"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				Long id = rs.getLong("MPAGE");
				Mpage product = pageService.get(id);
				if (product != null) {
					vals.put("PRODUCT_NAME", product.getName());
				}
				return vals;
			}
		});
		
		// 构造page对象
		page.setData(stats);
		PageParameters pageParameters = new PageParameters(pageIndex, pageSize, count);
		page.setPage(pageParameters);
		return page;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@RequestMapping("mpage_ip")
	public String mpageIp(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_MPAGE_IP A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY IP) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_MPAGE_IP A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2 GROUP BY IP) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_MPAGE_IP A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3 GROUP BY IP) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT IP FROM REPORT_MPAGE_IP GROUP BY IP) A").toString();
		
		Date today = DateUtils.getTodayTime();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S3", DateUtils.getMonthFirstDate(today));
		param.put("E3", DateUtils.getMonthLastDate(today));
		param.put("S2", DateUtils.getWeekFirstDate(today));
		param.put("E2", DateUtils.getWeekLastDate(today));
		param.put("S1", DateUtils.getDayFirstTime(today));
		param.put("E1", DateUtils.getDayLastTime(today));
		
		// 获得查询结果
		List<Integer> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("C");
			}
		});
		// 查询的条件
		model.addAttribute("stats", stats);
		model.addAttribute("start_date", DateUtils.getDayFirstTime(today));
		model.addAttribute("end_date", DateUtils.getDayLastTime(today));
		return "report/MpageFlowIp"; 
	}
	
	/**
	 * 下单数(分页)
	 */
	@ResponseBody
	@RequestMapping("mpage_ip/page")
	public Page mpage_ip_page(String start_date, String end_date, Page page) {
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 执行的 SQL
		String sql = null; String count_sql = null;
		if (StringUtils.isNotBlank(start_date)
				&& StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(IP) QUANTITY FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY MPAGE, IP) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, COUNT(IP) QUANTITY FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY MPAGE, IP) A GROUP BY MPAGE) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(IP) QUANTITY FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP WHERE REPORT_DATE >= :S1 GROUP BY MPAGE, IP) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, COUNT(IP) QUANTITY FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP WHERE REPORT_DATE >= :S1 GROUP BY MPAGE, IP) A GROUP BY MPAGE) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(IP) QUANTITY FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP WHERE REPORT_DATE <= :E1 GROUP BY MPAGE, IP) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, COUNT(IP) QUANTITY FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP WHERE REPORT_DATE <= :E1 GROUP BY MPAGE, IP) A GROUP BY MPAGE) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(IP) QUANTITY FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP GROUP BY MPAGE, IP) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) FROM (SELECT MPAGE, COUNT(IP) FROM (SELECT MPAGE, IP FROM REPORT_MPAGE_IP GROUP BY MPAGE, IP) A GROUP BY MPAGE) A").toString();
		}
		
		// 排序条件
		String order = page.getParam().orderBy("QUANTITY");
		sql = new StringBuilder(sql).append(" ORDER BY ").append(order).append(" DESC").toString();
		
		// 分页参数
		Integer pageIndex = page.getParam().getPageIndex();
		Integer pageSize = page.getParam().getPageSize();
		sql = new StringBuilder(sql).append(" LIMIT ").append((pageIndex -1)* pageSize).append(", ").append(pageSize).toString();
		
		// 总数
	    Integer count = JdbcSqlExecutor.count(count_sql, param);
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("MPAGE", rs.getObject("MPAGE"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				Long id = rs.getLong("MPAGE");
				Mpage product = pageService.get(id);
				if (product != null) {
					vals.put("PRODUCT_NAME", product.getName());
				}
				return vals;
			}
		});
		
		// 构造page对象
		page.setData(stats);
		PageParameters pageParameters = new PageParameters(pageIndex, pageSize, count);
		page.setPage(pageParameters);
		return page;
	}
	
	/**
	 * 网站流量
	 * @return
	 */
	@RequestMapping("mpage_uv")
	public String mpageUv(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_MPAGE_UV A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY UV) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_MPAGE_UV A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2 GROUP BY UV) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_MPAGE_UV A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3 GROUP BY UV) A")
				.append(" UNION ALL ")
				.append("SELECT COUNT(1) C FROM (SELECT UV FROM REPORT_MPAGE_UV GROUP BY UV) A").toString();
		
		Date today = DateUtils.getTodayTime();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		param.put("S3", DateUtils.getMonthFirstDate(today));
		param.put("E3", DateUtils.getMonthLastDate(today));
		param.put("S2", DateUtils.getWeekFirstDate(today));
		param.put("E2", DateUtils.getWeekLastDate(today));
		param.put("S1", DateUtils.getDayFirstTime(today));
		param.put("E1", DateUtils.getDayLastTime(today));
		
		// 获得查询结果
		List<Integer> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt("C");
			}
		});
		// 查询的条件
		model.addAttribute("stats", stats);
		model.addAttribute("start_date", DateUtils.getDayFirstTime(today));
		model.addAttribute("end_date", DateUtils.getDayLastTime(today));
		return "report/MpageFlowUv"; 
	}
	
	/**
	 * 下单数(分页)
	 */
	@ResponseBody
	@RequestMapping("mpage_uv/page")
	public Page mpage_uv_page(String start_date, String end_date, Page page) {
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 执行的 SQL
		String sql = null; String count_sql = null;
		if (StringUtils.isNotBlank(start_date)
				&& StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(UV) QUANTITY FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY MPAGE, UV) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, COUNT(UV) QUANTITY FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY MPAGE, UV) A GROUP BY MPAGE) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(UV) QUANTITY FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV WHERE REPORT_DATE >= :S1 GROUP BY MPAGE, UV) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, COUNT(UV) QUANTITY FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV WHERE REPORT_DATE >= :S1 GROUP BY MPAGE, UV) A GROUP BY MPAGE) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(UV) QUANTITY FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV WHERE REPORT_DATE <= :E1 GROUP BY MPAGE, UV) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MPAGE, COUNT(UV) QUANTITY FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV WHERE REPORT_DATE <= :E1 GROUP BY MPAGE, UV) A GROUP BY MPAGE) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT MPAGE, COUNT(UV) QUANTITY FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV GROUP BY MPAGE, UV) A GROUP BY MPAGE").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) FROM (SELECT MPAGE, COUNT(UV) FROM (SELECT MPAGE, UV FROM REPORT_MPAGE_UV GROUP BY MPAGE, UV) A GROUP BY MPAGE) A").toString();
		}
		
		// 排序条件
		String order = page.getParam().orderBy("QUANTITY");
		sql = new StringBuilder(sql).append(" ORDER BY ").append(order).append(" DESC").toString();
		
		// 分页参数
		Integer pageIndex = page.getParam().getPageIndex();
		Integer pageSize = page.getParam().getPageSize();
		sql = new StringBuilder(sql).append(" LIMIT ").append((pageIndex -1)* pageSize).append(", ").append(pageSize).toString();
		
		// 总数
	    Integer count = JdbcSqlExecutor.count(count_sql, param);
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("MPAGE", rs.getObject("MPAGE"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				Long id = rs.getLong("MPAGE");
				Mpage product = pageService.get(id);
				if (product != null) {
					vals.put("PRODUCT_NAME", product.getName());
				}
				return vals;
			}
		});
		
		// 构造page对象
		page.setData(stats);
		PageParameters pageParameters = new PageParameters(pageIndex, pageSize, count);
		page.setPage(pageParameters);
		return page;
	}
}