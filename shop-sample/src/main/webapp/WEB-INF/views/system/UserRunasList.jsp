<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户切换</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>用户切换<small> &gt;&gt; 赋权</small></h3>
	</div>
	<div style="margin-bottom: 5px;"><input type="text" id="userId" name="userId" style="margin: 0;margin-right: 10px;"><a class="btn grant">赋权</a></div>
	<table id="sample-table" class="table sample-table table-bordered" style="width: 350px;">
	    <thead><tr><th>用户名称</th><th>操作</th></tr></thead>
	    <tbody>
	      <c:forEach items="${runas}" var="item" varStatus="i">
	      <tr><td>${item.userName}</td><td class="tc"><a class="del" data-id="${item.grantUserId}">删除</a></td></tr>
	      </c:forEach>
	    </tbody>
	</table>
  </div>
</div>
<%@ include file="/WEB-INF/views/include/list-footer.jsp"%>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.addEvent();
	},
	addEvent : function(){		    
		Public.autoCombo('#userId', '${ctx}/system/tag/user/page');
		$(document).on('click', '.grant', function() {
			var userId = $('#userId').val();
			if(!!userId) {
			   Public.executex('确定赋权给选择的用户，赋权之后用户将可以行驶您的权限？', '${ctx}/system/user/runas/grant', {userId: userId}, function(data) {
				   if(data.success) {
					  Public.success(data.msg);
					  Public.doQuery();
				   } else {
					  Public.success(data.msg);
				   }
			   })
			}
		});
		
		$(document).on('click', '.del', function() {
			var userId = $(this).data('id');
			if(!!userId) {
			   Public.executex('确定收回权限？', '${ctx}/system/user/runas/revoke', {userId: userId}, function(data) {
				   if(data.success) {
					  Public.success(data.msg);
					  Public.doQuery();
				   } else {
					  Public.success(data.msg);
				   }
			   })
			}
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>