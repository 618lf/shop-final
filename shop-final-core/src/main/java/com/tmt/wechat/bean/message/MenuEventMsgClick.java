package com.tmt.wechat.bean.message;


/**
 * 自定义菜单事件, EVENT值为CLICK
 * 
 * 用户点击自定义菜单后，如果菜单按钮设置为click类型，则微信会把此次点击事件推送给开发者， 注意view类型（跳转到URL）的菜单点击不会上报。
 * 
 * @author rikky.cai
 * @qq:6687523
 * @Email:6687523@qq.com
 * 
 */
public class MenuEventMsgClick extends MenuEventMsg {

	private static final long serialVersionUID = 1L;

}
