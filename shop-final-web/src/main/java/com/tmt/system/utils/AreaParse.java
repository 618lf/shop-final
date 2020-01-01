package com.tmt.system.utils;

import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.tmt.common.exception.BaseRuntimeException;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.HtmlUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtils;
import com.tmt.system.entity.Area;

/**
 * 从中华人民共和国国家统计局网站统计数据
 * @author lifeng
 */
public class AreaParse {

	/**
	 * 解析省
	 * @return
	 */
	public List<Area> parseProvinces(String html) {
		List<Area> areas = Lists.newArrayList();
		Document doc = HtmlUtils.parse(html);
		Elements provinces = doc.getElementsByClass("provincetr");
		Iterator<Element>  it = provinces.iterator();
		while(it.hasNext()) {
			Element province = it.next();
			Elements _provinces = province.getElementsByTag("a");
			Iterator<Element>  _it = _provinces.iterator();
			while(_it.hasNext()) {
				Element _province = _it.next();
				String code = _province.attr("href");
				String name = _province.text();
				
				// 一个省
				Area area = new Area();
				area.setId(this.parseId(code));
				area.setParentId(IdGen.ROOT_ID);
				area.setCode(code);
				area.setName(name);
				areas.add(area);
			}
		}
		return areas;
	}
	
	/**
	 * 解析市
	 * @return
	 */
	public List<Area> parseOthers(String html, String clazz) {
		List<Area> areas = Lists.newArrayList();
		try {
			Document doc = HtmlUtils.parse(html);
			Elements provinces = doc.getElementsByClass(clazz);
			Iterator<Element>  it = provinces.iterator();
			while(it.hasNext()) {
				Element province = it.next();
				Elements _provinces = province.getElementsByTag("td");
				Iterator<Element>  _it = _provinces.iterator();
				Element eCode = _it.next();
				Element eName = _it.next();
				if (!eCode.getElementsByTag("a").isEmpty()) {
					_provinces = province.getElementsByTag("a");
					_it = _provinces.iterator();
					eCode = _it.next();
					eName = _it.next();
				}
				
				String id =  eCode.text();
				String code = eCode.text();
				String name = eName.text();
				
				// 取数据
				if (eName.hasAttr("href")) {
					code = eName.attr("href");
					name = eName.text();
				}
				
				// 一个市
				Area area = new Area();
				area.setId(this.parseId(id));
				area.setCode(code);
				area.setName(name);
				areas.add(area);
			}
		}catch(Exception e) {
			System.out.println("下面的页面解析错误:");
			System.out.println(html);
			throw new BaseRuntimeException(e);
		}
		return areas;
	}
	
	/**
	 * code 转化为 ID
	 * @param code
	 * @return
	 */
	public Long parseId(String code) {
		if (StringUtils.contains(code, "/")) {
			code = StringUtils.substringAfterLast(code, "/");
		}
		code = StringUtils.substringBefore(code, ".html");
		return Long.parseLong(code);
	}
}
