package com.tmt.common.utils;

import java.io.InputStream;

import com.tmt.common.utils.storager.Storager;

/**
 * 存储服务
 * @author root
 */
public class StorageUtils {

	//真实的存储服务
	private static Storager storager = SpringContextHolder.getBean(Storager.class);
	
	/**
	 * 上传文件
	 * fileName --- 包含存储的路径
	 * @param datas
	 * @param fileName
	 * @return
	 */
	public static String mergeChunks(String uuid, String group, String fileName){
		return storager.mergeChunks(uuid, group, fileName);
	}
	
	/**
	 * 上传文件
	 * fileName --- 包含存储的路径
	 * @param datas
	 * @param fileName
	 * @return
	 */
	public static String uploadChunk(byte[] datas, String group, String chunk, String uuid){
		return storager.uploadChunk(datas, group, chunk, uuid);
	}
	
	/**
	 * 上传文件
	 * fileName --- 包含存储的路径
	 * @param datas
	 * @param fileName
	 * @return
	 */
	public static String upload(byte[] datas, String group, String fileName){
		return storager.upload(datas, group, fileName);
	}
	
	/**
	 * 删除文件
	 * @param group
	 * @param fileName
	 * @return
	 */
	public static int delete(String group, String fileName){
		return storager.delete(group, fileName);
	}
	
	/**
	 * 修改文件
	 * @param datas
	 * @param fileName
	 * @param oldFileName
	 * @return
	 */
	public static String modify(byte[] datas, String group, String fileName, String oldFileName){
		return storager.modify(datas, group, fileName, oldFileName);
	}
	
	/**
	 * 下载文件
	 * @param fileName
	 * @return
	 */
	public static InputStream download(String fileName) {
		return storager.download(fileName);
	}
	
	/**
	 * 得到可显示的地址
	 * @param url
	 * @return
	 */
	public static String getShowUrl(String url) {
		return storager.getShowUrl(url);
	}
	
	/**
	 * 得到文件的名称，不包括扩展名
	 * @param file
	 * @return
	 */
	public static String getFileName(String fileUrl) {
		return StringUtils.removeStart(StringUtils.substringAfterLast(fileUrl, "/"), ".");
	}
	
	/**
	 * 得到文件的扩展名, 大写
	 * @param file
	 * @return
	 */
	public static String getFileSuffix(String fileUrl) {
		return StringUtils.lowerCase(StringUtils.substringAfterLast(fileUrl, "."));
	}
	
	
}