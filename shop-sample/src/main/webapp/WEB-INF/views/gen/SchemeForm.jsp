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
		<h3>生成方案<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="scheme" action="${ctx}/gen/scheme/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="flag"/>
		<div class="control-group formSep">
			<label class="control-label"><strong>生成结构：</strong></label>
			<div class="controls">
				<span class="help-inline">
					<strong>{包名}/{大模块}/{子模块}/{分层(dao,entity,service,web)}/{功能名}</strong>
				</span>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">业务表名:</label>
			<div class="controls">
				<input type='text' id='genTableId' name='genTableId' maxlength="64" class="required"
			           value='${scheme.genTableId}' data-name='${scheme.genTableName}'/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">方案名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="20" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">模版分类:</label>
			<div class="controls">
				<form:select path="category" cssClass="required iSelect">
					<form:options items="${config.categoryList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">右表:</label>
			<div class="controls">
				<form:input path="rightTable" htmlEscape="false" maxlength="20"/>
				<div style="padding: 5px;">注：1.如果是作为左树右表的右表，则填写左树的字段，2.如果是作为左树右表的左树，则填写右表的地址</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">生成包路径:</label>
			<div class="controls">
				<form:input path="packageName" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">大模块名:</label>
			<div class="controls">
				<form:input path="moduleName" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">子模块名:</label>
			<div class="controls">
				<form:input path="subModuleName" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">生成功能名:</label>
			<div class="controls">
				<form:input path="functionName" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">生成功能描述:</label>
			<div class="controls">
				<form:input path="functionNameSimple" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">功能增强:</label>
			<div class="controls">
				<label class="checkbox"><input type="checkbox" name="isImport" value="1"> 导入 </label>
				<label class="checkbox"><input type="checkbox" name="isExport" value="1"> 导出 </label>
				<label class="checkbox"><input type="checkbox" name="treeSelect" value="1"> 树组件 </label>
				<label class="checkbox"><input type="checkbox" name="tableSelect" value="1"> 表组件 </label>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存" onclick="$('#flag').val('0');"/>
			<input id="submitBtn" class="btn btn-danger" type="submit" value="保存并生成代码" onclick="$('#flag').val('1');"/>
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
		Public.autoCombo('genTableId', "${ctx}/gen/table/page");
		$(document).on('click','#cancelBtn',function(){
			window.location.href = "${ctx}/gen/scheme/list";
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>