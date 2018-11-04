package com.tmt.wechat.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.common.entity.BaseEntity;
import com.tmt.common.staticize.DomainSer;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.Maps;
import com.tmt.wechat.bean.base.WechatConfig;

/**
 * APP_ID 作为主键
 * @author root
 */
public class App implements Serializable, WechatConfig, DomainSer {

	private static final long serialVersionUID = -3806388675609631092L;
	
	private String id; // APP_ID
	private String name;// 公众号名称
	private Byte type;// 公众号类型: 1 服务号， 2 订阅号
	private String appSecret;// 应用密钥
	private String accessToken;// 令牌
	private String encodingAesKey;
	private String srcId;// 原id（收到消息时的toUserName） 
	private String qrCode;// 二维码图片
	private String attentionUrl; // 关注页面地址
	private String domain; // 关联的域名，一级域名或二级域名
	private Long createId;//创建人ID
	private String createName;//创建人名称
	private Date createDate;//创建时间
	private Date updateDate;//修改时间
	private MetaSetting setting; // 默认的响应设置
	private Map<String, Menu> menus; // 菜单配置
	
	// 处理之后的菜单配置方便查找
	public void setMenus(List<Menu> menus) {
		this.menus = Maps.newHashMap();
		for(Menu menu: menus) {
			this.menus.put(menu.getId().toString(), menu);
		}
	}
	public Menu getMenu(String menuKey) {
		return this.menus.get(menuKey);
	}
	public MetaSetting getSetting() {
		return setting;
	}
	public void setSetting(MetaSetting setting) {
		this.setting = setting;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getEncodingAesKey() {
		return encodingAesKey;
	}
	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}
	public String getSrcId() {
		return srcId;
	}
	public void setSrcId(String srcId) {
		this.srcId = srcId;
	}
	public String getQrCode() {
		return qrCode;
	}
	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}
	public String getAttentionUrl() {
		return attentionUrl;
	}
	public void setAttentionUrl(String attentionUrl) {
		this.attentionUrl = attentionUrl;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Long getCreateId() {
		return createId;
	}
	public void setCreateId(Long createId) {
		this.createId = createId;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 用户当前的操作
	 * @param user
	 */
	public void userOptions(BaseEntity<Long> user) {
		this.createId = user.getId(); 
		this.createName = user.getName();
		this.createDate = DateUtil3.getTimeStampNow();
		this.updateDate = this.createDate;
	}
	
	// 微信接入需要配置的基本参数
	@Override
	public String getAppId() {
		return this.getId();
	}
	@Override
	public String getSecret() {
		return this.getAppSecret();
	}
	@Override
	public String getToken() {
		return this.getAccessToken();
	}
	@Override
	public String getAesKey() {
		return this.getEncodingAesKey();
	}
	
	// 更新时间 -- 不需要
	@Override
	public Long getUpdateTime() {
		return null;
	}
}