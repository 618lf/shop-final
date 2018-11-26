<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>微信公众号</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>微信公众号<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="app" action="${ctx}/wechat/app/save" method="post" class="form-horizontal">
		<tags:token/>
		<div class="control-group formSep">
			<label class="control-label">APP_ID<span class="red">*</span>:</label>
			<div class="controls">
			  <c:if test="${empty app.id}">
		      <form:input path="id" htmlEscape="false" maxlength="100" class="required" />
		      </c:if>
		      <c:if test="${!(empty app.id)}">
		      <form:input path="id" htmlEscape="false" maxlength="100" class="required" readonly="true"/>
		      </c:if>
		      <div class="control-tip">微信后台APPID(保存之后不能修改，请谨慎填写)</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">APP_SECRET<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="appSecret" htmlEscape="false" maxlength="100" class="required" />
		      <div class="control-tip">微信后台APP_SECRET</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">公众账户名称<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="name" htmlEscape="false" maxlength="100" class="required"/>
		      <div class="control-tip">用于前台宣传显示，请和微信后台公众号名称设置一致</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">类型:</label>
			<div class="controls">
			  <form:select path="type">
			     <form:option value="0">订阅号</form:option>
			     <form:option value="1">服务号</form:option>
			  </form:select>
		      <div class="control-tip">描述公众号的类型：订阅号或服务号</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">微信号<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="srcId" htmlEscape="false" maxlength="100" class="required" />
		      <div class="control-tip">微信后台微信号</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">宣传二维码<span class="red">*</span>:</label>
			<div class="controls">
			  <tags:attachment name="qrCode" value="${app.qrCode}"></tags:attachment>
		      <div class="control-tip">自定义的微信二维码，用户扫描之后可以关注公众号</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">公众帐号TOKEN<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="accessToken" htmlEscape="false" maxlength="100" class="required"/>
		      <div class="control-tip">用于接入微信</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">关注页面地址<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="attentionUrl" htmlEscape="false" maxlength="255" class="required" />
		      <div class="control-tip">建议在微信后台建立微信公众号的描述文章，并将地址填入</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">域名:</label>
			<div class="controls">
		      <form:input path="domain" htmlEscape="false" maxlength="100" class=" " />
		      <div class="control-tip">此域名的请求会通过当前公众号来授权, 如果不填写，则作为默认授权的公众号</div>
		      <div class="control-tip">不包含http:// 或 https://</div>
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
			window.location.href = "${ctx}/wechat/app/list";
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>
