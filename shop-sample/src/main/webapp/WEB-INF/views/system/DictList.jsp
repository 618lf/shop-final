<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
</head>
<body>
<div class="wrapper">
    <div class="wrapper-inner">
		<div class="top">
		    <form name="queryForm" id="queryForm">
		        <input type="hidden" name="id" id="id" value="${id}"/>
				<div class="fl">
				  <div class="ui-btn-menu">
				      <span class="ui-ui-btn ui-menu-btn ui-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu" style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">字典名称:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="name"/>
								</div>
						   </div>
					       <div class="ui-btns"> 
				              <input class="btn btn-primary query" type="button" value="查询"/>
				              <input class="btn reset" type="button" value="重置"/>
				           </div> 
				      </div>
				  </div>
				  <input type="button" class="btn btn-primary" id="refreshBtn" value="&nbsp;刷&nbsp;新&nbsp;" onclick="Public.doQuery()">
				</div>
				<div class="fr">
				   <input type="button" id="addBtn" class="btn btn-primary" value="&nbsp;添&nbsp;加&nbsp;">
				   <input type="button" id="delBtn" class="btn"  value="&nbsp;删&nbsp;除&nbsp;">
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
				url: '${ctx}/system/dict/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
				colNames: ['名称', '键', '值', '操作'],
				colModel: [
					{name:'name', index:'name', width:120, sortable:false},
					{name:'code', index:'code', align:'left', width:150, sortable:false},
					{name:'value', index:'value',align:'left', width:250, sortable:false},
					{name:'options', index:'options',align:'center',width:150,sortable:false, formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
		var deleteDict = function(checkeds){
			if (!!checkeds) {
				var param = [];
				param.push({name:'idList',value:checkeds});
				Public.deletex("确定删除选中的参数配置？","${ctx}/system/dict/delete",param,function(data){
					Public.success('删除选中的参数配置成功');
					Public.doQuery();
				});
			} else {
				Public.error("请选择要删除的参数配置!");
			}
		};
		$(document).on('click','#addBtn',function(){
			window.location.href = "${ctx}/system/dict/form";
		});
		$(document).on('click','#refreshBtn',function(){
			Public.doQuery();
		});
		$(document).on('click','#delBtn',function(){
			var checkeds = $('#grid').getGridParam('selrow');
			deleteDict(checkeds);
		});
		$('#dataGrid').on('click','.add',function(e){
			window.location.href = "${ctx}/system/dict/form";
		});
        $('#dataGrid').on('click','.delete',function(e){
        	deleteDict($(this).attr('data-id'));
		});
        $('#dataGrid').on('click','.edit',function(e){
        	window.location.href = "${ctx}/system/dict/form?id="+$(this).attr('data-id');
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>