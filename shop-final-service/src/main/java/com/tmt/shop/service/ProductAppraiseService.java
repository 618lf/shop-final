package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.service.TopicServiceFacade;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.dao.ProductAppraiseContentDao;
import com.tmt.shop.dao.ProductAppraiseDao;
import com.tmt.shop.entity.ProductAppraise;
import com.tmt.shop.entity.ProductAppraiseContent;
import com.tmt.shop.utils.TagsUtils;

/**
 * 商品评价 管理
 * @author 超级管理员
 * @date 2017-04-10
 */
@Service("shopProductAppraiseService")
public class ProductAppraiseService extends BaseService<ProductAppraise,Long> implements ProductAppraiseServiceFacade{
	
	@Autowired
	private ProductAppraiseDao productAppraiseDao;
	@Autowired
	private TopicServiceFacade topicService;
	@Autowired
	private ProductAppraiseContentDao contentDao;
	@Autowired
	private UserRankService userRankService;
	
	@Override
	protected BaseDao<ProductAppraise, Long> getBaseDao() {
		return productAppraiseDao;
	}
	
	/**
	 * 得到明细
	 * @return
	 */
	public ProductAppraiseContent getContent(Long orderId) {
		return contentDao.get(orderId);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(ProductAppraise appraise) {
		appraise.setTags(TagsUtils.appraiseTags(appraise));
		appraise.setRemarks(StringUtils.abbrHtml(appraise.getContent(), 100));
		this.update(appraise);
		
		// 存储内容
		ProductAppraiseContent content = new ProductAppraiseContent();
		String _content = StringUtils.abbrHtml(appraise.getContent(), 500);
		String _addContent = StringUtils.abbrHtml(appraise.getAddContent(), 200);
		content.setContent(_content);
		content.setAddContent(_addContent);
		content.setId(appraise.getId());
		String images = appraise.getImages();
		if (StringUtils.isNotBlank(images)) {
			String[] _images = images.split(",");
			content.setImage(getImage(_images, 0));
			content.setImage2(getImage(_images, 1));
			content.setImage3(getImage(_images, 2));
			content.setImage4(getImage(_images, 3));
		}
		contentDao.update(content);
		
		// 同步发布动态
		topicService.save(appraise);
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<ProductAppraise> productAppraises) {
		this.batchDelete(productAppraises);
	}
	
	/**
	 * 评价
	 * @param appraise
	 */
	@Transactional
	public void appraise(ProductAppraise appraise) {
		appraise.setIsTop(ProductAppraise.NO);
		appraise.setIsShow(ProductAppraise.YES);
		appraise.setTags(TagsUtils.appraiseTags(appraise));
		this.insert(appraise);
		
		// 存储内容
		ProductAppraiseContent content = new ProductAppraiseContent();
		String _content = StringUtils.abbrHtml(appraise.getContent(), 500);
		content.setContent(_content);
		content.setId(appraise.getId());
		String images = appraise.getImages();
		if (StringUtils.isNotBlank(images)) {
			String[] _images = images.split(",");
			content.setImage(getImage(_images, 0));
			content.setImage2(getImage(_images, 1));
			content.setImage3(getImage(_images, 2));
			content.setImage4(getImage(_images, 3));
		}
		contentDao.insert(content);
		
		// 同步发布动态
		topicService.add(appraise);
	}
	
	// 得到图片最多四张
	private String getImage(String[] _images, int index) {
		if (_images.length <= index) {
			return null;
		}
		return _images[index];
	}
	
	/**
	 * 评价
	 * @param appraise
	 */
	@Transactional
	public void rappraise(ProductAppraise appraise) {
		
		// 仅仅修改评论内容
		ProductAppraiseContent content = new ProductAppraiseContent();
		String _content = StringUtils.abbrHtml(appraise.getContent(), 200);
		content.setAddContent(_content);
		content.setId(appraise.getId());
		this.contentDao.update("updateRappraise", content);
		
		// 同步发布动态
		appraise.setTags(TagsUtils.getRappraiseTag());
		topicService.rappraise(appraise);
	}

	/**
	 * 已评价的商品信息
	 */
	@Override
	public List<ProductAppraise> queryByOrder(Long orderId) {
		List<ProductAppraise> appraises = this.queryForList("queryByOrder", orderId);
		for(ProductAppraise appraise: appraises) {
			ProductAppraiseContent content = contentDao.get(appraise.getId());
			appraise.setPcontent(content);
		}
		return appraises;
	}
	
	/**
	 * 赠送积分
	 * @param appraise
	 */
	@Transactional
	public Boolean addPoints(ProductAppraise appraise, Boolean mode) {
		// 锁定首次赠送
		if (mode != null && mode) {
			Integer points = this.queryForAttr("firstPoints", appraise.getId());
			if (points != null) {
				return Boolean.FALSE;
			}
		}
		this.update("addPoints", appraise);
		userRankService.appraise(appraise.getCreateId(), appraise.getPoints());
		return Boolean.TRUE;
	}
}
