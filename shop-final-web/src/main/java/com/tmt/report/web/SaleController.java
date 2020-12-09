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
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;

/**
 * 销售额统计
 * @author lifeng
 */
@Controller("reportSaleController")
@RequestMapping(value = "${adminPath}/report/sale")
public class SaleController {

	/**
	 * 销售额
	 */
	@ResponseBody
	@RequestMapping("a")
	public List<Double> a() {
		
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(AMOUNT) C FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
			.append(" UNION ALL ")
			.append("SELECT SUM(AMOUNT) C FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
			.append(" UNION ALL ")
			.append("SELECT SUM(AMOUNT) C FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3").toString();
		
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
		List<Double> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Double>() {
			@Override
			public Double mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getDouble("C");
			}
		});
		
		return stats;
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@RequestMapping("stat")
	public String stat(Model model) {
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT SUM(AMOUNT) C FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1")
				.append(" UNION ALL ")
				.append("SELECT SUM(AMOUNT) C FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S2 AND :E2")
				.append(" UNION ALL ")
				.append("SELECT SUM(AMOUNT) C FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S3 AND :E3")
				.append(" UNION ALL ")
				.append("SELECT SUM(AMOUNT) C FROM REPORT_SALES A").toString();
		
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
		return "report/SaleStat";
	}
	
	/**
	 * 新增用户
	 * @return
	 */
	@ResponseBody
	@RequestMapping("stat_data")
	public List<Stat> stat_data(String start_date, String end_date) {
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
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%H') REPORT_DATE, SUM(AMOUNT) QUANTITY FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%H')").toString();
		} else {
			sql = new StringBuilder().append("SELECT DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d') REPORT_DATE, SUM(AMOUNT) QUANTITY FROM REPORT_SALES A WHERE A.REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY DATE_FORMAT(A.REPORT_DATE,'%Y-%m-%d')").toString();
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