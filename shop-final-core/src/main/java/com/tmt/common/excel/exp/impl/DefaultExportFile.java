package com.tmt.common.excel.exp.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.tmt.common.entity.ColumnMapper;
import com.tmt.common.entity.DataType;
import com.tmt.common.excel.exp.IExportFile;
import com.tmt.common.excel.exp.IExportStyleHandler;
import com.tmt.common.exception.BaseRuntimeException;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.ExcelUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.zip.ZipEntry;
import com.tmt.common.utils.zip.ZipOutputStream;

/**
 * 默认的导出工具类
 * 
 * @author lifeng
 */
public class DefaultExportFile implements IExportFile {

	/**
	 * 根据参数导出文件
	 */
	public File build(Map<String, Object> data) {
		List<File> files = this.buildExcels(data);
		return buildZip(files, data);
	}

	/**
	 * 根据参数导出文件
	 */
	public List<File> buildExcels(Map<String, Object> data) {

		/**
		 * 校验参数
		 */
		if (!this.checkDataAttr(data)) {
			return null;
		}

		/**
		 * 导出
		 */
		List<File> files = Lists.newArrayList();
		List<Map<String, Object>> datas = getDatas(data);
		int maxSize = MAX_ROWS - getStartPos(data);
		if (datas != null && datas.size() > maxSize) {
			List<List<Map<String, Object>>> pDatas = Lists.partition(datas, maxSize);
			for (List<Map<String, Object>> p : pDatas) {
				data.put(EXPORT_VALUES, p);
				// 生成文件
				File outFile = createFile(data);
				files.add(outFile);
			}
		} else {
			// 生成文件
			File outFile = createFile(data);
			files.add(outFile);
		}

		// 返回 excel 文件
		return files;
		// return getExportFile(files, getExportZipFile(data));
	}

	/**
	 * 创建zip 文件
	 * 
	 * @param files
	 * @param data
	 * @return
	 */
	public File buildZip(List<File> files, Map<String, Object> data) {
		return getExportFile(files, getExportZipFile(data));
	}

	public File getExportExcelFile(Map<String, Object> data) {
		return ContextHolderUtils.getTempFile(data.get(EXPORT_FILE_NAME) + XLS);
	}

	public File getExportZipFile(Map<String, Object> data) {
		return ContextHolderUtils.getTempFile(data.get(EXPORT_FILE_NAME) + ZIP);
	}

	public InputStream getTemplateFile(Map<String, Object> data) {
		String template = EXPORT_TEMPLATE_PATH + String.valueOf(data.get(TEMPLATE_NAME));
		return ContextHolderUtils.getTemplate(template);
	}

	@SuppressWarnings("unchecked")
	public List<ColumnMapper> getColumnMappers(Map<String, Object> data) {
		return (List<ColumnMapper>) data.get(EXPORT_COLUMNS);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getDatas(Map<String, Object> data) {
		return (List<Map<String, Object>>) data.get(EXPORT_VALUES);
	}

	public String getTitle(Map<String, Object> data) {
		return String.valueOf(data.get(EXPORT_FILE_TITLE));
	}

	public IExportStyleHandler getStyleHandler(Map<String, Object> data) {
		try {
			String handlerName = String.valueOf(data.get(CUSTEM_CELL_STYLE_OBJ));
			if (StringUtil3.isNotBlank(handlerName)) {
				return SpringContextHolder.getBean(handlerName, IExportStyleHandler.class);
			}
		} catch (Exception e) {
		}
		return null;
	}

	// 得到导出的开始位置
	public Integer getStartPos(Map<String, Object> data) {
		return (Integer) data.get(TEMPLATE_START_ROW);
	}

	// 是否自定义模板
	public Boolean isDefaultTemplate(Map<String, Object> data) {
		String templateName = (String) data.get(TEMPLATE_NAME);
		return DEFAULT_TEMPLATE_NAME.equals(templateName);
	}

	// 得到单元格的格式(题头)
	public CellStyle getHeaderCellStyle(Workbook template, Map<String, Object> data, String cellKey) {
		@SuppressWarnings("unchecked")
		Map<String, CellStyle> cells = (Map<String, CellStyle>) data.get(CELL_STYLE_NAMES);
		if (cells == null) {
			cells = loadcellStyles(template, data);
		}
		return cells.get(cellKey);
	}

	// 得到数据单元格的样式
	public CellStyle getCellStyle(Workbook template, Map<String, Object> data, ColumnMapper mapper) {
		@SuppressWarnings("unchecked")
		Map<String, CellStyle> cells = (Map<String, CellStyle>) data.get(CELL_STYLE_NAMES);
		if (cells == null) {
			cells = loadcellStyles(template, data);
		}
		if (isDefaultTemplate(data)) {
			if (mapper != null && mapper.getDataType() == DataType.DATE) {
				return cells.get("date");
			} else if (mapper != null && mapper.getDataType() == DataType.MONEY) {
				return cells.get("money");
			}
			return cells.get("template");
		} else {
			return cells.get(mapper == null ? "A" : mapper.getColumn());
		}
	}

	public Map<String, CellStyle> loadcellStyles(Workbook template, Map<String, Object> data) {
		Map<String, CellStyle> cells = Maps.newHashMap();
		if (isDefaultTemplate(data)) {
			// 单元格格式
			Cell objCellTemplate = template.getSheetAt(1).getRow(0).getCell(2);
			CellStyle templateStyle = getWrapCellStyle(objCellTemplate.getCellStyle());
			objCellTemplate = template.getSheetAt(1).getRow(0).getCell(0);
			CellStyle dateStyle = getWrapCellStyle(objCellTemplate.getCellStyle());
			objCellTemplate = template.getSheetAt(1).getRow(0).getCell(1);
			CellStyle moneyStyle = getWrapCellStyle(objCellTemplate.getCellStyle());
			objCellTemplate = template.getSheetAt(1).getRow(0).getCell(4);
			CellStyle headerStyle = getWrapCellStyle(objCellTemplate.getCellStyle());
			cells.put("template", templateStyle);
			cells.put("date", dateStyle);
			cells.put("money", moneyStyle);
			cells.put("header", headerStyle);
		} else {
			int iPos = getStartPos(data);
			Sheet sheet = template.getSheetAt(0);
			Row objRow = sheet.getRow(iPos);
			if (objRow == null) {
				objRow = sheet.createRow(iPos);
			}
			int iCellIndex = 0;
			Cell objCell = objRow.getCell(iCellIndex);
			if (objCell == null) {
				objCell = objRow.createCell(iCellIndex);
			}
			cells.put("A", getWrapCellStyle(objCell.getCellStyle()));
			iCellIndex++;
			for (ColumnMapper mapper : getColumnMappers(data)) {
				objCell = objRow.getCell(iCellIndex);
				if (objCell == null) {
					objCell = objRow.createCell(iCellIndex);
				}
				CellStyle cellStyle = getWrapCellStyle(objCell.getCellStyle());
				cells.put(mapper.getColumn(), cellStyle);
				iCellIndex++;
			}
		}
		return cells;
	}

	public CellStyle getWrapCellStyle(CellStyle cellStyle) {
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);// 右边框
		return cellStyle;
	}

	// 只能导出一个文件，如果有多个，则合并为zip
	public File getExportFile(List<File> files, File zipFile) {
		if (files != null && files.size() == 1) {
			return files.get(0);
		} else if (files != null && files.size() > 1) {
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
		return null;
	}

	// 创建文件
	public File createFile(Map<String, Object> data) {
		File outFile = getExportExcelFile(data);
		InputStream templateFile = getTemplateFile(data);
		FileOutputStream out = null;
		try {
			Workbook template = ExcelUtils.loadExcelFile(templateFile);
			out = FileUtils.openOutputStream(outFile);
			writeData(template, data);
			// 写入数据
			template.write(out);
		} catch (Exception e) {
			throw new BaseRuntimeException(e);
		} finally {
			IOUtils.closeQuietly(out);
		}
		return outFile;
	}

	// 写入数据
	public void writeData(Workbook template, Map<String, Object> data) {
		// 写入题头
		writeHeader(template, data);
		// 写入实际的数据
		writeBody(template, data);
		// 自定义样式
		custemStyle(template, data);
	}

	// 写入题头
	public void writeHeader(Workbook template, Map<String, Object> data) {
		if (isDefaultTemplate(data)) {
			Row objRow = null;
			Cell objCell = null;
			Sheet sheet = template.getSheetAt(0);
			// 写题头
			int iHeader = getStartPos(data) - 1;
			int iRegLeght = 1;
			if (iHeader > 0) {
				objRow = sheet.getRow(iHeader);
				if (objRow == null) {
					objRow = sheet.createRow(iHeader);
					objRow.setHeight((short) 500);
				}
				int iCellIndex = 0;
				// 设置序号
				objCell = objRow.getCell(iCellIndex);
				if (objCell == null) {
					objCell = objRow.createCell(iCellIndex);
					objCell.setCellStyle(getHeaderCellStyle(template, data, "template"));
				}
				objCell.setCellValue("序号");
				objCell.setCellStyle(getHeaderCellStyle(template, data, "header"));
				iCellIndex++;
				// 具体的数据
				for (ColumnMapper mapper : getColumnMappers(data)) {
					objCell = objRow.getCell(iCellIndex);
					if (objCell == null) {
						objCell = objRow.createCell(iCellIndex);
						objCell.setCellStyle(getHeaderCellStyle(template, data, "template"));
					}

					iCellIndex++;
					String value = mapper.getTitle();
					if (StringUtil3.isBlank(value) || "null".equals(value)) {
						value = "";
					}
					objCell.setCellValue(value);
					objCell.setCellStyle(getHeaderCellStyle(template, data, "header"));

					iRegLeght++;
				}
			}

			int lastCellNum = iRegLeght;
			if (lastCellNum > 14) {
				lastCellNum = 14;
			}
			// 表头
			CellRangeAddress region = new CellRangeAddress(0, 0, 0, lastCellNum - 1);
			sheet.addMergedRegion(region);
			objRow = sheet.getRow(0);
			if (objRow == null) {
				objRow = sheet.createRow(0);
			}
			objCell = objRow.getCell(0);
			if (objCell == null) {
				objCell = objRow.createCell(0);
			}
			objCell.setCellValue(getTitle(data));
			CellStyle style = objCell.getCellStyle();
			style.setAlignment(CellStyle.ALIGN_CENTER);
			objCell.setCellStyle(style);
			template.setSheetName(0, getTitle(data));
			objRow = null;
			objCell = null;
		}
	}

	// 写入实际的数据
	public void writeBody(Workbook template, Map<String, Object> data) {
		Row objRow = null;
		Cell objCell = null;
		Sheet sheet = template.getSheetAt(0);
		int iPos = getStartPos(data);
		List<Map<String, Object>> datas = getDatas(data);
		if (datas != null && datas.size() != 0) {
			for (int i = 0, j = getDatas(data).size(); i < j; i++) {
				Map<String, Object> oneData = getDatas(data).get(i);
				objRow = sheet.getRow(i + iPos);
				if (objRow == null) {
					objRow = sheet.createRow(i + iPos);
					objRow.setHeight((short) 500);
				}
				int iCellIndex = 0;
				// 设置序号
				objCell = objRow.getCell(iCellIndex);
				if (objCell == null) {
					objCell = objRow.createCell(iCellIndex);
					objCell.setCellStyle(getCellStyle(template, data, null));
				}
				objCell.setCellValue(i + 1);
				objCell.setCellType(Cell.CELL_TYPE_NUMERIC);
				iCellIndex++;
				// 具体的数据
				for (ColumnMapper mapper : getColumnMappers(data)) {
					objCell = objRow.getCell(iCellIndex);
					if (objCell == null) {
						objCell = objRow.createCell(iCellIndex);
					}
					iCellIndex++;
					String value = String.valueOf(oneData.get(mapper.getProperty()));
					if (StringUtil3.isBlank(value) || "null".equals(value)) {
						value = "";
					}
					// 格式
					if (mapper.getDataType() == DataType.DATE && !StringUtil3.isBlank(value)) {
						objCell.setCellValue(value);
					} else if (mapper.getDataType() == DataType.MONEY && !StringUtil3.isBlank(value)) {
						objCell.setCellValue(Double.parseDouble(value));
					} else {
						objCell.setCellValue(value);
					}
					objCell.setCellStyle(getCellStyle(template, data, mapper));
				}
			}
		}
	}

	public void custemStyle(Workbook template, Map<String, Object> data) {
		IExportStyleHandler handler = getStyleHandler(data);
		if (handler != null) {
			Row objRow = null;
			Cell objCell = null;
			Sheet sheet = template.getSheetAt(0);
			int iPos = getStartPos(data);
			List<Map<String, Object>> datas = getDatas(data);
			if (datas != null && datas.size() != 0) {
				for (int i = 0, j = getDatas(data).size(); i < j; i++) {
					Map<String, Object> oneData = getDatas(data).get(i);
					objRow = sheet.getRow(i + iPos);
					int iCellIndex = 1;
					// 具体的数据
					for (ColumnMapper mapper : getColumnMappers(data)) {
						objCell = objRow.getCell(iCellIndex);
						handler.addStyle(template, objRow, objCell, oneData, mapper);
						iCellIndex++;
					}
				}
			}
		}
	}

	// 检查相应的参数
	public Boolean checkDataAttr(Map<String, Object> data) {
		if (data == null || (!data.containsKey(EXPORT_COLUMNS) && !data.containsKey(EXPORT_VALUES))) {
			return Boolean.FALSE;
		}
		if (!data.containsKey(TEMPLATE_NAME)) {
			data.put(TEMPLATE_NAME, DEFAULT_TEMPLATE_NAME);
		}
		if (!data.containsKey(EXPORT_FILE_NAME)) {
			data.put(EXPORT_FILE_NAME, "导出_");
		}
		if (!data.containsKey(TEMPLATE_START_ROW)) {
			data.put(TEMPLATE_START_ROW, 2);// 导出数据（不包括题头）的开始行，Excel中默认是0开始
		}
		data.put(EXPORT_FILE_NAME, data.get(EXPORT_FILE_NAME) + String
				.valueOf(DateUtil3.getTodayStr("yyyy-MM-dd HH:mm:ss").replaceAll(" ", "_").replaceAll(":", "_")));
		return Boolean.TRUE;
	}
}
