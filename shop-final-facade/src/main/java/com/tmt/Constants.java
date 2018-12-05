package com.tmt;

/**
 * 系统常量
 * 
 * @author root
 */
public interface Constants {

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

	// ######### update #########
	// Wechat
	Byte META = 21;
	Byte META_TEXT = 22;
	Byte META_IMAGE = 23;
	Byte USER_OPS = 24;

	// System
	Byte USER_IN = 1; // 登录
	Byte USER_UP = 2; // 注册
	Byte USER_OUT = 3; // 退出
	Byte TASK_UPDATE = 4; // 定时任务
}