<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>图片回复</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>图片回复<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="metaImage" action="${ctx}/wechat/meta/image/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="content"/>
		<div class="control-group formSep">
			<label class="control-label">公众号<span class="red">*</span>:</label>
			<div class="controls">
		      <form:select path="appId" items="${apps}" itemLabel="name" itemValue="id" cssClass="required"></form:select>
		      <div class="control-tip">所属公众号</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">关键词<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="keyword" htmlEscape="false" maxlength="255" class="required iTag"/>
		      <div class="control-tip">关键词用于匹配回复，支持填入多关键词。例如：电话,联系,地址。</div>
			</div>
		</div>
        <div class="control-group formSep">
			<label class="control-label">图片<span class="red">*</span>:</label>
			<div class="controls">
			   <tags:attachment name="image" value="${metaImage.material.url}"/>
			   <div class="control-tip">图片会存储到微信服务器的永久素材（有数量限制，请及时清理）</div>
			</div>
			<div class="controls">
			  <label class='checkbox inline'>
		       <form:checkbox path="isUpdate" value="1"/>如果修改图片，请勾选此选项，才会重新上传图片。
		      </label>
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
			Public.validate()
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
