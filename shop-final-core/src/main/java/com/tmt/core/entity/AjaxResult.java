package com.tmt.core.entity;

import java.io.Serializable;

import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.StringUtils;

/**
 * $.ajax后需要接受的JSON
 * 
 * 此类返回的信息，特别是错误的信息，应该可以设定不同的格式模版， 例如 错误类别：详细信息，系统应提供几个错误模版
 * 成功也是一样的，也可以提供一个模版，可以专门提供一个消息处理类
 * 
 * @author
 */
public class AjaxResult implements Serializable {

	private static final long serialVersionUID = 80931494242205860L;

	private Boolean success = Boolean.TRUE;// 是否成功
	private String msg = "操作成功";// 提示信息
	private Object obj = null;// 返回对象

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObj() {
		return (T) obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public static AjaxResult success() {
		AjaxResult result = new AjaxResult();
		result.setSuccess(Boolean.TRUE);
		return result;
	}

	public static AjaxResult success(Object o) {
		AjaxResult result = AjaxResult.success();
		result.setSuccess(Boolean.TRUE);
		result.setObj(o);
		return result;
	}

	public static AjaxResult error(String msg) {
		AjaxResult result = new AjaxResult();
		result.setSuccess(Boolean.FALSE);
		result.setMsg(msg);
		return result;
	}

	public static AjaxResult error(String msg, Object o) {
		AjaxResult result = new AjaxResult();
		result.setSuccess(Boolean.FALSE);
		result.setMsg(msg);
		result.setObj(o);
		return result;
	}

	/**
	 * 格式化输出 %s 代表 msg数组中的一个
	 * 
	 * @param template
	 * @param msg
	 * @return
	 */
	public static AjaxResult error(String template, Object... msg) {
		return error(StringUtils.format(template, msg));
	}

	/**
	 * 得到json格式的数据
	 * 
	 * @return
	 */
	public String toJson() {
		return JsonMapper.toJson(this);
	}
}