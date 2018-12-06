<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>项目</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/api.css?v=${version}" rel="stylesheet"/>
</head>
<body>
<div class="project-site">
  <div class="-name">${project.name}<a class="-ops add-group"><i class="iconfont icon-jiaxianxing"></i>添加分组</a></div>
  <ul class="groups">
	<li class="cur"><a><i class="iconfont icon-project"></i>全部</a></li>
	<li><a data-id="0"><i class="iconfont icon-project"></i>默认</a></li>
    <c:forEach items="${groups}" var="item">
    <li><a data-id="${item.id}"><i class="iconfont icon-project"></i>${item.name}</a></li>
    </c:forEach>
  </ul>
</div>
<div class="project-main">
  <div class="title">全部接口<a class="ops add-document"><i class="iconfont icon-jiaxianxing"></i>添加接口</a></div>
  <table id="project-table" class="table table-striped">
     <tr><th width="200">接口名称</th><th>接口URL</th><th width="120">分组</th><th width="120">更新日期</th><th width="80">操作</th></tr>
     <tbody id="docs"></tbody>
  </table>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxModules}/system/api.js?v=${version}" type="text/javascript"></script>
<script type="text/javascript">
var projectId = '${project.id}';
$(function() {
	Project.init();
	Doc.list();
});
</script>
<script type="text/html" id="docTemplate">
{{ for(var i = 0; i < datas.length; i++) { var item = datas[i]; }}
<tr data-id="{{=item.id}}"><td><a class="-detail"><i class="iconfont icon-jiekou"></i>{{=item.name}}</a></td><td><code>{{=item.requestMethod}}</code>{{=item.requestUrl}}</td><td>{{=item.groupName}}</td><td>{{=item.updateDate}}</td><td><a class="-edit">修改</a><a class="-del">删除</a></td></tr>
{{ } }}
</script>
</body>
</html>