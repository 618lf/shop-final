package com.tmt.core.security.filter;

import javax.servlet.Filter;

public interface PathConfigProcessor {

	/**
	 * 填充配置信息
	 * @param path
	 * @param config
	 * @return
	 */
	Filter processPathConfig(String path, String config);
}
