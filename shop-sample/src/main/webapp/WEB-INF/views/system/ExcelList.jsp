<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/common/common-file.css" rel="stylesheet" />
<script type="text/html" id="impDiv">
<div class="row-fluid">
   <form id="impForm" method="post" class="form-horizontal" enctype="multipart/form-data">
      <tags:token/>
      <input type="hidden" id="templateId" name="templateId" value=""> 
      <div class="control-group formSep">
		<label class="control-label">选择文件(*.xls):</label>
		<div class="controls">
		     <input type="file" name="file" class="required selectFile"/>
		</div>
	  </div>
   </form>
</div>
</script>
</head>
<body>
<div class="wrapper">
    <div class="wrapper-inner">
		<div class="top">
		    <form name="queryForm" id="queryForm">
		        <input type="hidden" name="orgId" id="orgId" value="${orgId}"/>
				<div class="fl">
				  <div class="ui-btn-menu">
				      <span class="ui-btn ui-menu-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu " style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">模板名称:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="menuName"/>
								</div>
						   </div>
					       <div class="ui-btns"> 
				              <input class="btn btn-primary query" type="button" value="查询"/>
				              <input class="btn reset" type="button" value="重置"/>
				           </div> 
				      </div>
				  </div>
				  <input type="button" id="refreshBtn" class="btn btn-primary" value="&nbsp;刷&nbsp;新&nbsp;">
				</div>
				<div class="fr">
				   <input type="button" id="addBtn" class="btn btn-primary" value="&nbsp;添&nbsp;加&nbsp;"/>
				   <input type="button" id="expBtn" class="btn btn-success" value="&nbsp;导出模版&nbsp;"/>
				   <input type="button" id="impBtn" class="btn btn-warning" value="&nbsp;校验模版&nbsp;"/>
				   <input type="button" id="delBtn" class="btn"  value="&nbsp;删&nbsp;除&nbsp;"/>
				</div>
			</form>
		</div>
	</div>
	<div id="dataGrid" class="autoGrid">
		<table id="grid"></table>
		<div id="page"></div>
	</div> 
</div>
<%@ include file="/WEB-INF/views/include/list-footer.jsp"%>
<script src="${ctxStatic}/common/common-file.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.loadGrid();
		this.addEvent();
	},
	loadGrid : function(){
		var init = Public.setGrid();
		var optionsFmt = function (cellvalue, options, rowObject) {
			return Public.billsOper(cellvalue, options, rowObject);
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/excel/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:true, 
				colNames: [' ', '模板名称', '模板类型', '目标类名称', '开始行号', '创建人', '创建日期', '特殊属性','操作'],
				colModel: [
                    {name:'id', index:'id', width:150,sortable:false,hidden:true},
					{name:'name', index:'name', width:150,sortable:false},
					{name:'type', index:'type',width:150,sortable:false},
					{name:'targetClass', index:'targetClass',align:'center',width:150,sortable:false},
					{name:'startRow', index:'startRow', align:'center', width:150,sortable:false},
					{name:'createName', index:'createName',align:'center',width:150,sortable:false},
					{name:'createDate', index:'createDate',align:'center',width:150,sortable:false},
					{name:'extendAttr', index:'extendAttr',align:'center',width:150,sortable:false},
					{name:'options', index:'options',align:'center',width:120,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		var that = this;
		Public.initBtnMenu();
		var deleteTemplate = function(checkeds){
			var param = [];
			if(typeof(checkeds) === 'object'){//数组
				$.each(checkeds,function(index,item){
					var id = $('#grid').getRowData(item).id;
					param.push({name:'idList',value:id});
				});
			} else {
				param.push({name:'idList',value:checkeds});
			}
			if(param.length != 0){
				Public.deletex("确定删除选中的模板？","${ctx}/system/excel/delete",param,function(){
					Public.success("删除成功！");
					Public.doQuery();
				});
			}else {
				Public.error("请选择要删除的模板!");
			}
		};
		$(document).on('click','#addBtn',function(){
			window.location.href = "${ctx}/system/excel/form";
		});
		$(document).on('click','#refreshBtn',function(){
			Public.doQuery();
		});
		$(document).on('click','#delBtn',function(){
			var checkeds = $('#grid').getGridParam('selarrrow');
			deleteTemplate(checkeds);
		});
		$(document).on('click','#expBtn',function(){
			var param = [];
			var checkeds = $('#grid').getGridParam('selarrrow');
			if(typeof(checkeds) === 'object'){//数组
				$.each(checkeds,function(index,item){
					var id = $('#grid').getRowData(item).id;
					param.push({name:'id',value:id});
				});
			}
			if(param.length != 0){
				Public.doExport('${ctx}/system/excel/export',param);
			}else {
				Public.error("请选择要导出的模板!");
			}
		});
        $('#dataGrid').on('click','.delete',function(e){
        	deleteTemplate($(this).attr('data-id'));
		});
        $('#dataGrid').on('click','.edit',function(e){
        	window.location.href = "${ctx}/system/excel/form?id="+$(this).attr('data-id');
		});
        //导入
		$(document).on('click','#impBtn',function(e){
			var ids = Public.selectedRowIds();
			var id = null;
			if(!ids || ids.length != 1) {
			   Public.error('请只选择一个需要验证模板');
			} else {
			   id = ids[0].value;
			   Public.openImportDialog($("#impDiv").html(), "#impForm", '${ctx}/system/excel/doImport', '导入Excel校验模板', function() {
				   Public.success('Excel 验证通过');
			   }, null, function() {
				   $('#templateId').val(id);
			   });
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