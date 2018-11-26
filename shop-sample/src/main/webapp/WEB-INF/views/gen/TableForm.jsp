<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
  .table-edit {
    background-color: #FFAA24;
    color: #fff;
  }
  .table-query {
    background-color: #5CB85C;
    color: #fff;
  }
</style>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills" style="width: 94%;">
    <div class="page-header">
		<h3>业务表配置<small> &gt;&gt; 编辑</small></h3>
	</div>
	<c:choose>
	   <c:when test="${empty table.name}">
	   <form:form id="inputForm" modelAttribute="table" action="${ctx}/gen/table/save" method="post" class="form-horizontal">
			<tags:token/>
			<form:hidden path="id"/>
			<div class="control-group">
				<label class="control-label">业务表名:</label>
				<div class="controls">
					<form:select path="name" items="${tables}" itemLabel="comments" itemValue="name" cssClass="iSelect"/>
				</div>
			</div>
			<div class="form-actions">
				<input id="submitBtn" class="btn btn-primary" type="submit" value="下一步"/>
				<input id="cancelBtn" class="btn" type="button" value="返回"/>
			</div>
	   </form:form>
	   </c:when>
	   <c:otherwise>
	   <form:form id="inputForm" modelAttribute="table" action="${ctx}/gen/table/save" method="post" class="form-horizontal form-fluid" cssStyle="margin:0px;">
			<tags:token/>
			<form:hidden path="id"/>
			<div class="clearfix">
			<div class="control-group">
				<label class="control-label">业务表名:</label>
				<div class="controls">
				    <form:input path="name" readonly="true"/>
				</div>
			</div>
			</div>
			<div class="widget-box transparent">
			    <div class="widget-body">
			        <div class="widget-body-inner">
			        <div class="widget-main  no-padding">
			             <table id="sample-table" class="table sample-table table-striped table-bordered">
		                    <thead>
		                       <tr>
									<th width="80">列名</th><th width="80">说明</th><th width="80">DB类型</th><th width="80">JAVA类型</th><th width="80">JAVA属性名</th><th>主键</th>
									<th class="table-edit">编辑</th><th width="100" class="table-edit">显示类型</th><th width="100" class="table-edit">类型校验</th><th class="table-edit">可空</th>
									<th class="table-query">列表</th><th class="table-query">查询</th><th width="80" class="table-query">查询匹配</th>
								</tr>
		                    </thead>
		                    <tbody>
		                        <c:forEach items="${table.columns}" var="column" varStatus="i">
		                        <tr>
		                            <td>${column.name}<input type="hidden" name="items.name" id="${i.index}_name" value="${column.name}"/></td>
		                            <td><input type="text" name="items.comments" id="${i.index}_comments" value="${column.comments}"/></td>
		                            <td>${column.dbType}<input type="hidden" name="items.dbType" id="${i.index}_dbType" value="${column.dbType}"/></td>
		                            <td><select name="items.javaType" id="${i.index}_javaType" data-value="${column.javaType}" class="select2" disabled="disabled">
		                                  <c:forEach items="${config.javaTypeList}" var="type">
		                                  <option value="${type.value}"  ${type.value==column.javaType?'selected':''}>${type.label}</option>
		                                  </c:forEach>
		                                </select>
		                            </td>
		                            <td><input type="text" name="items.javaField" id="${i.index}_javaField" value="${column.javaField}" readonly="readonly"/></td>
		                            <td><input type="checkbox" disabled="disabled" name="items.isPk" id="${i.index}_isPk" value="1" ${column.isPk eq '1' ? 'checked' : ''}/></td>
		                            <td><input type="checkbox" <c:if test="${column.isPk == '1'}">disabled="disabled"</c:if> name="items.isEdit" id="${i.index}_isEdit" value="1" ${column.isEdit eq '1' ? 'checked' : ''}/></td>
		                            <td><select name="items.showType" id="${i.index}_showType" data-value="${column.showType}" class="select2" <c:if test="${column.isPk == '1'}">disabled="disabled"</c:if>>
		                                  <c:forEach items="${config.showTypeList}" var="type">
		                                  <option value="${type.value}"  ${type.value==column.showType?'selected':''}>${type.label}</option>
		                                  </c:forEach>
		                                </select>
		                            </td>
		                            <td><select name="items.checkType" id="${i.index}_checkType" data-value="${column.checkType}" class="select2" <c:if test="${column.isPk == '1'}">disabled="disabled"</c:if>>
		                                  <option value=""></option>
		                                  <c:forEach items="${config.checkTypeList}" var="type">
		                                  <option value="${type.value}"  ${type.value==column.checkType?'selected':''}>${type.label}</option>
		                                  </c:forEach>
		                                </select>
		                            </td>
		                            <td><input type="checkbox" <c:if test="${column.isDbNull == '0' || column.isPk == '1'}">disabled="disabled"</c:if> name="items.isNull" id="${i.index}_isNull" value="1" ${column.isNull eq '1' ? 'checked' : ''}/></td>
		                            <td><input type="checkbox" <c:if test="${column.isPk == '1'}">disabled="disabled"</c:if> name="items.isList" id="${i.index}_isList" value="1" ${column.isList eq '1' ? 'checked' : ''}/></td>
		                            <td><input type="checkbox" <c:if test="${column.isPk == '1'}">disabled="disabled"</c:if> name="items.isQuery" id="${i.index}_isQuery" value="1" ${column.isQuery eq '1' ? 'checked' : ''}/></td>
		                            <td><select name="items.queryType" id="${i.index}_queryType" data-value="${column.queryType}" class="select2" <c:if test="${column.isPk == '1'}">disabled="disabled"</c:if>>
		                                  <c:forEach items="${config.queryTypeList}" var="type">
		                                  <option value="${type.value}"  ${type.value==column.queryType?'selected':''}>${type.label}</option>
		                                  </c:forEach>
		                                </select>
		                            </td>
		                        </tr>
		                        </c:forEach>
		                    </tbody>
			             </table>
			        </div>
			        </div>
			    </div>
		    </div>
			<div class="form-actions">
				<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
				<input id="cancelBtn" class="btn" type="button" value="返回"/>
			</div>
	   </form:form>
	   </c:otherwise>
	</c:choose>
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
		$("#title").focus();
		$("#inputForm").validate(
			Public.validate({
				submitHandler: function(form){
					Public.loading('正在提交，请稍等...');
					$("#sample-table input[type=checkbox]").each(function(){
						$(this).after("<input type=\"hidden\" name=\""+$(this).attr("name")+"\" value=\""
								+($(this).is(":checked")?"1":"0")+"\"/>");
						$(this).attr("name", "_"+$(this).attr("name"));
					});
					$("#sample-table select").each(function(){
						if( $(this).attr('disabled') == 'disabled') {
							$(this).removeAttr('disabled');
						}
					});
					form.submit();
				}
			})
		);
	},
	addEvent : function(){
		$(".select2").select2();
		Public.combo('parentTable');
		Public.autoCascadeCombo('parentTableFk', "${ctx}/gen/table/column/page",null,'name', 'tableName');
		$(document).on('click','#cancelBtn',function(){
			window.location.href = "${ctx}/gen/table/list";
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>