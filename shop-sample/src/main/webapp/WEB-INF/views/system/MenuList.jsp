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
				<input type="hidden" name="id" value="${id}"/>
				<div class="fl">
				  <div class="ui-btn-menu">
				      <span class="ui-btn ui-menu-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu " style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">菜单名称:</label>
								<div class="controls">
									<input type="text" name="name"/>
								</div>
						   </div>
						   <div class="control-group formSep" id="more-conditions" style="display: none;">
								<label class="control-label">菜单地址:</label>
								<div class="controls">
									<input type="text" name="href"/>
								</div>
						   </div>
					       <div class="ui-btns"> 
					          <a href="javascript:void(0)" id="conditions-trigger" class="ui-conditions-trigger" tabindex="-1">更多条件<b></b></a>
				              <input class="btn btn-primary query" type="button" value="查询"/>
				              <input class="btn reset" type="button" value="重置"/>
				           </div> 
				      </div>
				  </div>
				  <input type="button" id="refreshBtn" class="btn btn-primary" value="&nbsp;刷&nbsp;新&nbsp;">
				</div>
				<div class="fr">
				   <input type="button" id="addBtn" class="btn btn-primary" value="&nbsp;添&nbsp;加&nbsp;">
				   <input type="button" id="sortBtn" class="btn" value="&nbsp;保存排序&nbsp;">
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
var curRow,curCol;
var THISPAGE = {
	_init : function(){
		this.loadGrid();
		this.addEvent();
	},
	loadGrid : function(){
		var init = Public.setGrid();
		var menuTypeFmt = function(cellvalue, options, rowObject) {
			var text = "";
			if( cellvalue == 1) {
				text = "目录";
			} else if( cellvalue == 2 ) {
				text = "菜单";
			} else if( cellvalue == 3 ) {
				text = "权限";
			}
			return text;
		};
		var optionsFmt = function (cellvalue, options, rowObject) {
			return Public.billsOper(cellvalue, options, rowObject, true);
		};
		var showFmt = function(cellvalue, options, rowObject) {
			return cellvalue == '1'?'是':'否';
		};
		var nameFmt = function(cellvalue, options, rowObject) {
			return "<a class='a-edit' data-id='"+rowObject.id+"'>"+cellvalue+"<a>";
		};
		$('#grid').jqGrid(
			Public.treeGrid({
				url: '${ctx}/system/menu/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1,
				rownumbers: !1,//序号
				cellsubmit: "clientArray",
				colNames: ['名称', 'ID', '类型', '权限', '排序', '显示', '操作'],
				colModel: [
					{name:'name', index:'name', width:200,sortable:false,formatter:nameFmt},
					{name:'id', index:'id', width:150,sortable:false,hidden:true},
					{name:'type', index:'type',align:'center',width:100,sortable:false,formatter:menuTypeFmt},
					{name:'permission', index:'permission',align:'left',width:150,sortable:false},	
					{name:'sort', index:'sort', align:'center',width:80,sortable:false,editable: !0, edittype:'text'},	
					{name:'isShow', index:'isShow', align:'center',width:80,sortable:false,formatter:showFmt},
					{name:'options', index:'options',align:'center',width:150,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
		$("#grid").jqGrid("setGridParam", { cellEdit: !0  });
	},
	addEvent : function(){
		var that = this;
		Public.initBtnMenu();
		var deleteMenu = function(t){
			if(!!t && t.length) {
				var param = [];
				param.push({name:'idList',value:t});
				Public.deletex("确定删除选中的菜单？","${ctx}/system/menu/delete",param,function(data){
					if(!!data.success){
						Public.success('删除成功！');
						Public.doQuery();
					} else {
						Public.error("要删除的菜单存在子菜单或分配了权限!");
					}
				});
			} else {
				Public.error("请选择要删除的菜单!");
			}
		};
		$(document).on('click','#addBtn',function(){
			var checkeds = $('#grid').getGridParam('selrow');
			if(!!checkeds) {
				window.location.href = "${ctx}/system/menu/form?parentId="+checkeds;
			} else {
				window.location.href = "${ctx}/system/menu/form";
			}
		});
		$(document).on('click','#refreshBtn',function(){
			Public.doQuery();
		});
		$(document).on('click','#delBtn',function(){
			var checkeds = $('#grid').getGridParam('selrow');
			deleteMenu(checkeds);
		});
		$(document).on('click','#sortBtn',function(){
			var checkeds = that.getPostData();
			Public.executex("确定保存菜单的顺序？","${ctx}/system/menu/sort",{
				postData: JSON.stringify(checkeds)
			},function(data){
				if(!!data.success){
					Public.success('修改成功');
					Public.doQuery();
				} else {
					Public.error(data.msg);
				}
			});
		});
		$('#dataGrid').on('click','.add',function(e){
			window.location.href = "${ctx}/system/menu/form?parentId="+$(this).attr('data-id');
		});
        $('#dataGrid').on('click','.delete',function(e){
        	deleteMenu($(this).attr('data-id'));
		});
        $('#dataGrid').on('click','.edit',function(e){
        	window.location.href = "${ctx}/system/menu/form?id="+$(this).attr('data-id');
		});
        $('#dataGrid').on('click','.a-edit',function(e){
        	window.location.href = "${ctx}/system/menu/form?id="+$(this).attr('data-id');
		});
	},
	getPostData : function(){
		if(curRow !== null && curCol !== null){
		   $("#grid").jqGrid("saveCell", curRow, curCol);
		   curRow = null;
	       curCol = null;
		}
		for (var t = [], e = $("#grid").jqGrid("getDataIDs"), i = 0, a = e.length; a > i; i++) {
			var r, n = e[i],  o = $("#grid").jqGrid("getRowData", n);
			r = {
   	            id: o.id,
   	            sort: o.sort
   	        };
   	        t.push(r);
		}
		return t;
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>