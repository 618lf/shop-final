package com.tmt.core.searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;

import com.tmt.core.utils.StringUtils;

/**
 * 文档构建器
 * 
 * @author lifeng
 */
public class Documents {

	// 文档
	private Document document = new Document();
	
	public Document getDocument() {
		return document;
	}

	/**
	 * 添加数值
	 * @param name
	 * @param value
	 * @param boost
	 * @param store
	 * @param sort
	 */
	public Documents addIntField(String name, int value, boolean store, boolean sort, String boost) {
		Field field = new IntField(name, value, store?Store.YES:Store.NO);
		field = setBoost(field, boost);
		document.add(field);
		
		// 是否排序
		if (sort) {
			field = new NumericDocValuesField(name, value);
		    document.add(field);
		}
		return this;
	}
	
	/**
	 * 添加字符串
	 * @param name
	 * @param value
	 * @param boost
	 * @param store
	 * @param sort
	 */
	public Documents addStringField(String name, String value, boolean store, boolean sort, String boost) {
		Field field = new StringField(name, value, store?Store.YES:Store.NO);
		field = setBoost(field, boost);
		document.add(field);
		
		// 是否排序
		if (sort) {
			field = new SortedDocValuesField(name, new BytesRef(value));
		    document.add(field);
		}
		
		return this;
	}
	
	/**
	 * 添加文本
	 * @param name
	 * @param value
	 * @param boost
	 * @param store
	 * @param sort
	 */
	public Documents addTextField(String name, String value, boolean store, boolean vector, String boost) {
		FieldType type = store ? TextField.TYPE_STORED : TextField.TYPE_NOT_STORED;
		if (vector) {
			type = this.vectorField(type);
		}
		Field field = new Field(name, value, type);
		field = setBoost(field, boost);
		document.add(field);
		
		return this;
	}

	/**
	 * 权重支持
	 * 
	 * @param field
	 * @param boost
	 * @return
	 */
	public Field setBoost(Field field, String boost) {
		if (StringUtils.isNotEmpty(boost)) {
			try {
				field.setBoost(Float.parseFloat(boost));
				return field;
			} catch (Exception e) {
				return field;
			}
		} else {
			return field;
		}
	}
	
	/**
	 * 高亮的字段类型
	 * @return
	 */
	public FieldType vectorField(FieldType _type) {
		 FieldType type = new FieldType(_type);   
		 type.setStoreTermVectorOffsets(true);//记录相对增量  
	     type.setStoreTermVectorPositions(true);//记录位置信息  
	     type.setStoreTermVectors(true);//存储向量信息  
	     type.freeze();//阻止改动信息  
	     return type;
	}
}