package com.tmt.common.excel.exp;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 定义导出数据源的规范
 * @author lifeng
 *
 * @param <T>
 */
public interface IExportDataSource<T> {

	/**
	 * 导出数据的入口
	 * @param request
	 * @param response
	 */
	public void export(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 实际创建导出参数的地方
	 * @param param
	 * @param request
	 * @return
	 */
	public Map<String,Object> doExport(T param, HttpServletRequest request);
	
}
