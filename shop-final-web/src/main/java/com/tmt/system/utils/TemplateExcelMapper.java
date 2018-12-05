package com.tmt.system.utils;

import com.tmt.common.entity.ColumnMapper;
import com.tmt.common.excel.AbstractExcelMapper;
import com.tmt.common.exception.BaseRuntimeException;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.ExcelItem;
import com.tmt.system.entity.ExcelTemplate;
import com.tmt.system.service.ExcelTemplateServiceFacade;

/**
 * Excel Template Mapper excel 模版映射
 * 
 * @author liFeng 2014年9月23日
 */
public class TemplateExcelMapper<T> extends AbstractExcelMapper<T> {

	// Excel模版
	private ExcelTemplate template;
	private ExcelTemplateServiceFacade templateService = null;

	public TemplateExcelMapper(Long templateId) {
		templateService = SpringContextHolder.getBean(ExcelTemplateServiceFacade.class);
		template = templateService.getWithItems(templateId);
	}

	public TemplateExcelMapper(ExcelTemplate template) {
		this.template = template;
	}

	@Override
	public int getStartRow() {
		return template.getStartRow();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<T> getTargetClass() {
		if (this.clazz == null) {
			try {
				this.clazz = (Class<T>) Class.forName(template.getTargetClass());
			} catch (ClassNotFoundException e) {
				throw new BaseRuntimeException(e.getMessage());
			}
		}
		return this.clazz;
	}

	/**
	 * 初始化配置文件信息用到的时候会加载
	 */
	protected void initRowMapper() {
		if (!(this.template != null && this.template.getItems() != null && this.template.getItems().size() != 0)) {
			return;
		}
		// 取数据
		for (ExcelItem item : this.template.getItems()) {
			ColumnMapper mapper = item.toMapper();
			rowMapper.put(mapper.getColumn(), mapper);
		}
	}
}
