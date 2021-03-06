package com.tmt.gen.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 方案
 * @author lifeng
 *
 */
public class Scheme extends BaseEntity<Long> implements Serializable{
    
	private static final long serialVersionUID = 974399490610094603L;
	
	private String name;
    private String category;
    private String packageName;
    private String moduleName;
    private String subModuleName;
    private String functionName;
    private String functionNameSimple;
    private String functionAuthor;
    private Long genTableId;
    private String genTableName;
    //右表地址
    private String rightTable;
    
    //功能支持
    private Byte isImport;
    private Byte isExport;
    private Byte treeSelect;
    private Byte tableSelect;
    
	//标识提交的方式:仅保存、保存并生成代码
    private Integer flag = 0;
    private Table table;//当前生成的table
    
	@Override
	public Long getId() {
		return this.id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public String getRightTable() {
		return rightTable;
	}
	public void setRightTable(String rightTable) {
		this.rightTable = rightTable;
	}
	public Table getTable() {
		return table;
	}
	public void setTable(Table table) {
		this.table = table;
	}
	public String getGenTableName() {
		return genTableName;
	}
	public void setGenTableName(String genTableName) {
		this.genTableName = genTableName;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getSubModuleName() {
		return subModuleName;
	}
	public void setSubModuleName(String subModuleName) {
		this.subModuleName = subModuleName;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	public String getFunctionNameSimple() {
		return functionNameSimple;
	}
	public void setFunctionNameSimple(String functionNameSimple) {
		this.functionNameSimple = functionNameSimple;
	}
	public String getFunctionAuthor() {
		return functionAuthor;
	}
	public void setFunctionAuthor(String functionAuthor) {
		this.functionAuthor = functionAuthor;
	}
	public Long getGenTableId() {
		return genTableId;
	}
	public void setGenTableId(Long genTableId) {
		this.genTableId = genTableId;
	}
	public Byte getIsImport() {
		return isImport == null?0:isImport;
	}
	public void setIsImport(Byte isImport) {
		this.isImport = isImport;
	}
	public Byte getIsExport() {
		return isExport == null?0:isExport;
	}
	public void setIsExport(Byte isExport) {
		this.isExport = isExport;
	}
	public Byte getTreeSelect() {
		return treeSelect == null?0:treeSelect;
	}
	public void setTreeSelect(Byte treeSelect) {
		this.treeSelect = treeSelect;
	}
	public Byte getTableSelect() {
		return tableSelect == null?0:tableSelect;
	}
	public void setTableSelect(Byte tableSelect) {
		this.tableSelect = tableSelect;
	}
}