package com.tmt.common.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.tmt.common.entity.ColumnMapper;
import com.tmt.common.excel.exp.IExportFile;
import com.tmt.common.excel.exp.impl.DefaultExportFile;

public class ExcelExpUtils {

	/**
	 * 构建导出参数
	 * 
	 * @param fileName
	 *            -- 导出的文件名称
	 * @param title
	 *            -- Excel 文件中的title(sheet页的名称)
	 * @param columns
	 *            -- 要导出的列 key和 columns 中的property对应
	 * @param vaules
	 *            -- 要导出的数据通过 key和 columns 中的property对应
	 * @return
	 */
	public static <T> Map<String, Object> buildExpParams(String fileName, String title, List<ColumnMapper> columns,
			List<T> vaules) {
		Map<String, Object> datas = Maps.newHashMap();
		datas.put(IExportFile.EXPORT_COLUMNS, columns);
		datas.put(IExportFile.EXPORT_FILE_NAME, fileName);
		datas.put(IExportFile.EXPORT_FILE_TITLE, title);
		datas.put(IExportFile.EXPORT_VALUES, vaules);
		return datas;
	}

	/**
	 * 根据模板构建导出参数
	 * 
	 * @param fileName
	 *            -- 导出的文件名称
	 * @param title
	 *            -- Excel 文件中的title(sheet页的名称)
	 * @param columns
	 *            -- 要导出的列 key和 columns 中的property对应
	 * @param vaules
	 *            -- 要导出的数据通过 key和 columns 中的property对应
	 * @param templatenName
	 *            -- 要导出的模板名，模板默认放在WIN-INF/template/excel下面
	 * @param startRow
	 *            -- 要导出的模板从第几行开始写数据，值为开始写数据行减一（若从sheet第3行开始写数据就要写2）
	 * @return
	 */
	public static <T> Map<String, Object> buildExpParams(String fileName, String title, List<ColumnMapper> columns,
			List<T> vaules, String templatenName, int startRow) {
		Map<String, Object> datas = Maps.newHashMap();
		datas.put(IExportFile.EXPORT_COLUMNS, columns);
		datas.put(IExportFile.EXPORT_FILE_NAME, fileName);
		datas.put(IExportFile.EXPORT_FILE_TITLE, title);
		datas.put(IExportFile.EXPORT_VALUES, vaules);
		datas.put(IExportFile.TEMPLATE_NAME, templatenName);
		datas.put(IExportFile.TEMPLATE_START_ROW, startRow);
		return datas;
	}

	/**
	 * 直接构建Excel 文件
	 * 
	 * @param fileName
	 * @param title
	 * @param columns
	 * @param vaules
	 * @param templatenName
	 * @param startRow
	 * @return
	 */
	public static <T> File buildExcelFile(String fileName, String title, List<ColumnMapper> columns, List<T> vaules,
			String templatenName, int startRow) {
		try {
			Map<String, Object> data = buildExpParams(fileName, title, columns, vaules, templatenName,
					startRow);
			return new DefaultExportFile().build(data);
		} catch (Exception e) {
			return null;
		}
	}
}
