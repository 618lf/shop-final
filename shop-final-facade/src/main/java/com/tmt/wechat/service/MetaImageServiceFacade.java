package com.tmt.wechat.service;

import java.util.List;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.wechat.entity.MetaImage;

/**
 * 图片 管理
 * @author 超级管理员
 * @date 2017-01-12
 */
public interface MetaImageServiceFacade extends BaseServiceFacade<MetaImage,Long> {
	
	/**
	 * 保存
	 */
	public void save(MetaImage metaImage);
	
	/**
	 * 删除
	 */
	public void delete(List<MetaImage> metaImages);
	
}