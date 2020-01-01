package com.tmt.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.tmt.Constants;
import com.tmt.common.exception.BaseRuntimeException;
import com.tmt.common.utils.zip.ZipEntry;
import com.tmt.common.utils.zip.ZipOutputStream;

/**
 * 文件操作类
 * 
 * @ClassName: ExportUtils
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author 李锋
 * @date 2013-3-9 下午04:39:30
 *
 */
public class ExportUtils {

	/**
	 * 下载之后不删除 -- 默认的下载
	 * 
	 * @param filePath
	 * @param fileName
	 * @param response
	 */
	public static void downloadFile(String filePath, String fileName, HttpServletResponse response) {
		downloadFile(filePath, fileName, response, Constants.DEFAULT_ENCODING.toString(), Boolean.FALSE);
	}

	/**
	 * 下载之后删除
	 * 
	 * @param filePath
	 * @param fileName
	 * @param response
	 */
	public static void downloadFileAndDel(String filePath, String fileName, HttpServletResponse response) {
		downloadFile(filePath, fileName, response, Constants.DEFAULT_ENCODING.toString(), Boolean.TRUE);
	}

	/**
	 * bDelete -- 下载完之后是否删除 ，对于普通的导出列表 ，建议删除
	 * 
	 * @Title: downloadFile
	 * @return void 返回类型
	 */
	public static void downloadFile(String filePath, String fileName, HttpServletResponse response, String encoding,
			Boolean bDelete) {
		downloadFile(new File(filePath), fileName, response, encoding, bDelete);
	}

	/**
	 * bDelete -- 下载完之后是否删除 ，对于普通的导出列表 ，建议删除
	 * 
	 * @Title: downloadFile
	 * @return void 返回类型
	 */
	public static void downloadFile(File srcFile, String fileName, HttpServletResponse response, String encoding,
			Boolean bDelete) {
		BufferedInputStream objBufferedInputStream = null;
		BufferedOutputStream objBufferedOutputStream = null;
		OutputStream objOutputStream = null;
		InputStream objInputStream = null;
		File objFile = srcFile;
		try {
			response.reset();
			// url encode会将空格转换为+号
			response.setHeader("Content-disposition",
					"attachment;success=true;filename=" + URLEncoder.encode(fileName, encoding).replaceAll("\\+", ""));
			response.setContentType("application/x-excel;charset=UTF-8");
			objInputStream = new FileInputStream(objFile);
			objBufferedInputStream = new BufferedInputStream(objInputStream);
			objOutputStream = response.getOutputStream();
			objBufferedOutputStream = new BufferedOutputStream(objOutputStream);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = objBufferedInputStream.read(buffer, 0, 8192)) != -1) {
				objBufferedOutputStream.write(buffer, 0, bytesRead);
			}
			objBufferedOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				objInputStream.close();
				objBufferedInputStream.close();
				objOutputStream.close();
				objBufferedOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// 下载文件完成后删除文件
			if (bDelete) {
				objFile.delete();
			}
		}
	}

	/**
	 * 导出多个文件
	 * 
	 * @param files
	 * @param fileName
	 * @param response
	 * @param encoding
	 * @param bDelete
	 */
	public static void downloadFile(List<File> files, String fileName, HttpServletResponse response, String encoding,
			Boolean bDelete) {
		if (files.size() == 1) {
			ExportUtils.downloadFile(files.get(0), fileName, response, encoding, bDelete);
		} else {
			File zipFile = ContextHolderUtils.getTempFile();
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
				String _fileName = StringUtils.substringBeforeLast(fileName, ".");
				ExportUtils.downloadFile(zipFile, _fileName + ".zip", response, encoding, Boolean.TRUE);
			} catch (Exception e) {
				throw new BaseRuntimeException(e.getMessage());
			} finally {
				IOUtils.closeQuietly(objInputStream);
				IOUtils.closeQuietly(objZipOutputStream);
				if (bDelete != null && bDelete) {
					for (File file : files) {
						file.delete();
					}
				}
			}
		}
	}
}
