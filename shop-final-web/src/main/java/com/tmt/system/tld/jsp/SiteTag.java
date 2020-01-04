package com.tmt.system.tld.jsp;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import com.tmt.core.web.tld.jsp.SecureTag;
import com.tmt.system.entity.Site;
import com.tmt.system.utils.SiteUtils;

public class SiteTag extends SecureTag {

	private String NAME = "name";
	private String SHORT_NAME = "shortName";
	private String COMPANY = "company";
	
	private static final long serialVersionUID = 7878751065898171243L;
	private String property;

	@Override
	public int onDoStartTag() throws JspException {
		String strValue = null;
	    Site site = SiteUtils.getSite();
	    if(site != null) {
	       if(property == null || NAME.equals(property)) {
	    	  strValue = site.getName();
	       }else if(SHORT_NAME.equals(property)){
	    	  strValue = site.getShortName();
	       }else if(COMPANY.equals(property)){
	    	  strValue = site.getCompany();
	       }else {
	    	  strValue = this.getUserProperty(site, property);
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
	private String getUserProperty(Site site, String property) throws JspTagException {
		String strValue = null;
        try {
            BeanInfo bi = Introspector.getBeanInfo(site.getClass());
            // Loop through the properties to get the string value of the specified property
            boolean foundProperty = false;
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(property)) {
                    Object value = pd.getReadMethod().invoke(site, (Object[]) null);
                    strValue = String.valueOf(value);
                    foundProperty = true;
                    break;
                }
            }

            if (!foundProperty) {
                final String message = "Property [" + property + "] not found in principal of type [" + site.getClass().getName() + "]";
                if (logger.isErrorEnabled()) {
                	logger.error(message);
                }
                throw new JspTagException(message);
            }
        } catch (Exception e) {
            final String message = "Error reading property [" + property + "] from principal of type [" + site.getClass().getName() + "]";
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
