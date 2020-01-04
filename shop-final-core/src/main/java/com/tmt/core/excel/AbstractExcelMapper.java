package com.tmt.core.excel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.poi.ss.usermodel.Sheet;

import com.tmt.core.entity.ColumnMapper;

public abstract class AbstractExcelMapper<T> implements IExcelMapper<T>, IReceiver<T> {

	protected Class<T> clazz;
	protected Multimap<String, ColumnMapper> rowMapper = null;
	protected IReceiver<T> receiver;

	@Override
	public Boolean returnWhenError() {
		return Boolean.FALSE;
	}

	@Override
	public abstract Class<T> getTargetClass();

	@Override
	public T receive(Map<String, Object> valueMap) {
		return (T) this.receive(valueMap, this.getTargetClass());
	}

	@Override
	public T receive(Map<String, Object> valueMap, Class<T> clazz) {
		return this.getReceiver().receive(valueMap, clazz);
	}

	public ImportResult<T> getExcelData(Sheet sheet) {
		return DefaultExcelExecuter.getInstance().getExcelData(this, sheet);
	}

	// 初始化
	protected abstract void initRowMapper();

	@Override
	public Iterable<ColumnMapper> getColumnMappers(String column) {
		if (rowMapper == null) {
			rowMapper = new Multimap<String, ColumnMapper>();
			this.initRowMapper();
		}
		return rowMapper.get(column);
	}

	public IReceiver<T> getReceiver() {
		if (receiver == null) {
			receiver = new DefaulReceiver<T>();
		}
		return receiver;
	}

	// 默认的转换器
	public static class DefaulReceiver<T> implements IReceiver<T> {
		@SuppressWarnings("unchecked")
		@Override
		public T receive(Map<String, Object> valueMap, Class<T> clazz) {
			if (clazz == HashMap.class) {
				return (T) valueMap;
			} else {
				T obj = null;
				try {
					obj = (T) clazz.newInstance();
					BeanUtils.copyProperties(obj, valueMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return obj;
			}

		}
	}

	/**
	 * 全局注册转换器
	 */
	static {
		ConvertUtils.register(new DateConverter(), java.util.Date.class);
		ConvertUtils.register(new BigDecimalConverter(null), java.math.BigDecimal.class);
	}
}