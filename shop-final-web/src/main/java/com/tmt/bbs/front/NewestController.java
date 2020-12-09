package com.tmt.bbs.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.bbs.entity.HotSpot;
import com.tmt.bbs.entity.TopicMin;
import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;

/**
 * 最新
 * 
 * @author root
 */
@Controller("frontNewestController")
@RequestMapping(value = "${frontPath}/member/bbs/newest")
public class NewestController {

	@Autowired
	private TopicSearcherFacade topicSearcher;
	@Autowired
	private HotspotSearcherFacade hotspotSearcher;

	/**
	 * 最新数据
	 * 
	 * @return
	 */
	@RequestMapping("/index.html")
	public String list() {
		return "/front/bbs/NewestList";
	}

	/**
	 * 数据
	 * 
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/page.json")
	public Page page(ScorePage page) {
		Page _page = topicSearcher.newestpage(page);
		List<TopicMin> topics = _page.getData();
		for(TopicMin topic: topics) {
			HotSpot hotSpot = hotspotSearcher.get(topic.getId());
			if (hotSpot != null) {
				topic.setHits(hotSpot.getHits());
				topic.setReplys(hotSpot.getReplys());
			} else {
				topic.setHits(0);
				topic.setReplys(0);
			}
		}
		return _page;
	}
}