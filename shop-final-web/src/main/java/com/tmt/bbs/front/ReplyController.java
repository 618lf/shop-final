package com.tmt.bbs.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.HotSpot;
import com.tmt.bbs.entity.TopicReply;
import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.bbs.service.ReplyHitServiceFacade;
import com.tmt.bbs.service.ReplySearcherFacade;
import com.tmt.bbs.service.ReplyServiceFacade;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 回复
 * @author lifeng
 */
@Controller("frontReplyController")
@RequestMapping(value = "${frontPath}/member/bbs/reply")
public class ReplyController {

	@Autowired
	private ReplyServiceFacade replyService;
	@Autowired
	private ReplySearcherFacade replySearcher;
	@Autowired
	private HotspotSearcherFacade hotspotSearcher;
	@Autowired
	private ReplyHitServiceFacade hitService;
	
	/**
	 * 前三条
	 * @param topicId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/top3/{topicId}.json", method = RequestMethod.POST)
	public AjaxResult top3(@PathVariable Long topicId) {
		List<TopicReply> replys = replySearcher.top3(topicId);
		for(TopicReply reply: replys) {
			HotSpot hotSpot = hotspotSearcher.get(reply.getId());
			if (hotSpot != null) {
				reply.setHits(hotSpot.getHits());
			} else {
				reply.setHits(0);
			}
		}
		return AjaxResult.success(replys);
	}
	
	/**
	 * 列表
	 * @param topicId
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/page/{topicId}.json", method = RequestMethod.POST)
	public Page page(@PathVariable Long topicId, ScorePage page) {
		Page _page = replySearcher.page(topicId, page);
		List<TopicReply> topics = _page.getData();
		for(TopicReply topic: topics) {
			HotSpot hotSpot = hotspotSearcher.get(topic.getId());
			if (hotSpot != null) {
				topic.setHits(hotSpot.getHits());
			} else {
				topic.setHits(0);
			}
		}
		return _page;
	}
	
	/**
	 * 点赞
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/hit/{id}.json", method = RequestMethod.POST)
	public AjaxResult hit(@PathVariable Long id) {
		User user = UserUtils.getUser();
		TopicReply reply = new TopicReply();
		reply.setId(id); reply.userOptions(user);
		this.replyService.hit(reply);
		return AjaxResult.success();
	}
	
	/**
	 * 回复
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/topic/{topicId}.json", method = RequestMethod.POST)
	public AjaxResult reply(@PathVariable Long topicId, TopicReply reply) {
		User user = UserUtils.getUser();
		reply.setTopicId(topicId);; reply.userOptions(user);
		reply.setCreateImage(user.getHeadimg());
		
		// 回复别人的
		TopicReply _reply = null;
		if (reply.getReplyId() != null && (_reply = this.replySearcher.get(reply.getReplyId())) != null) {
			String replyUser = _reply.getCreateName();
			reply.setReplyUser(replyUser);
		}
		
		// 最多100字
		String content = StringUtils.abbr(reply.getContent(), 100);
		reply.setContent(content);
		this.replyService.reply(reply);
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
}
