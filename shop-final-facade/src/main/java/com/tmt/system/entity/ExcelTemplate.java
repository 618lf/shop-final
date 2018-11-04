package com.tmt.system.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.common.entity.BaseEntity;
import com.tmt.common.entity.ColumnMapper;
import com.tmt.common.utils.Lists;

public class ExcelTemplate extends BaseEntity<Long> implements Serializable{
   
	private static final long serialVersionUID = 1L;
    private String type;
    private String targetClass;
    private Byte startRow;
    private String extendAttr;
    private List<ExcelItem> items;
    
	public List<ExcelItem> getItems() {
		return items;
	}
	public void setItems(List<ExcelItem> items) {
		this.items = items;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTargetClass() {
		return targetClass;
	}
	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}
	public Byte getStartRow() {
		return startRow;
	}
	public void setStartRow(Byte startRow) {
		this.startRow = startRow;
	}
	public String getExtendAttr() {
		return extendAttr;
	}
	public void setExtendAttr(String extendAttr) {
		this.extendAttr = extendAttr;
	}
	
	/**
	 * @ExcelItem --> @ColumnMapper
	 * @return
	 */
	public List<ColumnMapper> getItemsMapper() {
		List<ColumnMapper> mappers = Lists.newArrayList();
		if(this.items != null && this.items.size() != 0) {
			for(ExcelItem item: this.items) {
				mappers.add(item.toMapper());
			}
		}
		return mappers;
	}
}