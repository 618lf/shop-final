<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>系统发生内部错误【500】</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
<link href="${ctxStatic}/weui/style/weui.min.css" rel="stylesheet"/>
<script src="${ctxStatic}/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
</head>
<body>
<div class="weui_msg">
    <div class="weui_icon_area"><i class="weui_icon_msg weui_icon_warn"></i></div>
    <div class="weui_text_area">
        <h2 class="weui_msg_title">系统发生内部错误</h2>
        <p class="weui_msg_desc">请反馈给系统管理员，谢谢！</p>
    </div>
    <div class="weui_opr_area">
        <p class="weui_btn_area">
           <a href="${ctxFront}" class="weui_btn weui_btn_primary">去首页逛逛</a>
        </p>
    </div>
</div>
</body>
</html>