<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<title>测试</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function() {
		$("#title").focus();
		$("#inputForm").validate(
			Public.validate()
		);
	},
	addEvent : function() {
	        
        $(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</head>
<body>
	<div class="row-fluid wrapper">
	   <div class="bills">
	    <div class="page-header">
			<h3>测试<small> &gt;&gt; 编辑</small></h3>
		</div>
		<form:form id="inputForm" modelAttribute="test" action="${ctx}/api/test/save" method="post" class="form-horizontal">
			<tags:token/>
			<form:hidden path="id"/>
			<div class="form-actions">
				<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
				<input id="cancelBtn" class="btn" type="button" value="关闭"/>
			</div>
		</form:form>
	  </div>
	</div>
</body>
</html>
