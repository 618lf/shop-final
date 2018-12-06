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
		<h3>用户组<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="group" action="${ctx}/system/group/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<div class="control-group formSep">
			<label class="control-label">用户组名称<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">用户组编码<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
		    <label class="control-label">用户组角色:</label>
			<div class="controls">
			     <tags:multiselect name="roleNames" nameValue="${group.roleNames}" id="roleIds"
			           idValue="${group.roleIds}" defaultText="输入菜单 ..." selectUrl="${ctx}/system/role/treeSelect" title="角色" checked="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">用户组描述:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="5" cssStyle="width:90%;"/>
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
			window.location.href = "${ctx}/system/group/list";
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>