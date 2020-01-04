package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.entity.ColumnMapper;
import com.tmt.core.entity.DataType;

public class ExcelItem extends BaseEntity<Long> implements Serializable{
    
	private static final long serialVersionUID = 1L;
    private Long templateId;
    private String columnName;
    private String property;
    private DataType dataType;
    private String dataFormat;
    private String verifyFormat;
    
	public String getVerifyFormat() {
		return verifyFormat;
	}
	public void setVerifyFormat(String verifyFormat) {
		this.verifyFormat = verifyFormat;
	}
	public Long getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public DataType getDataType() {
		return dataType;
	}
	public String getDataTypeStr(){
		if( dataType != null) {return dataType.getName();}
		return null;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	public String getDataFormat() {
		return dataFormat;
	}
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	public ColumnMapper toMapper() {
		ColumnMapper mapper = new ColumnMapper();
		mapper.setTitle(this.getName());
		mapper.setColumn(this.getColumnName());
		mapper.setDataFormat(this.getDataFormat());
		mapper.setDataType(this.getDataType());
		mapper.setProperty(this.getProperty());
		mapper.setVerifyFormat(this.getVerifyFormat());
		return mapper;
	}
}