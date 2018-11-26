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
				<div class="fl">
				  <div class="ui-btn-menu">
				      <span class="ui-btn ui-menu-btn ui-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu" style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">类型编码:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="title"/>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">类型名称:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="content"/>
								</div>
						   </div>
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
				url: '${ctx}/gen/scheme/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
				rownumbers: !0,//序号
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['ID', '方案名称', '生成模块', '功能名称', ''],
				colModel: [
                    {name:'id', index:'id', width:80,sortable:false,hidden:true},
	                {name:'name', index:'name', width:150,sortable:false},
	                {name:'moduleName', index:'moduleName', width:150,sortable:false},
	                {name:'functionName', index:'functionName', width:150,sortable:false},
					{name:'options', index:'options',align:'center',width:80,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
		var delBusinessType = function(checkeds){
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
				Public.deletex("确定删除选中的业务类型？","${ctx}/gen/scheme/delete",param,function(data){
					if(!!data.success) {
						Public.success('删除业务类型成功');
						Public.doQuery();
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error("请选择要删除的业务类型!");
			}
		};
		$('#dataGrid').on('click','.edit',function(e){
			window.location.href = "${ctx}/gen/scheme/form?id="+$(this).attr('data-id');
		});
		$('#dataGrid').on('click','.delete',function(e){
			delBusinessType($(this).attr('data-id'));
		});
		$(document).on('click','#addBtn',function(e){
			window.location.href = "${ctx}/gen/scheme/form";
		});
		$(document).on('click','#delBtn',function(e){
			var checkeds = $('#grid').getGridParam('selarrrow');
			delBusinessType(checkeds);
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>