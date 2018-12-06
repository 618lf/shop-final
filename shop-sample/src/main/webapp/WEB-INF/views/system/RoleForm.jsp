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
       <h3>角色管理 <small> &gt;&gt; 编辑</small></h3>
    </div>
	<form:form id="inputForm" modelAttribute="role" action="${ctx}/system/role/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<div class="control-group formSep">
			<label class="control-label">角色名称<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">角色编码<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">角色权限标识:</label>
			<div class="controls">
			    <form:input path="permission" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">数据范围:</label>
			<div class="controls">
			    <form:select path="dataScope" items="${scopes}" itemLabel="label" itemValue="value" cssClass="iSelect"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">所属组织<span class="red">*</span>:</label>
			<div class="controls">
				<tags:treeselect id="officeId" name="officeName" idValue="${role.officeId}" nameValue="${role.officeName}"
				      title="所属组织" url="${ctx}/system/office/treeSelect" extId="-1" cssClass="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">选择菜单:</label>
			<div class="controls">
			     <tags:multiselect name="menuNames" nameValue="${role.menuNames}" id="menuIds"
			           idValue="${role.menuIds}" defaultText="输入菜单 ..." selectUrl="${ctx}/system/menu/treeSelect" title="菜单" checked="true"/>  
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">选择按钮权限:</label>
			<div class="controls">
			     <tags:multiselect name="optionNames" nameValue="${role.optionNames}" id="optionIds"
			           idValue="${role.optionIds}" defaultText="输入菜单 ..." selectUrl="${ctx}/system/menu/treeSelect/option" title="菜单" checked="true"></tags:multiselect>  
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
			window.location.href = "${ctx}/system/role/list?id="+$('#id').val();
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>