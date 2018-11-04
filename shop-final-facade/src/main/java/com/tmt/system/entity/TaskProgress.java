package com.tmt.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.tmt.common.utils.BigDecimalUtil;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.Ints;


/**
 * 任务进度
 * @author root
 */
public class TaskProgress implements Serializable{
	
	public static String CACHE_NAME = "TASK#";
	private static final long serialVersionUID = 2512799665378507481L;
	
	private Integer total = 0; // 总数
	private Integer done = 0;  // 已完成
	private Long startTime;// 开始时间
	private Long avgTime = 0L;// 平均时间
	private Long endTime;// 预计结束时间
	
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getDone() {
		return done;
	}
	public void setDone(Integer done) {
		this.done = done;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getAvgTime() {
		return avgTime;
	}
	public void setAvgTime(Long avgTime) {
		this.avgTime = avgTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 得到进度
	 * @return
	 */
	public double getProgress() {
		if (this.done != null && this.total != null && this.total != 0) {
			return BigDecimalUtil.div(this.done * 100, this.total, 2);
		}
		return 0;
	}
	
	/**
	 * 得到开始时间
	 * @return
	 */
	public String getStartDate() {
		return DateUtil3.getFormatDate(new Date(this.getStartTime()), "yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 得到结束时间
	 * @return
	 */
	public String getEndDate() {
		if (this.getEndTime() != null) {
			return DateUtil3.getFormatDate(new Date(this.getEndTime()), "yyyy-MM-dd HH:mm:ss");
		}
		return "";
	}
	
	/**
	 * 得到剩余时间
	 * @return
	 */
	public String getPrettySeconds() {
		if (this.getEndTime() != null) {
			int time = (int) ((this.endTime - DateUtil3.getTimeStampNow().getTime())/1000);
			return DateUtil3.prettySeconds(time);
		}
		return "";
	}
	
	/**
	 * 做了多少
	 * @param done
	 */
	public void done(Integer done) {
		this.done = Ints.addI(this.getDone(), done);
		Long time = DateUtil3.getTimeStampNow().getTime();
		if (this.done != 0) {
		    this.avgTime = (time - this.startTime) / this.done;
		    this.endTime = this.startTime + this.total * this.avgTime;
		}
	}
	
	/**
	 * 新建一个任务
	 * @param total
	 * @param done
	 * @return
	 */
	public static TaskProgress newTaskProgress(Integer total, Integer done) {
		TaskProgress progress = new TaskProgress();
		progress.setTotal(Ints.nullToZero(total));
		progress.setStartTime(DateUtil3.getTimeStampNow().getTime());
		progress.done(Ints.nullToZero(done));
		return progress;
	}
	
	/**
	 * 任务进度
	 * @param id
	 * @return
	 */
	public static TaskProgress getTaskProgress(Long taskId) {
		String key = new StringBuilder(CACHE_NAME).append(taskId).toString();
		return CacheUtils.getSessCache().get(key);
	}
	
	/**
	 * 删除进度
	 * @param id
	 * @return
	 */
	public static void removeTaskProgress(Long taskId) {
		String key = new StringBuilder(CACHE_NAME).append(taskId).toString();
		CacheUtils.getSessCache().evict(key);
	}
	
	/**
	 * 记录进度
	 * @param total
	 * @param done
	 */
	public static void progress(Long taskId, Integer total, Integer done) {
		String key = new StringBuilder(CACHE_NAME).append(taskId).toString();
		TaskProgress progress = CacheUtils.getSessCache().get(key);
		if (progress == null && total != null && total > 0) {
		    progress = TaskProgress.newTaskProgress(total, done);
		} else {
		    progress.done(done);
		}
		if (progress != null) {
		   CacheUtils.getSessCache().put(key, progress);
		}
	}
}