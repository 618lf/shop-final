package com.tmt.core.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.WebUtils;

/**
 * 导入(Excel 导入)
 * @author lifeng
 */
public abstract class BaseImpController extends BaseController{

	/**
	 * IE浏览器下会出现下载问题
	 * 返回 AjaxResult 时有问题，返回String 没问题。值得研究
	 * 感觉并不是编码的问题，而是解码的问题,
	 * IE8下传递的是accept ： image/jpeg 很是奇怪
	 * IE9下是text/html 中文解码有问题
	 * 其他浏览器正常
	 * ---- 上传只要返回中文就乱码和页面等待很长时间,不返回中就没问题。
	 *      而且IE8 和IE9 遇到的情况还不同。
	 * ---- 后来发现是springmvc3.2.3的一个Bug
	 * @param templateId
	 * @param request
	 * @return
	 */
	@RequestMapping("doImport")
	public void importFile(Long templateId, HttpServletRequest request, HttpServletResponse response) {
		MultipartFile[] files = WebUtils.uploadFile(request);
		AjaxResult result = this.doImport(templateId, request, files);
		try {
			if (result != null) {
				String sResult = JsonMapper.toJson(result);
				IOUtils.write(sResult, response.getWriter());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 子类实现, 如果只有一个文件，则只需要实现第二个方法
	 * @param templateId
	 * @param file
	 * @return
	 */
    protected AjaxResult doImport(Long templateId, HttpServletRequest request, MultipartFile ...file) {
    	if (file != null && file.length == 1) {
    		return this.doImport(templateId, request, file[0]);
    	}
    	return AjaxResult.success();
	}
	
	/**
	 * 子类实现
	 * @param templateId
	 * @param file
	 * @return
	 */
	protected AjaxResult doImport(Long templateId, HttpServletRequest request, MultipartFile file) {
		return AjaxResult.success();
	}
}