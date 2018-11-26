<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/pageMeta.jsp"%>
<link href="${ctxStatic}/bootstrap/2.3.2/css/bootstrap.css" rel="stylesheet" />
<link href="${ctxStatic}/fonts/iconfont.css" rel="stylesheet"/>
<link href="${ctxStatic}/jquery-tab/tabs.css" rel="stylesheet" />
<link href="${ctxStatic}/common/common.css?v=${version}" rel="stylesheet" />
<link href="${ctxStatic}/common/layout.css?v=${version}" rel="stylesheet" />
</head>
<body>
<div id="container" class="clearfix"> 
   <div class="sidebar" id="sidebar" data-href='${ctx}/system/self/menu' data-version="${ctxStatic}/img/admin/icon_v_b.png"></div>
   <div class="content" id="content">
     <sys:isRunAs><div class="runas"><a id="runas-exit" class="runas-exit">退出切换</a><div class="bd"></div></div></sys:isRunAs>
     <div class="content-head" id="content-head">
       <div class="title"><span class="first"><sys:site/></span><span class="second">管理后台</span></div>
       <ul class="user-menu">
         <li><a href="https://mpkf.weixin.qq.com/cgi-bin/kfloginpage" target="_blank"><i class="iconfont icon-weixin"></i><span>客服</span></a></li><li class="space">|</li>
         <li><a id="messageInfo" href="javascript:void(0)"><i class="iconfont icon-message message"></i><span>(<b>0</b>)</span></a></li><li class="space">|</li>
         <sys:hasRole name="admin:runas"><li><a id="switchto" href="javascript:void(0)">切换</a></li><li class="space">|</li></sys:hasRole>
         <li><a id="persionInfo" href="javascript:void(0)">您好,<sys:user/></a></li><li class="space">|</li>
         <sys:rememberMe><li id="loginDialogWrap"><a href="javascript:void(0)" onclick="User.loginDialog()">密码认证</a></li><li class="space">|</li></sys:rememberMe>
         <li><a href="javascript:void(0)" onclick="User.lockScreen()">锁屏</a></li><li class="space">|</li>
         <li><a href="${ctx}/logout">退出</a></li>
       </ul>
     </div>
     <div class="content-body" id="content-body">
         <div id="page-tab" class="page-tab"></div>
     </div>
   </div>
</div>
<script src="${ctxStatic}/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/template-native.js" type="text/javascript" ></script>
<script src="${ctxStatic}/jquery-tab/tabs.js" type="text/javascript"></script>
<script src="${ctxStatic}/layer/layer.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common.js?v=${version}" type="text/javascript"></script>
<script src="${ctxStatic}/common/common-dialog.js?v=${version}" type="text/javascript"></script>
<script src="${ctxStatic}/common/SysIndex.js?v=${version}" type="text/javascript"></script>
<script src="${ctxStatic}/common/UserCenter.js?v=${version}" type="text/javascript"></script>
<script type="text/javascript">
var GUser = {
	id : '<sys:user property="id"/>',
	name: '<sys:user/>',
	headimg: '<sys:user property="headimg"/>',
	uname: '<sys:user property="username"/>'
};
</script>
</body>
</html>