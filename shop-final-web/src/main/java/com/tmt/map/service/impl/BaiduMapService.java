package com.tmt.map.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtils;
import com.tmt.map.entity.BaiduResultObj;
import com.tmt.map.entity.BaiduResultObj.Poi;
import com.tmt.map.entity.BaiduSuggestionObj;
import com.tmt.map.entity.LocationPoi;
import com.tmt.map.service.MapService;

/**
 * 百度地图服务
 * @author root
 */
public class BaiduMapService implements MapService{

	/**
	 * 获取周边位置
	 */
	@Override
	public AjaxResult getLocationPois(String ip) {
		StringBuilder url = new StringBuilder("http://api.map.baidu.com/highacciploc/v1?qterm=pc&extensions=2&coord=bd09ll");
		if (!"127.0.0.1".equals(ip)) {
			url.append("&qcip=").append(ip);
		}
		url.append("&ak=");
		String resultStr = this.getJsonPositions(url.toString(), null);
		if (resultStr == null) {
			return AjaxResult.error("没查到数据");
		}
		
		// 转换数据
		BaiduResultObj obj = JsonMapper.fromJson(resultStr, BaiduResultObj.class);
		if (null != obj && obj.getContent() != null && obj.getContent().getPois() != null && obj.getContent().getPois().size() > 0) {
			List<Poi> pois = obj.getContent().getPois();
			List<LocationPoi> _pois = Lists.newArrayList();
			for(Poi poi: pois) {
				String lat = poi.getLocation() != null?poi.getLocation().getLat():"";
				String lnt = poi.getLocation() != null?poi.getLocation().getLng():"";
				_pois.add(LocationPoi.newPoi(poi.getName(), poi.getAddress(), lat, lnt));
			}
			return AjaxResult.success(_pois);
		}
		return AjaxResult.error("没查到数据");
	}

	/**
	 * 搜索位置
	 */
	@Override
	public AjaxResult queryLocationPois(String query) {
		String url = "http://api.map.baidu.com/place/v2/suggestion?region=340&output=json&ak=";
		String resultStr = this.getJsonPositions(url, query);
		if (resultStr == null) {
			return AjaxResult.error("没查到数据");
		}
		BaiduSuggestionObj obj = JsonMapper.fromJson(resultStr, BaiduSuggestionObj.class);
		if (null != obj) {
			List<com.tmt.map.entity.BaiduSuggestionObj.Poi> pois = obj.getResult();
			List<LocationPoi> _pois = Lists.newArrayList();
			for(com.tmt.map.entity.BaiduSuggestionObj.Poi poi: pois) {
				String lat = poi.getLocation() != null?poi.getLocation().getLat():"";
				String lnt = poi.getLocation() != null?poi.getLocation().getLng():"";
				_pois.add(LocationPoi.newPoi(poi.getName(), poi.getAddress(), lat, lnt));
			}
			return AjaxResult.success(_pois);
		}
		return AjaxResult.error("没查到数据");
	}
	
	/**
	 * 获取地址JSON 数据
	 * @param query
	 * @return
	 */
	private String getJsonPositions(String url, String query) {
		String baiduAK = "jGLhyZz5P91Yu8VSi0nHHhMpWuvGyTIZ";
		StringBuilder _url = new StringBuilder(url).append(baiduAK);
		if (StringUtils.isNotBlank(query)) {
			_url.append("&query=").append(this.encode(query));
		}
//		try {
//			return WebUtils.doGet(_url.toString(), null);
//		} catch (IOException e) {}
		return null;
	}
	
	/**
	 * 编码
	 * @param chars
	 * @return
	 */
	private String encode(String chars){
		try {
			return URLEncoder.encode(chars, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return chars;
	}
}
