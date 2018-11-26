<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
  .bills {
    width: auto;
    float: none;
    margin: 15px;
  }
</style>
</head>
<body class="white">
<div class="row-fluid wrapper white">
<form:form id="inputForm" modelAttribute="log" action="${ctx}/system/area/save" method="post" class="form-horizontal form-min">
 	<form:hidden path="id"/>
	<div class="control-group formSep">
		<label class="control-label">日志类型:</label>
		<div class="controls">
			<form:select path="type" disabled="true" cssClass="iSelect">
                 <form:option value="1">访问</form:option>
                 <form:option value="2">异常</form:option>
            </form:select>
		</div>
	</div>
	<div class="control-group formSep">
		<label class="control-label">操作用户:</label>
		<div class="controls">
			<form:input path="createName"/>
		</div>
	</div>
	<div class="control-group formSep">
		<label class="control-label">用户的IP地址:</label>
		<div class="controls">
			<form:input path="remoteAddr" cssStyle="width:75%"/>
		</div>
	</div>
	<div class="control-group formSep">
		<label class="control-label">操作的URI:</label>
		<div class="controls">
			<form:input path="requestUri" cssStyle="width:75%"/>
		</div>
	</div>
	<div class="control-group formSep">
		<label class="control-label">操作的方式:</label>
		<div class="controls">
			<form:input path="method" cssStyle="width:75%"/>
		</div>
	</div>
	<div class="control-group formSep">
		<label class="control-label">用户代理信息:</label>
		<div class="controls">
		    <form:textarea path="userAgent" cols="40" rows="3" cssStyle="width:75%"/>
		</div>
	</div>
	<div class="control-group formSep">
		<label class="control-label">异常信息:</label>
		<div class="controls">
		    <form:textarea path="exception" cols="40" rows="10" cssStyle="width:75%"/>
		</div>
	</div>
</form:form>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
</div>
</body>
</html>