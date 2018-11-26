<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>文本回复</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/wechat.css" rel="stylesheet"/>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>文本回复<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="metaText" action="${ctx}/wechat/meta/text/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
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
			<label class="control-label">内容<span class="red">*</span>:</label>
			<div class="controls">
			 <div class="config-content">
		       <textarea name="content" class="config-content-textarea -config" placeholder="填写文本回复" maxlength="600" style="overflow: hidden; word-wrap: break-word; height: 120px;">${metaText.content}</textarea>
		       <div class="config-content-bar">
		          <a class="-qq-emo -tools" title="插入表情"><i class="iconfont icon-biaoqing"></i></a>
		          <a class="-set-link -tools" title="插入链接"><i class="iconfont icon-lianjie"></i></a>
		          <div class="pull-right word-num">还能输入 600 字</div>
		       </div>
		      </div>
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
<script src="${ctxModules}/system/wechat.js" type="text/javascript"></script>
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