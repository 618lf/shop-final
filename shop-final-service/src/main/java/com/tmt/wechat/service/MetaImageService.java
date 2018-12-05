package com.tmt.wechat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.Constants;
import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;
import com.tmt.wechat.dao.MetaImageDao;
import com.tmt.wechat.entity.MetaImage;

/**
 * 图片 管理
 * 
 * @author 超级管理员
 * @date 2017-01-12
 */
@Service("wechatMetaImageService")
public class MetaImageService extends BaseService<MetaImage, Long> implements MetaImageServiceFacade {

	@Autowired
	private MetaImageDao metaImageDao;

	// 数据更新
	@Autowired
	private UpdateServiceFacade updateService;

	@Override
	protected BaseDao<MetaImage, Long> getBaseDao() {
		return metaImageDao;
	}

	/**
	 * 保存
	 */
	@Transactional
	public void save(MetaImage metaImage) {
		if (IdGen.isInvalidId(metaImage.getId())) {
			this.insert(metaImage);
		} else {
			this.update(metaImage);
		}
		// 更新其中一个
		this._update(metaImage, (byte) 0);
	}

	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<MetaImage> metaImages) {
		for (MetaImage rich : metaImages) {
			this._update(rich, (byte) 1);
		}
		this.batchDelete(metaImages);
	}

	// 设置修改项
	private void _update(MetaImage metaImage, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(metaImage.getId());
		updateData.setModule(Constants.META_IMAGE);
		updateData.setOpt(opt);
		updateService.save(updateData);
	}
}