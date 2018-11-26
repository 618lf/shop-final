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
				url: '${ctx}/api/test/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
                rownumbers: !0,//序号
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['ID', ''],
				colModel: [
                    {name:'id', index:'id', width:80,sortable:false,hidden:true},
					{name:'options', index:'options',align:'center',width:80,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
		var delTest = function(checkeds){
			if (!!checkeds) {
				var param = [];
				if(typeof(checkeds) === 'object'){
					$.each(checkeds,function(index,item){
						var userId = $('#grid').getRowData(item).id;
						param.push({name:'idList',value:userId});
					});
				} else {
					param.push({name:'idList',value:checkeds});
				}
				Public.deletex("确定删除选中的测试？","${ctx}/api/test/delete", param, function(data){
					if(!!data.success) {
						Public.success('删除测试成功');
						Public.doQuery();
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error("请选择要删除的测试!");
			}
		};
		$('#dataGrid').on('click','.edit',function(e){
			var url = "${ctx}/api/test/form?id="+$(this).data('id');
		    Public.openOnTab('test-edit', '编辑测试', url);
		});
		$('#dataGrid').on('click','.delete',function(e){
			delTest($(this).data('id'));
		});
		$(document).on('click','#addBtn',function(e){
			var url = "${ctx}/api/test/form";
			Public.openOnTab('test-add', '添加测试', url);
		});
		$(document).on('click','#delBtn',function(e){
			var checkeds = $('#grid').getGridParam('selarrrow');
			delTest(checkeds);
		});
	}};
$(function(){
	THISPAGE._init();
});
</script>
</head>
<body>
<div class="wrapper">
    <div class="wrapper-inner">
		<div class="top">
		    <form name="queryForm" id="queryForm">
				<div class="fl">
				  <div class="ui-btn-menu">
				      <span class="ui-btn ui-menu-btn">
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu" style="width: 320px;">
					       <div class="ui-btns">
				              <input class="btn btn-primary query" type="button" value="查询"/>
				              <input class="btn reset" type="button" value="重置"/>
				           </div> 
				      </div>
				  </div>
				  <input type="button" class="btn btn-primary" value="&nbsp;刷&nbsp;新&nbsp;" onclick="Public.doQuery()">
				</div>
				<div class="fr">
				   <input type="button" class="btn btn-primary" id="addBtn" value="&nbsp;添&nbsp;加&nbsp;">&nbsp;
				   <input type="button" class="btn" id="delBtn" value="&nbsp;删&nbsp;除&nbsp;">&nbsp;
				</div>
			</form>
		</div>
	</div>
	<div id="dataGrid" class="autoGrid">
		<table id="grid"></table>
		<div id="page"></div>
	</div> 
</div>
</body>
</html>
