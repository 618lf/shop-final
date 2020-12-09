package com.tmt.api.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.api.entity.Document;
import com.tmt.api.entity.Project;
import com.tmt.api.entity.Test;
import com.tmt.api.service.DocumentService;
import com.tmt.api.service.ProjectService;
import com.tmt.api.service.TestService;
import com.tmt.api.utils.RequestConfig;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.web.BaseController;

/**
 * 测试 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Controller("apiTestController")
@RequestMapping(value = "${adminPath}/api/test")
public class TestController extends BaseController{
	
	@Autowired
	private TestService testService;
	@Autowired
	private DocumentService documentService;
	@Autowired
	private ProjectService projectService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Test test, Model model){
		return "/api/TestList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param test
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Test test, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(test, c);
		return testService.queryForPage(qc, param);
	}
	
	/**
	 * 添加接口测试
	 * @return
	 */
	@RequestMapping("add")
	public String add(Long documentId, Model model) {
		Document document = this.documentService.get(documentId);
		model.addAttribute("document", document);
		return "/api/TestAdd";
	}
	
	/**
	 * 表单
	 * @param test
	 * @param model
	 */
	@RequestMapping("modify")
	public String modify(Test test, Model model) {
	    test = this.testService.get(test.getId());
	    Document document = this.documentService.get(test.getDocumentId());
	    model.addAttribute("document", document);
		model.addAttribute("test", test);
		return "/api/TestModify";
	}
	
	/**
	 * 表单
	 * @param test
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Test test, Model model) {
	    test = this.testService.get(test.getId());
	    Document document = this.documentService.get(test.getDocumentId());
	    model.addAttribute("document", document);
		model.addAttribute("test", test);
		return "/api/TestForm";
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
	public AjaxResult save(Test test) {
		Document document = this.documentService.get(test.getDocumentId());
		test.setRequestHeaders(document.getRequestHeaders());
		test.setRequestMethod(document.getRequestMethod());
		test.setRequestUrl(document.getRequestUrl());
		this.testService.save(test);
		return AjaxResult.success();
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
		List<Test> tests = Lists.newArrayList();
		for(Long id: idList) {
			Test test = new Test();
			test.setId(id);
			tests.add(test);
		}
		this.testService.delete(tests);
		return AjaxResult.success();
	}
	
	/**
	 * 运行(需要指定环境)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("run")
	@SuppressWarnings("rawtypes")
	public AjaxResult run(Long id) {
		
		// 测试
		Test test = this.testService.get(id);
		Document document = documentService.get(test.getDocumentId());
		Project project = projectService.get(document.getProjectId());
		String href = new StringBuilder(project.getBaseUrlQa()).append(test.getRequestUrl()).toString();
		
		// headers
		Map<String, String> _requestHeaders = null;
		List<Map> requestHeaders = JsonMapper.fromJsonToList(test.getRequestHeaders(), Map.class);
		if (!CollectionUtils.isEmpty(requestHeaders)) {
			_requestHeaders = Maps.newHashMap();
			for(Map map: requestHeaders) {
				_requestHeaders.put(String.valueOf(map.get("name")), String.valueOf(map.get("value")));
			}
		}
		
		// request params
		Map<String, String> _queryParams = null;
		List<Map> queryParams = JsonMapper.fromJsonToList(test.getQueryParams(), Map.class);
		if (!CollectionUtils.isEmpty(queryParams)) {
			_queryParams = Maps.newHashMap();
			for(Map map: queryParams) {
				_queryParams.put(String.valueOf(map.get("name")), String.valueOf(map.get("val")));
			}
		}
		
		// invoke 1/3
		HttpRequestBase remoteRequest = null;
		if (RequestConfig.RequestMethodEnum.POST.name().equals(test.getRequestMethod())) {
			HttpPost httpPost = new HttpPost(href);
			if (!CollectionUtils.isEmpty(_queryParams)) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for(Map.Entry<String,String> entry : _queryParams.entrySet()){
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					logger.error(e.getMessage(), e);
				}
			}
			remoteRequest = httpPost;
		} else if (RequestConfig.RequestMethodEnum.GET.name().equals(test.getRequestMethod())) {
			remoteRequest = new HttpGet(markGetUrl(href, _queryParams));
		}
		
		
		// invoke 2/3
		if (!CollectionUtils.isEmpty(_requestHeaders)) {
			for(Map.Entry<String,String> entry : _requestHeaders.entrySet()){
				remoteRequest.setHeader(entry.getKey(), entry.getValue());
			}
		}
		
		// invoke 3/3
		String responseContent = remoteCall(remoteRequest);
		
		return AjaxResult.success(responseContent);
	}
	
	// get 请求
	private String markGetUrl(String url, Map<String, String> queryParamMap){
		String finalUrl = url + "?";
		if (queryParamMap!=null && !queryParamMap.isEmpty()) {
			for(Map.Entry<String,String> entry : queryParamMap.entrySet()){
				finalUrl += entry.getKey() + "=" + entry.getValue() + "&";
			}
		}
		finalUrl = finalUrl.substring(0, finalUrl.length()-2);
		return finalUrl;
	}
	
	// 远程调用
	private String remoteCall(HttpRequestBase remoteRequest){
		// remote test
		String responseContent = null;

		CloseableHttpClient httpClient = null;
		try{
			org.apache.http.client.config.RequestConfig requestConfig = org.apache.http.client.config.RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
			remoteRequest.setConfig(requestConfig);

			httpClient = HttpClients.custom().disableAutomaticRetries().build();

			// ajax
			remoteRequest.addHeader("X-Requested-With","XMLHttpRequest");
			
			// parse response
			HttpResponse response = httpClient.execute(remoteRequest);
			HttpEntity entity = response.getEntity();
			if (null != entity) {
				int statusCode = response.getStatusLine().getStatusCode();
				if (statusCode == 200) {
					responseContent = EntityUtils.toString(entity, "UTF-8");
				} else {
					responseContent = "请求状态异常：" + response.getStatusLine().getStatusCode();
					if (statusCode == 302) {
						responseContent += "；Redirect地址：" + response.getHeaders("Location");
					}

				}
				EntityUtils.consume(entity);
			}
			logger.info("http statusCode error, statusCode:" + response.getStatusLine().getStatusCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			responseContent = "请求异常：" + e.getMessage();
		} finally{
			if (remoteRequest!=null) {
				remoteRequest.releaseConnection();
			}
			if (httpClient!=null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return responseContent;
	}
	
	//查询条件
	private void initQc(Test test, Criteria c) {}
}
