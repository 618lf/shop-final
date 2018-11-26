<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/pageMeta.jsp"%>
<title>图标选择</title>
<link href="${ctxStatic}/bootstrap/2.3.2/css/bootstrap.css" rel="stylesheet" />
<link href="${ctxStatic}/fonts/iconfont.css" rel="stylesheet"/>
<style type="text/css">
	.the-icons {padding:25px 10px 15px;list-style:none;}
	.the-icons li {float:left;width:22%;line-height:25px;margin:2px 5px;cursor:pointer;}
	.the-icons i {margin:1px 5px; font-size: 16px;} .the-icons li:hover {background-color:#efefef;}
	.the-icons li.active {background-color:#0088CC;color:#ffffff;}
</style>
</head>
<body>
<input type="hidden" id="icon" value="${value}" />
<ul class="the-icons clearfix">
    <li><i class="iconfont icon-xialajiantou"></i>icon-xialajiantou</li>
    <li><i class="iconfont icon-shanglajiantou"></i>icon-shanglajiantou</li>
    <li><i class="iconfont icon-menu"></i>icon-menu</li>
    <li><i class="iconfont icon-location"></i>icon-location</li>
    <li><i class="iconfont icon-zhanghuweihu"></i>icon-zhanghuweihu</li>
    <li><i class="iconfont icon-bianji"></i>icon-bianji</li>
    <li><i class="iconfont icon-quxiao"></i>icon-quxiao</li>
    <li><i class="iconfont icon-chevron-right"></i>icon-chevron-right</li>
    <li><i class="iconfont icon-chevron-left"></i>icon-chevron-left</li>
    <li><i class="iconfont icon-chevron-up"></i>icon-chevron-up</li>
    <li><i class="iconfont icon-chevron-down"></i>icon-chevron-down</li>
    <li><i class="iconfont icon-search"></i>icon-search</li>
    <li><i class="iconfont icon-pencil"></i>icon-pencil</li>
    <li><i class="iconfont icon-edit"></i>icon-edit</li>
    <li><i class="iconfont icon-plus"></i>icon-plus</li>
    <li><i class="iconfont icon-remove"></i>icon-remove</li>
    <li><i class="iconfont icon-refresh"></i>icon-refresh</li>
    <li><i class="iconfont icon-list-alt"></i>icon-list-alt</li>
    <li><i class="iconfont icon-code"></i>icon-code</li>
    <li><i class="iconfont icon-networkmonitor"></i>icon-networkmonitor</li>
    <li><i class="iconfont icon-report"></i>icon-report</li>
    <li><i class="iconfont icon-lock"></i>icon-lock</li>
    <li><i class="iconfont icon-modify"></i>icon-modify</li>
    <li><i class="iconfont icon-caret-down"></i>icon-caret-down</li>
    <li><i class="iconfont icon-caret-right"></i>icon-caret-right</li>
    <li><i class="iconfont icon-caret-left"></i>icon-caret-left</li>
    <li><i class="iconfont icon-caret-up"></i>icon-caret-up</li>
    <li><i class="iconfont icon-config"></i>icon-config</li>
    <li><i class="iconfont icon-message"></i>icon-message</li>
    <li><i class="iconfont icon-image"></i>icon-image</li>
    <li><i class="iconfont icon-order"></i>icon-order</li>
    <li><i class="iconfont icon-product"></i>icon-product</li>
    <li><i class="iconfont icon-free"></i>icon-free</li>
    <li><i class="iconfont icon-shirt"></i>icon-shirt</li>
    <li><i class="iconfont icon-content"></i>icon-content</li>
    <li><i class="iconfont icon-weixin"></i>icon-weixin</li>
    <li><i class="iconfont icon-qq"></i>icon-qq</li>
    <li><i class="iconfont icon-sina"></i>icon-sina</li>
    <li><i class="iconfont icon-shequ2"></i>icon-shequ2</li>
    <li><i class="iconfont icon-api"></i>icon-api</li>
</ul>   
<script type="text/javascript" src="${ctxStatic}/jquery/jquery-1.8.3.min.js"></script> 
<script type="text/javascript">
 $(document).ready(function(){
 	$(".the-icons li").click(function(){
 		$(".the-icons li").removeClass("active");
 		$(".the-icons li i").removeClass("icon-white");
 		$(this).addClass("active");
 		$("#icon").val($(this).children('i').attr('class'));
 		$(this).children("i").addClass("icon-white");
 	});
 	$(".the-icons li").each(function(){
 		if ($(this).text()=="${value}"){
 			$(this).click();
 		}
 	});
 });
</script>
</body>
</html>