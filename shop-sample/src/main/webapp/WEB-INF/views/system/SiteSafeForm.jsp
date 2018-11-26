<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>站点设置</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>站点设置<small> &gt;&gt; 编辑</small></h3>
		<div class="page-toolbar no-border">
		  <ul class="nav nav-tabs" id="recent-tab">
			 <li><a data-toggle="tab" href="${ctx}/system/site/form/base"><i class="fa fa-tag"></i>基本信息</a></li>
			 <li class="active"><a data-toggle="tab" href="${ctx}/system/site/form/safe"><i class="fa fa-gift"></i>注册与安全</a></li>
			 <li><a data-toggle="tab" href="${ctx}/system/site/form/email"><i class="fa fa-mail-reply"></i>邮箱信息</a></li>
		  </ul>
		</div>
	</div>
	<form:form id="inputForm" modelAttribute="site" action="${ctx}/system/site/save/safe" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
        <div class="control-group formSep">
			<label class="control-label">是否开放注册:</label>
			<div class="controls">
		      <label class='radio inline'>
		       <form:radiobutton path="isRegisterEnabled" value="1" data-form='uniform'/>&nbsp;是
		      </label>
		      <label class='radio inline'>
		       <form:radiobutton path="isRegisterEnabled" value="0" data-form='uniform'/>&nbsp;否
		      </label>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">禁用用户名:</label>
			<div class="controls">
		      <form:input path="disabledUsername" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">用户名最小长度:</label>
			<div class="controls">
		      <form:input path="usernameMinLength" htmlEscape="false" maxlength="10" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">用户名最大长度:</label>
			<div class="controls">
		      <form:input path="usernameMaxLength" htmlEscape="false" maxlength="10" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">密码最小长度:</label>
			<div class="controls">
		      <form:input path="passwordMinLength" htmlEscape="false" maxlength="10" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">密码最大长度:</label>
			<div class="controls">
		      <form:input path="passwordMaxLength" htmlEscape="false" maxlength="10" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">初始积分:</label>
			<div class="controls">
		      <form:input path="registerPoint" htmlEscape="false" maxlength="10" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">注册条款:</label>
			<div class="controls">
		      <form:textarea path="registerAgreement" htmlEscape="false" maxlength="500" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">是否允许用户同时在线:</label>
			<div class="controls">
		      <label class='radio inline'>
		       <form:radiobutton path="isMultiLogin" value="1"/>&nbsp;是
		      </label>
		      <label class='radio inline'>
		       <form:radiobutton path="isMultiLogin" value="0"/>&nbsp;否
		      </label>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">连续登录失败最大次数:</label>
			<div class="controls">
		      <form:input path="accountLockCount" htmlEscape="false" maxlength="10" class=" " />
		      <div class="control-tip">默认3次，如果设置为0次，则每次登录都需要输入验证码</div>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">自动解锁时间分钟:</label>
			<div class="controls">
		      <form:input path="accountLockTime" htmlEscape="false" maxlength="10" class=" " />
		      <div class="control-tip">默认30分钟</div>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">安全密匙有效时间:</label>
			<div class="controls">
		      <form:input path="safeKeyExpiryTime" htmlEscape="false" maxlength="10" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">上传文件最大限制:</label>
			<div class="controls">
		      <form:input path="uploadFileMaxSize" htmlEscape="false" maxlength="10" class=" " />
		      <div class="control-tip">单位是M</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">上传图片最大限制:</label>
			<div class="controls">
		      <form:input path="uploadImageMaxSize" htmlEscape="false" maxlength="10" class=" " />
		      <div class="control-tip">单位是M</div>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">允许上传图片扩展名:</label>
			<div class="controls">
		      <form:input path="uploadImageExtension" htmlEscape="false" maxlength="255" class=" " />
		      <div class="control-tip">为空使用默认值（.jpg,.jpeg,.bmp,.gif,.png）</div>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">允许上传媒体扩展名:</label>
			<div class="controls">
		      <form:input path="uploadMediaExtension" htmlEscape="false" maxlength="255" class=" " />
		      <div class="control-tip">为空使用默认值（.flv, .swf, .mkv, .avi, .rm, .rmvb, .mpeg, .mpg,.ogg, .ogv, .mov, .wmv, .mp4, .webm, .mp3, .wav, .mid）</div>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">允许上传文件扩展名:</label>
			<div class="controls">
		      <form:input path="uploadFileExtension" htmlEscape="false" maxlength="255" class=" " />
		      <div class="control-tip">为空使用默认值（.png, .jpg, .jpeg, .gif, .bmp,.flv, .swf, .mkv, .avi, .rm, .rmvb, .mpeg, .mpg,.ogg, .ogv, .mov, .wmv, .mp4, .webm, .mp3, .wav, .mid,.rar, .zip, .tar, .gz, .7z, .bz2, .cab, .iso,.doc, .docx, .xls, .xlsx, .ppt, .pptx, .pdf, .txt, .md, .xml）</div>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">图片上传路径:</label>
			<div class="controls">
                 <form:input path="imageUploadPath" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">媒体上传路径:</label>
			<div class="controls">
		      <form:input path="mediaUploadPath" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">文件上传路径:</label>
			<div class="controls">
		      <form:input path="fileUploadPath" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="cancelBtn" class="btn" type="button" value="返回"/>
		</div>
	</form:form>
  </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function(){
		$("#title").focus();
		$("#inputForm").validate(
			Public.validate()
		);
	},
	addEvent : function(){		    
        $(document).on('click','#cancelBtn',function(){
        	Public.closeTab();
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>
