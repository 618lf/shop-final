package com.tmt.system.utils;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.excel.AbstractExcelMapper;
import com.tmt.core.utils.ExcelUtils;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.system.entity.ExcelTemplate;
import com.tmt.system.service.ExcelTemplateServiceFacade;

/**
 * 模板导入帮助
 * 
 * @author root
 */
public class ExcelImpUtil {

	/**
	 * 根据模板取数据
	 * 
	 * @param type
	 * @return
	 */
	public static List<ExcelTemplate> queryByType(String type) {
		ExcelTemplateServiceFacade templateService = SpringContextHolder.getBean(ExcelTemplateServiceFacade.class);
		return templateService.queryByType(type);
	}

	/**
	 * 根据目标类取数据
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<ExcelTemplate> queryByTargetClass(Class<?> clazz) {
		ExcelTemplateServiceFacade templateService = SpringContextHolder.getBean(ExcelTemplateServiceFacade.class);
		return templateService.queryByTargetClass(clazz.getName());
	}

	/**
	 * 获取数据
	 * 
	 * @param templateId
	 * @param obj
	 * @param file
	 * @return
	 */
	public static <T> AjaxResult fetchObjectFromTemplate(Long templateId, MultipartFile file, boolean first) {
		try {
			Workbook book = ExcelUtils.loadExcelFile(file.getInputStream());
			return fetchObjectFromTemplate(templateId, book, first);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResult.error("数据导入错误");
	}

	/**
	 * 获取数据
	 * 
	 * @param templateId
	 * @param obj
	 * @param file
	 * @return
	 */
	public static <T> AjaxResult fetchObjectFromTemplate(Long templateId, MultipartFile file) {
		try {
			Workbook book = ExcelUtils.loadExcelFile(file.getInputStream());
			return fetchObjectFromTemplate(templateId, book, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResult.error("数据导入错误");
	}

	/**
	 * 获取数据
	 * 
	 * @param templateId
	 * @param obj
	 * @param file
	 * @return
	 */
	public static <T> AjaxResult fetchObjectFromTemplate(Long templateId, File file, boolean first) {
		try {
			Workbook book = ExcelUtils.loadExcelFile(file);
			return fetchObjectFromTemplate(templateId, book, first);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResult.error("数据导入错误");
	}

	/**
	 * 获取数据
	 * 
	 * @param templateId
	 * @param obj
	 * @param file
	 * @return
	 */
	public static <T> AjaxResult fetchObjectFromTemplate(Long templateId, InputStream file, boolean first) {
		try {
			Workbook book = ExcelUtils.loadExcelFile(file);
			return fetchObjectFromTemplate(templateId, book, first);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResult.error("数据导入错误");
	}

	/**
	 * 获取数据
	 * 
	 * @param templateId
	 * @param obj
	 * @param file
	 * @return
	 */
	public static <T> AjaxResult fetchObjectFromTemplate(Long templateId, Workbook book, boolean first) {
		TemplateExcelMapper<T> mapper = new TemplateExcelMapper<T>(templateId);
		return ExcelUtils.fetchObjectFromTemplate(mapper, book, first);
	}

	/**
	 * 获取数据
	 * 
	 * @param <T>
	 * @param mapper
	 * @param file
	 * @param first
	 * @return
	 */
	public static <T> AjaxResult fetchObjectFromTemplate(AbstractExcelMapper<T> mapper, MultipartFile file) {
		try {
			Workbook book = ExcelUtils.loadExcelFile(file.getInputStream());
			return ExcelUtils.fetchObjectFromTemplate(mapper, book, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResult.error("数据导入错误");
	}

	/**
	 * 获取数据
	 * 
	 * @param <T>
	 * @param mapper
	 * @param file
	 * @param first
	 * @return
	 */
	public static <T> AjaxResult fetchObjectFromTemplate(AbstractExcelMapper<T> mapper, MultipartFile file,
			boolean first) {
		try {
			Workbook book = ExcelUtils.loadExcelFile(file.getInputStream());
			return ExcelUtils.fetchObjectFromTemplate(mapper, book, first);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return AjaxResult.error("数据导入错误");
	}
}
