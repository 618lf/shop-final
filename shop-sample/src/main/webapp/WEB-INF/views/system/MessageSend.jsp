<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/message.css" rel="stylesheet"/>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>站内信<small> &gt;&gt; 发送站内信 </small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="msg" action="${ctx}/system/message/send" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="msgBox"/>
		<div class="control-group formSep">
			<label class="control-label">收件人:</label>
			<div class="controls">
			    <tags:tableSelect url="${ctx}/system/tag/user/tableSelect" id="selectUsers"
			          idValue="${msg.receiverUserId}" name="receiverUserName" nameValue="${msg.receiverUserName}" 
			          title="会员" cssClass="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="500" class="required" cssStyle="width:98%;"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">内容:</label>
			<div class="controls">
			    <form:textarea htmlEscape="false" path="content" rows="4" maxlength="200" cssStyle="width:100%;height:280px;"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="发送" style="width: 100px;"/>
			<input id="saveBtn" class="btn btn-success" type="button" value="存草稿"/>
		</div>
	</form:form>
  </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/ueditor.all.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/lang/zh-cn/zh-cn.js" type="text/javascript"></script>
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
		//只保存草稿，不发送邮件
		$(document).on('click','#saveBtn',function(){
			$('#inputForm').attr('action', '${ctx}/system/message/save');
			$('#inputForm').submit();
		});
		//编辑框
		Public.simpleUEditor('content');
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>
