package com.tmt.bbs.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.HotSpot;
import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicMin;
import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.bbs.service.ReplyServiceFacade;
import com.tmt.bbs.service.TopicHitServiceFacade;
import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.bbs.service.TopicServiceFacade;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 主题
 * @author lifeng
 */
@Controller("frontTopicController")
@RequestMapping(value = "${frontPath}/member/bbs/topic")
public class TopicController {

	@Autowired
	private TopicSearcherFacade topicSearcher;
	@Autowired
	private TopicServiceFacade topicService;
	@Autowired
	private TopicHitServiceFacade hitService;
	@Autowired
	private ReplyServiceFacade replyService;
	@Autowired
	private HotspotSearcherFacade hotspotSearcher;
	
	/**
	 * 预览
	 * @param id
	 * @return
	 */
	@RequestMapping("/view/{id}.html")
	public String view(@PathVariable Long id, Model model) {
		TopicMin topic = topicSearcher.detail(id);
		HotSpot hot = hotspotSearcher.get(id);
		if (topic != null && hot != null) {
			topic.setHits(hot.getHits());
			topic.setReplys(hot.getReplys());
		} else if(topic != null) {
			topic.setHits(0);
			topic.setReplys(0);
		}
		model.addAttribute("topic", topic);
		return "/front/bbs/TopicView"; 
	}
	
	/**
	 * 添加
	 * @param id
	 * @return
	 */
	@RequestMapping("/add.html")
	public String add() {
		return "/front/bbs/TopicAdd"; 
	}
	
	/**
	 * 发布动态
	 * @param topic
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="save.json", method = RequestMethod.POST)
	public AjaxResult save(Topic topic) {
		User user = UserUtils.getUser();
		topic.userOptions(user);
		topic.setCreateImage(user.getHeadimg());
		topic.setIsTop(Topic.NO);
		topic.setIsGood(Topic.NO);
		topic.setIsShow(Topic.YES);
		String content = StringUtils.abbr(topic.getContent(), 500);
		topic.setContent(content);
		topicService.add(topic);
		return AjaxResult.success();
	}
	
	/**
	 * 点赞
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/hit/{id}.json", method = RequestMethod.POST)
	public AjaxResult hit(@PathVariable Long id) {
		User user = UserUtils.getUser();
		Topic topic = new Topic();
		topic.setId(id); topic.userOptions(user);
		this.topicService.hit(topic);
		return AjaxResult.success();
	}
	
	/**
	 * 我是否点赞过这些
	 * 15个一起查询 -- 会一次最多传递15个过来
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/me_hits.json", method = RequestMethod.POST)
	public AjaxResult me_hits(String ids) {
		if (StringUtils.isBlank(ids)) {
			return AjaxResult.success();
		}
		
		// 列表
		String[] _ids = ids.split(",");
		List<Long> topicIds = Lists.newArrayList();
		for(String id: _ids) {
			topicIds.add(Long.parseLong(id));
		}
		User user = UserUtils.getUser();
		List<Hit> hits = hitService.user_hits(user, topicIds);
		return AjaxResult.success(hits);
	}
	
	/**
	 * 我是否点赞过这些
	 * 15个一起查询 -- 会一次最多传递15个过来
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/me_replys.json", method = RequestMethod.POST)
	public AjaxResult me_replys(String ids) {
		if (StringUtils.isBlank(ids)) {
			return AjaxResult.success();
		}
		
		// 列表
		String[] _ids = ids.split(",");
		List<Long> topicIds = Lists.newArrayList();
		for(String id: _ids) {
			topicIds.add(Long.parseLong(id));
		}
		User user = UserUtils.getUser();
		List<Hit> hits = replyService.user_replys(user, topicIds);
		return AjaxResult.success(hits);
	}
}