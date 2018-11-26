<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
  #curFilterClass {
	font-weight: normal;
	font-size: 14px;
	line-height: 30px;
	width: 300px;
  }
</style>
</head>
<body>
<div class="wrapper">
    <div class="wrapper-inner">
		<div class="top">
		    <form name="queryForm" id="queryForm">
		        <input type="hidden" name="officeId" id="officeId" value="${officeId}"/>
				<div class="fl">
				  <div class="ui-btn-menu">
				      <span class="ui-btn ui-menu-btn ui-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu" style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">角色名称:</label>
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
				<div id="curFilterClass" class="fl es" title="组织结构" style="width: 200px;"></div>
				<div class="fr">
				   <input type="button" id="addBtn" class="btn btn-primary" value="&nbsp;添&nbsp;加&nbsp;">
				   <input type="button" id="delBtn" class="btn"  value="&nbsp;删&nbsp;除&nbsp;">
				</div>
			</form>
		</div>
	</div>
	<div class="main-wrap">
		<div class="col-sub">
			<div class="mod-box" id="fixedAssets-box">
				<div class="hd">组织结构 <a class='fr' id="cancelSelectOffice">取消选择</a></div>
				<div class="bd">
				     <ul id="department-tree" class="ztree"></ul>
				</div>
			</div>
	    </div>
	    <div class="col-main">
		    <div id="dataGrid" class="autoGrid">
				<table id="grid"></table>
				<div id="page"></div>
			</div> 
	    </div>
	</div>
</div>
<%@ include file="/WEB-INF/views/include/list-footer.jsp"%>
<script src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common-tree.js" type="text/javascript" ></script>
<script type="text/javascript">
var THISPAGE = {
	zTree : null,
	_init : function(){
		this.loadGrid();
		this.loadTree();
		this.addEvent();
	},
	loadGrid : function(){
		var optionsFmt = function (cellvalue, options, rowObject) {
			return Public.billsOper(cellvalue, options, rowObject);
		};
		var init = Public.setGrid();
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/role/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:true, 
				colNames: ['角色ID','角色名称', '角色编码', '角色权限标识', '创建人', '创建时间', '操作'],
				colModel: [
	                {name:'id', index:'id', width:150,sortable:false,hidden:true},
					{name:'name', index:'name', width:200,sortable:false},
					{name:'code', index:'code',width:100,sortable:false},
					{name:'permission', index:'permission',width:120,sortable:false},
					{name:'createName', index:'creator', align:'center', width:150,sortable:false},
					{name:'createDate', index:'createDate',align:'center',width:150,sortable:false},	
					{name:'options', index:'options',align:'center',width:120,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	loadTree : function(){
		zTree = Public.tree({ztreeDomId:'department-tree',
	        remoteUrl:'${ctx}/system/office/treeSelect',
	        callback:{onClick:function(event, treeId, treeNode){
				if(treeNode.id) {
					$('#officeId').val(treeNode.id);
					$("#curFilterClass").html('「组织结构：' + treeNode.name + '」').attr('title', treeNode.name);
					Public.doQuery();
				}
	         }}
	    });
	},
	addEvent : function(){
		Public.initBtnMenu();
		var deleteRole = function(t){
			if( !!t && t.length ) {
				var param = [];
				if(typeof(t) === 'object'){
					$.each(t,function(index,item){
						var roleId = $('#grid').getRowData(item).id;
						param.push({name:'idList',value:roleId});
					});
				} else {
					param.push({name:'idList',value:t});
				}
				Public.deletex("确定删除选中的角色？","${ctx}/system/role/delete",param,function(data){
					if(data.success){
						Public.success("删除成功");
						Public.doQuery();
					}else{
						Public.error(data.msg);
					}
				});
			} else {
				Public.error("请选择要删除的角色!");
			}
		};
		$(document).on('click','#addBtn',function(){
			window.location.href = "${ctx}/system/role/form?officeId=" + $('#officeId').val();
		});
		$(document).on('click','#refreshBtn',function(){
			Public.doQuery();
		});
		$(document).on('click','#delBtn',function(){
			var checkeds = $('#grid').getGridParam('selarrrow');
			deleteRole(checkeds);
		});
		$(document).on('click','#cancelSelectOffice',function(){
			var zTreeObj = zTree._getZTreeObj();
			var nodes = zTreeObj.getSelectedNodes();
			$.each(nodes,function(index,item){
				zTreeObj.cancelSelectedNode(item);
			});
			$('#officeId').val('');
			$("#curFilterClass").html('').attr('title', '');
			Public.doQuery();
		});
        $('#dataGrid').on('click','.delete',function(e){
        	deleteRole($(this).attr('data-id'));
		});
        $('#dataGrid').on('click','.edit',function(e){
        	window.location.href = "${ctx}/system/role/form?id="+$(this).attr('data-id');
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>