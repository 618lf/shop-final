<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/></title>
<%@ include file="/WEB-INF/views/include/pageMeta.jsp"%>
<link href="${ctxStatic}/bootstrap/2.3.2/css/bootstrap.css" rel="stylesheet" />
<link href="${ctxStatic}/common/login.css" rel="stylesheet" />
<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/common/opa-icons.css" rel="stylesheet" />
</head>
<body class="login-dialog-body">
<div class="login-warp login-dialog">
<form id="loginForm" class="form-signin" method="post" action="${ctx}/login">
    <input type="hidden" name="isDialog" value="1">
    <input type="hidden" name="returnUrl" value="${ctx}/login/success">
    <input type="hidden" id="username" name="username" value="${username}">
	<div class="-user">
		<div class="user-img"><img alt="" src="${ctxStatic}/img/default_user.jpg" onerror="User.notHeadimg();"></div>
		<div class="user-name">超级管理员</div>
		<div class="tip">已锁定，请输入密码解锁</div>
		<div class="ops input-append">
		  <input type="password" id="password" name="password" class="-text required" placeholder="请输入密码解锁">
		  <button class="add-on -btn" type="submit">解锁</button>
		</div>
		<c:if test="${isValidateCodeLogin}">
        <div class="validateCode">
			<tags:validateCode name="captcha"/>
        </div>
		</c:if>
	</div>
	<div id="messageBox" class="messages ${(empty error)?'hide':''}">${error.msg}</div>
</form>
</div>
<script src="${ctxStatic}/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#loginForm").validate({
		submitHandler: function(form){
			form.submit();
		},
		rules: {
			captcha: {remote: "${ctx}/validate/code", minlength:4}
		},
		messages: {
			username: {required: "请填写用户名."},password: {required: "请填写密码."},
			captcha: {remote: "验证码不正确.", required: "请填写验证码."}
		},
		errorPlacement : function(error) {
			var text = $(error).text();
			$('#messageBox').show().html(text);
		}
	});
	<sys:isGuest> 
	var GUser = top.GUser;
	if (!GUser.uname) {
	    top.location.href="${ctx}/login";
	} else {
		$('#username').val(GUser.uname);
		if (!!GUser.headimg) {
		   $('.user-img > img').attr('src', GUser.headimg);
		}
		$('.user-name').text(GUser.name);
	}
	</sys:isGuest>
});
</script>
</body>
</html>