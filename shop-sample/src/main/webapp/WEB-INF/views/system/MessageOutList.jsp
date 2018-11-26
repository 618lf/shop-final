<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>站内信-发件箱</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
.ui-jqgrid tr.jqgrow td {
	white-space: normal !important;
}
#dataGrid .trash {
	display: inline-block;
	zoom: 1;
	margin: 0 6px;
	overflow: hidden;
	cursor: pointer;
	vertical-align: middle;
	font-size: 18px;
	color: #777;
}
</style>
</head>
<body>
<div class="wrapper message-wrap">
    <div class="wrapper-inner">
       <ul class="nav-tabs">
          <li><a href="${ctx}/system/message/inBox"><i class="fa fa-inbox blue"></i>收件箱</a></li>
          <li class="active"><a href="${ctx}/system/message/outBox"><i class="fa fa-location-arrow orange"></i>发件箱</a></li>
          <li><a href="${ctx}/system/message/draftBox"><i class="fa fa-pencil green"></i>草稿箱</a></li>
          <li><a href="${ctx}/system/message/trashBox"><i class="fa fa-trash"></i>垃圾箱</a></li>
        </ul>
		<div class="top">
		    <form name="queryForm" id="queryForm">
				<div class="fl">
				  <a class="btn btn-danger box" id="addBox">写信</a>
				  <a class="btn btn-default" id="trashBtn">删除</a>
				  <div class="ui-btn-menu">
				      <span class="ui-btn ui-menu-btn ui-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu left" style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">标题:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="title"/>
								</div>
						   </div>
					       <div class="ui-btns"> 
				              <input class="btn btn-primary query" type="button" value="查询"/>
				              <input class="btn reset" type="button" value="重置"/>
				           </div> 
				      </div>
				  </div>
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
<script src="${ctxModules}/system/message.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.loadGrid();
		this.addEvent();
	},
	loadGrid : function(){
		var init = Public.setGrid();
		var optionsFmt = function (cellvalue, options, rowObject) {
			var text =  "<i class='iconfont icon-edit edit' data-id='"+rowObject.id+"' data-msg='"+rowObject.title+"' title='编辑'></i>";
		        text += "<i class='iconfont icon-remove trash' data-id='"+rowObject.id+"' title='删除'></i>";
		    return text;
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/message/jSonOutList?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
				rownumbers: !0,//序号
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['ID', '标题', '内容', '收件人', '发件时间', ''],
				colModel: [
                    {name:'id', index:'id', width:80,sortable:false,hidden:true},
	                {name:'title', index:'title', width:150, sortable:false},
	                {name:'content', index:'content', width:350,sortable:false},
	                {name:'receiverUserName', index:'receiverUserName', width:120,sortable:false},
	                {name:'sendTime', index:'sendTime', width:150,sortable:false},
					{name:'options', index:'options',align:'center',width:80,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
		Message.initEvent('${ctx}');
		//拦截
		$(document).on('click','.search-item .sepV_a', function(e){
			e.preventDefault();
			var url = $(this).attr('href');
			Public.openOnTab('qa-file-view', '答疑详情', url);
        });
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>