<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>权限错误</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<link href="${ctxStatic}/weui/style/weui.min.css" rel="stylesheet"/>
</head>
<body>
<div class="weui_msg">
    <div class="weui_icon_area"><i class="weui_icon_msg weui_icon_warn"></i></div>
    <div class="weui_text_area">
        <h2 class="weui_msg_title">您没有相关权限</h2>
        <p class="weui_msg_desc">请反馈给系统管理员，谢谢！</p>
        <sys:isRunAs>
        <div class="weui_opr_area">
	        <p class="weui_btn_area">
	           <a id="runas-exit" class="runas-exit" href="javascript:" class="weui_btn weui_btn_primary">退出切换</a>
	        </p>
	    </div>
        </sys:isRunAs>
    </div>
</div>
<script src="${ctxStatic}/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery/jquery.cookie.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common.js?v=${version}" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	$(document).on('click','#runas-exit',function(){
		Public.postAjax('${ctx}/system/user/runas/release', null, function(data) {
			window.location.href = '${ctx}';
		}, false);
	});
});
</script>
</body>
</html>