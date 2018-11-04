package com.tmt.system.entity;


/**
 * 系统常量
 * @author root
 */
public class SystemConstant{

	//字典缓存名称
	public static final String T = "T#";
	public static final String C = "V";
	
	//用户组缓存名称
	public static final String CACHE_GROUP_MENUS = "GROUP_MENUS#";
	public static final String CACHE_GROUP = "GROUP#";
	
	//消息缓存名称
	public static final String CACHE_MSG = "MSG_T#";
	
	//消息配置缓存
	public static final String CACHE_MSG_CONFIG = "MSG_C#";
	
	//组织结构缓存名称
	public static final String CACHE_OFFICE = "OFFICE#";
	
	//站点缓存名称
	public static final String CACHE_SITE = "SITE";
	
	//全局会员编码，注册时生成
	public static final String GLOBAL_MEMBER_CODE = "GLOBAL_MEMBER_CODE";
	
	//任务
	public static final String TASK_PREFIX = "TASK-";
	public static final String RUNNING_ABLE_TASKS = "SYS-TASKS";
	
	//用户
	public static final String CACHE_USER = "U#";
	public static final String CACHE_MENU_LIST = "UM#";
	public static final String LOGIN_VALIDATE_COUNT = "LOGIN_VALIDATE_COUNT#";
	public static final Integer DEFAULT_FAILE_COUNT = 3;
	public static final Integer DEFAULT_LOCK_TIME = 30;
	
	//通用
	public static final String CACHE_ALL = "*";
	public static final String CACHE_DIV = "#";
	
	//线程变量
	public static final String USER_KEY = "USER_KEY";
	public static final String SITE_KEY = "SITE_KEY";
	
	//信号量
	public static final Byte USER_IN = 127; // 登录
	public static final Byte USER_UP = 126; // 注册
	public static final Byte USER_OUT = 125; // 退出
	public static final Byte TASK_UPDATE = 126; // 定时任务
}