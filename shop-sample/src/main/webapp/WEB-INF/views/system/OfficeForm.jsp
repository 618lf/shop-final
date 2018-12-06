<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
       <h3>组织管理 <small> &gt;&gt; 编辑</small></h3>
    </div>
	<form:form id="inputForm" modelAttribute="office" action="${ctx}/system/office/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="parentIds"/>
		<form:hidden path="level"/>
		<form:hidden path="path"/>
		<div class="control-group formSep">
			<label class="control-label">上级组织:</label>
			<div class="controls">
			    <tags:treeselect id="parentId" name="parentName" idValue="${office.parentId}" nameValue="${office.parentName}"
				      title="上级组织" url="${ctx}/system/office/treeSelect" extId="${office.id}"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">区域位置:</label>
			<div class="controls">
				<tags:areaselect nameValue="${office.areaName}" idValue="${office.areaId}" name="areaName" id="areaId"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">组织名称<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">组织编码<span class="red">*</span>:</label>
			<div class="controls">
			    <form:input path="code" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">组织类型:</label>
			<div class="controls">
			    <form:select path="type" cssClass="iSelect">
			         <form:option value="1">公司</form:option>
			         <form:option value="2">部门</form:option>
			         <form:option value="3">小组</form:option>
			    </form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">办公地址:</label>
			<div class="controls">
			    <form:input path="address" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">邮政编码:</label>
			<div class="controls">
			    <form:input path="zipCode" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">负责人:</label>
			<div class="controls">
			    <form:input path="master" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">手机:</label>
			<div class="controls">
			    <form:input path="phone" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">固定电话:</label>
			<div class="controls">
			    <form:input path="fax" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">电子邮件:</label>
			<div class="controls">
			    <form:input path="email" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="cancelBtn" class="btn" type="button" value="返 回"/>
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
		$("#name").focus();
		$("#inputForm").validate(
			Public.validate()
		);
	},
	addEvent : function(){
		$(document).on('click','#cancelBtn',function(){
			window.location.href = "${ctx}/system/office/list?id="+$('#id').val();
		});
	}
};
$(function(){
	THISPAGE._init();
});	
</script>
</body>
</html>