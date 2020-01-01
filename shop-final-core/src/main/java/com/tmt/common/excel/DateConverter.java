package com.tmt.common.excel;

import org.apache.commons.beanutils.Converter;

import com.tmt.Constants;
import com.tmt.common.utils.StringUtils;
import com.tmt.common.utils.time.DateUtils;

public class DateConverter implements Converter{

	private static final String DEFAULT_DATE_FPRMAT = "yyyy-MM-dd HH:mm:ss";
	private String dateFormat = DEFAULT_DATE_FPRMAT;
	private String[] dateFormats = null;
	
	public DateConverter() {
		initDateFormats();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object convert(Class type, Object source) {
		String str = null;
		if (source == null || StringUtils.isBlank(str = String.valueOf(source))) {
			return null;
		} else {
			str = StringUtils.trim(str);
			return DateUtils.parseDate(str, dateFormats);
		}
	}
	
	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		initDateFormats();
	}
	
	private void initDateFormats() {
		dateFormats = new String[Constants.DATE_PATTERNS.length+1];
		dateFormats[0] = this.dateFormat;
		for(int i =0;i<Constants.DATE_PATTERNS.length; i++) {
			dateFormats[i+1] = Constants.DATE_PATTERNS[i];
		}
	}
}
