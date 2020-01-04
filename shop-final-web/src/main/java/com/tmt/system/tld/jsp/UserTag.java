package com.tmt.system.tld.jsp;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import com.tmt.core.utils.StringUtils;
import com.tmt.core.web.tld.jsp.SecureTag;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 用户信息标签
 * @author lifeng
 */
public class UserTag extends SecureTag{

	private String HEAD_IMG = "headimg";
	private String NAME = "name";
	private String ID = "id";
	private String NO = "no";
	private String USER_NAME = "username";
	
	//默认获取name的属性
	private static final long serialVersionUID = 7878751065898171243L;
	private String property;//获取相关属性

	@Override
	public int onDoStartTag() throws JspException {
		String strValue = null;
	    User user = UserUtils.getUser();
	    if(user != null) {
	       if(property == null || NAME.equals(property)) {
	    	  strValue = user.getName();//快捷的返回方式,最常用
	       }else if(HEAD_IMG.equals(property)){
	    	  strValue = user.getHeadimg();
	       }else if(ID.equals(property)){
	    	  strValue = String.valueOf(user.getId());
	       }else if(NO.equals(property)){
	    	  strValue = user.getNo();
	       }else if(USER_NAME.equals(property)){//登录名可以是用户名或邮箱
	    	  strValue = StringUtils.isNotBlank(user.getLoginName())?user.getLoginName():user.getEmail();
	       }else {
	    	  strValue = this.getUserProperty(user, property);
	       }
	    }
	    if (strValue != null) {
            try {
                pageContext.getOut().write(strValue);
            } catch (IOException e) {
                throw new JspTagException("Error writing [" + strValue + "] to JSP.", e);
            }
        }
        return SKIP_BODY;
	}
	
	/**
	 * 得到用户的属性
	 * @param user
	 * @param property
	 * @return
	 * @throws JspTagException
	 */
	private String getUserProperty(User user, String property) throws JspTagException {
		String strValue = null;
        try {
            BeanInfo bi = Introspector.getBeanInfo(user.getClass());
            // Loop through the properties to get the string value of the specified property
            boolean foundProperty = false;
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(property)) {
                    Object value = pd.getReadMethod().invoke(user, (Object[]) null);
                    strValue = String.valueOf(value);
                    foundProperty = true;
                    break;
                }
            }

            if (!foundProperty) {
                final String message = "Property [" + property + "] not found in principal of type [" + user.getClass().getName() + "]";
                if (logger.isErrorEnabled()) {
                	logger.error(message);
                }
                throw new JspTagException(message);
            }
        } catch (Exception e) {
            final String message = "Error reading property [" + property + "] from principal of type [" + user.getClass().getName() + "]";
            if (logger.isErrorEnabled()) {
            	logger.error(message, e);
            }
            throw new JspTagException(message, e);
        }
        return strValue;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}
}
