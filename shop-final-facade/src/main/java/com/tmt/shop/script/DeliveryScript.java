package com.tmt.shop.script;

import groovy.lang.Script;

import java.util.Date;

import com.tmt.common.utils.DateUtil3;

/**
 * 表达式计算基类
 * @author root
 */
public class DeliveryScript extends Script {

	@Override
	public Object run() {
		return null;
	}
	
	/**
	 * 几点钟之前 --- 和当前时间相比
	 * dates 只支持两种格式
	 * yyyy-MM-dd HH:mm:ss
	 * yyyy-MM-dd
	 * HH:mm:ss
	 * @return
	 */
	public Boolean before(String dates) {
		String _pattern = "yyyy-MM-dd HH:mm:ss";
		String pattern = null;
		if (dates.length() == 8) {
			pattern = new StringBuilder("yyyy-MM-dd ").append(dates).toString();
		} else if(dates.length() == 10){
			pattern = new StringBuilder(dates).append(" 00:00:00").toString();
		} else {
			pattern = dates;
		}
		Date currDate = DateUtil3.getTodayTime();
		String dateStr = DateUtil3.getFormatDate(currDate, pattern);
		Date formateDate = DateUtil3.getFormatDate(dateStr, _pattern);
		return currDate.compareTo(formateDate) <= 0;
	}
	
	/**
	 * 添加天数
	 * @param date
	 * @param offset
	 * @return
	 */
	public Date addDay(Date date, int offset) {
		return DateUtil3.getDateByOffset(date, offset);
	}
	
	/**
	 * 日期转string
	 * @param date
	 * @param pattern
	 * @return
	 */
	public String toStr(Date date, String pattern) {
		return DateUtil3.getFormatDate(date, pattern);
	}
	
	/**
	 * 字符串拼接
	 * @param values
	 * @return
	 */
	public String concat(String ... values){
		StringBuilder times = new StringBuilder();
		for(String value: values) {
			times.append(value);
		}
		return times.toString();
	}
}