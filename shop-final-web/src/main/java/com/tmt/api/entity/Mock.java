package com.tmt.api.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
/**
 * Mock 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
public class Mock extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long documentId; // 接口ID
	private String uuid; // UUID
	private String respType; // RESPONSE CONTENT-TYPE：如JSON、XML、HTML、TEXT、JSONP
	private String respExample; // RESPONSE CONTENT
    
    public Long getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
    
    public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
    
    public String getRespType() {
		return respType;
	}
	public void setRespType(String respType) {
		this.respType = respType;
	}
    
    public String getRespExample() {
		return respExample;
	}
	public void setRespExample(String respExample) {
		this.respExample = respExample;
	}
}
