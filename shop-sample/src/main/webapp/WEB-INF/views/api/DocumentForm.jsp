<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>接口</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link rel="stylesheet" href="${ctxStatic}/editor.md-1.5.0/main/editormd.min.css">
<link href="${ctxModules}/system/api.css?v=${version}" rel="stylesheet"/>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills" style="width: 90%;">
    <div class="page-header">
		<h3>接口<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="document" action="${ctx}/api/document/save" method="post" class="form-horizontal" data-monitorable="0">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="projectId"/>
		<form:hidden path="requestHeaders"/>
		<form:hidden path="queryParams"/>
		<form:hidden path="responseParams"/>
		<form:hidden path="remarks"/>
		<form:hidden path="starLevel"/>
		<div class="control-group formSep">
			<label class="control-label">接口名称<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="name" htmlEscape="false" maxlength="255" class="required " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">接口地址<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="requestUrl" htmlEscape="false" maxlength="255" class="required " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">请求方法<span class="red">*</span>:</label>
			<div class="controls">
		      <form:select path="requestMethod" cssClass="iSelect">
		         <form:option value="POST">POST</form:option>
		         <form:option value="GET">GET</form:option>
		      </form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">接口分组<span class="red">*</span>:</label>
			<div class="controls">
		      <form:select path="groupId">
		         <form:option value="0">默认</form:option>
		         <c:forEach items="${groups}" var="item">
		         <form:option value="${item.id}">${item.name}</form:option>
		         </c:forEach>
		      </form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">接口状态<span class="red">*</span>:</label>
			<div class="controls">
		      <label class='radio inline'>
		        <form:radiobutton path="status" value="0"/>启用
		      </label>
		      <label class='radio inline'>
		        <form:radiobutton path="status" value="1"/>维护
		      </label>
		      <label class='radio inline'>
		        <form:radiobutton path="status" value="2"/>废弃
		      </label>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">请求头部:</label>
			<div class="controls">
			   <a class="add-header -btn">添加头部</a>
			   <table id="sample-table" class="table simple-table table-striped table-bordered">
			      <tbody id="headers"></tbody>
			   </table>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">请求参数:</label>
			<div class="controls">
			   <a class="add-reqParams -btn">添加参数</a>
			   <table id="sample-table" class="table simple-table table-striped table-bordered">
			      <tbody id="reqParams"></tbody>
			   </table>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">响应结果:</label>
			<div class="controls">
			    <ul class="nav nav-tabs success">
				  <li class="active"><a href="#success_resp" data-toggle="tab">成功响应结果</a></li>
				</ul>
				<div class="tab-content">
				  <div class="tab-pane active" id="success_resp">
					  <div class="response-type">
					  <c:forEach items="${ResponseContentType}" var="item">
                      <label class="radio inline"><input <c:if test="${document.successRespType eq item}">checked="checked"</c:if> type="radio" class="iCheck" name="successRespType" value="${item}">${item}</label>
                      </c:forEach>
                      </div>
					  <textarea placeholder="输入内容" id="successRespExample" name="successRespExample">${document.successRespExample}</textarea>
				  </div>
				</div>
				<ul class="nav nav-tabs failure">
				  <li class="active"><a href="#failure_resp" data-toggle="tab">失败响应结果</a></li>
				</ul>
				<div class="tab-content">
				  <div class="tab-pane active" id="failure_resp">
				     <div class="response-type">
				     <c:forEach items="${ResponseContentType}" var="item">
                     <label class="radio inline"><input <c:if test="${document.failRespType eq item}">checked="checked"</c:if> type="radio" class="iCheck" name="failRespType" value="${item}">${item}</label>
                     </c:forEach>
                     </div>
				    <textarea placeholder="输入内容" id="failRespExample" name="failRespExample">${document.failRespExample}</textarea>
				  </div>
				</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">响应结果参数:</label>
			<div class="controls">
			   <a class="add-resParams -btn">添加参数</a>
			   <table id="sample-table" class="table simple-table table-striped table-bordered">
			      <tbody id="resParams"></tbody>
			   </table>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">接口备注:</label>
			<div class="controls">
			   <div id="remark_wrap">
			     <textarea style="display:none;">${document.remarks}</textarea>
			   </div>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="cancelBtn" class="btn" type="button" value="关闭"/>
		</div>
	</form:form>
  </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/editor.md-1.5.0/main/editormd.min.js"></script>
<script src="${ctxModules}/system/api.js?v=${version}" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function() {
		var self = this;
		$("#title").focus();
		$("#inputForm").validate(
			Public.ajaxValidate({
				submitHandler: function(form){
					$('.price-val').each(function(index, item) {
						_priceValRfmt(item);   
			        });
					
					// 构造参数
					var postData = self.buildPostData();
					if (!postData) {
						return;
					}
					$('#remarks').val(self.remarkEditor.getMarkdown());
					$('#requestHeaders').val(JSON.stringify(postData.headers));
					$('#queryParams').val(JSON.stringify(postData.reqParams));
					$('#responseParams').val(JSON.stringify(postData.resParams));
					Public.loading('正在提交，请稍等...');
					$(form).ajaxSubmit({
						url: $(form).attr('action'),
						dataType:"json",
						beforeSubmit : function(){},  
						async: true,
						success: function(data){
							Public.success('操作成功！', function() {
								$('#id').val(data.obj);
							});
						},
						error : function(x){
							Public.loaded();
							var msg = $.parseJSON(x.responseText).msg;
							Error.out(msg);
						}
					});
				}
			})
		);
	},
	addEvent : function() {
        var remarkEditor = editormd("remark_wrap", {
    		width   : "100%",
    		height  : 550,
    		syncScrolling : "single",
    		path    : base + "/static/editor.md-1.5.0/lib/",
    		autoFocus:false,
            placeholder : "请输入备注",
            saveHTMLToTextarea : true
    	});
        
        // 方便获取数据
        this.remarkEditor = remarkEditor;
        
        // 添加头部
        $(document).on('click', '.add-header', function() {
        	var html = $('#headersTemplate').html();
        	$('#headers').append(html);
        });
        // 请求参数
        $(document).on('click', '.add-reqParams', function() {
        	var html = $('#reqParamsTemplate').html();
        	$('#reqParams').append(html);
        });
        // 请求参数
        $(document).on('click', '.add-resParams', function() {
        	var html = $('#resParamsTemplate').html();
        	$('#resParams').append(html);
        });
        // 请求参数
        $(document).on('click', '.-del', function() {
        	$(this).closest('tr').remove();
        });
        $(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
	},
	
	// 构造参数
	buildPostData : function() {
		return Doc.buildPostData();
	},
};
$(function(){
	THISPAGE._init();
	Doc.init();
});
</script>
<script type="text/html" id="headersTemplate">
<tr><td width="150"><select class="iSelect name"><c:forEach items="${requestHeadersEnum}" var="item"><option value="${item}">${item}</option></c:forEach></select>
</td><td width="150"><input type="text" class="value" placeholder="头部内容"></td>
<td width="40"><a class="-del"><i class="iconfont icon-remove"></i></a></td></tr>
</script>
<script type="text/html" id="reqParamsTemplate">
<tr>
  <td><select class="iSelect notNull"><option value="true">必填</option><option value="false">非必填</option></select></td>
  <td><select class="iSelect type"><c:forEach items="${QueryParamTypeEnum}" var="item"><option value="${item}">${item}</option></c:forEach></select></td>
  <td><input type="text" class="name" placeholder="参数名称"></td><td><input type="text" class="value" placeholder="参数说明"></td>
  <td width="40"><a class="-del"><i class="iconfont icon-remove"></i></a></td>
</tr>
</script>
<script type="text/html" id="resParamsTemplate">
<tr>
  <td><select class="iSelect notNull"><option value="true">非空</option><option value="false">可空</option></select></td>
  <td><select class="iSelect type"><c:forEach items="${QueryParamTypeEnum}" var="item"><option value="${item}">${item}</option></c:forEach></select></td>
  <td><input type="text" class="name" placeholder="参数名称"></td><td><input type="text" class="value" placeholder="参数说明"></td>
  <td width="40"><a class="-del"><i class="iconfont icon-remove"></i></a></td>
</tr>
</script>
</body>
</html>