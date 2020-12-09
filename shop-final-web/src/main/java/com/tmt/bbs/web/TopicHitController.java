package com.tmt.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.bbs.entity.TopicHit;
import com.tmt.bbs.service.TopicHitServiceFacade;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.web.BaseController;

/**
 * 主题点赞 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Controller("bbsTopicHitController")
@RequestMapping(value = "${adminPath}/bbs/topic/hit")
public class TopicHitController extends BaseController{
	
	@Autowired
	private TopicHitServiceFacade topicHitService;
	
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(TopicHit topicHit, Model model){
		model.addAttribute("topicId", topicHit.getTopicId());
		return "/bbs/TopicHitList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param topicHit
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(TopicHit topicHit, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(topicHit, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		return topicHitService.queryForPage(qc, param);
	}
	
	//查询条件
	private void initQc(TopicHit topicHit, Criteria c) {
		c.andEqualTo("TOPIC_ID", topicHit.getTopicId());
		if(StringUtils.isNotBlank(topicHit.getCreateName())) {
           c.andEqualTo("CREATE_NAME", topicHit.getCreateName());
        }
	}
}
