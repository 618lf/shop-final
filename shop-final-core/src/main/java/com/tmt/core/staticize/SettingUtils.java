package com.tmt.core.staticize;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.tmt.core.utils.JaxbMapper;
import com.tmt.core.utils.Maps;

/**
 * 配置服务
 * @author root
 *
 */
public class SettingUtils {

	private static Setting setting = null;
	
	static {
		try {
			InputStream is = SettingUtils.class.getResourceAsStream("/staticize/staticize-setting.xml");
			List<String> lines = IOUtils.readLines(is, "UTF-8");
			StringBuilder sb = new StringBuilder();  
			for(String line: lines) {
				sb.append(line).append("\r\n");
			}
			IOUtils.closeQuietly(is);
			setting = JaxbMapper.fromXml(sb.toString(), Setting.class);
			setting.transform();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回存储路径
	 * @return
	 */
	public static String getStoragePath() {
		return setting.getStoragePath();
	}
	
	/**
	 * 返回url识别
	 * @return
	 */
	public static String getUrlPath() {
		return setting.getUrlPath();
	}
	
	/**
	 * 获得快照模板
	 * @param name
	 * @return
	 */
	public static Template getSnapshot(String name) {
		return setting.get_snapshots().get(name);
	}
	
	/**
	 * 获得模板
	 * @param name
	 * @return
	 */
	public static Template getTemplate(String name) {
		return setting.get_templates().get(name);
	}
	
	/**
	 * 获得基础的root
	 * @return
	 */
	public static Map<String, Object> getConfs() {
		Map<String, Object> root = Maps.newHashMap();
		root.putAll(setting.get_confs());
		return root;
	}
}