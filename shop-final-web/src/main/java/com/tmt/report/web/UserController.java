package com.tmt.report.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.tree.RowMapper;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;

/**
 * 用户统计
 * @author lifeng
 */
@Controller("reportUserController")
@RequestMapping(value = "${adminPath}/report/user")
public class UserController {

	/**
	 * 用户登录
	 */
	@ResponseBody
	@RequestMapping("a")
	public List<Integer> a() {
		
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(QUANTITY) C FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3").toString();
		
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
		
		return stats;
	}
	
	/**
	 * 用户注册
	 */
	@ResponseBody
	@RequestMapping("z")
	public List<Integer> z() {
		
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(QUANTITY) C FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3").toString();
		
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
		
		return stats;
	}
	
	/**
	 * 下单数
	 */
	@ResponseBody
	@RequestMapping("stat")
	public List<Map<String, Object>> stat() {
		
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT A.USER_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER GROUP BY USER_ID, USER_NAME) A ORDER BY A.AMOUNT DESC LIMIT 10").toString();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("USER_NAME", rs.getObject("USER_NAME"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				vals.put("AMOUNT", rs.getObject("AMOUNT"));
				return vals;
			}
		});
		
		return stats;
	}
	
	/**
	 * 下单数(分页)
	 */
	@RequestMapping("stat_page")
	public String stat(Model model) {
		return "report/UserOrder";
	}
	
	/**
	 * 下单数(分页)
	 */
	@ResponseBody
	@RequestMapping("stat_page/page")
	public Page stat(String start_date, String end_date, Page page) {
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
				
		// 执行的 SQL
		String sql = null; String count_sql = null;
		if (StringUtils.isNotBlank(start_date)
				&& StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT A.USER_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY USER_ID, USER_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY USER_ID, USER_NAME) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT A.USER_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER WHERE REPORT_DATE >= :S1 GROUP BY USER_ID, USER_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER WHERE REPORT_DATE >= :S1 GROUP BY USER_ID, USER_NAME) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT A.USER_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER WHERE REPORT_DATE <= :E1 GROUP BY USER_ID, USER_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER WHERE REPORT_DATE <= :E1 GROUP BY USER_ID, USER_NAME) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT A.USER_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER GROUP BY USER_ID, USER_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(USER_NAME) USER_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_USER_ORDER GROUP BY USER_ID, USER_NAME) A").toString();
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
				vals.put("USER_NAME", rs.getObject("USER_NAME"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				vals.put("AMOUNT", rs.getObject("AMOUNT"));
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
	 * 新增用户
	 * @return
	 */
	@RequestMapping("new")
	public String _new(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(QUANTITY) C FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERZ A").toString();
		
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
		return "report/UserNew";
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping("new_data")
	public List<Stat> _new_data(String start_date, String end_date) {
		if (StringUtils.isBlank(start_date) || StringUtils.isBlank(end_date)) {
			start_date = end_date = DateUtils.getTodayStr();
		}
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
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%H') REPORT_DATE, SUM(QUANTITY) QUANTITY FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%H')").toString();
		} else {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d') REPORT_DATE, SUM(QUANTITY) QUANTITY FROM REPORT_USERZ A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d')").toString();
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
	 * 新增用户
	 * @return
	 */
	@RequestMapping("rank")
	public String rank(Model model) {
		
		// 查询的sql
		String sql = "SELECT A.NAME, IFNULL(B.C, 0) C FROM SHOP_RANK A LEFT JOIN (SELECT A.GRADE, COUNT(1) C FROM SHOP_RANK A, SHOP_USER_RANK B WHERE A.ID = B.RANK_ID GROUP BY A.GRADE) B ON A.GRADE = B.GRADE";
		Map<String, Date> param = Maps.newHashMap();
		List<Stat> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Stat>() {
			@Override
			public Stat mapRow(ResultSet rs, int rowNum) throws SQLException {
				Stat stat = new Stat();
				stat.setX(rs.getString("NAME"));
				stat.setY(rs.getObject("C"));
				return stat;
			}
		});
		model.addAttribute("stats", stats);
		return "report/UserRank";
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@RequestMapping("active")
	public String active(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(QUANTITY) C FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3")
				.append(" UNION ALL ")
				.append("SELECT SUM(QUANTITY) C FROM REPORT_USERA A").toString();
		
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
		return "report/UserActive";
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping("active_data")
	public List<Stat> active_data(String start_date, String end_date) {
		if (StringUtils.isBlank(start_date) || StringUtils.isBlank(end_date)) {
			start_date = end_date = DateUtils.getTodayStr();
		}
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
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%H') REPORT_DATE, SUM(QUANTITY) QUANTITY FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%H')").toString();
		} else {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d') REPORT_DATE, SUM(QUANTITY) QUANTITY FROM REPORT_USERA A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d')").toString();
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
}