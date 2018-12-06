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
		        <input type="hidden" name="orgId" id="orgId" value="${orgId}"/>
				<div class="fl">
				  <div class="ui-btn-menu">
				      <span class="ui-btn ui-menu-btn ui-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu" style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">任务名称:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="name"/>
								</div>
						   </div>
					       <div class="ui-btns"> 
				              <input class="btn btn-primary" type="button" onclick="Public.doQuery()" value="查询"/>
				              <input class="btn" type="button" value="重置"/>
				           </div> 
				      </div>
				  </div>
				  <input type="button" class="btn btn-primary" value="&nbsp;刷&nbsp;新&nbsp;" onclick="Public.doQuery()">
				</div>
				<div class="fr">
				   <input type="button" class="btn btn-primary" id="addBtn" value="&nbsp;添&nbsp;加&nbsp;">
				   <input type="button" class="btn btn-success" id="startBtn" value="&nbsp;启&nbsp;动&nbsp;">
				   <input type="button" class="btn btn-warning" id="pauseBtn" value="&nbsp;暂&nbsp;停&nbsp;">
				   <input type="button" class="btn" id="delBtn" value="&nbsp;删&nbsp;除&nbsp;">
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
		var nextExecuteTimeFmt = function(cellvalue, options, rowObject){
			return cellvalue;
		};
		var allowExecuteFmt = function(cellvalue, options, rowObject){
			if( !cellvalue ) {
				return '无限制';
			} else {
				return cellvalue;
			}
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/task/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
				colNames: ['任务Id','任务名称', '已经执行次数', '上次执行时间','预计下次执行时间', '状态', '待执行命令', '操作'],
				colModel: [
	                {name:'id', index:'id', width:150,sortable:false,hidden:true},
					{name:'name', index:'name', width:150,sortable:false},
					{name:'yetExecuteCount', index:'yetExecuteCount', align:'center', width:100,sortable:false},
					{name:'preExecuteTime', index:'preExecuteTime',align:'center',width:150,sortable:false},
					{name:'nextExecuteTime', index:'nextExecuteTime',align:'center',width:150,sortable:false,formatter:nextExecuteTimeFmt},
					{name:'taskStatusName', index:'taskStatusName',align:'center',width:80,sortable:false},
					{name:'ops', index:'ops',align:'center',width:100,sortable:false},
					{name:'options', index:'options',align:'center',width:80,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
		var deleteTask = function(checkeds){
			if( !!checkeds  ) {
				var param = [];
				if(typeof(checkeds) === 'object'){
					$.each(checkeds,function(index,item){
						var userId = $('#grid').getRowData(item).id;
						param.push({name:'idList',value:userId});
					});
				} else {
					param.push({name:'idList',value:checkeds});
				}
				Public.deletex("确定删除选中的定时任务？","${ctx}/system/task/delete",param,function(data){
					if(!!data){
						Public.success("删除定时任务成功");
						Public.doQuery();
					} else {
						Public.error("删除错误!");
					}
				});
			} else {
				Public.error("请选择要删除的定时任务!");
			}
		};
		//添加
		$(document).on('click','#addBtn',function(){
			window.location.href = "${ctx}/system/task/form";
		});
		//执行
		$(document).on('click','#excBtn',function(){
			var param = [];
			var checkeds = $('#grid').getGridParam('selarrrow');
			if( !!checkeds ) {
				$.each(checkeds,function(index,item){
					var taskId = $('#grid').getRowData(item).id;
					param.push({name:'idList',value:taskId});
				});
				Public.executex("确定执行选中的定时任务,任务会立即执行一次？","${ctx}/system/task/executes",param,function(data){
					if(!!data){
						Public.success("设置立即执行任务成功");
						Public.doQuery();
					} else {
						Public.error("立即执行错误!");
					}
				});
			} else {
				Public.error("请选择要执行的任务!");
			}
		});
		//删除
		$(document).on('click','#delBtn',function(){
			var checkeds = $('#grid').getGridParam('selarrrow');
			deleteTask(checkeds);
		});
		//行事件
		$(document).on('click', '.edit', function(){
			window.location.href = "${ctx}/system/task/form?id="+$(this).attr('data-id');
		});
		//行事件
		$('#dataGrid').on('click','.delete',function(e){
			deleteTask($(this).attr('data-id'));
		});
		//启动
		$(document).on('click','#startBtn',function(){
			var ids = Public.selectedRowIds();
			$.each(ids, function(index, item) {
				var id = item.value;
				Public.executexQuietly("${ctx}/system/task/start", {id:id},function(data){});
			});
			Public.success('启动成功！');
			Public.doQuery();
		});
		//暂停
		$(document).on('click','#pauseBtn',function(){
			var ids = Public.selectedRowIds();
			$.each(ids, function(index, item) {
				var id = item.value;
				Public.executexQuietly("${ctx}/system/task/pause", {id:id},function(data){});
			});
			Public.success('暂停成功！');
			Public.doQuery();
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>