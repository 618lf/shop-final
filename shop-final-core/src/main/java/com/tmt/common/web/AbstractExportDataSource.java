package com.tmt.common.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.entity.Result;
import com.tmt.common.excel.exp.IExportDataSource;
import com.tmt.common.excel.exp.IExportFile;
import com.tmt.common.excel.exp.impl.DefaultExportFile;
import com.tmt.common.exception.BaseRuntimeException;
import com.tmt.common.utils.ExportUtils;
import com.tmt.common.utils.FileUtils;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.StorageUtils;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;

/**
 * 导出Excel的数据源设置
 * 
 * @author lifeng
 *
 * @param <T>
 *            -- 参数实体
 */
public abstract class AbstractExportDataSource<T> implements IExportDataSource<T> {

	/**
	 * 需要子类去实现
	 * 
	 * @param param
	 * @param request
	 * @return
	 */
	public abstract Map<String, Object> doExport(T param, HttpServletRequest request);

	/**
	 * 导出工具类
	 * 
	 * @return
	 */
	public IExportFile getExportFileTool() {
		return new DefaultExportFile();
	}

	/**
	 * 搜集参数，判断是否导出 Zip还是Excel,执行导出.
	 */
	@Override
	public void export(HttpServletRequest request, HttpServletResponse response) {

		// 创建导出工具类
		IExportFile exportFile = this.getExportFileTool();

		List<File> files = Lists.newArrayList();
		try {
			Map<String, Object> oneData = null;
			// 读取参数
			List<T> params = this.fetchObjectFromRequest(request);
			for (T param : params) {

				// 准备数据
				Map<String, Object> data = this.doExport(param, request);// 获取数据,模版,导出名

				// 创建的 excel 文件
				List<File> excels = exportFile.buildExcels(data);
				if (excels != null) {
					files.addAll(excels);
				}

				if (oneData == null) {
					oneData = data;
				}
			}
			if (oneData != null) {
				File file = exportFile.buildZip(files, oneData);
				// 下载且删除
				if (file != null) {
					this.exportFile(file, request, response);
				}
			}
		} catch (Exception e) {
			throw new BaseRuntimeException(e.getMessage());
		} finally {
			// 删除 files
			for (File file : files) {
				if (file.exists()) {
					file.delete();
				}
			}
		}
	}

	// 输出文件
	private void exportFile(File file, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String export_mode = request.getParameter(IExportFile.EXPORT_MODE);
		if ("inapp".equals(export_mode)) {
			String url = StorageUtils.upload(FileUtils.readFileToByteArray(file), null,
					"/export_tmp/" + file.getName());
			Map<String, String> data = Maps.newHashMap();
			data.put("url", url);
			data.put("name", file.getName());
			WebUtils.sendJson(response, Result.success(data).toJson());
		} else {
			ExportUtils.downloadFileAndDel(file.getAbsolutePath(), file.getName(), response);
		}
	}

	// 得到参数
	private List<T> fetchObjectFromRequest(HttpServletRequest request) {
		Map<String, String[]> params = Maps.newOrderMap();
		for (Object param : request.getParameterMap().keySet()) {
			String key = String.valueOf(param);
			if (key != null && StringUtil3.startsWith(key, IExportFile.EXPORTS_PARAM)) {
				params.put(StringUtil3.substringAfter(key, IExportFile.EXPORTS_PARAM), request.getParameterValues(key));
			}
		}
		List<Map<String, String>> relas = Lists.newArrayList();
		if (params != null && !params.isEmpty()) {
			String[] ids = params.entrySet().iterator().next().getValue();// 取第一个值
			for (int i = 0, j = ids.length; i < j; i++) {
				Map<String, String> rela = Maps.newHashMap();
				for (String key : params.keySet()) {
					String[] values = params.get(key);
					if (values == null || values.length <= i) {
						continue;
					}
					rela.put(key, values[i]);
				}
				relas.add(rela);
			}
		}
		List<T> items = JsonMapper.fromJsonToList(JsonMapper.toJson(relas), this.getTargetClass());
		return items;
	}

	// 得到泛型的类型
	@SuppressWarnings("unchecked")
	protected Class<T> getTargetClass() {
		Class<T> clazz = null;
		if (clazz == null) {
			Type type = getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				ParameterizedType ptype = ((ParameterizedType) type);
				Type[] args = ptype.getActualTypeArguments();
				clazz = (Class<T>) args[0].getClass();
			}
		}
		return clazz;
	}
}