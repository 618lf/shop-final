<%@ page contentType="text/html;charset=UTF-8" session="false"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台登录</title>
<%@ include file="/WEB-INF/views/include/pageMeta.jsp"%>
<link href="${ctxStatic}/bootstrap/2.3.2/css/bootstrap.css" rel="stylesheet" />
<link href="${ctxStatic}/common/login.css" rel="stylesheet" />
<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
<link href="${ctxStatic}/swiper/swiper.min.css" rel="stylesheet" />
<link href="${ctxStatic}/common/opa-icons.css" rel="stylesheet" />
</head>
<body>
<div class="header">
  <h4 class="header-logo"><sys:site/></h4>
  <h4>后台管理</h4>
  <ul class="nav">
    <li><a href="/" target="_blank">网站首页</a></li>
    <li><a href="/" target="_blank">系统帮助</a></li>
    <li><a href="/" target="_blank">关于我们</a></li>
  </ul>
</div>
<div class="main" id="mainBg">
	<div id="errorMsg" class="main-errors ${(empty error)?'hide mini-errors':''}">
		<h4 class="main-errors-title icons-color"><span class="icon32 icon-alert"></span><span class="msg">${error.msg}</span></h4>
		<p>提示：</p>
		<p>1. 请检查帐号拼写，是否输入有误</p>
		<p>2. 如忘记密码，请联系管理员</p>
	</div>
	<div class="login">
		<div class="loginForm">
		    <form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
		        <tags:token/>
		        <div class="control-group">
		           <label class="input-label control-label" for="username">登录名</label>
		           <div class="controls">
		             <input type="text" id="username" name="username" class="input-block-level required" value="${username}" placeholder="用户名/邮箱">
		           </div>
		        </div>
		        <div class="control-group">
			        <label class="input-label control-label" for="password">密码</label>
					<div class="controls" style="position: relative;">
					  <input type="password" id="password" name="password" class="input-block-level required" value="${password}" placeholder="密码">
					  <div id="passwordTip" class="control-tip" style="display: none;">大写锁定已开启</div>
					</div>
		        </div>
		        <c:if test="${isValidateCodeLogin}">
		        <div class="control-group validateCode">
					<tags:validateCode name="captcha"/>
		        </div>
				</c:if>
				<div class="control-group clearfix">
					<div class="forgetPwdLine" style="display: inline-block;float: left;">						
						 <label for="rememberMe" title="十天内免登录" style="display: inline-block;"> 
						 <input type="checkbox" id="rememberMe" name="rememberMe"/> 十天内免登录（公共环境慎用）
						 </label>
					</div>
					<div class="forgetPwdLine" style="display: none;float: right;">	
						<label for="rememberMe" title="找回密码" style="display: inline-block;"> 
						 <a class="forgetPwd" target="_blank" title="找回密码">忘记密码了?</a></label>					
					</div>
				</div>
				<div class="control-group clearfix">
					<input class="btn btn-large btn-primary" type="submit" value="登 录" style="width:100%;float: left;"/>
				</div>
		    </form>
		</div>
	</div>
	<div class="main-img">
	<div class="swiper-container">
        <div class="swiper-wrapper">
            <div class="swiper-slide"><img alt="" src="${ctxStatic}/img/admin/1.jpg"></div>
            <div class="swiper-slide"><img alt="" src="${ctxStatic}/img/admin/2.jpg"></div>
            <div class="swiper-slide"><img alt="" src="${ctxStatic}/img/admin/3.jpg"></div>
            <div class="swiper-slide"><img alt="" src="${ctxStatic}/img/admin/4.jpg"></div>
            <div class="swiper-slide"><img alt="" src="${ctxStatic}/img/admin/5.jpg"></div>
            <div class="swiper-slide"><img alt="" src="${ctxStatic}/img/admin/6.jpg"></div>
        </div>
    </div>
	</div>
</div>
<div id="footer" class="footer">
	<div class="footer-inner" id="footerInner">
	    <p><sys:site property="company"/> Copyright©2001-<script>document.write(new Date().getFullYear());</script> All Right Reserved.</p>
	</div>
</div>
<script src="${ctxStatic}/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctxStatic}/bootstrap/2.3.2/js/bootstrap.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/template-native.js" type="text/javascript" ></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.method.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common.js" type="text/javascript"></script>
<script src="${ctxStatic}/swiper/swiper.min.js" type="text/javascript"></script>
<script type="text/javascript">
//基础方法
(function(a) {
	a.CapsLock = function(e) {
		var g = {
			preStatus: undefined,
			status: undefined,
			initialize: c,
			isOn: i,
			trigger: d,
			_keyDown: b,
			_keyPress: f,
			_blur: h
		};
		$.extend(CapsLock.prototype, g, e);
		this.initialize(e);
		function c(m) {
			var l = this;
			var j = l.el = m.el;
			var k = {
				keypress: function(n) {
					l._keyPress(n)
				},
				keydown: function(n) {
					l._keyDown(n)
				},
				blur: function(n) {
					l._blur(n)
				}
			};
			l.capsLockInfo = k;
			l.change = m.change;
			$(j).bind('keypress', k.keypress);
			$(j).bind('keydown', k.keydown);
			$(j).bind('blur', k.blur);
		}
		function i() {
			var j = this;
			return j.status
		}
		function d() {
			var j = this;
			if (j.change) {
				j.change(j.status)
			}
		}
		function b(j) {
			var k = this;
			var l = j.which || j.keyCode;
			if (l === 20 && k.status !== undefined) {
				k.preStatus = k.status = !k.status;
				k.trigger(k.status)
			}
		}
		function f(j) {
			var m = this;
			var n = j.which || j.keyCode;
			var k = false;
			if (j.shiftKey) {
				k = true
			} else {
				if (j.modifiers) {
					k = !! (j.modifiers & 4)
				}
			}
			var l = m.preStatus;
			m.preStatus = m.status;
			if (n >= 65 && n <= 90 && !k || n >= 97 && n < 122 && k) {
				m.status = true
			} else {
				if (n >= 65 && n <= 90 && k || n >= 97 && n < 122 && !k) {
					m.status = false
				} else {
					m.status = l
				}
			}
			if (m.status !== m.preStatus) {
				m.trigger(m.status)
			}
		}
		function h() {
			var j = this;
			j.preStatus = j.status = undefined;
			j.trigger(j.status)
		}
	}
})(window);
$(document).ready(function() {
	$("#loginForm").validate({
		submitHandler: function(form){
			$(form).find('input').attr('readonly','readonly');
			$(form).find('.btn-primary').attr('disabled','disabled').val('系统登录中,请稍等...');
			form.submit();
		},
		rules: {
			captcha: {remote: "${ctx}/validate/code", minlength:4}
		},
		messages: {
			username: {required: "请填写用户名."},password: {required: "请填写密码."},
			captcha: {remote: "验证码不正确.", required: "请填写验证码."}
		},
		errorLabelContainer: "#messageBox",
		errorPlacement: function(error, element) {
			var hasMini = $("#errorMsg").hasClass('mini-errors');
			if(!hasMini) {
			   $("#errorMsg").addClass('mini-errors');
			}
			$("#errorMsg").show();
			$("#errorMsg .msg").html(error)
			$("#errorMsg .msg").html($("#errorMsg .msg").html())
		} 
	});
	
	// 背景图片
	var swiper = new Swiper('.swiper-container', {
		slidesPerView: 'auto',
        centeredSlides: true,
        paginationClickable: true,
        loop: true,
        autoplay: 2500,
        autoplayDisableOnInteraction: false,
        effect: 'fade'
	});
	
	// 大小写锁定提示
	var oCapsLockTest = new CapsLock({
		el : $('#password').get(0),
		change : function(bFlag){
			var oHint = $('#passwordTip').get(0);
			if(bFlag){
				oHint.style.display = 'block';
			}else{
				oHint.style.display = 'none';
			}
		}
	});
});
//如果在框架中，则跳转刷新上级页面 --- 有bug,如果在页面输入的第三方登录，则无限循环（虽然不可能在页面输入第三方登录）
if (self.frameElement && (self.frameElement.tagName=="IFRAME"||self.frameElement.tagName=="FRAME")){
	top.location.href="${ctx}/login";//直接这样写，更好一点
}
</script>
</body>
</html>
