package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.IdEntity;
import com.tmt.core.utils.Lists;

/**
 * 产品参数组 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
public class GoodsParameterGroup extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long goodsId; // 编号
	private String groupName; // 参数组名称
	private Integer sort; // 排序
	private List<GoodsParameter> parameters;
    
    public List<GoodsParameter> getParameters() {
		return parameters;
	}
	public void setParameters(List<GoodsParameter> parameters) {
		this.parameters = parameters;
	}
	public void fillParameters(List<ParameterOption> options) {
		List<GoodsParameter> parameters  =Lists.newArrayList();
		for(ParameterOption option: options) {
			GoodsParameter parameter = new GoodsParameter();
			parameter.setParameterName(option.getName());
			parameter.setSort(option.getSort());
			parameters.add(parameter);
		}
		this.parameters = parameters;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
    public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
}