<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>用户选择</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
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
<div class="wrapper-dialog">
	<div class="top">
	    <form name="queryForm" id="queryForm" class="form-horizontal form-inline form-fluid">
			<div class="control-group">
				<div class="controls">
				  <input type="text" class="input-txt" name="name" placeholder="用户名"/>
				</div>
		    </div>
		    <div class="control-group">
				<div class="controls">
					<input type="text" class="input-txt" name="no" placeholder="会员号"/>
				</div>
		    </div>
		    <div class="form-actions">
		      <input class="btn btn-primary query" type="button" value="查询"/>
		      <input class="btn reset" type="button" value="重置"/>
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
var THISPAGE = {
	_init : function(){
		this.loadGrid();
		this.addEvent();
	},
	loadGrid : function(){
		var init = Public.setGrid();
		var imgFmt = function (cellvalue) {
			return '<div class="image-wrap"><img src="'+cellvalue+'" onerror="THISPAGE.notFound();"></div>';
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/tag/user/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:false,
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['Id', '头像', '用户名称', '会员号', '省', '市'],
				colModel: [
	                {name:'id', index:'id', width:150,sortable:false,hidden:true},
	                {name:'headimg', index:'headimg',width:65,sortable:false,align:'center', formatter:imgFmt},
					{name:'name', index:'name', width:120,sortable:false,align:'center'},
					{name:'no', index:'no',width:80,sortable:false,align:'center'},
					{name:'province', index:'province',align:'center',width:100,sortable:false},
					{name:'city', index:'city',align:'center',width:100,sortable:false}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
	},
	notFound : function() {
		var img = event.srcElement;
    	$(img).attr('src', '${ctxStatic}/img/default_user.jpg');
	}
};
$(function(){
	setTimeout(function(){
		THISPAGE._init();
	},100);
});
</script>
</body>
</html>