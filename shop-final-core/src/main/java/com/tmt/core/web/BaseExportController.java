package com.tmt.core.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.core.excel.exp.IExportDataSource;

/**
 * 导出基础类 --主要针对excel导出,其他导出直接读取文件，使用ExportUtils 下载
 * @author lifeng
 *
 * @param <T> -- 对应的实体,每次会将请求中的参数转换为 T
 */
public abstract class BaseExportController<T> extends BaseController implements IExportDataSource<T>{

	private IExportDataSource<T> dataSource;

	/**
	 * 声明为一个服务
	 */
	@RequestMapping(value = {"export"})
	public void export(HttpServletRequest request, HttpServletResponse response) {
		this.getDataSource().export(request, response);
	}

	/**
	 * 如果子类没实现,则创建一个默认的
	 * @return
	 */
	public IExportDataSource<T> getDataSource() {
		if( dataSource == null) {
			dataSource = new AbstractExportDataSource<T>(){
				@Override
				public Map<String,Object> doExport(T param, HttpServletRequest request) {
					return BaseExportController.this.doExport(param, request);
				}
				@Override
				protected Class<T> getTargetClass(){
					return BaseExportController.this.getTargetClass();
				}
			};
		}
		return dataSource;
	}

	/**
	 * 子类可以去实现相应的dataSource
	 * @param dataSource
	 */
	public void setDataSource(IExportDataSource<T> dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * 子类实现
	 * @return
	 */
	protected abstract Class<T> getTargetClass();
}
