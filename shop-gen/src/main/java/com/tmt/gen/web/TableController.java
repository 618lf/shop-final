package com.tmt.gen.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.gen.entity.Table;
import com.tmt.gen.entity.TableColumn;
import com.tmt.gen.service.TableService;
import com.tmt.gen.utils.GenUtils;

/**
 * 业务表管理
 * @author lifeng
 *
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/gen/table")
public class TableController extends BaseController{
	
	@Autowired
	private TableService tableService;

	/**
	 * 初始化页面的查询条件
	 * @param qa
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Table table, Model model){
		return "/gen/TableList";
	}
	
	/**
	 * 初始化页面的数据
	 * @param qa
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Table table, Model model, Page page){
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		return tableService.queryForPage(qc, param);
	}
	
	/**
	 * 业务表表单
	 * @param qa
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Table table, Model model){
		if (table != null && !IdGen.isInvalidId(table.getId())) {
		    table = this.tableService.getWithColumns(table.getId());
		} else {
		    if (table != null && StringUtil3.isNotBlank(table.getName())) {
			    table = this.tableService.queryDbTableByTableName(table.getName());
		    }
		    table.setId(IdGen.INVALID_ID);
		}
		List<Table> dbTables = this.tableService.queryDbTables(null);
		model.addAttribute("table", table);
		model.addAttribute("tables", dbTables);
		model.addAttribute("config",GenUtils.getConfig());//配置
		return "/gen/TableForm";
	}
	
	/**
	 * 业务表保存
	 * @param businessType
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Table table, Model model, RedirectAttributes redirectAttributes) {
		List<TableColumn> columns = WebUtils.fetchItemsFromRequest(ContextHolderUtils.getRequest(), TableColumn.class,"items.");
		table.setColumns(columns);
		this.tableService.save(table);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "保存业务表配置", table.getName(), "成功"));
		redirectAttributes.addAttribute("id", table.getId()); 
		return "redirect:"+Globals.adminPath+"/gen/table/form";
	}
	
	/**
	 *  表删除
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList) {
		List<Table> tables = Lists.newArrayList();
		for(Long id: idList) { 
			Table table = new Table();
			table.setId(id);
			tables.add(table);
		}
		this.tableService.delete(tables);
		return AjaxResult.success();
	}
}