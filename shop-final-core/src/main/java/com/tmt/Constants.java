package com.tmt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 系统常量
 * 
 * @author root
 */
public interface Constants {

	/** 日期 */
	String[] DATE_PATTERNS = { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM",
			"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy", "yyyyMM", "yyyy/MM", "yyyyMMddHHmmss", "yyyyMMdd" };

	/** 系统错误码 **/
	String REQUEST_ERROR_CODE_PARAM = "REQUEST_ERROR_CODE_PARAM";

	/** 配置项前缀 */
	String APPLICATION_PREFIX = "spring.application";
	String CACHE_PREFIX = "spring.cache";
	String HTTP_CLIENT_PREFIX = "spring.http-client";
	String SECURITY_PREFIX = "spring.security";
	String MYBATIS_PREFIX = "spring.mybatis";
	String DATASOURCE_PREFIX = "spring.datasource";
	String STORAGE_PREFIX = "spring.storage";
	String QUARTZ_PREFIX = "spring.quartz";
	String EVENT_BUS_PREFIX = "spring.eventbus";
	String MONGO_PREFIX = "spring.mongo";

	/** 保留ID **/
	long ROOT = 0L;
	long INVALID_ID = -1;

	/** 是/否/删除 - TINYINT **/
	byte YES = 1;
	byte NO = 0;
	byte DEL = -1;

	/** 新用户 **/
	String NEW_USER = "_nu";

	/** 默认编码 **/
	Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;

	/** permission **/
	String security_OR_DIVIDER_TOKEN = "|";
	String security_AND_DIVIDER_TOKEN = ",";
	String security_WILDCARD_TOKEN = "*";
	String security_PART_DIVIDER_TOKEN = ":";

	// 字典缓存名称
	String T = "T#";
	String C = "V";

	// 用户组缓存名称
	String CACHE_GROUP_MENUS = "GROUP_MENUS#";
	String CACHE_GROUP = "GROUP#";

	// 消息缓存名称
	String CACHE_MSG = "MSG_T#";

	// 消息配置缓存
	String CACHE_MSG_CONFIG = "MSG_C#";

	// 组织结构缓存名称
	String CACHE_OFFICE = "OFFICE#";

	// 站点缓存名称
	String CACHE_SITE = "SITE";

	// 全局会员编码，注册时生成
	String GLOBAL_MEMBER_CODE = "GLOBAL_MEMBER_CODE";

	// 任务
	String TASK_PREFIX = "TASK-";
	String RUNNING_ABLE_TASKS = "SYS-TASKS";
	String JOB_TASK_KEY = "TASK_KEY";
	String JOB_EXECUTOR_KEY = "EXECUTOR_KEY";

	// 日志
	String SYS_ACCESS = "sys-access";

	// 用户
	String CACHE_USER = "U#";
	String CACHE_MENU_LIST = "UM#";
	String LOGIN_VALIDATE_COUNT = "LOGIN_VALIDATE_COUNT#";
	Integer DEFAULT_FAILE_COUNT = 3;
	Integer DEFAULT_LOCK_TIME = 30;

	// 通用
	String CACHE_ALL = "*";
	String CACHE_DIV = "#";

	// 线程变量
	String USER_KEY = "USER_KEY";
	String SITE_KEY = "SITE_KEY";

	// 系统事件
	String EVENT_LOG = "EVENT_LOG";

}