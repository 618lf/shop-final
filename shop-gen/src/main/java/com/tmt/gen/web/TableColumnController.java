package com.tmt.gen.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.web.BaseController;
import com.tmt.gen.entity.TableColumn;
import com.tmt.gen.service.TableColumnService;

/**
 * 业务表列管理
 * @author lifeng
 *
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/gen/table/column")
public class TableColumnController extends BaseController{

	@Autowired
	private TableColumnService tableColumnService;
	
	/**
	 * 初始化页面的数据
	 * @param qa
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(TableColumn column, Model model, Page page){
		PageParameters param = page.getParam();
		page = this.tableColumnService.queryDbPage(column.getName(), column.getTableName(), param);
		if( page != null && page.getData() != null && page.getData().size() != 0) {
			List<TableColumn> columns = page.getData();
			for(TableColumn _column: columns) {
				_column.setId(_column.getId());
				_column.setName(_column.getName() + ":" +_column.getComments());
			}
		}
		return page;
	}
}