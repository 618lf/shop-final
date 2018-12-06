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
         <h3>区域管理 <small> &gt;&gt; 编辑</small></h3>
      </div>
      <form:form id="inputForm" modelAttribute="area" action="${ctx}/system/area/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="parentIds"/>
		<form:hidden path="level"/>
		<form:hidden path="path"/>
		<div class="control-group formSep">
			<label class="control-label">上级区域<span class="red">*</span>:</label>
			<div class="controls">
			    <tags:treeselect id="parentId" name="parentName" idValue="${area.parentId}" nameValue="${area.parentName}"
				      title="区域" url="${ctx}/system/area/treeSelect" extId="${area.id}"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">区域名称<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">区域CODE<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">区域类型<span class="red">*</span>:</label>
			<div class="controls">
			    <form:select path="type" cssClass="iSelect required">
			       <form:option value="1">省份</form:option>
			       <form:option value="2">城市</form:option>
			       <form:option value="3">县区</form:option>
			       <form:option value="4">街道</form:option>
			       <form:option value="5">自治区</form:option>
			       <form:option value="6">直辖市</form:option>
			    </form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">排序<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" maxlength="50" class="required"/>
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
	bindValidate : function(){
		$("#name").focus();
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