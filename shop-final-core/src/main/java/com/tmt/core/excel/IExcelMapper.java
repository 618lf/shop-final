package com.tmt.core.excel;

import java.util.Map;

import org.apache.poi.ss.usermodel.Sheet;

import com.tmt.core.entity.ColumnMapper;

/**
 * 导入的配置项，读取Excel 的配置项
 * 
 * @author liFeng 2014年9月22日
 */
public interface IExcelMapper<T> {

	/**
	 * 读取Excel 的起始行
	 * 
	 * @return
	 */
	int getStartRow();

	/**
	 * 返回当有错误时
	 * 
	 * @return
	 */
	Boolean returnWhenError();

	/**
	 * 默认使用的 Map 是有序还是无顺的： 默认 无序
	 * 
	 * @return
	 */
	default Boolean columnOrder() {
		return false;
	};

	/**
	 * 通过对应的列得到 对应的列映射
	 * 
	 * @param column
	 * @return
	 */
	Iterable<ColumnMapper> getColumnMappers(String column);

	/**
	 * 得到目标类型
	 * 
	 * @return
	 */
	Class<?> getTargetClass();

	/**
	 * 得到类型转化器
	 * 
	 * @return
	 */
	T receive(Map<String, Object> valueMap);

	/**
	 * 得到Excel的数据
	 * 
	 * @param sheet
	 * @return
	 */
	ImportResult<T> getExcelData(Sheet sheet);

}
