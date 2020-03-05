package com.tmt.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.exception.BaseRuntimeException;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.zip.ZipEntry;
import com.tmt.core.utils.zip.ZipOutputStream;

/**
 * web 上下文
 * 
 * @ClassName: ContextHolderUtils
 * @author 李锋
 * @date Jul 1, 2016 10:16:18 AM
 */
public class ContextHolderUtils {

	public static HttpServletRequest getRequest() {
		ServletRequestAttributes _request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return _request == null ? null : _request.getRequest();
	}
	
	public static HttpServletResponse getResponse() {
		ServletRequestAttributes _request = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return _request == null ? null : _request.getResponse();
	}

	public static String getWebRoot() {
		return XSpringContextHolder.getServletContext().getContextPath();
	}

	// ---------------------物理路劲----------------------------------------

	/**
	 * 嵌入式： 不打包：指向的时webapp目录（这个目录很重要） 打包： 指向的是tomcat临时目录
	 * 
	 * @return
	 */
	public static String getWebRootPath() {
		String path = XSpringContextHolder.getServletContext().getRealPath(File.separator);
		return StringUtils.endsWith(path, File.separator) ? path : path + File.separator;
	}

	/**
	 * WEB-INF 目录
	 * 
	 * @return
	 */
	public static String getWebInfPath() {
		return new StringBuilder(getWebRootPath()).append("WEB-INF").append(File.separator).toString();
	}

	/**
	 * 系统临时目录
	 * 
	 * @return
	 */
	public static File getTempsPath() {
		if (Globals.temps == null) {
			Globals.temps = new File(
					new StringBuilder(getWebRootPath()).append("temps").append(File.separator).toString());
		}
		return Globals.temps;
	}

	// ---------------------Web 文件服务----------------------------------------
	/**
	 * 获得一个临时文件
	 * 
	 * @return
	 */
	public static File getTempFile() {
		File tmpDir = getTempsPath();
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		String tmpFileName = IdGen.stringKey();
		return new File(tmpDir, tmpFileName);
	}

	/**
	 * 获得一个临时文件 -- 指定了文件名称
	 * 
	 * @return
	 */
	public static File getTempFile(String fileName) {
		File tmpDir = getTempsPath();
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		fileName = StringUtils.isBlank(fileName) ? IdGen.stringKey() : fileName;
		return new File(tmpDir, fileName);
	}

	/**
	 * 获得一个临时文件 -- 指定了数据
	 * 
	 * @param is
	 * @param suffix
	 * @param maxSize
	 * @return
	 */
	public static File getTempFile(InputStream is) {
		File tmpFile = getTempFile();
		byte[] dataBuf = new byte[2048];
		BufferedInputStream bis = new BufferedInputStream(is, 8192);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), 8192);
			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(bis);
		} catch (IOException e) {
		}
		return tmpFile;
	}

	/**
	 * 获得一个临时文件 -- 指定了数据
	 * 
	 * @param is
	 * @param suffix
	 * @param maxSize
	 * @return
	 */
	public static File getTempFile(InputStream is, String fileName) {
		File tmpFile = getTempFile(fileName);
		byte[] dataBuf = new byte[2048];
		BufferedInputStream bis = new BufferedInputStream(is, 8192);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tmpFile), 8192);
			int count = 0;
			while ((count = bis.read(dataBuf)) != -1) {
				bos.write(dataBuf, 0, count);
			}
			bos.flush();
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(bis);
		} catch (IOException e) {
		}
		return tmpFile;
	}

	/**
	 * 生成一个ZIP文件
	 * 
	 * @param files
	 * @param fileName
	 * @return
	 */
	public static File getTempZIPFile(List<File> files, String fileName) {
		if (files.size() == 1) {
			return files.get(0);
		} else {
			File zipFile = getTempFile(fileName);
			InputStream objInputStream = null;
			ZipOutputStream objZipOutputStream = null;
			try {
				objZipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
				objZipOutputStream.setEncoding("GBK");
				for (File file : files) {
					objZipOutputStream.putNextEntry(new ZipEntry(file.getName()));
					objInputStream = new FileInputStream(file);
					byte[] blobbytes = new byte[10240];
					int bytesRead = 0;
					while ((bytesRead = objInputStream.read(blobbytes)) != -1) {
						objZipOutputStream.write(blobbytes, 0, bytesRead);
					}
					// 重要，每次必须关闭此流，不然下面的临时文件是删不掉的
					if (objInputStream != null) {
						objInputStream.close();
					}
					objZipOutputStream.closeEntry();
				}
				return zipFile;
			} catch (Exception e) {
				throw new BaseRuntimeException(e.getMessage());
			} finally {
				IOUtils.closeQuietly(objInputStream);
				IOUtils.closeQuietly(objZipOutputStream);
			}
		}
	}

	// ---------------------Web 文件服务----------------------------------------

}