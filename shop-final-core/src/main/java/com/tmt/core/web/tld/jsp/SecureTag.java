package com.tmt.core.web.tld.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 自定义的安全标签
 * 
 * @author lifeng
 */
public abstract class SecureTag extends TagSupport {

	private static final long serialVersionUID = -986562212122324131L;
	// 日志记录器
	protected Logger logger = LoggerFactory.getLogger(SecureTag.class);

	protected void verifyAttributes() throws JspException {}

	public int doStartTag() throws JspException {
		verifyAttributes();
		return onDoStartTag();
	}

	/**
	 * 子类实现
	 * @return
	 * @throws JspException
	 */
	public abstract int onDoStartTag() throws JspException;

}
