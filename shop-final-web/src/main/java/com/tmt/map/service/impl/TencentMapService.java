package com.tmt.map.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtils;
import com.tmt.map.entity.LocationPoi;
import com.tmt.map.entity.TencentIpResultObj;
import com.tmt.map.entity.TencentResultObj;
import com.tmt.map.entity.TencentResultObj.Poi;
import com.tmt.map.entity.TencentSuggestionObj;
import com.tmt.map.entity.tencent.Location;
import com.tmt.map.service.MapService;

/**
 * 腾讯地图服务
 * 
 * @author root
 */
public class TencentMapService implements MapService {

	/**
	 * 搜索周边的地址
	 */
	@Override
	public AjaxResult getLocationPois(String ip) {
		StringBuilder url = new StringBuilder("http://apis.map.qq.com/ws/location/v1/ip?output=json");
		if (!"127.0.0.1".equals(ip)) {
			url.append("&ip=").append(ip);
		}
		url.append("&key=");
		String resultStr = this.getJsonPositions(url.toString(), null);
		if (resultStr == null) {
			return AjaxResult.error("没查到数据");
		}
		TencentIpResultObj ipObj = JsonMapper.fromJson(resultStr, TencentIpResultObj.class);
		if (ipObj == null || ipObj.getResult() == null || ipObj.getResult().getLocation() == null) {
			return AjaxResult.error("没查到数据");
		}

		// 和用IP获取的是一样的，这是前端获取的地理位置
		// String _nearby = StringUtils.format("nearby(%s,%s,1000)", "22.543099",
		// "114.057868");

		// 当前地址
		Location Location = ipObj.getResult().getLocation();
		StringBuilder _url = new StringBuilder("http://apis.map.qq.com/ws/place/v1/search?output=json");
		String nearby = StringUtils.format("nearby(%s,%s,1000)", Location.getLat(), Location.getLng());
		_url.append("&boundary=").append(nearby);
		_url.append("&keyword=").append(this.encode("街"));
		_url.append("&key=");
		resultStr = this.getJsonPositions(_url.toString(), null);

		// 周边地址
		TencentResultObj rangObj = JsonMapper.fromJson(resultStr, TencentResultObj.class);
		if (rangObj == null || rangObj.getData() == null || rangObj.getData().size() == 0) {
			return AjaxResult.error("没查到数据");
		}
		List<Poi> pois = rangObj.getData();
		List<LocationPoi> _pois = Lists.newArrayList();
		for (Poi poi : pois) {
			_pois.add(LocationPoi.newPoi(poi.getTitle(), poi.getAddress(), String.valueOf(poi.getLocation().getLat()),
					String.valueOf(poi.getLocation().getLng())));
		}
		return AjaxResult.success(_pois);
	}

	/**
	 * 查询范围
	 */
	@Override
	public AjaxResult queryLocationPois(String query) {
		StringBuilder url = new StringBuilder("http://apis.map.qq.com/ws/place/v1/suggestion?output=json");
		url.append("&region=").append(this.encode("深圳市")).append("&key=");
		String resultStr = this.getJsonPositions(url.toString(), query);
		if (resultStr == null) {
			return AjaxResult.error("没查到数据");
		}

		// 范围地址
		TencentSuggestionObj suggestionObj = JsonMapper.fromJson(resultStr, TencentSuggestionObj.class);
		if (suggestionObj == null || suggestionObj.getData() == null || suggestionObj.getData().size() == 0) {
			return AjaxResult.error("没查到数据");
		}
		List<com.tmt.map.entity.TencentSuggestionObj.Poi> pois = suggestionObj.getData();
		List<LocationPoi> _pois = Lists.newArrayList();
		for (com.tmt.map.entity.TencentSuggestionObj.Poi poi : pois) {
			_pois.add(LocationPoi.newPoi(poi.getTitle(), poi.getAddress(), String.valueOf(poi.getLocation().getLat()),
					String.valueOf(poi.getLocation().getLng())));
		}
		return AjaxResult.success(_pois);
	}

	/**
	 * 获取地址JSON 数据
	 * 
	 * @param query
	 * @return
	 */
	private String getJsonPositions(String url, String query) {
		String baiduAK = "YKYBZ-MIQCX-67P4F-T6FRU-JOXVQ-SFB3T";
		StringBuilder _url = new StringBuilder(url).append(baiduAK);
		if (StringUtils.isNotBlank(query)) {
			_url.append("&keyword=").append(this.encode(query));
		}
//		try {
//			// return WebUtils.doGet(_url.toString(), null);
//		} catch (IOException e) {
//		}
		return null;
	}

	/**
	 * 编码
	 * 
	 * @param chars
	 * @return
	 */
	private String encode(String chars) {
		try {
			return URLEncoder.encode(chars, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return chars;
	}
}