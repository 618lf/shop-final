<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
         <h3>字典管理 <small> &gt;&gt; 编辑</small></h3>
    </div>
	<form:form id="inputForm" modelAttribute="dict" action="${ctx}/system/dict/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<div class="control-group formSep">
			<label class="control-label">名称<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required" cssStyle="width:250px;"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">键<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="code" htmlEscape="false" maxlength="50" class="required" cssStyle="width:250px;"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">值<span class="red">*</span>:</label>
			<div class="controls">
				<form:textarea path="value" rows="5" cols="5" cssStyle="width:75%;" cssClass="required"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="cancelBtn" class="btn" type="button" value="返 回"/>
		</div>
	</form:form>
   </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function(){
		$("#label").focus();
		$("#inputForm").validate(
			Public.validate({
				rules:{
					code:{
						 required: true,
						 remote: {
							    url: "${ctx}/system/dict/checkDictCode", 
							    type: "post",  
							    dataType: "json", 
							    data: {     
							    	id: function() {
							            return $("#id").val();
							        },
							        dictCode: function() {
							            return $("#code").val();
							        }
							    }
						 }
					}
				},
				messages: {
					code:{ remote:'键重复！' }
				}
			})
		);
	},
	addEvent : function(){
		$(document).on('click','#cancelBtn',function(){
			window.location.href = "${ctx}/system/dict/list";
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>