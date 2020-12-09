package com.tmt.shop.utils;

import java.util.List;

import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.ProductAppraise;

/**
 * 标签设置
 * @author lifeng
 */
public class TagsUtils {

	// 内定的标签
	private static String[] TAGS = new String[]{"好评","中评","差评","服务好","送货快","有图","追加"};
	
	/**
	 * 评价的标签
	 * @param appraise
	 * @return
	 */
	public static String appraiseTags(ProductAppraise appraise) {
		List<String> tags = Lists.newArrayList();
		if (appraise.getProductGrade() >= 5) {
			tags.add(TAGS[0]);
		} else if (appraise.getProductGrade() >= 2) {
			tags.add(TAGS[1]);
		} else {
			tags.add(TAGS[2]);
		}
		if (appraise.getPackageGrade() >= 4) {
			tags.add(TAGS[3]);
		}
		if (appraise.getDeliveryGrade() >= 4) {
			tags.add(TAGS[4]);
		}
		if (StringUtils.isNotBlank(appraise.getImages())) {
			tags.add(TAGS[5]);
		}
		if (StringUtils.isNotBlank(appraise.getAddContent())) {
			tags.add(TAGS[6]);
		}
		return StringUtils.join(tags, ",");
	}
	
	/**
	 * 追评的标签
	 * @return
	 */
	public static String getRappraiseTag() {
		return TAGS[6];
	}
	
	/**
	 * 得到所有的标签
	 * @return
	 */
	public static List<String> getTags() {
		return Lists.newArrayList(TAGS);
	}
}
