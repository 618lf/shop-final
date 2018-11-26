<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/include/pageMeta.jsp"%>
<title>二维码服务</title>
<link href="${ctxStatic}/bootstrap/2.3.2/css/bootstrap.css" rel="stylesheet" />
<link href="${ctxStatic}/fonts/iconfont.css" rel="stylesheet"/>
<style type="text/css">
.qrcode-wrap, .qrcode-wrap * {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}
.qrcode-wrap {
  width: 200px;
}
.qrcode-wrap .qrcode { 
  width: 200px;
  height: 200px;
  text-align: center;
  padding: 10px;
}
.qrcode-wrap .toolbar {
  height: 30px;
  line-height: 30px;
  padding-left:10px;
  padding-right: 10px;
  font-size: 13px;
  text-align: center;
  cursor: pointer;
}
.qrcode-wrap .toolbar .qrcode-copy {
  border-right: 1px solid #ddd;
  padding-right: 5px;
}
.qrcode-wrap .toolbar .qrcode-open {
  padding-left: 5px;
}
.qrcode-wrap .fa {
  margin-right: 5px;
  cursor: pointer;
}
</style>
</head>
<body>
<input type="hidden" id="url" value="${url}"/>
<div class="qrcode-wrap">
  <div id="qrcodeCanvas" class="qrcode"></div>
  <div class="toolbar"><a class="qrcode-copy icopy"><i class="iconfont icon-copy"></i>复制</a><a class="qrcode-open" href="${url}" target="_blank"><i class="iconfont icon-share"></i>打开</a></div>
</div>
<script src="${ctxStatic}/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-form/jquery.qrcode.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/zeroclipboard/ZeroClipboard.min.js" type="text/javascript"></script> 
<script src="${ctxStatic}/common/common.js?v=${version}" type="text/javascript" ></script> 
<script type="text/javascript">
$(function() {
  $('.icopy').each(function() {
	(new ZeroClipboard($(this))).on( "copy", function (event) {
	  var clipboard = event.clipboardData;
	  clipboard.setData( "text/plain", $('#url').val());
	  top.Public.info('已复制到剪贴板');
	});
  });
  Public.simpleQrcode('#qrcodeCanvas', "${url}");
});
</script>
</body>
</html>