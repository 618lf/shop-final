package com.tmt.core.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Html 简单的解析 jSoup
 * 主要是提供入口
 * @author lifeng
 */
public class HtmlUtils {

	/**
	 * 解析html 得到 文档对象
	 * @param html
	 * @return
	 */
	public static Document parse(String html) {
		return Jsoup.parse(html);
	}
}