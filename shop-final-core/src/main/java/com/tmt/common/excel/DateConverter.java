package com.tmt.common.excel;

import java.text.ParseException;

import org.apache.commons.beanutils.Converter;

import com.tmt.common.config.Globals;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.StringUtil3;

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
		if (source == null || StringUtil3.isBlank(str = String.valueOf(source))) {
			return null;
		} else {
			str = StringUtil3.trim(str);
			try {
				return DateUtil3.parseDate(str, dateFormats);
			} catch (ParseException localParseException) {
				return null;
			}
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
		dateFormats = new String[Globals.DATE_PATTERNS.length+1];
		dateFormats[0] = this.dateFormat;
		for(int i =0;i<Globals.DATE_PATTERNS.length; i++) {
			dateFormats[i+1] = Globals.DATE_PATTERNS[i];
		}
	}
}
