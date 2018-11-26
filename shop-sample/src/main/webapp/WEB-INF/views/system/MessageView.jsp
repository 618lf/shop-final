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
		<h3>站内信<small> &gt;&gt; 查看站内信 &nbsp;&nbsp;</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="msg" action="${ctx}/system/message/send" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="msgBox"/>
		<div class="control-group formSep">
			<label class="control-label">发件人:</label>
			<div class="controls">
				<form:input path="sendUserName" htmlEscape="false" maxlength="500" readonly="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">收件人:</label>
			<div class="controls">
				<form:input path="receiverUserName" htmlEscape="false" maxlength="500" readonly="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">标题:</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="500" class="required" cssStyle="width:95%;" readonly="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">内容:</label>
			<div class="controls">
			    <form:textarea htmlEscape="false" path="content" rows="4" maxlength="200" cssStyle="width:100%;height:280px;" readonly="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">发送时间:</label>
			<div class="controls">
				<input type="text" readonly="readonly" value="${fns:formatDate(msg.sendTime,'yyyy-MM-dd HH:mm:ss')}">
			</div>
		</div>
		<div class="form-actions">
			<input id="reBtn" class="btn btn-primary" type="button" value="回复"/>
			<input id="cancelBtn" class="btn" type="button" value="关闭"/>
		</div>
	</form:form>
  </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxModules}/system/message.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/ueditor.all.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/lang/zh-cn/zh-cn.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.addEvent();
	},
	addEvent : function(){
		$(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
		$(document).on('click','#reBtn',function(){
			var id = $('#id').val();
			window.location.href = "${ctx}/system/message/replay?id=" + id;
		});
		//是否读
		if('${msg.msgBox}' === 'IN' && '${msg.status}' != '1') {//收件箱,未读
			Message.read($('#id').val());
		}
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