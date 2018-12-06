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
<div class="projects">
<div class="projects-inner">
   <div class="project add-project">
      <div class="-img"><i class="iconfont icon-jiaxianxing"></i></div>
   </div>
</div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script type="text/html" id="projectTemplate">
{{ for(var i=0; i<datas.length; i++) { var item = datas[i]; }}
<div class="project" data-id="{{=item.id}}">
	<div class="-img"><i class="iconfont icon-project"></i></div>
	<div class="-name">{{=item.name}}</div>
	<div class="-remarks">{{=item.remarks}}</div>
	<div class="-ops"><a class="-edit">编辑</a><a class="-del">删除</a><a class="-manage">管理</a></div>
</div>
{{ } }}
</script>
<script type="text/javascript">
$(function() {
	var params = {
		'param.pageIndex' : 1,
		'param.pageSize': 20
	};
	Public.postAjax('${ctx}/api/project/page', params, function(page) {
		var datas = page.data;
		var html = Public.runTemplate($('#projectTemplate').html(), {datas: datas});
		$('.projects-inner').append(html);
	});
	$(document).on('click', '.-del', function() {
		var id = $(this).closest('.project').data('id'); var $p = $(this).closest('.project');
		Public.postAjax('${ctx}/api/project/delete', [{'name': 'idList', 'value': id}], function(data) {
			if (data.success) {
				Public.success('删除成功', function() {
					$p.remove();
				});
			} else {
				Public.error(data.msg);
			}
		});
	});
	$(document).on('click', '.-edit', function() {
		var url = '${ctx}/api/project/form?id=' + $(this).closest('.project').data('id');
		Public.openOnTab('project-edit', '编辑项目', url);
	});
	$(document).on('click', '.-manage', function() {
		var url = '${ctx}/api/project/manage?id=' + $(this).closest('.project').data('id');
		Public.openOnTab('project-manage', '接口管理', url);
	});
	$(document).on('click', '.add-project', function() {
		var url = '${ctx}/api/project/form';
		Public.openOnTab('project-edit', '添加项目', url);
	});
});
</script>
</body>
</html>