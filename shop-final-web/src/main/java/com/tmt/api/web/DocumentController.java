package com.tmt.api.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.api.entity.Document;
import com.tmt.api.service.DocumentService;
import com.tmt.api.service.GroupService;
import com.tmt.api.service.TestService;
import com.tmt.api.utils.RequestConfig;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.web.BaseController;

/**
 * 接口 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Controller("apiDocumentController")
@RequestMapping(value = "${adminPath}/api/document")
public class DocumentController extends BaseController{
	
	@Autowired
	private DocumentService documentService;
	@Autowired
	private GroupService groupService;
	@Autowired
	private TestService testService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Document document, Model model){
		return "/api/DocumentList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param document
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Document document, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(document, c);
		return documentService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param document
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Document document, Model model) {
	    if(document != null && !IdGen.isInvalidId(document.getId())) {
		   document = this.documentService.get(document.getId());
		} else {
		   if(document == null) {
			  document = new Document();
		   }
		   document.setId(IdGen.INVALID_ID);
		   document.setStatus((byte)0);
		   document.setStarLevel((byte)0);
		   document.setSuccessRespType(RequestConfig.ResponseContentType.JSON.name());
		   document.setFailRespType(RequestConfig.ResponseContentType.JSON.name());
		}
	    model.addAttribute("groups", this.groupService.queryByProject(document.getProjectId()));
		model.addAttribute("document", document);
		// enum
		model.addAttribute("RequestMethodEnum", RequestConfig.RequestMethodEnum.values());
		model.addAttribute("requestHeadersEnum", RequestConfig.getRequestHeadersEnum());
		model.addAttribute("QueryParamTypeEnum", RequestConfig.QueryParamTypeEnum.values());
		model.addAttribute("ResponseContentType", RequestConfig.ResponseContentType.values());
		return "/api/DocumentForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxResult save(Document document) {
		this.documentService.save(document);
		return AjaxResult.success(document.getId());
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList) {
		List<Document> documents = Lists.newArrayList();
		for(Long id: idList) {
			Document document = new Document();
			document.setId(id);
			documents.add(document);
		}
		this.documentService.delete(documents);
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Document document, Criteria c) {
		if (document.getProjectId() != null) {
			c.andEqualTo("PROJECT_ID", document.getProjectId());
		}
		if (document.getGroupId() != null) {
			c.andEqualTo("GROUP_ID", document.getGroupId());
		}
	}
	
	/**
	 * 表单
	 * @param document
	 * @param model
	 */
	@RequestMapping("detail")
	public String detail(Document document, Model model) {
		document = this.documentService.get(document.getId());
		model.addAttribute("document", document);
		model.addAttribute("tests", testService.queryByDocumnet(document.getId()));
		return "/api/DocumentDetail";
	}
}