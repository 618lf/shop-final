package com.tmt.common.staticize;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 配置点
 * @author root
 */
@XmlRootElement(name="conf")
public class Conf implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private String value;
	private Object _value;
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlAttribute
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Object get_value() {
		return _value;
	}
	public void set_value(Object _value) {
		this._value = _value;
	}
}
