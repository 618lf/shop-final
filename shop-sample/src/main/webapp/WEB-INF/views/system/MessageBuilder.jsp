<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/message.css" rel="stylesheet"/>
<link href="${ctxStatic}/common/common-file.css" rel="stylesheet" />
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>站内信<small> &gt;&gt; 站内信编辑 </small></h3>
	</div>
	<form id="inputForm" name="inputForm" action="${ctx}/system/message/extend/build" method="post" enctype="multipart/form-data" class="form-horizontal">
	  <tags:token/>
	  <div class="control-group formSep">
		<label class="control-label">选择模版:</label>
		<div class="controls">
		     <select id="templateId" name="templateId" class="required iSelect">
		         <c:forEach items="${templates}" var="template">
			    	 <option value="${template.id}">${template.name}</option>
			     </c:forEach>
		     </select>
		</div>
	  </div>
	  <div class="control-group formSep">
			<label class="control-label">选择文件:</label>
			<div class="controls">
		        <input type="file" name="uploadFile" id="uploadFile" class="required selectFile"/>
		        <div class="control-tip">请选择拥有合法后缀名的文件(XLS)</div>
		    </div>
		 </div>
	  <div class="control-group formSep">
			<label class="control-label">标题:</label>
			<div class="controls">
			    <input type="text" id="title" name="title" maxlength="500" class="required" style="width:98%;">
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">内容:</label>
			<div class="controls">
			    <textarea id="content" name="content" style="width:100%;height:280px;"></textarea>
			</div>
		</div>
		<div class="form-actions">
			<input id="saveBtn" class="btn btn-primary" type="button" value="保存"/>
			<input id="cancelBtn" class="btn" type="button" value="关闭"/>
		</div>
	</form>
  </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/ueditor/ueditor.config.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/ueditor.all.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/lang/zh-cn/zh-cn.js" type="text/javascript"></script>
<script src="${ctxStatic}/ueditor/plugins/ueditor.template.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common-file.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function(){
		$("#title").focus();
		$("#inputForm").validate(
			Public.validate({
				submitHandler: function(form){
					var flag = Public.checkFile(form);
					if(!!flag) {
						//Public.loading('正在提交，请稍等...');
						//form.submit();
						Public.doImport(form, null, function() {
							Public.doQuery();
						});
					}
					
				}
			})
		);
	},
	addEvent : function(){
		//暂时只支持填
		$(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
		//只保存草稿，不发送邮件
		$(document).on('click','#saveBtn',function(){
			$('#inputForm').submit();
		});
		//编辑框
		Public.simpleUEditor('content');
		Public.singleFile("input[type='file'].selectFile");
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>