package com.tmt.wechat.service;

import java.io.File;
import java.io.InputStream;

import com.tmt.wechat.bean.base.Constants.MediaType;
import com.tmt.wechat.bean.massmsg.MassNews;
import com.tmt.wechat.bean.material.Material;
import com.tmt.wechat.bean.material.MaterialBatchGetResult;
import com.tmt.wechat.bean.material.MaterialCountResult;
import com.tmt.wechat.bean.material.MaterialImageUploadResult;
import com.tmt.wechat.bean.material.MaterialNewsBatchGetResult;
import com.tmt.wechat.bean.material.MaterialNewsResult;
import com.tmt.wechat.bean.material.MaterialNewsUpdate;
import com.tmt.wechat.bean.material.MaterialUploadResult;
import com.tmt.wechat.bean.material.MaterialVideoResult;
import com.tmt.wechat.bean.material.Media;
import com.tmt.wechat.exception.WechatErrorException;

/**
 * 素材服务
 * @author lifeng
 */
public interface WechatMaterialService {

	/**
	 * 新增临时素材
	 */
	Media mediaUpload(MediaType mediaType, InputStream inputStream) throws WechatErrorException;

	/**
	 * 获取临时素材
	 */
	File mediaDownload(String media_id) throws WechatErrorException;

	/**
	 * 上传图文消息内的图片获取URL
	 */
	MaterialImageUploadResult materialImgUpload(File file) throws WechatErrorException;

	/**
	 * 新增永久图文素材
	 */
	MaterialUploadResult materialNewsUpload(MassNews news) throws WechatErrorException;

	/**
	 * 新增非图文永久素材
	 */
	MaterialUploadResult materialUpload(MediaType mediaType, Material material) throws WechatErrorException;

	/**
	 * 获取声音或者图片永久素材
	 */
	InputStream materialImageOrVoiceDownload(String mediaId) throws WechatErrorException;

	/**
	 * 获取视频永久素材的信息
	 */
	MaterialVideoResult materialVideoInfo(String mediaId) throws WechatErrorException;

	/**
	 * 获取图文永久素材的信息
	 */
	MaterialNewsResult materialNewsInfo(String mediaId) throws WechatErrorException;

	/**
	 * 修改永久图文素材
	 */
	boolean materialNewsUpdate(MaterialNewsUpdate news) throws WechatErrorException;

	/**
	 * 删除永久素材
	 */
	boolean materialDelete(String mediaId) throws WechatErrorException;

	/**
	 * 获取各类素材总数
	 */
	MaterialCountResult materialCount() throws WechatErrorException;

	/**
	 * 分页获取图文素材列表
	 * 
	 * @param offset
	 *            从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count
	 *            返回素材的数量，取值在1到20之间
	 */
	MaterialNewsBatchGetResult materialNewsBatchGet(int offset, int count) throws WechatErrorException;

	/**
	 * 分页获取其他媒体素材列表
	 *
	 * @param type
	 *            媒体类型, 请看{@link me.chanjar.weixin.common.api.WxConsts}
	 * @param offset
	 *            从全部素材的该偏移位置开始返回，0表示从第一个素材 返回
	 * @param count
	 *            返回素材的数量，取值在1到20之间
	 */
	MaterialBatchGetResult materialFileBatchGet(String type, int offset, int count) throws WechatErrorException;
}