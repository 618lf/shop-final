<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<%@ taglib prefix="fnc" uri="/WEB-INF/tld/fns-com.tld"%>
<html>
<head>
<title>模版配置</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
 .verifyFormats-wrap {
    padding: 10px;
 }
 #verifyFormats  label {
   position: relative;
   padding-left: 35px;
   display: block;
 }
 #verifyFormats  label .checkbox {
   position: absolute;
   left: 0;
   right: auto;
   width: 29px;
   top: 50%;
   margin-top: -10px;
 }
 #verifyFormats td {
   vertical-align: middle;
 }
 #verifyFormats td.on {
   color: #C63E37;
 }
</style>
<script type="text/html" id="verifyFormatTemplate">
<div class="verifyFormats-wrap">
 <table id="verifyFormats" class="table sample-table">
   <thead>
	 <tr>
		<th>校验项目</th>
		<th width="20%">参数</th>
		<th width="40%">提示信息</th>
     </tr>
   </thead>
   <tbody>
    {{ for(var i = 0; i< datas.length; i++ ) { }}
    {{ var data = datas[i]; }}
     <tr>
		<td><label><input class="checkbox" type="checkbox" data-type="{{=data.type}}">{{=data.name}}</label></td>
		<td><input class="param" type="text"></td>
		<td><input class="message" type="text" value="{{=data.message}}"></td>
     </tr>
    {{ } }}
   </tbody>
 </table>
</div>
</script>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills" style="width: 93%;">
     <div class="page-header">
         <h3>模版配置 <small> &gt;&gt; 编辑</small></h3>
     </div>
     <form:form id="inputForm" modelAttribute="template" action="${ctx}/system/excel/save" method="post" class="form-horizontal form-fluid" cssStyle="margin:0px;">
     <tags:token/>
     <form:hidden path="id"/>
     <div class="clearfix">
        <div class="control-group">
			<label class="control-label">模版名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="100" class="required"/>
			</div>
		 </div>
		 <div class="control-group">
				<label class="control-label">模版类型:</label>
				<div class="controls">
					<form:input path="type" htmlEscape="false" maxlength="100" class="required"/>
				</div>
		 </div>
		 <div class="control-group">
				<label class="control-label">目标类:</label>
				<div class="controls">
					<form:input path="targetClass" htmlEscape="false" maxlength="100" class="required"/>
				</div>
		 </div>
		 <div class="control-group">
				<label class="control-label">扩展属性:</label>
				<div class="controls">
					<form:input path="extendAttr" htmlEscape="false" maxlength="100"/>
				</div>
		 </div>
		 <div class="control-group">
				<label class="control-label">开始行:</label>
				<div class="controls">
					<form:select path="startRow" cssClass="iSelect">
					  <form:option value="3">3</form:option>
					  <form:option value="4">4</form:option>
					  <form:option value="5">5</form:option>
					</form:select>
				</div>
		 </div>
     </div>
     <div class="widget-box transparent">
	    <div class='widget-header widget-header-flat'>
		    <h3><small>模版列配置</small></h3>
	    </div>
	    <div class="widget-body">
		     <div class="widget-body-inner">
		     <div class="widget-main  no-padding">
		     <table id="sample-table" class="table sample-table table-striped table-bordered">
				<thead>
					<tr>
						<th class="tc">序号</th>
						<th>操作</th>
						<th>Excel列名称</th>
						<th width="8%">Excel列序号</th>
						<th>类属性</th>
						<th width="8%">数据类型</th>
						<th>格式化表达式</th>
						<th width="25%">校验项目</th>
					</tr>
				</thead>
				<tbody>
				    <c:if test="${fns:length(template.items) == 0 }"> 
				    <tr class='sample-table-row'>
						<td class="index tc">1</td>
						<td class="tc options"><i class="iconfont icon-plus add" title="添加"></i><i class="iconfont icon-remove delete" title="删除"></i></td>
						<td class="tc"><input  name="items.id" type='hidden' maxlength="50" value='-1'/><input  name="items.name" type='text' class="required" maxlength="50"/></td>
						<td class="tc"><select name="items.columnName" class="columnName">
						       <c:forEach items="${fnc:getExcelColumns(26)}" var="vo">
						        <option value="${vo.label}">${vo.value}</option>
						       </c:forEach>
						    </select></td>
						<td class="tc"><input  name="items.property" type='text' class="required"  maxlength="50"/></td>
						<td class="tc"><select name="items.dataType" class="dataType">
						       <c:forEach items="${fnc:getDataTypes()}" var="vo">
						        <option value="${vo.label}">${vo.value}</option>
						       </c:forEach>
						    </select></td>
						<td class="tc"><input  name="items.dataFormat" type='text'  maxlength="200"/></td>
						<td class="tc"><div class="pr"><input type="text" name="items.verifyFormat" class="textbox verifyFormat" readonly="readonly" value=""/><i class="icon-iselect"></i></div></td>
					</tr>
				    </c:if>
				    <c:forEach items="${template.items}" var="item" varStatus="i">
				    <tr class='sample-table-row'>
						<td class="index tc">${i.index +1}</td>
						<td class="tc options"><i class="iconfont icon-plus add" title="添加"></i><i class="iconfont icon-remove delete" title="删除"></i></td>
						<td class="tc"><input  name="items.id" type='hidden' maxlength="50" value='${item.id}'/><input  name="items.name" type='text' class="required" maxlength="50" value="${item.name}"/></td>
						<td class="tc"><select name="items.columnName" class="columnName">
						       <c:forEach items="${fnc:getExcelColumns(32)}" var="vo">
						       <c:if test="${item.columnName eq vo.label}"><option value="${vo.label}" selected="selected">${vo.value}</option></c:if>
						       <c:if test="${not(item.columnName eq vo.label)}"><option value="${vo.label}">${vo.value}</option></c:if>
						       </c:forEach>
						    </select></td>
						<td class="tc"><input  name="items.property" type='text' class="required"  maxlength="50" value="${item.property}"/></td>
						<td class="tc"><select name="items.dataType" class="dataType">
						       <c:forEach items="${fnc:getDataTypes()}" var="vo">
						        <c:if test="${item.dataType eq vo.label}"><option value="${vo.label}" selected="selected">${vo.value}</option></c:if>
						        <c:if test="${not(item.dataType eq vo.label)}"><option value="${vo.label}">${vo.value}</option></c:if>
						       </c:forEach>
						    </select></td>
						<td class="tc"><input  name="items.dataFormat" type='text'  maxlength="200" value='${item.dataFormat}'/></td>
						<td class="tc"><div class="pr"><input type="text" name="items.verifyFormat" class="textbox verifyFormat" readonly="readonly" value='${item.verifyFormat}'/><i class="icon-iselect"></i></div></td>
					</tr>
				    </c:forEach>
				</tbody>
			 </table>
		     </div>
		     </div>
		</div>
	 </div>
	 <div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="btnBack" class="btn" type="button" value="返回"/>
	 </div>
     </form:form>
   </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/common/excel-validate.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(t){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function(){
		$("#templateName").focus();
		$("#inputForm").validate({
				submitHandler: function(form){
					Public.loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					element.addClass('text_error');
				}
		});
	},
	addEvent : function(){
        $("#btnBack").on('click',function(){
        	window.location.href = "${ctx}/system/excel/list";
		});
        Public.initSimpleTableEvent('sample-table');
        $(document).on('focus','#inputForm input', function(){
        	$(this).removeClass('text_error');
        });
        //选择校验表达式
		$(document).on('click','.icon-iselect',function(){
			var datas = []; var that = $(this).parent().find('.verifyFormat');
			$.each(validator.messages, function(index, item) {
				datas.push({
					type: index,
					name: validator.tips[index],
					message: item
				})
			});
			var html = $('#verifyFormatTemplate').html();
			Public.openWindow('选择校验项目', Public.runTemplate(html, {datas:datas}),  580, 470, function() {
				var t = [], r = [], p = [], m = [];
				$('#verifyFormats input[type="checkbox"]:checked').each(function(index, item) {
					var _r = $(this).data('type');
					var _p = $(this).closest('tr').find('.param').val();
					var _m = $(this).closest('tr').find('.message').val();
					r.push(_r);p.push(_p);m.push(_m);
				});
				t[0] = r; t[1] = p; t[2] = m;
				if(p.length == 0) {
				   that.val('');
				} else {
				   var postData = JSON.stringify(t);
				   that.val(postData);
				}
			}, null, function() {
				var postData = that.val();
				if(!!postData) {
					var t = eval(postData), r = t[0], p = t[1], m = t[2];
					$.each(r, function(i, item) {
						$('#verifyFormats input[data-type="'+ item +'"]').click();
						$('#verifyFormats input[data-type="'+ item +'"]').closest('td').addClass('on');
						$('#verifyFormats input[data-type="'+ item +'"]').closest('tr').find('.param').val(p[i]);
						$('#verifyFormats input[data-type="'+ item +'"]').closest('tr').find('.message').val(m[i]);
					})
				}
			});
		});
        //相关事件
        $(document).on('click','#verifyFormats label',function(){
        	var _p = $(this).parent();
        	if( !!$(this).find('input:checked').eq(0).get(0)) {
        		_p.addClass('on');
        	} else {
        		_p.removeClass('on');
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