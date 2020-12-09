package com.tmt.bbs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.bbs.entity.TopicReply;
import com.tmt.bbs.service.ReplyHitServiceFacade;
import com.tmt.bbs.service.ReplyService;
import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;

/**
 * 主题回复 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Controller("bbsTopicReplyController")
@RequestMapping(value = "${adminPath}/bbs/topic/reply")
public class ReplyController extends BaseController{
	
	@Autowired
	private ReplyService topicReplyService;
	@Autowired
	private ReplyHitServiceFacade topicReplyHitService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(TopicReply topicReply, Model model){
		model.addAttribute("topicId", topicReply.getTopicId());
		return "/bbs/ReplyList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param topicReply
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(TopicReply topicReply, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		this.initQc(topicReply, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		page = topicReplyService.queryForPage(qc, param);
		List<TopicReply> topicReplys = page.getData();
		for(TopicReply item : topicReplys){
			item.setHits(this.topicReplyHitService.count_replyId(item.getId()));
		}
		return page;
	}
	
	/**
	 * 表单
	 * @param topicReply
	 * @param model
	 */
	@RequestMapping("form")
	public String form(TopicReply topicReply, Model model) {
	    if(topicReply != null && !IdGen.isInvalidId(topicReply.getId())) {
		   topicReply = this.topicReplyService.get(topicReply.getId());
		} else {
		   if(topicReply == null) {
			  topicReply = new TopicReply();
		   }
		   topicReply.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("topicReply", topicReply);
		return "/bbs/ReplyForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(TopicReply topicReply, Model model, RedirectAttributes redirectAttributes) {
		this.topicReplyService.save(topicReply);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改主题回复", topicReply.getContent(), "成功"));
		redirectAttributes.addAttribute("id", topicReply.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/bbs/topic/reply/form");
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
		List<TopicReply> topicReplys = Lists.newArrayList();
		for(Long id: idList) {
			TopicReply topicReply = new TopicReply();
			topicReply.setId(id);
			topicReplys.add(topicReply);
		}
		this.topicReplyService.delete(topicReplys);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(TopicReply topicReply, Criteria c) {
		if (null != topicReply.getTopicId()){
			c.andEqualTo("TOPIC_ID", topicReply.getTopicId());
		}
	}
	
}
