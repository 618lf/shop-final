package com.tmt.wechat.bean.semantic;

import java.io.Serializable;

/**
 * 语义查询接口返回
 * @author lifeng
 */
public class SemanticQueryResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private String query;
	private String type;
	private String semantic;
	private String result;
	private String answer;
	private String text;
	
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSemantic() {
		return semantic;
	}
	public void setSemantic(String semantic) {
		this.semantic = semantic;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}