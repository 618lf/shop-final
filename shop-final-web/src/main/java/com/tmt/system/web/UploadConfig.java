package com.tmt.system.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.tmt.common.utils.Ints;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.entity.Site;

/**
 * 上传配置
 * @author lifeng
 *
 */
public class UploadConfig {

	// 实例化的数据
	public static UploadConfig INSTANCE;
	
	//文件上传服务配置
	protected Map<String, Object> config;
		
	/**
	 * 只需要保存一个即可
	 * @return
	 */
	public static synchronized UploadConfig getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UploadConfig();
			
			// 加载数据
			INSTANCE.getConfig();
		}
		return INSTANCE;
	}
	
	/**
	 * 实例化后初始化的方法
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getConfig() {
		if (config == null) {
			try {
				InputStream is = UploadConfig.class.getResourceAsStream("config.json");
				List<String> lines = IOUtils.readLines(is, "UTF-8");
				StringBuilder sb = new StringBuilder();  
				for(String line: lines) {sb.append(line);}
				config = JsonMapper.fromJson(sb.toString(), Map.class);
				if (is != null) {is.close();}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return config;
	}
	
	/**
	 * 获取配置的值
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		return config.get(key);
	}
	
	/**
	 * 获得相应的配置项
	 * --- 每次会读取site中的配置（只配置文件修改的）
	 * @param type
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getConfig(Site site, Action type) {
		Map<String, Object> config = this.getConfig();
		Map<String, Object> conf = new HashMap<String, Object>();
		switch (type) {
			case uploadfile:
				conf.put("isBase64", "false");
				conf.put("fieldName", config.get("fileFieldName"));
				conf.put("maxSize", Ints.defaultInteger(site.getUploadFileMaxSize() != null?(site.getUploadFileMaxSize() * 1024 * 1024):null, (Integer)config.get("fileMaxSize")));
				conf.put("allowFiles", Lists.defaultList(site.getFileAllowFiles(), (List<String>)config.get("fileAllowFiles")));
				conf.put("pathFormat", StringUtil3.defaultString(site.getFileUploadPath(), (String)config.get("filePathFormat")));
				break;
			case uploadimage:
				conf.put("isBase64", "false");
				conf.put("fieldName", config.get("imageFieldName"));
				conf.put("maxSize", Ints.defaultInteger(site.getUploadImageMaxSize() != null?(site.getUploadImageMaxSize() * 1024 * 1024):null, (Integer)config.get("imageMaxSize")));
				conf.put("allowFiles", Lists.defaultList(site.getImageAllowFiles(), (List<String>)config.get("imageAllowFiles")));
				conf.put("pathFormat", StringUtil3.defaultString(site.getImageUploadPath(), (String)config.get("imagePathFormat")));
				break;
			case uploadvideo:
				conf.put("fieldName", config.get("videoFieldName"));
				conf.put("maxSize", Ints.defaultInteger(site.getUploadMediaMaxSize() != null?(site.getUploadMediaMaxSize() * 1024 * 1024):null, (Integer)config.get("videoMaxSize")));
				conf.put("allowFiles", Lists.defaultList(site.getMediaAllowFiles(), (List<String>)config.get("videoAllowFiles")));
				conf.put("pathFormat", StringUtil3.defaultString(site.getMediaUploadPath(), (String)config.get("videoPathFormat")));
				break;
			case uploadscrawl:
				conf.put("isBase64", "true");
				conf.put("fieldName", config.get("scrawlFieldName"));
				conf.put("maxSize", Ints.defaultInteger(site.getUploadImageMaxSize() != null?(site.getUploadImageMaxSize() * 1024 * 1024):null, (Integer)config.get("imageMaxSize")));
				conf.put("allowFiles", Lists.defaultList(site.getImageAllowFiles(), (List<String>)config.get("imageAllowFiles")));
				conf.put("pathFormat", StringUtil3.defaultString(site.getImageUploadPath(), (String)config.get("imagePathFormat")));
				break;
			case catchimage:
				conf.put("fieldName", config.get("catcherFieldName"));
				conf.put("filter", config.get("catcherLocalDomain"));
				conf.put("maxSize", Ints.defaultInteger(site.getUploadImageMaxSize() != null?(site.getUploadImageMaxSize() * 1024 * 1024):null, (Integer)config.get("imageMaxSize")));
				conf.put("allowFiles", Lists.defaultList(site.getImageAllowFiles(), (List<String>)config.get("imageAllowFiles")));
				conf.put("pathFormat", StringUtil3.defaultString(site.getImageUploadPath(), (String)config.get("imagePathFormat")));
				break;
			default:;
		}
		return conf;
	}
	
	/**
	 * 操作类型
	 * @author root
	 */
	public enum Action {
		config, uploadimage, uploadscrawl, uploadvideo, uploadfile, catchimage, listfile, listimage;
		public static Action valueBy(String action) {
			try{
				if( action != null) {
					return  Action.valueOf(action.toLowerCase());
				}
				return null;
			}catch(Exception e) {
				return null;
			}
		}
	}
}