<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>测试</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
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
		var runsFmt = function (cellvalue, options, rowObject) {
			return '<a class="-run" data-id="'+(rowObject.id)+'">运行</a>';
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/api/test/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
                rownumbers: !0,//序号
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['ID', '接口名称', '接口URL', '提交方式', '运行', ''],
				colModel: [
                    {name:'id', index:'id', width:80,sortable:false,hidden:true},
                    {name:'documentName', index:'documentName',align:'center',width:150},
                    {name:'requestUrl', index:'requestUrl',align:'center',width:120},
                    {name:'requestMethod', index:'requestMethod',align:'center',width:120},
                    {name:'run', index:'run',align:'center',width:80,formatter:runsFmt},
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
		$('#dataGrid').on('click','.-run',function(e){
			var id = $(this).data('id');
    		var url = webRoot + '/admin/api/test/run?id=' + id;
    		Public.loading();
    		Public.postAjax(url, {}, function(data) {
    			Public.success('结果：【' + data.obj + '】', -1, function() {});
    		});
		});
		$(document).on('click','#delBtn',function(e){
			var checkeds = $('#grid').getGridParam('selarrrow');
			delTest(checkeds);
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>
