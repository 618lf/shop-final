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
				      <span class="ui-btn ui-menu-btn btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu" style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">组织名称:</label>
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
				  <input type="button" id="refreshBtn" class="btn btn-primary" value="&nbsp;刷&nbsp;新&nbsp;">
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
		var that = this;
		this.loadGrid();
		this.addEvent();
	},
	loadGrid : function(){
		var optionsFmt = function (cellvalue, options, rowObject) {
			return Public.billsOper(cellvalue, options, rowObject, true);
		};
		var typeFmt = function(cellvalue, options, rowObject){
			if(cellvalue == '1') return '公司';
			if(cellvalue == '2') return '部门';
			if(cellvalue == '3') return '小组';
			return '';
		};
		var init = Public.setGrid();
		$('#grid').jqGrid(
			Public.treeGrid({
				url: '${ctx}/system/office/page?timeid=' + Math.random(),
				height:init.h,
				shrinkToFit:!1, 
				colNames: ['组织名称', '组织CODE', '组织类型', '组织全路径','操作'],
				colModel: [
					{name:'treeName', index:'treeName', width:100,sortable:false},
					{name:'treeCode', index:'treeCode',width:100,sortable:false},
					{name:'treeType', index:'treeType',align:'center',width:100,sortable:false,formatter:typeFmt},
					{name:'treePath', index:'treePath', align:'left', width:180,sortable:false},
					{name:'options', index:'options',align:'center',width:120,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		var that = $(this);
		var deleteOffice = function(t){
			if( !!t ) {
				var param = [];
				param.push({name:'idList',value:t});
				Public.deletex("确定删除选中的组织结构？","${ctx}/system/office/delete",param,function(data){
					if(!!data&&data.success){
						Public.success("删除成功");
						$('#id').val(data.obj.id);
						Public.doQuery();
					} else {
						Public.error("要删除的组织结构存在子组织结构或存在用户!");
					}
				});
			} else {
				Public.error("请选择要删除的组织结构!");
			}
		};
		
		Public.initBtnMenu();
		
		$(document).on('click','#addBtn',function(){
			var checkeds = $('#grid').getGridParam('selrow');
			if(!!checkeds) {
				window.location.href = "${ctx}/system/office/form?parentId="+checkeds;
			} else {
				window.location.href = "${ctx}/system/office/form";
			}
		});
		$(document).on('click','#refreshBtn',function(){
			Public.doQuery();
		});
		$(document).on('click','#delBtn',function(){
			var checkeds = $('#grid').getGridParam('selrow');
			deleteMenu(checkeds);
		});
		
		$('#dataGrid').on('click','.add',function(e){
			window.location.href = "${ctx}/system/office/form?parentId="+$(this).attr('data-id');
		});
        $('#dataGrid').on('click','.delete',function(e){
        	deleteOffice($(this).attr('data-id'));
		});
        $('#dataGrid').on('click','.edit',function(e){
        	window.location.href = "${ctx}/system/office/form?id="+$(this).attr('data-id');
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>