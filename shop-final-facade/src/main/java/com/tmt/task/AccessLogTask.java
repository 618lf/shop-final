package com.tmt.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.Constants;
import com.tmt.core.persistence.dialect.Dialect;
import com.tmt.core.persistence.dialect.MySQLDialect;
import com.tmt.core.utils.FileUtils;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;

/**
 * 访问统计（为避免冲突，处理前两分钟的文件） 例如：2016-08-30-14-55 时处理(结尾的文件) 2016-08-30-14-53 的文件
 * 每分钟执行一次 : /home/logs/access/2016-08-30-14-55.log 1. 锁文件
 * /home/logs/access/2016-08-30-14-55.log.lock 2. 处理文件 3. 转移文件 -- 删除
 * 
 * @author lifeng
 */
public class AccessLogTask implements TaskExecutor {

	private final String logPath; // 日志文件路径
	private byte offsetMinute = -2; // 分钟偏移量
	private byte logNum = 3; // 同时处理的文件数

	/**
	 * 直接的数据库访问方式
	 */
	@Autowired
	private Dialect dialect;

	/**
	 * 日志文件的路径
	 * 
	 * @param logPath
	 */
	public AccessLogTask() {
		this.logPath = this.getLogPath();
	}

	@Override
	public Boolean doTask(Task task) {

		// 日志路径
		if (!StringUtils.isNoneBlank(logPath)) {
			return Boolean.TRUE;
		}

		// 锁文件
		List<File> files = this.getLogableFiles();

		if (files != null && files.size() != 0) {
			for (File file : files) {
				this.storeByDialect(file);
			}
		}

		// for gc
		files.clear();
		files = null;
		return Boolean.TRUE;
	}

	/**
	 * 日志路径
	 * 
	 * @return
	 */
	private String getLogPath() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		Appender<ILoggingEvent> appender = loggerContext.getLogger(Constants.SYS_ACCESS).getAppender("ASYNC_ACCESS");
		AsyncAppender asyncAppender = (AsyncAppender) appender;
		FileAppender<ILoggingEvent> fileAppender = (FileAppender<ILoggingEvent>) asyncAppender.getAppender("ACCESS");
		return new File(fileAppender.getFile()).getParent();
	}

	/**
	 * 存储数据
	 * 
	 * @param file
	 */
	private void storeByDialect(File file) {
		if (dialect instanceof MySQLDialect) {
			storeToMysql(file);
		} else {
			storeToOther(file);
		}
	}

	/**
	 * 存储到数据库 1百万条数据 18S
	 */
	private void storeToOther(File file) {
		String bulk_Load_Sql = "INSERT INTO SYS_LOG (ID, TYPE, CREATE_ID, CREATE_NAME, CREATE_DATE, REMOTE_ADDR, USER_AGENT, REQUEST_URI, METHOD, DEAL_TIME, EXCEPTION, REFERER) values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)";
		BufferedReader reader = null;
		try {
			// 缓存设置为1M
			reader = new BufferedReader(new FileReader(file), 1 * 1024 * 1024);

			// 批量存储的sql
			List<String> batchSql = Lists.newArrayList();

			// 每次读取一行
			String line = null;
			int size = 0;
			while ((line = reader.readLine()) != null) {
				String sql = StringUtils.format(bulk_Load_Sql, this.parseLine(line));
				batchSql.add(sql);
				if (size++ >= 1500) {
					dialect.bulkSave(batchSql, null);
					batchSql.clear();
					size = 0;
				}
			}

			// 遗留处理
			if (batchSql.size() != 0) {
				dialect.bulkSave(batchSql, null);
				batchSql.clear();
			}

			// 删除日志文件
			file.delete();

			// for gc
			batchSql = null;
			file = null;
		} catch (Exception e) {
			logger.error("保存日志文件异常", e);
			File newFile = new File(file.getParent(), StringUtils.substringBefore(file.getName(), ".lock"));
			file.renameTo(newFile);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	// 解析 line
	private Object[] parseLine(String line) {
		String[] params = new String[] { null, null, null, null, null, null, null, null, null, null, null, null };
		Object[] args = line.split("\\t");
		for (int i = 0; i < args.length; i++) {
			params[i] = String.valueOf(args[i]);
			if (params[i].equals("\\N")) {
				params[i] = null;
			}
			params[i] = StringUtils.escapeDb(params[i]);
			if (params[i] != null) {
				params[i] = "'" + params[i] + "'";
			}
		}
		return params;
	}

	/**
	 * 存储到数据库 1百万条数据 18S
	 */
	private void storeToMysql(File file) {
		List<String> bulk_Load_Sql = Collections.singletonList(
				"LOAD DATA LOCAL INFILE 'LOGS.SQL' IGNORE INTO TABLE SYS_LOG (ID, TYPE, CREATE_ID, CREATE_NAME, CREATE_DATE, REMOTE_ADDR, USER_AGENT, REQUEST_URI, METHOD, DEAL_TIME, EXCEPTION, REFERER)");
		BufferedReader reader = null;
		try {
			// 缓存设置为1M
			reader = new BufferedReader(new FileReader(file), 1 * 1024 * 1024);

			// 1500 个存一次数据库
			StringBuilder logs = new StringBuilder();

			// 每次读取一行
			String line = null;
			int size = 0;
			while ((line = reader.readLine()) != null) {
				logs.append(line).append("\n");
				if (size++ >= 1500) {
					dialect.bulkSave(bulk_Load_Sql, logs.toString().getBytes());
					logs.setLength(0);
					size = 0;
				}
			}

			// 遗留处理
			if (logs.length() != 0) {
				dialect.bulkSave(bulk_Load_Sql, logs.toString().getBytes());
				logs.setLength(0);
			}

			// 删除日志文件
			file.delete();

			// for gc
			logs = null;
			file = null;
		} catch (Exception e) {
			logger.error("保存日志文件异常", e);
			File newFile = new File(file.getParent(), StringUtils.substringBefore(file.getName(), ".lock"));
			file.renameTo(newFile);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	// 得到需要日结的文件(最多1个)
	private synchronized List<File> getLogableFiles() {

		// 获取需要日结的文件
		final String nows = DateUtils.getFormatDate(DateUtils.addMinutes(DateUtils.getTodayTime(), offsetMinute),
				"yyyy-MM-dd-HH-mm");

		String[] files = null;
		File parent = FileUtils.getFile(logPath);
		files = parent.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if (!StringUtils.endsWith(name, ".log")) {
					return false;
				}

				// 早于指定时间的
				String _name = StringUtils.substringBefore(name, ".");
				if (_name.compareTo(nows) <= 0) {
					return true;
				}
				return false;
			}
		});

		// 重命名
		List<File> _files = Lists.newArrayList();
		if (files != null && files.length != 0) {
			int i = this.logNum;
			Arrays.sort(files);
			for (String file : files) {

				// 重命名
				File newFile = new File(parent, new StringBuilder(file).append(".lock").toString());
				new File(parent, file).renameTo(newFile);
				_files.add(newFile);

				// 控制最大数
				if (--i <= 0) {
					break;
				}
			}
		}

		return _files;
	}

	@Override
	public String getName() {
		return "保存日志信息";
	}
}