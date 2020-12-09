package com.tmt.report.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.tree.RowMapper;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.tmt.shop.service.ProductServiceFacade;

/**
 * 订单统计
 * @author lifeng
 */
@Controller("reportProductController")
@RequestMapping(value = "${adminPath}/report/product")
public class ProductController {

	@Autowired
	private ProductServiceFacade productService;
	
	/**
	 * 订单数
	 */
	@ResponseBody
	@RequestMapping("a")
	public List<Map<String, Object>> a() {
		
		// 执行的 SQL
		String sql = new StringBuilder().append("SELECT A.PRODUCT_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT GROUP BY PRODUCT_ID, PRODUCT_NAME) A ORDER BY A.QUANTITY DESC LIMIT 10").toString();
		
		// 查询的条件
		Map<String, Date> param = Maps.newHashMap();
		
		// 获得查询结果
		List<Map<String, Object>> stats = JdbcSqlExecutor.query(sql, param, new RowMapper<Map<String, Object>>() {
			@Override
			public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
				Map<String, Object> vals = Maps.newHashMap();
				vals.put("PRODUCT_NAME", rs.getObject("PRODUCT_NAME"));
				vals.put("QUANTITY", rs.getObject("QUANTITY"));
				return vals;
			}
		});
		
		return stats;
	}
	
	/**
	 * 商品统计
	 * @return
	 */
	@ResponseBody
	@RequestMapping("stat")
	public Map<String, Integer> stat() {
		return productService.statProduct();
	}
	
	/**
	 * 下单数(分页)
	 */
	@RequestMapping("stat_page")
	public String stat(Model model) {
		return "report/ProductOrder";
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
			sql = new StringBuilder().append("SELECT A.PRODUCT_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT WHERE REPORT_DATE BETWEEN :S1 AND :E1 GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(start_date)) {
			sql = new StringBuilder().append("SELECT A.PRODUCT_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT WHERE REPORT_DATE >= :S1 GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
			param.put("S1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(start_date, "yyyy-MM-dd")));
		} else if(StringUtils.isNotBlank(end_date)) {
			sql = new StringBuilder().append("SELECT A.PRODUCT_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT WHERE REPORT_DATE <= :E1 GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
			param.put("E1", DateUtils.getDayFirstTime(DateUtils.getFormatDate(end_date, "yyyy-MM-dd")));
		} else {
			sql = new StringBuilder().append("SELECT A.PRODUCT_NAME, A.QUANTITY, A.AMOUNT FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
			count_sql = new StringBuilder().append("SELECT COUNT(1) C FROM (SELECT MAX(PRODUCT_NAME) PRODUCT_NAME, SUM(QUANTITY) QUANTITY, SUM(AMOUNT) AMOUNT FROM REPORT_PRODUCT GROUP BY PRODUCT_ID, PRODUCT_NAME) A").toString();
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
				vals.put("PRODUCT_NAME", rs.getObject("PRODUCT_NAME"));
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
}
