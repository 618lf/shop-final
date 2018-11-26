<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>缓存监控</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/json/jquery.jsonview.min.css" rel="stylesheet"/>
<style type="text/css">
.col {
   float: left;
   width: 60%;
   border: 1px solid #ddd;
   box-sizing: border-box;
   min-height: 500px;
   padding: 20px;
}
.col-min {
   width: 40%;
   border: 0px solid #ddd;
}
.col .text {
   margin-bottom: 0;
   margin-right: 10px;
}
.col-ops {
   margin-bottom: 20px;
}
.col-content {
   margin-bottom: 20px;
}
.col-detail .col-ops  {
  background-color: #f4f4f4;
  padding: 10px;
  color: #333;
  font-size: 14px;
}
.col-detail .col-ops > span {
  margin-right: 10px;
}
.col-content {
  word-break:break-all;
  word-wrap:break-word;
}
</style>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills" style="width: 90%;">
	  <div class="col col-min">
	     <div class="col-ops"><input type="text" class="key_name text" placeholder="输入key查询"/><a class="key_search btn">查询</a></div>
	     <div class="col-table">
	     <table id="sample-table" class="table sample-table table-striped table-bordered">
            <thead>
				<tr>
					<th class="tc" width="15%">序号</th>
					<th width="20%">缓存级别</th>
					<th>缓存名称</th>
					<th width="15%">操作</th>
				</tr>
			</thead>
			<tbody id="keys"></tbody>
	     </table>
	     </div>
	  </div>
	  <div class="col col-detail">
	     <div class="col-ops">缓存键:<span id="key_key"></span> 有效期:<span id="key_ttl"></span></div>
	     <div class="col-ops">二级缓存:<span id="key_local">/</span></div>
	     <div class="col-ops">缓存值:</div>
	     <div class="col-content">
	         <textarea  id="key_json" style="width: 100%; height: 120px;"></textarea>
	     </div>
	     <div class="col-ops">格式化:</div>
	     <div class="col-content" id="key_json_view">
	     </div>
	  </div>
   </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/json/jquery.jsonview.min.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	 _init : function(){
		
		// 删除
		$(document).on('click', '.-del', function() {
			var type = $(this).closest('.-key').data('type');
			var name = $(this).closest('.-key').data('name');
			var tr = $(this).closest('.-key');
			Public.postAjax('${ctx}/system/cache/delete', {type: type, name : name}, function() {
				Public.toast('删除成功！');
				$('#sample-table').simpleTable('del',tr.get(0));
			});
		});
		
		// 查询
		$(document).on('click', '.key_search', function() {
			var url = '${ctx}/system/cache/search'; var name = $('.key_name').val();
			Public.postAjax(url, {name : name}, function(keys) {
				var htmls = Public.runTemplate($('#keysTemplate').html(), {datas: keys});
				$('#keys').html(htmls);
			});
		});
		
		// 回车查询
		Public.bindEnterDo('.col-min', function() {
			var url = '${ctx}/system/cache/search'; var name = $('.key_name').val();
			Public.postAjax(url, {name : name}, function(keys) {
				var htmls = Public.runTemplate($('#keysTemplate').html(), {datas: keys});
				$('#keys').html(htmls);
			});
		});
		
		// 详情
		$(document).on('click', '.-key', function() {
			var url = '${ctx}/system/cache/detail'; 
			var type = $(this).data('type'); var name = $(this).data('name');
			Public.postAjax(url, {type: type, name : name}, function(data) {
				if (data.success) {
					$('#key_key').text(data.obj.key);
					$('#key_json').val(data.obj.json);
					$('#key_ttl').text(data.obj._ttl);
					$('#key_local').text(!!data.obj.local?('有,' + data.obj.local_ttl):'/');
					$("#key_json_view").JSONView(data.obj.json);
					$("#key_json_view a").each(function() {
						$(this).attr('target', '_blank');
					});
				} else {
					$('#key_key').text('');
					$('#key_json').val('');
					$('#key_ttl').text('');
					$('#key_local').text('/');
					$("#key_json_view").JSONView('{}');
				}
			});
		});
	 }
};
$(function(){
    THISPAGE._init();
});
</script>
<script type="text/html" id="keysTemplate">
{{ for(var i = 0; i< datas.length; i++) { var key = datas[i]; }}
   <tr class="-key" data-type="{{=key.label}}" data-name="{{=key.value}}">
	  <td class="tc">{{=i+1}}</td>
      <td>{{=key.label}}</td>
	  <td>{{=key.value}}</td>
	  <td class="tc"><a class="-del">删除</a></td>
   </tr>
{{ } }}
</script>
</body>
</html>