package com.tmt.wechat.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.Result;
import com.tmt.wechat.entity.MetaKeyword;
import com.tmt.wechat.service.MetaKeywordServiceFacade;

/**
 * 关键词查询
 * @author root
 */
@Controller("wechatMetaKeywordController")
@RequestMapping(value = "${adminPath}/wechat/meta/keyword")
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
	public Result top(String query, String appId) {
		List<MetaKeyword> keywords = keywordService.searchList(query, appId);
		return Result.success(keywords);
	}
}