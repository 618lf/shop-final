package com.tmt.common.staticize;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.BaseEntity;
import com.tmt.common.utils.FileUtils;
import com.tmt.common.utils.FreemarkerUtils;
import com.tmt.common.utils.StringUtils;
import com.tmt.common.utils.time.DateUtils;

/**
 * 静态化工具类
 * @author root
 */
public class StaticUtils {
   
   /**
    * 根据服务获取实际的域名
    * @param ser
    * @return
    */
   public static StringBuilder staticDomain(DomainSer ser) {
	   String domain = ser.getDomain();
	   if (StringUtils.isBlank(domain)) {
		   return new StringBuilder(Globals.domain);
	   }
	   return new StringBuilder("http://").append(domain);
   }
   
   /**
    * 静态化
    * @param name -- 静态化名称
    * @param root -- 对象
    */
   public static String staticSinglePage(DomainSer ser, String name, Map<String, Object> root) {
	   Template template = SettingUtils.getTemplate(name);
	   if (template != null) {
		   Map<String, Object> rootMap = getRoot(name, null);
		   if (root != null) {
			   rootMap.putAll(root);
		   }
		   StringBuilder domain = staticDomain(ser);
		   rootMap.put("ser", ser); rootMap.put("domain", domain.toString());
		   return domain.append(storage(template, rootMap)).toString();
	   }
	   return null;
   }
   
   /**
    * 静态化
    * @param name -- 静态化名称
    * @param root -- 对象
    */
   public static <T extends BaseEntity<Long>> String staticSinglePage(DomainSer ser, String name, T root) {
	   Template template = SettingUtils.getTemplate(name);
	   if (template != null) {
		   Map<String, Object> rootMap = getRoot(name, root);
		   StringBuilder domain = staticDomain(ser);
		   rootMap.put("ser", ser); rootMap.put("domain", domain.toString());
		   return domain.append(storage(template, rootMap)).toString();
	   }
	   return null;
   }
   
   /**
    * 返回一个实例静态化的可访问地址
    * 如果不存在，则新建一个
    * @param name
    * @param root
    * @return
    */
   public static <T extends BaseEntity<Long>> String touchStaticizePage(DomainSer ser, String name, T root) {
	   Template template = SettingUtils.getTemplate(name);
	   if (template != null) {
		   Map<String, Object> rootMap = getRoot(name, root);
		   StringBuilder domain = staticDomain(ser);
		   rootMap.put("ser", ser); rootMap.put("domain", domain.toString());
		   return domain.append(touchStorage(template, rootMap)).toString();
	   }
	   return null;
   }
   
   /**
    * 快照
    * 返回一个实例静态化的可访问地址
    * 如果不存在，则新建一个
    * @param name
    * @param root
    * @return
    */
   public static <T extends BaseEntity<Long>> String touchSnapshotPage(DomainSer ser, String name, T root) {
	   Template template = SettingUtils.getSnapshot(name);
	   if (template != null) {
		   Map<String, Object> rootMap = getRoot(name, root);
		   StringBuilder domain = staticDomain(ser);
		   rootMap.put("ser", ser); rootMap.put("domain", domain.toString());
		   return domain.append(touchStorage(template, rootMap)).toString();
	   }
	   return null;
   }
   
   /**
    * 删除静态化的页面
    * @param name
    * @param root
    */
   public static <T extends BaseEntity<Long>> void deleteStaticPage(String name, T root) {
	   Template template = SettingUtils.getTemplate(name);
	   if (template != null) {
		   Map<String, Object> rootMap = getRoot(name, root);
		   delete(template, rootMap);
	   }
   }
   
   //----------------------静态话基础支持-----------------------
   private static void delete(Template template, Map<String, Object> root) {
	   String staticUrl = getStaticFile(template, root);
	   String _file = StringUtils.substringBeforeLast(staticUrl,"?");
	   String filePath = new StringBuilder(SettingUtils.getStoragePath()).append(_file).toString();
	   File file = new File(filePath);
	   if (file.exists()) {
		   file.delete();
	   }
   }
   private static String touchStorage(Template template, Map<String, Object> root){
	   String staticUrl = getStaticFile(template, root);
	   String _file = StringUtils.substringBeforeLast(staticUrl,"?");
	   String filePath = new StringBuilder(SettingUtils.getStoragePath()).append(_file).toString();
	   File file = new File(filePath);
	   if (!file.exists()) {
		   String html = FreemarkerUtils.processUseTemplate(getTemplateFile(template,root), root);
		   try {
				FileUtils.write(new File(filePath), html, Globals.DEFAULT_ENCODING);
		   } catch (IOException e) {}
	   }
	   return new StringBuilder(SettingUtils.getUrlPath()).append(staticUrl).toString();
   }
   private static String storage(Template template, Map<String, Object> root){
	   String staticUrl = getStaticFile(template, root);
	   String _file = StringUtils.substringBeforeLast(staticUrl,"?");
	   String filePath = new StringBuilder(SettingUtils.getStoragePath()).append(_file).toString();
	   String html = FreemarkerUtils.processUseTemplate(getTemplateFile(template,root), root);
	   try {
			FileUtils.write(new File(filePath), html, Globals.DEFAULT_ENCODING);
	   } catch (IOException e) {}
	   return new StringBuilder(SettingUtils.getUrlPath()).append(staticUrl).toString();
   }
   private static String getTemplateFile(Template template, Map<String, Object> root) {
	   return FreemarkerUtils.processNoTemplate(template.getTemplatePath(), root);
   }
   private static String getStaticFile(Template template, Map<String, Object> root) {
	   return FreemarkerUtils.processNoTemplate(template.getStaticPath(), root);
   }
   private static <T extends BaseEntity<Long>> Map<String, Object> getRoot(String name, T root) {
	   Map<String, Object> rootMap = SettingUtils.getConfs();
	   if (root != null) {
		   rootMap.put(name, root);
	   }
	   Long updateTime = null;
	   if (root != null && root.getUpdateDate() != null) {
		   updateTime = root.getUpdateDate().getTime();
	   }
	   updateTime = updateTime == null ? DateUtils.getTimeStampNow().getTime(): updateTime;
	   rootMap.put("update_time", updateTime);
	   return rootMap;
   }
}