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
			 <li><a data-toggle="tab" href="${ctx}/system/site/form/safe"><i class="fa fa-gift"></i>注册与安全</a></li>
			 <li class="active"><a data-toggle="tab" href="${ctx}/system/site/form/email"><i class="fa fa-mail-reply"></i>邮箱信息</a></li>
		  </ul>
		</div>
	</div>
	<form:form id="inputForm" modelAttribute="site" action="${ctx}/system/site/save/email" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
           <div class="control-group formSep">
			<label class="control-label">SMTP服务器地址<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="smtpHost" htmlEscape="false" maxlength="255" class="required " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">SMTP服务器端口<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="smtpPort" htmlEscape="false" maxlength="255" class="required" />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">SMTP用户名<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="smtpUsername" htmlEscape="false" maxlength="255" class="required" />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">SMTP密码:</label>
			<div class="controls">
		      <input name="smtpPassword" id="smtpPassword" type="text">
		      <div class="control-tip">不修改密码请留空</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">SMTP发送超时时间:</label>
			<div class="controls">
		      <form:input path="smtpTimeout" htmlEscape="false" maxlength="100" class="digits"/>
		      <div class="control-tip">留空则使用服务提供商默认的超时时间，</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">SMTP是否匿名:</label>
			<div class="controls">
		      <label class='radio inline'>
		       <form:radiobutton path="smtpAnonymousEnabled" value="1" data-form='uniform'/>&nbsp;是
		      </label>
		      <label class='radio inline'>
		       <form:radiobutton path="smtpAnonymousEnabled" value="0" data-form='uniform'/>&nbsp;否
		      </label>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">SMTP是否启用SSL:</label>
			<div class="controls">
		      <label class='radio inline'>
		       <form:radiobutton path="smtpSSLEnabled" value="1" data-form='uniform'/>&nbsp;是
		      </label>
		      <label class='radio inline'>
		       <form:radiobutton path="smtpSSLEnabled" value="0" data-form='uniform'/>&nbsp;否
		      </label>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">邮件测试:</label>
			<div class="controls">
			   <input type="text" name="to" id="to"><a class="btn" style="margin-left: 10px;" id="sendEmail">发送</a>
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
        $(document).on('click', '#sendEmail', function() {
        	var to = $('#to').val();
        	if(!!to) {
        	   Public.loading('邮件发送中');
        	   Public.postAjax('${ctx}/system/site/email_test', {to: to}, function(data) {
        		   Public.close();
        		   if(data.obj) {
        			  Public.success('邮件发送成功，请登录查询');
        		   } else {
        			   Public.error('邮件发送失败，请正确填写邮件服务器的用户名和密码或联系系统维护人员解决！');
        		   }
        	   })
        	} else {
        	   Public.toast('请填写接收邮箱');
        	}
        });
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>
