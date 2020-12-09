package com.tmt.task;

import java.util.Date;
import java.util.Map;

import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;

/**
 * 基本的报表功能
 * @author lifeng
 */
public abstract class AbstractReportTask implements TaskExecutor{

	@Override
	public Boolean doTask(Task arg0) {
		
		// 统计的结束时间比当前时间晚 10 分钟
		Date eDate = new Date(DateUtils.getTimeStampNow().getTime() - 10 * 60 * 1000);
		
		// 运行时间
		Map<String, Date> dates = Maps.newHashMap();
		
		// 报表的运行参数
		Period period = StringUtils.isNotBlank(arg0.getParams())? 
			   JsonMapper.fromJson(arg0.getParams(), Period.class) : null;
		if (period == null) {
			period = new Period();
			period.setS(DateUtils.getTodayDate());
			period.setE(eDate);
		} else {
			Period newPeriod = new Period();
			newPeriod.setS(period.getE());
			newPeriod.setE(eDate);
			period = newPeriod;
		}
		
		// 执行的时间
		dates.put("start_date", period.getS());
		dates.put("end_date", period.getE());
		
		// 下次执行的依据
		arg0.setParams(JsonMapper.toJson(period));
		
		// 执行具体的逻辑
		this.doInner(dates);
		return Boolean.TRUE;
	}
	
	/**
	 * 具体的执行过程
	 * @param dates
	 */
	protected abstract void doInner(Map<String, Date> dates);
	
	
	/**
	 * 周期
	 * @author lifeng
	 */
	public static class Period {
		private Date s;
		private Date e;
		public Date getS() {
			return s;
		}
		public void setS(Date s) {
			this.s = s;
		}
		public Date getE() {
			return e;
		}
		public void setE(Date e) {
			this.e = e;
		}
	}
}
