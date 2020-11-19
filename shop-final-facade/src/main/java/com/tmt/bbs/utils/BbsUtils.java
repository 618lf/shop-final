package com.tmt.bbs.utils;

import java.util.List;

import com.tmt.bbs.entity.Section;
import com.tmt.bbs.service.SectionServiceFacade;
import com.tmt.bbs.service.TopicSearcherFacade;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.shop.utils.TagsUtils;

/**
 * 栏目帮助类
 * @author lifeng
 *
 */
public class BbsUtils {

	private static String SECTION_CACHE_NAME = "BBS#STAT#";
	private static String TAG_CACHE_NAME = "BBS#TAG#";
	
	// -----------------产品---------------------
	/**
	 * 统计所有的分类
	 * @return
	 */
	public static List<Stat> statSections() {
		String key = new StringBuilder(SECTION_CACHE_NAME).append("S").toString();
		List<Stat> sStat = CacheUtils.getSysCache().get(key);
		if (sStat == null) {
			sStat = _statSections();
			CacheUtils.getSysCache().put(key, sStat);
		}
		return sStat;
	}
	
	// 实际的获取统计数目
	private static List<Stat> _statSections() {
		SectionServiceFacade sectionService = SpringContextHolder.getBean(SectionServiceFacade.class);
		TopicSearcherFacade topicSearcher = SpringContextHolder.getBean(TopicSearcherFacade.class);
		
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("IS_SHOW", Section.YES);
	    qc.setOrderByClause("SORT");
		List<Section> sections = sectionService.queryByCondition(qc);
		
		List<Stat> stats = Lists.newArrayList();
		// 所有的产品
		int num = topicSearcher.countProduct();
		Stat stat = new Stat();
		stat.setName("全部");
		stat.setNum(num);
		stats.add(stat);
		for(Section section: sections) {
			num = topicSearcher.countProduct(section.getId());
			stat = new Stat();
			stat.setId(section.getId());
			stat.setName(section.getName());
			stat.setNum(num);
			stats.add(stat);
		}
		return stats;
	}
	
	/**
	 * 统计商品ID
	 * @param productId
	 * @return
	 */
	public static Stat statSection(Long productId) {
		String key = new StringBuilder(SECTION_CACHE_NAME).append(productId).toString();
		Stat stat = CacheUtils.getSysCache().get(key);
		if (stat == null) {
			TopicSearcherFacade topicSearcher = SpringContextHolder.getBean(TopicSearcherFacade.class);
			int num = topicSearcher.countProduct(productId);
			stat = new Stat();
			stat.setId(productId);
			stat.setNum(num);
			CacheUtils.getSysCache().put(key, stat);
		}
		return stat;
	}
	
	/**
	 * 删除分类的统计项目
	 */
	public static void clearStats() {
		String key = new StringBuilder(SECTION_CACHE_NAME).append("*").toString();
		CacheUtils.getSysCache().evict(key);
		key = new StringBuilder(TAG_CACHE_NAME).append("*").toString();
		CacheUtils.getSysCache().evict(key);
	}
	
	/**
	 * 删除分类的统计项目
	 */
	public static void clearSectionStats(Long sectionId) {
		String key = new StringBuilder(SECTION_CACHE_NAME).append("S").toString();
		CacheUtils.getSysCache().evict(key);
		if (sectionId != null) {
			key = new StringBuilder(SECTION_CACHE_NAME).append(sectionId).toString();
			CacheUtils.getSysCache().evict(key);
		}
	}
	
	/**
	 * 删除分类的统计项目
	 */
	public static void clearTagStats(Long sectionId) {
		String key = new StringBuilder(TAG_CACHE_NAME).append(sectionId).toString();
		CacheUtils.getSysCache().evict(key);
	}
	
	// -----------------标签---------------------
	/**
	 * 统计所有的分类
	 * @return
	 */
	public static List<Stat> statTags(Long sectionId) {
		String key = new StringBuilder(TAG_CACHE_NAME).append(sectionId).toString();
		List<Stat> sStat = CacheUtils.getSysCache().get(key);
		if (sStat == null) {
			sStat = _statTags(sectionId);
			CacheUtils.getSysCache().put(key, sStat);
		}
		return sStat;
	}
	
	// 实际的获取统计数目
	private static List<Stat> _statTags(Long sectionId) {
		TopicSearcherFacade topicSearcher = SpringContextHolder.getBean(TopicSearcherFacade.class);
	    List<String> tags = TagsUtils.getTags();
		List<Stat> stats = Lists.newArrayList();
		
		// 所有的产品
		int num = topicSearcher.countProduct(sectionId);
		Stat stat = new Stat();
		stat.setName("全部");
		stat.setNum(num);
		stats.add(stat);
		for(String tag: tags) {
			num = topicSearcher.countProduct(sectionId, tag);
			stat = new Stat();
			stat.setId((long)tag.hashCode());
			stat.setName(tag);
			stat.setNum(num);
			stats.add(stat);
		}
		return stats;
	}
}