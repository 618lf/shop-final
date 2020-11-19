package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.time.DateUtils;
/**
 * 产品评价 管理
 * @author 超级管理员
 * @date 2016-10-17
 */
public class GoodsEvaluate extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private Double score; // 分数
	private String content; // 内容
	private Byte isEnabled; // 是否显示
	private String createHead; // 创建者图像
	private Byte createGrade; // 用户等级
    
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
    public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
    public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
    public String getCreateHead() {
		return createHead;
	}
	public void setCreateHead(String createHead) {
		this.createHead = createHead;
	}
    public Byte getCreateGrade() {
		return createGrade;
	}
	public void setCreateGrade(Byte createGrade) {
		this.createGrade = createGrade;
	}
	
	/**
	 * 现阶段默认是商品ID
	 */
	@Override
	public Long prePersist() {
		this.createDate = DateUtils.getTimeStampNow();
		this.updateDate = this.createDate;
		this.id =  this.getGoodsId();
		return this.id;
	}
}