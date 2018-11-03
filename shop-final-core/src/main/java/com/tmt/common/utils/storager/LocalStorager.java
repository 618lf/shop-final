package com.tmt.common.utils.storager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.io.FileUtils;

import com.tmt.common.utils.StringUtil3;

/**
 * 本地的存储服务
 * @author root
 */
public class LocalStorager implements Storager{
	
	private String chunkPath = "chunk";
	private String storagePath; //存储的根目录
	private String urlPath;//访问的前缀
	private String domain;//域名
	
	/**
	 * 合并分片
	 */
	@Override
	public String mergeChunks(String uuid, String group, String fileName) {
		File chunkFile = new File(storagePath, chunkPath);
		if((!chunkFile.exists()) && (!chunkFile.mkdirs())) {
			return null;
		}
		File parentPath = new File(chunkFile, uuid);
		if(!parentPath.exists()) {
		   return null;
		}
		File[] files = parentPath.listFiles();
		Arrays.sort(files, new Comparator<File>() {
			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		try {
			File temp = new File(parentPath, uuid);
			for(File file: files) {
		       FileUtils.writeByteArrayToFile(temp, FileUtils.readFileToByteArray(file), true);
			}
			return this.upload(FileUtils.readFileToByteArray(temp), group, fileName);
		} catch (IOException e) { } finally {
			try {
				FileUtils.deleteDirectory(parentPath);
			} catch (IOException e) {}
		}
		return null;
	}

	/**
	 * 上传分片数据
	 */
	@Override
	public String uploadChunk(byte[] datas, String group, String chunk, String uuid) {
		File chunkFile = new File(storagePath, chunkPath);
		if((!chunkFile.exists()) && (!chunkFile.mkdirs())) {
			return null;
		}
		File parentPath = new File(chunkFile, uuid);
		if((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return null;
		}
		File file = new File(parentPath, chunk);
		try {
			FileUtils.writeByteArrayToFile(file, datas);
		} catch (IOException ioe) {
			return null;
		}
		return new StringBuilder(urlPath).append(StringUtil3.remove(file.getAbsolutePath(), storagePath)).toString();
	}
	
	/**
	 * 上传一个文件
	 */
	@Override
	public String upload(byte[] datas, String group, String fileName) {
		File file = new File(storagePath, fileName);
		File parentPath = file.getParentFile();
		if ((!parentPath.exists()) && (!parentPath.mkdirs())) {
			return null;
		}
		if (!parentPath.canWrite()) {
			return null;
		}
		try {
			FileUtils.writeByteArrayToFile(file, datas);
		} catch (IOException ioe) {
			return null;
		}
		return new StringBuilder(urlPath).append(StringUtil3.remove(file.getAbsolutePath(), storagePath)).toString();
	}

	/**
	 * 删除当前文件
	 */
	@Override
	public int delete(String group, String fileName) {
		String realFileName = fileName;
		if (StringUtil3.isNotBlank(realFileName)) {
			realFileName = StringUtil3.remove(realFileName, urlPath);
			realFileName = new StringBuilder(storagePath).append(realFileName).toString();
		}
		File file = new File(realFileName);
		if(file.exists()&& file.isFile()) {file.delete();} else {return 0;}
		return 1;
	}

	/**
	 * 修改-删除、上传
	 */
	@Override
	public String modify(byte[] datas, String group, String fileName, String oldFileName) {
		this.delete(group, oldFileName);
		return this.upload(datas, group, oldFileName);
	}

	/**
	 * 下载服务
	 */
	@Override
	public InputStream download(String fileName) {
		String realFileName = fileName;
		if (StringUtil3.isNotBlank(realFileName)) {
			realFileName = StringUtil3.remove(realFileName, urlPath);
			realFileName = new StringBuilder(storagePath).append(realFileName).toString();
		}
		File file = new File(realFileName);
		if(file != null && file.exists()) {
		   try { return FileUtils.openInputStream(file);} catch (IOException e) {return null;}
		}
		return null;
	}
	
    /**
     * 得到可以显示的地址
     */
	@Override
	public String getShowUrl(String url) {
		return new StringBuilder(domain).append(url).toString();
	}

	public String getStoragePath() {
		return storagePath;
	}
	public void setStoragePath(String storagePath) {
		this.storagePath = storagePath;
	}
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
}