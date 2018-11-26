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
  .image-wrap {
    width: 50px;
    height: 50px;
    padding: 4px;
  }
  .image-wrap > img {
    width: 100%;
    height: 100%;
    border-radius: 8px;
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
								<label class="control-label">用户名称:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="name"/>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">用户编码:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="no"/>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">注册类型:</label>
								<div class="controls">
									<select name="userType" class="iSelect">
									 <option value=""></option>
									  <option value="QQ">QQ</option>
									  <option value="SINA">微博</option>
									  <option value="SITE">网站</option>
									  <option value="WX">微信网站</option>
									  <option value="WE_CHAT">微信公众号</option>
									</select>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">注册时间:</label>
								<div class="controls">
									<input type="text" class="input-txt Wdate" name="createDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">登录时间:</label>
								<div class="controls">
									<input type="text" class="input-txt Wdate" name="loginDate" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
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
				   <input type="button" id="lockBtn" class="btn" value="&nbsp;锁&nbsp;定&nbsp;"/>
				   <input type="button" id="unLockBtn" class="btn" value="&nbsp;解&nbsp;锁&nbsp;"/>
				</div>
			</form>
		</div>
	</div>
	<div class="main-wrap">
		<div class="col-sub">
			<div class="mod-box" id="fixedAssets-box">
				<div class="hd">组织结构<a class='fr' id="cancelSelectOffice">取消选择</a></div>
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
		var init = Public.setGrid();
		var optionsFmt = function (cellvalue, options, rowObject) {
			return Public.billsOper(cellvalue, options, rowObject);
		};
		var nameFmt = function (cellvalue, options, rowObject) {
			return "<a href='javascript:void(0)' class='a-edit' data-id='"+rowObject.id+"' title='编辑'>"+cellvalue+"</a>";
		};
		var imgFmt = function (cellvalue) {
			return '<div class="image-wrap"><img src="'+cellvalue+'" onerror="User.notHeadimg();"></div>';
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/user/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:false,
				rownumbers: !0,
				colNames: ['Id', '头像', '用户名称', '会员号', '状态', '注册时间', '省', '市', '登录IP', '登录时间'],
				colModel: [
	                {name:'id', index:'id', width:150,sortable:false,hidden:true},
	                {name:'headimg', index:'headimg',width:65,sortable:false,align:'center', formatter:imgFmt},
					{name:'name', index:'name', width:120,sortable:false,align:'center', formatter:nameFmt},
					{name:'no', index:'no',width:80,sortable:false,align:'center'},
					{name:'userStatus', index:'userStatus',align:'center',width:100,sortable:false},
					{name:'createDate', index:'createDate',align:'center',width:120,sortable:false},
					{name:'province', index:'province',align:'center',width:100,sortable:false},
					{name:'city', index:'city',align:'center',width:100,sortable:false},
					{name:'loginIp', index:'loginIp',align:'center',width:80,sortable:false},
					{name:'loginDate', index:'loginDate',align:'center',width:120,sortable:false}
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
		var deleteUser = function(checkeds){
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
				Public.deletex("确定删除选中的用户？","${ctx}/system/user/delete",param,function(data){
					if(!!data.success) {
						Public.success('删除用户成功');
						Public.doQuery();
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error("请选择要删除的用户!");
			}
		};
		$(document).on('click','#addBtn',function(){
			var url = null;
			if(!!$('#officeId').val()) {
				url = "${ctx}/system/user/form?officeId=" + $('#officeId').val();
			} else {
				url = "${ctx}/system/user/form";
			}
			Public.openOnTab('user-edit', '添加用户', url);
		});
		$(document).on('click','#refreshBtn',function(){
			Public.doQuery();
		});
		$(document).on('click','#delBtn',function(){
			var checkeds = $('#grid').getGridParam('selarrrow');
			deleteUser(checkeds);
		});
		$(document).on('click','#lockBtn',function(){
			var param = [];
			var checkeds = $('#grid').getGridParam('selarrrow');
			if( !!checkeds  ) {
				$.each(checkeds,function(index,item){
					var userId = $('#grid').getRowData(item).id;
					param.push({name:'idList',value:userId});
				});
				Public.executex("确定锁定选中的用户？","${ctx}/system/user/lockUser",param,function(){
					Public.success('锁定用户成功');
					Public.doQuery();
				});
			} else {
				Public.error("请选择要锁定的用户!");
			}
		});
		$(document).on('click','#unLockBtn',function(){
			var param = [];
			var checkeds = $('#grid').getGridParam('selarrrow');
			if( !!checkeds  ) {
				$.each(checkeds,function(index,item){
					var userId = $('#grid').getRowData(item).id;
					param.push({name:'idList',value:userId});
				});
				Public.executex("确定解锁选中的用户？","${ctx}/system/user/unLockUser",param,function(){
					Public.success('解锁用户成功');
					Public.doQuery();
				});
			} else {
				Public.error("请选择要解锁的用户!");
			}
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
        $('#dataGrid').on('click','.a-edit',function(e){
        	var url = "${ctx}/system/user/form?id="+$(this).attr('data-id');
        	Public.openOnTab('user-add', '编辑用户', url);
		});
	}
};
$(function(){
	THISPAGE._init();
}); 
</script>
</body>
</html>