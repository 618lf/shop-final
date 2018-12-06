<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>接口</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/api.css?v=${version}" rel="stylesheet"/>
<link rel="stylesheet" href="${ctxStatic}/editor.md-1.5.0/main/editormd.min.css">
</head>
<body class="doc-detail-body">
<input type="hidden" id="requestHeaders" value='${document.requestHeaders}'>
<input type="hidden" id="queryParams" value='${document.queryParams}'>
<input type="hidden" id="responseParams" value='${document.responseParams}'>
<div class="doc-detail">
   <h3>${document.name}</h3>
   <div class="url"><code>${document.requestMethod}</code>${document.requestUrl}</div>
   <div id="remarks"><textarea style="display:none;">${document.remarks}</textarea></div>
   <h3>请求头</h3>
   <table id="sample-table" class="table simple-table table-striped table-bordered">
      <thead><tr><th>头部标签</th><th>头部内容</th></tr></thead>
      <tbody id="headers"></tbody>
   </table>
   <h3>请求参数</h3>
   <table id="sample-table" class="table simple-table table-striped table-bordered">
      <thead><tr><th>是否必填</th><th>参数类型</th><th>参数名称</th><th>参数说明</th></tr></thead>
      <tbody id="reqParams"></tbody>
   </table>
   <h3>响应结果</h3>
   <div class="response-type">正确响应返回${document.successRespType}格式如下：</div>
   <div class="example">${document.successRespExample}</div>
   <div class="response-type">失败响应返回${document.failRespType}格式如下：</div>
   <div class="example">${document.failRespExample}</div>
   <h3>响应结果参数</h3>
   <table id="sample-table" class="table simple-table table-striped table-bordered">
      <thead><tr><th>是否必填</th><th>参数类型</th><th>参数名称</th><th>参数说明</th></tr></thead>
      <tbody id="resParams"></tbody>
   </table>
   <h3>接口测试</h3>
   <div class="test-wrap">
      <a class="ops add-test" data-id="${document.id}" style="margin-bottom: 10px; display: block;"><i class="iconfont icon-jiaxianxing"></i>添加接口测试</a>
      <table id="sample-table" class="table simple-table table-striped table-bordered">
	      <thead><tr><th width="150">创建时间</th><th width="120">操作</th></tr></thead>
	      <tbody>
	      <c:forEach items="${tests}" var="test">
	      <tr data-id="${test.id}"><td>${fns:formatDate(test.createDate, 'yyyy-MM-dd HH:mm')}</td><td><a class="modify-test">修改</a><a class="-doc_run">运行</a><a class="-doc_del">删除</a></td></tr>
	      </c:forEach>
	      </tbody>
	  </table>
   </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/editor.md-1.5.0/main/editormd.min.js"></script>
<script src="${ctxStatic}/editor.md-1.5.0/lib/marked.min.js"></script>
<script src="${ctxStatic}/editor.md-1.5.0/lib/prettify.min.js"></script>
<script src="${ctxModules}/system/api.js?v=${version}" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
	remarkView = editormd.markdownToHTML("remarks", {
		htmlDecode      : "style,script,iframe",  // you can filter tags decode
		emoji           : false,
		taskList        : false,
		tex             : false,  // 默认不解析
		flowChart       : false,  // 默认不解析
		sequenceDiagram : false,  // 默认不解析
	});
	
	// 初始化详情
	Doc.initDetail();
});
</script>
<script type="text/html" id="headersTemplate">
<tr><td width="150"><span class="name"></span></td><td width="250"><span class="value"></span></td></tr>
</script>
<script type="text/html" id="reqParamsTemplate">
<tr><td width="150"><span class="notNull"></span></td><td width="150"><span class="type"></span></td>
    <td width="150"><span class="name"></span></td><td width="150"><span class="value"></span></td>
</tr>
</script>
<script type="text/html" id="resParamsTemplate">
<tr><td width="150"><span class="notNull"></span></td><td width="150"><span class="type"></span></td>
    <td width="150"><span class="name"></span></td><td width="150"><span class="value"></span></td>
</tr>
</script>
</body>
</html>