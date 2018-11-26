<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>图文回复</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/wechat.css" rel="stylesheet" />
</head>
<body style="overflow: hidden; background-color: white;">
<div class="wrapper wrapper-dialog">
    <div class="top">
	    <form name="queryForm" id="queryForm" class="form-horizontal form-inline form-fluid">
	      <input type="hidden" value="${extId}" name="extId">
	      <div class="dialog-menu">
			<div class="control-group">
			   <div class="controls">
			      <div class="input-prepend">
				  <span class="add-on">关键词</span>
				  <input type="text" name="keyword" maxlength="255">
				  </div>
			    </div>
			</div>
			<div class="control-group">
			   <div class="controls">
			      <div class="input-prepend">
				  <span class="add-on">标题</span>
				  <input type="text" name="title" maxlength="255">
				  </div>
			    </div>
			</div>
			<div class="form-actions">
		      <input class="btn btn-primary query" type="button" value="查询"/>
		      <input class="btn reset" type="button" value="重置"/>
		   	</div>
		  </div>
		</form>
	</div>
	<div id="dataGrid" class="autoGrid">
		<table id="grid"></table>
		<div id="page"></div>
	</div> 
</div>
<%@ include file="/WEB-INF/views/include/list-footer.jsp"%>
<script type="text/javascript">
/**
 * 重写获取NAME值
 */
Public.selectedRowNames = function(grid){
	var param = [];
	var checkeds = $(grid||'#grid').getGridParam('selarrrow');
	if( !!checkeds && checkeds.length ) {
		if(typeof(checkeds) === 'object'){
			$.each(checkeds,function(index,item){
				var id = $(grid||'#grid').getRowData(item).title;
				param.push({name:'idList',value:id});
			});
		} else {
			param.push({name:'idList',value:checkeds});
		}
	}
	return param;
};
var THISPAGE = {
	_init : function(){
		this.loadGrid();
		this.addEvent();
	},
	loadGrid : function(){
		var init = Public.setGrid();
		var keywordsFmt = function (cellvalue, options, rowObject) {
			var keys = [];
			$.each(cellvalue.split(','), function(index, item) {
				keys.push('<a class="keyword">' + item + '</a>');
			});
			return keys.join('');
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/wechat/meta/rich/tableSelect/data?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
                rownumbers: !0,//序号
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['ID', '关键词', '标题', '创建时间', ], 
				colModel: [
                    {name:'id', index:'id', width:80,sortable:false,hidden:true},
                    {name:'keyword', index:'keyword', width:120, align:'left', sortable:false,formatter:keywordsFmt},
                    {name:'title', index:'title', width:250, align:'left', sortable:false},
                    {name:'createDate', index:'createDate', width:120, align:'left', sortable:false},
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>
