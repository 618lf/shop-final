package com.tmt.report.web;

import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;

/**
 * 时间
 * @author lifeng
 */
@Controller("reportTimesController")
@RequestMapping(value = "${adminPath}/report/times")
public class TimesController {

	/**
	 * 今天
	 * @return
	 */
	@ResponseBody
	@RequestMapping("today")
	public Map<String, String> today() {
		Map<String, String> times = Maps.newHashMap();
		Date today = DateUtils.getTodayDate();
		times.put("start_date", DateUtils.getFormatDate(DateUtils.getDayFirstTime(today), "yyyy-MM-dd"));
		times.put("end_date", DateUtils.getFormatDate(DateUtils.getDayLastTime(today), "yyyy-MM-dd"));
		return times;
	}
	
	/**
	 * 本周
	 * @return
	 */
	@ResponseBody
	@RequestMapping("week")
	public Map<String, String> week() {
		Map<String, String> times = Maps.newHashMap();
		Date today = DateUtils.getTodayDate();
		times.put("start_date", DateUtils.getFormatDate(DateUtils.getWeekFirstDate(today), "yyyy-MM-dd"));
		times.put("end_date", DateUtils.getFormatDate(DateUtils.getWeekLastDate(today), "yyyy-MM-dd"));
		return times;
	}
	
	/**
	 * 本月
	 * @return
	 */
	@ResponseBody
	@RequestMapping("month")
	public Map<String, String> month() {
		Map<String, String> times = Maps.newHashMap();
		Date today = DateUtils.getTodayDate();
		times.put("start_date", DateUtils.getFormatDate(DateUtils.getMonthFirstDate(today), "yyyy-MM-dd"));
		times.put("end_date", DateUtils.getFormatDate(DateUtils.getMonthLastDate(today), "yyyy-MM-dd"));
		return times;
	}
	
	/**
	 * 上月
	 * @return
	 */
	@ResponseBody
	@RequestMapping("pmonth")
	public Map<String, String> pmonth() {
		Map<String, String> times = Maps.newHashMap();
		Date today = DateUtils.getMonthByOffset(DateUtils.getTodayDate(), -1);
		times.put("start_date", DateUtils.getFormatDate(DateUtils.getMonthFirstDate(today), "yyyy-MM-dd"));
		times.put("end_date", DateUtils.getFormatDate(DateUtils.getMonthLastDate(today), "yyyy-MM-dd"));
		return times;
	}
}