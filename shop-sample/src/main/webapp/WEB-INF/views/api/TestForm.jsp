<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>测试</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/api.css?v=${version}" rel="stylesheet"/>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>测试<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="test" action="${ctx}/api/test/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="documentId"/>
		<form:hidden path="requestHeaders"/>
	    <form:hidden path="queryParams"/>
	    <div class="control-group formSep">
			<label class="control-label">接口地址<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="requestUrl" htmlEscape="false" maxlength="255" class="required " disabled="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">请求方法<span class="red">*</span>:</label>
			<div class="controls">
		      <form:select path="requestMethod" cssClass="iSelect" disabled="true">
		         <form:option value="POST">POST</form:option>
		         <form:option value="GET">GET</form:option>
		      </form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">请求头部:</label>
			<div class="controls">
			   <table id="sample-table" class="table simple-table table-striped table-bordered">
			      <thead><tr><th>头部标签</th><th>头部内容</th></tr></thead>
			      <tbody id="headers"></tbody>
			   </table>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">请求参数:</label>
			<div class="controls">
			   <table id="sample-table" class="table simple-table table-striped table-bordered">
			      <thead><tr><th>是否必填</th><th>参数类型</th><th>参数名称</th><th>参数值</th><th>参数说明</th></tr></thead>
			      <tbody id="reqParams"></tbody>
			   </table>
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
<script src="${ctxModules}/system/api.js?v=${version}" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function() {
		$("#title").focus();
		$("#inputForm").validate(
			Public.ajaxValidate({
				submitHandler: function(form){
					// 构造参数
					var reqParams = Test.buildPostData();
					if (!reqParams) {
						return;
					}
					$('#queryParams').val(JSON.stringify(reqParams));
					Public.loading('正在提交，请稍等...');
					$(form).ajaxSubmit({
						url: $(form).attr('action'),
						dataType:"json",
						beforeSubmit : function(){},  
						async: true,
						success: function(data){
							Public.success('操作成功！', function() {
								$('#id').val(data.obj);
							});
						},
						error : function(x){
							Public.loaded();
							var msg = $.parseJSON(x.responseText).msg;
							Error.out(msg);
						}
					});
				}
			})
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
	Test.init();
});
</script>
<script type="text/html" id="headersTemplate">
<tr><td width="150"><span class="name"></span></td><td width="150"><span class="value"></span></td></tr>
</script>
<script type="text/html" id="reqParamsTemplate">
<tr><td width="80"><span class="notNull"></span></td><td width="120"><span class="type"></span></td>
    <td width="120"><span class="name"></span></td><td width="120"><input type="text" class="val" placeholder="参数值"></td>
    <td width="150"><span class="value"></span></td>
</tr>
</script>
</body>
</html>
