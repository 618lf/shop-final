package com.tmt.system.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.email.EmailParam;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;

/**
 * 站点设置 管理
 * @author 超级管理员
 * @date 2016-01-18
 */
public class Site extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String siteUrl; // 站点地址
    private String logo; // logo
	private String shortName; // 简短名称
	private String keywords; // 站点关键字
	private String description; // 站点描述
	private String company; // 公司名称
	private String address;// 联系地址
	private String phone; // 联系电话
	private String zipCode; //邮政编码
	private String email;// E-mail
	private String certtext; // 备案编号
	private Byte isSiteEnabled; // 是否网站开启
	private String siteCloseMessage;//网站关闭消息
	private String locale; // 区域设置
	private String watermarkAlpha;// 水印透明度
	private String watermarkImageFile; //水印图片
	private String watermarkPosition; //水印位置
	private Byte isRegisterEnabled; // 是否开放注册
	private String disabledUsername;//禁用用户名,默认SuperAdmin是一定禁用的
	private String usernameMinLength; // 用户名最小长度
	private String usernameMaxLength; // 用户名最大长度
	private String passwordMinLength; // 密码最小长度
	private String passwordMaxLength; // 密码最大长度
	private Integer registerPoint; // 初始积分
	private String registerAgreement; // 注册条款
	private Byte isMultiLogin; // 是否允许Email登录
	private Integer accountLockCount = 5;// 连续登录失败最大次数
	private Integer accountLockTime = 0;// 自动解锁时间分钟
	private Integer safeKeyExpiryTime = 30;// 安全密匙有效时间(找回密码)
	
	//文件服务
	private String uploadImageExtension = ".jpg,.jpeg,.bmp,.gif,.png";//允许上传图片扩展名
	private String uploadMediaExtension = " .flv, .swf, .mkv, .avi, .rm, .rmvb, .mpeg, .mpg,.ogg, .ogv, .mov, .wmv, .mp4, .webm, .mp3, .wav, .mid"; //允许上传媒体扩展名
	private String uploadFileExtension = ".png, .jpg, .jpeg, .gif, .bmp,.flv, .swf, .mkv, .avi, .rm, .rmvb, .mpeg, .mpg,.ogg, .ogv, .mov, .wmv, .mp4, .webm, .mp3, .wav, .mid,.rar, .zip, .tar, .gz, .7z, .bz2, .cab, .iso,.doc, .docx, .xls, .xlsx, .ppt, .pptx, .pdf, .txt, .md, .xml";//允许上传文件扩展名
	private String imageUploadPath = "/upload/image/${.now?string('yyyyMM')}/";// 图片上传路径
	private String mediaUploadPath = "/upload/media/${.now?string('yyyyMM')}/";// 媒体上传路径
	private String fileUploadPath = "/upload/file/${.now?string('yyyyMM')}/";// 文件上传路径
	private Integer uploadImageMaxSize = 2;// 上传图片最大限制 2M
	private Integer uploadMediaMaxSize = 10;// 上传媒体最大限制KB
	private Integer uploadFileMaxSize = 5;// 上传文件最大限制KB
	private List<String> fileAllowFiles; // LIST 格式
	private List<String> imageAllowFiles;// LIST 格式
	private List<String> mediaAllowFiles;// LIST 格式
	
	//邮箱服务
	private String smtpHost; //SMTP服务器地址
	private Integer smtpPort = 25;//SMTP服务器端口
	private String smtpUsername;//SMTP用户名
	private String smtpPassword;//SMTP密码
	private Byte smtpSSLEnabled = BaseEntity.NO;//SMTP是否启用SSL
	private Byte smtpAnonymousEnabled = BaseEntity.NO;//SMTP是否匿名
	private String smtpTimeout; //发送超时时间
	
    public Byte getSmtpSSLEnabled() {
		return smtpSSLEnabled;
	}
	public List<String> getFileAllowFiles() {
		return fileAllowFiles;
	}
	public void setFileAllowFiles(List<String> fileAllowFiles) {
		this.fileAllowFiles = fileAllowFiles;
	}
	public List<String> getImageAllowFiles() {
		return imageAllowFiles;
	}
	public void setImageAllowFiles(List<String> imageAllowFiles) {
		this.imageAllowFiles = imageAllowFiles;
	}
	public List<String> getMediaAllowFiles() {
		return mediaAllowFiles;
	}
	public void setMediaAllowFiles(List<String> mediaAllowFiles) {
		this.mediaAllowFiles = mediaAllowFiles;
	}
	public String getSmtpTimeout() {
		return smtpTimeout;
	}
	public void setSmtpTimeout(String smtpTimeout) {
		this.smtpTimeout = smtpTimeout;
	}
	public Byte getSmtpAnonymousEnabled() {
		return smtpAnonymousEnabled;
	}
	public void setSmtpAnonymousEnabled(Byte smtpAnonymousEnabled) {
		this.smtpAnonymousEnabled = smtpAnonymousEnabled;
	}
	public void setSmtpSSLEnabled(Byte smtpSSLEnabled) {
		this.smtpSSLEnabled = smtpSSLEnabled;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getSiteUrl() {
		return siteUrl;
	}
	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCerttext() {
		return certtext;
	}
	public void setCerttext(String certtext) {
		this.certtext = certtext;
	}
	public String getSiteCloseMessage() {
		return siteCloseMessage;
	}
	public void setSiteCloseMessage(String siteCloseMessage) {
		this.siteCloseMessage = siteCloseMessage;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getWatermarkAlpha() {
		return watermarkAlpha;
	}
	public void setWatermarkAlpha(String watermarkAlpha) {
		this.watermarkAlpha = watermarkAlpha;
	}
	public String getWatermarkImageFile() {
		return watermarkImageFile;
	}
	public void setWatermarkImageFile(String watermarkImageFile) {
		this.watermarkImageFile = watermarkImageFile;
	}
	public String getWatermarkPosition() {
		return watermarkPosition;
	}
	public void setWatermarkPosition(String watermarkPosition) {
		this.watermarkPosition = watermarkPosition;
	}
	public Byte getIsSiteEnabled() {
		return isSiteEnabled;
	}
	public void setIsSiteEnabled(Byte isSiteEnabled) {
		this.isSiteEnabled = isSiteEnabled;
	}
	public Byte getIsRegisterEnabled() {
		return isRegisterEnabled;
	}
	public void setIsRegisterEnabled(Byte isRegisterEnabled) {
		this.isRegisterEnabled = isRegisterEnabled;
	}
	public Byte getIsMultiLogin() {
		return isMultiLogin;
	}
	public void setIsMultiLogin(Byte isMultiLogin) {
		this.isMultiLogin = isMultiLogin;
	}
	public String getDisabledUsername() {
		return disabledUsername;
	}
	public void setDisabledUsername(String disabledUsername) {
		this.disabledUsername = disabledUsername;
	}
	public String getUsernameMinLength() {
		return usernameMinLength;
	}
	public void setUsernameMinLength(String usernameMinLength) {
		this.usernameMinLength = usernameMinLength;
	}
	public String getUsernameMaxLength() {
		return usernameMaxLength;
	}
	public void setUsernameMaxLength(String usernameMaxLength) {
		this.usernameMaxLength = usernameMaxLength;
	}
	public String getPasswordMinLength() {
		return passwordMinLength;
	}
	public void setPasswordMinLength(String passwordMinLength) {
		this.passwordMinLength = passwordMinLength;
	}
	public String getPasswordMaxLength() {
		return passwordMaxLength;
	}
	public void setPasswordMaxLength(String passwordMaxLength) {
		this.passwordMaxLength = passwordMaxLength;
	}
	public Integer getRegisterPoint() {
		return registerPoint;
	}
	public void setRegisterPoint(Integer registerPoint) {
		this.registerPoint = registerPoint;
	}
	public String getRegisterAgreement() {
		return registerAgreement;
	}
	public void setRegisterAgreement(String registerAgreement) {
		this.registerAgreement = registerAgreement;
	}
	public Integer getAccountLockCount() {
		return accountLockCount;
	}
	public void setAccountLockCount(Integer accountLockCount) {
		this.accountLockCount = accountLockCount;
	}
	public Integer getAccountLockTime() {
		return accountLockTime;
	}
	public void setAccountLockTime(Integer accountLockTime) {
		this.accountLockTime = accountLockTime;
	}
	public Integer getSafeKeyExpiryTime() {
		return safeKeyExpiryTime;
	}
	public void setSafeKeyExpiryTime(Integer safeKeyExpiryTime) {
		this.safeKeyExpiryTime = safeKeyExpiryTime;
	}
	public Integer getUploadImageMaxSize() {
		return uploadImageMaxSize;
	}
	public void setUploadImageMaxSize(Integer uploadImageMaxSize) {
		this.uploadImageMaxSize = uploadImageMaxSize;
	}
	public Integer getUploadMediaMaxSize() {
		return uploadMediaMaxSize;
	}
	public void setUploadMediaMaxSize(Integer uploadMediaMaxSize) {
		this.uploadMediaMaxSize = uploadMediaMaxSize;
	}
	public Integer getUploadFileMaxSize() {
		return uploadFileMaxSize;
	}
	public void setUploadFileMaxSize(Integer uploadFileMaxSize) {
		this.uploadFileMaxSize = uploadFileMaxSize;
	}
	public String getUploadImageExtension() {
		return uploadImageExtension;
	}
	public void setUploadImageExtension(String uploadImageExtension) {
		this.uploadImageExtension = uploadImageExtension;
		if(StringUtils.isNotBlank(this.uploadImageExtension)) {
		   this.uploadImageExtension = StringUtils.removeZ(this.uploadImageExtension);
		   String[] files = this.uploadImageExtension.split(",");
		   this.setImageAllowFiles(Lists.newArrayList(files));
		}
	}
	public String getUploadMediaExtension() {
		return uploadMediaExtension;
	}
	public void setUploadMediaExtension(String uploadMediaExtension) {
		this.uploadMediaExtension = uploadMediaExtension;
		if(StringUtils.isNotBlank(this.uploadMediaExtension)) {
		   this.uploadMediaExtension = StringUtils.removeZ(this.uploadMediaExtension);
		   String[] files = this.uploadMediaExtension.split(",");
		   this.setMediaAllowFiles(Lists.newArrayList(files));
		}
	}
	public String getUploadFileExtension() {
		return uploadFileExtension;
	}
	public void setUploadFileExtension(String uploadFileExtension) {
		this.uploadFileExtension = uploadFileExtension;
		if(StringUtils.isNotBlank(this.uploadFileExtension)) {
		   this.uploadFileExtension = StringUtils.removeZ(this.uploadFileExtension);
		   String[] files = this.uploadFileExtension.split(",");
		   this.setFileAllowFiles(Lists.newArrayList(files));
		}
	}
	public String getImageUploadPath() {
		return imageUploadPath;
	}
	public void setImageUploadPath(String imageUploadPath) {
		this.imageUploadPath = imageUploadPath;
	}
	public String getMediaUploadPath() {
		return mediaUploadPath;
	}
	public void setMediaUploadPath(String mediaUploadPath) {
		this.mediaUploadPath = mediaUploadPath;
	}
	public String getFileUploadPath() {
		return fileUploadPath;
	}
	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}
	public String getSmtpHost() {
		return smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}
	public Integer getSmtpPort() {
		return smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this.smtpPort = smtpPort;
	}
	public String getSmtpUsername() {
		return smtpUsername;
	}
	public void setSmtpUsername(String smtpUsername) {
		this.smtpUsername = smtpUsername;
	}
	public String getSmtpPassword() {
		return smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}
    public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
    public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 获得Email 配置参数
	 * @return
	 */
	public EmailParam getEmailParam() {
		EmailParam email = new EmailParam();
		email.setSmtpAnonymousEnabled(smtpAnonymousEnabled);
		email.setSmtpHost(smtpHost);
		email.setSmtpPassword(smtpPassword);
		email.setSmtpPort(smtpPort);
		email.setSmtpSSLEnabled(smtpSSLEnabled);
		email.setSmtpTimeout(smtpTimeout);
		email.setSmtpUsername(smtpUsername);
		return email;
	}
}