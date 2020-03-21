package com.tmt.core.excel;

import com.google.common.collect.Lists;
import com.tmt.core.entity.ColumnMapper;

/**
 * 全数据读取Excel文件
 * 
 * @author lifeng
 *
 * @param <T>
 */
public class FullDataExcelMapper<T> extends AbstractExcelMapper<T> {

	private int startRow;
	Iterable<ColumnMapper> mappers;

	public FullDataExcelMapper(int startRow) {
		this.startRow = startRow;
		this.initMapper();
	}

	private void initMapper() {
		ColumnMapper mapper = new ColumnMapper();
		mappers = Lists.newArrayList(mapper);
	}

	@Override
	public int getStartRow() {
		return startRow;
	}

	/**
	 * 返回唯一的列映射
	 */
	@Override
	public Iterable<ColumnMapper> getColumnMappers(String column) {
		return mappers;
	}
}