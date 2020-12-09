package com.tmt.bbs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicHot;
import com.tmt.bbs.service.HotspotServiceFacade;
import com.tmt.bbs.service.TopicServiceFacade;
import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;

/**
 * 主题 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Controller("bbsTopicController")
@RequestMapping(value = "${adminPath}/bbs/topic")
public class TopicController extends BaseController{
	
	@Autowired
	private TopicServiceFacade topicService;
	@Autowired
    private HotspotServiceFacade hotspotService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Topic topic, Model model){
		return "/bbs/TopicList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param topic
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Topic topic, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		this.initQc(topic, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		page = topicService.queryForPage(qc, param);
		List<Topic> topics = page.getData();
		for(Topic _topic: topics) {
			TopicHot hot = hotspotService.getTopicHot(_topic.getId());
			if (hot != null) {
				_topic.setHits(hot.getHits());
				_topic.setReplys(hot.getReplys());
			}
		}
		return page;
	}
	
	/**
	 * 表单
	 * @param topic
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Topic topic, Model model) {
		topic = this.topicService.getWithContent(topic.getId());
		model.addAttribute("topic", topic);
		return "/bbs/TopicForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Topic topic, Model model, RedirectAttributes redirectAttributes) {
		this.topicService.save(topic);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改动态", topic.getRemarks(), "成功"));
		redirectAttributes.addAttribute("id", topic.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/bbs/topic/form");
	}
	
	@ResponseBody
	@RequestMapping("isShow")
	public AjaxResult isShow(long[] idList, int isShow){
		List<Topic> topics = Lists.newArrayList();
		for(long id : idList){
			Topic topic = new Topic();
			topic.setId(id);
			topic.setIsShow((byte)isShow);
			topics.add(topic);
		}
		topicService.updateIsShow(topics);
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
		List<Topic> topics = Lists.newArrayList();
		for(Long id: idList) {
			Topic topic = new Topic();
			topic.setId(id);
			topics.add(topic);
		}
		this.topicService.delete(topics);
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Topic topic, Criteria c) {
        if (StringUtils.isNotBlank(topic.getRemarks())) {
            c.andLike("REMARKS", topic.getRemarks());
        }
	}
}