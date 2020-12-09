package com.tmt.bbs.front;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.bbs.entity.HotSpot;
import com.tmt.bbs.entity.TopicMin;
import com.tmt.bbs.service.HotspotSearcherFacade;
import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.bbs.utils.BbsUtils;
import com.tmt.bbs.utils.Stat;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;
import com.tmt.core.utils.Maps;

/**
 * 分类
 * @author lifeng
 */
@Controller("frontSectionController")
@RequestMapping(value = "${frontPath}/member/bbs/section")
public class SectionController {

	@Autowired
	private TopicSearcherFacade topicSearcher;
	@Autowired
	private HotspotSearcherFacade hotspotSearcher;
	
	/**
	 * 最新数据
	 * @return
	 */
	@RequestMapping("/index.html")
	public String list(Model model) {
		// 所有的分类的统计项目
		List<Stat> stats = BbsUtils.statSections();
		model.addAttribute("stats", stats);
		return "/front/bbs/SectionList"; 
	}
	
	/**
	 * 指定分类
	 * @return
	 */
	@RequestMapping("/{productId}.html")
	public String list_product(@PathVariable Long productId, Model model) {
		Stat stat = BbsUtils.statSection(productId);
		List<Stat> stats = BbsUtils.statTags(productId);
		model.addAttribute("stats", stats);
		model.addAttribute("stat", stat);
		return "/front/bbs/SectionPList"; 
	}
	
	/**
	 * 统计产品评价
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/stat/{productId}.json")
	public AjaxResult stat_product(@PathVariable Long productId) {
		// 统计项目
		Stat stat = BbsUtils.statSection(productId);
		if (stat != null && stat.getNum() != null && stat.getNum() > 0) {
			TopicMin topic = topicSearcher.newestProduct(productId);
			Map<String, Object> map = Maps.newHashMap();
			map.put("stat", stat);
			map.put("topic", topic);
			return AjaxResult.success(map);
		}
		return AjaxResult.error("没有评价");
	}
	
	/**
	 * 数据
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/page.json")
	public Page page(Long sectionId, String tag, ScorePage page) {
		Page _page = topicSearcher.productPage(sectionId, tag, page);
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