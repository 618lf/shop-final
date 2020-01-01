package com.tmt.common.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.tmt.common.config.Globals;
import com.tmt.common.utils.StringUtils;
import com.tmt.common.utils.time.DateUtils;

/**
 * String to Date
 * 
 * @author root
 */
public class DateConverter implements Converter<String, Date> {

	private static final String DEFAULT_DATE_FPRMAT = "yyyy-MM-dd HH:mm:ss";
	private String dateFormat = DEFAULT_DATE_FPRMAT;
	private String[] dateFormats = null;

	public DateConverter() {
		initDateFormats();
	}

	@Override
	public Date convert(String source) {
		if (source == null || StringUtils.isBlank(source)) {
			return null;
		} else {
			String str = source.trim();
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
		dateFormats = new String[Globals.DATE_PATTERNS.length + 1];
		dateFormats[0] = this.dateFormat;
		for (int i = 0; i < Globals.DATE_PATTERNS.length; i++) {
			dateFormats[i + 1] = Globals.DATE_PATTERNS[i];
		}
	}
}
