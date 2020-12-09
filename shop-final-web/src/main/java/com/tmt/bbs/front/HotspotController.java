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
import com.tmt.core.utils.Lists;

/**
 * 热点
 * @author root
 */
@Controller("frontHotspotController")
@RequestMapping(value = "${frontPath}/member/bbs/hotspot")
public class HotspotController {

	@Autowired
	private HotspotSearcherFacade hotspotSearcher;
	@Autowired
	private TopicSearcherFacade topicSearcher;
	
	/**
	 * 热点数据
	 * @return
	 */
	@RequestMapping("/index.html")
	public String list() {
		return "/front/bbs/HotspotList"; 
	}
	
	/**
	 * 数据
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/page.json")
	public Page page(ScorePage page) {
		List<TopicMin> topics = Lists.newArrayList();
		Page _page = hotspotSearcher.page(page);
		List<HotSpot> hots = _page.getData();
		for(HotSpot hot: hots) {
			TopicMin topic = topicSearcher.get(hot.getId());
			if (topic != null) {
				topic.setHits(hot.getHits());
				topic.setReplys(hot.getReplys());
				topics.add(topic);
			}
		}
		_page.setData(topics);
		return _page;
	}
}