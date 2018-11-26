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
			 <li class="active"><a data-toggle="tab" href="${ctx}/system/site/form/base"><i class="fa fa-tag"></i>基本信息</a></li>
			 <li><a data-toggle="tab" href="${ctx}/system/site/form/safe"><i class="fa fa-gift"></i>注册与安全</a></li>
			 <li><a data-toggle="tab" href="${ctx}/system/site/form/email"><i class="fa fa-mail-reply"></i>邮箱信息</a></li>
		  </ul>
		</div>
	</div>
	<form:form id="inputForm" modelAttribute="site" action="${ctx}/system/site/save/base" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
           <div class="control-group formSep">
			<label class="control-label">站点名称:</label>
			<div class="controls">
		      <form:input path="name" htmlEscape="false" maxlength="100" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">站点地址:</label>
			<div class="controls">
		      <form:input path="siteUrl" htmlEscape="false" maxlength="100" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">站点LOGO:</label>
			<div class="controls">
			  <tags:attachment name="logo" value="${site.logo}"/>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">简短名称:</label>
			<div class="controls">
		      <form:input path="shortName" htmlEscape="false" maxlength="100" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">站点关键字:</label>
			<div class="controls">
		      <form:input path="keywords" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">站点描述:</label>
			<div class="controls">
		      <form:input path="description" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">公司名称:</label>
			<div class="controls">
		      <form:input path="company" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">联系地址:</label>
			<div class="controls">
		      <form:input path="address" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">联系电话:</label>
			<div class="controls">
		      <form:input path="phone" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">邮政编码:</label>
			<div class="controls">
		      <form:input path="zipCode" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">E-MAIL:</label>
			<div class="controls">
		      <form:input path="email" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">备案编号:</label>
			<div class="controls">
		      <form:input path="certtext" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">区域设置:</label>
			<div class="controls">
		      <form:select path="locale" cssClass="iSelect">
		        <form:option value="zh_CN">中文(简体, 中国)</form:option>
		        <form:option value="zh_TW">中文(繁体, 台湾)</form:option>
		        <form:option value="en_US">英语(美国)</form:option>
		      </form:select>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">水印透明度:</label>
			<div class="controls">
		      <form:input path="watermarkAlpha" htmlEscape="false" maxlength="10" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">水印图片:</label>
			<div class="controls">
		      <form:input path="watermarkImageFile" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">水印位置:</label>
			<div class="controls">
		      <form:select path="watermarkPosition" cssClass="iSelect">
		        <form:option value="no">无</form:option>
		        <form:option value="lt">左上</form:option>
		        <form:option value="rt">右上</form:option>
		        <form:option value="ce">居中</form:option>
		        <form:option value="lb">左下</form:option>
		        <form:option value="rb">右下</form:option>
		      </form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">是否网站开启:</label>
			<div class="controls">
		      <label class='radio inline'>
		       <form:radiobutton path="isSiteEnabled" value="1" data-form='uniform'/>&nbsp;是
		      </label>
		      <label class='radio inline'>
		       <form:radiobutton path="isSiteEnabled" value="0" data-form='uniform'/>&nbsp;否
		      </label>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">网站关闭消息:</label>
			<div class="controls">
		      <form:textarea path="siteCloseMessage" htmlEscape="false" maxlength="500" class=" " />
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
