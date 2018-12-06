<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>项目</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>项目<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="project" action="${ctx}/api/project/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<div class="control-group formSep">
			<label class="control-label">项目名称<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="name" htmlEscape="false" maxlength="100" class="required " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">项目描述<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="remarks" htmlEscape="false" maxlength="100" class="required " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">访问权限<span class="red">*</span>:</label>
			<div class="controls">
		      <label class='radio inline'>
		       <form:radiobutton path="permission" value="1" data-form='uniform'/>&nbsp;公开
		      </label>
		      <label class='radio inline'>
		       <form:radiobutton path="permission" value="0" data-form='uniform'/>&nbsp;私有
		      </label>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">线上环境<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="baseUrlProduct" htmlEscape="false" maxlength="100" class="required " />
		      <div class="control-tip">注意：默认情况下，只测试“测试环境”</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">预发布环境<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="baseUrlPpe" htmlEscape="false" maxlength="100" class="required " />
		      <div class="control-tip">注意：默认情况下，只测试“测试环境”</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">测试环境<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="baseUrlQa" htmlEscape="false" maxlength="100" class="required " />
		      <div class="control-tip">注意：默认情况下，只测试“测试环境”</div>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="cancelBtn" class="btn" type="button" value="关闭"/>
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
	bindValidate : function() {
		$("#title").focus();
		$("#inputForm").validate(
			Public.ajaxValidate()
		);
	},
	addEvent : function() {
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
