package com.tmt.wechat.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.entity.AjaxResult;
import com.tmt.wechat.entity.MetaKeyword;
import com.tmt.wechat.service.MetaKeywordServiceFacade;

/**
 * 关键词查询
 * @author root
 */
@Controller("wechatMetaKeywordController")
@RequestMapping(value = "${spring.application.web.admin}/wechat/meta/keyword")
public class MetaKeywordController {

	@Autowired
	private MetaKeywordServiceFacade keywordService;
	
	/**
	 * 只显示前10个
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("top")
	public AjaxResult top(String query, String appId) {
		List<MetaKeyword> keywords = keywordService.searchList(query, appId);
		return AjaxResult.success(keywords);
	}
}