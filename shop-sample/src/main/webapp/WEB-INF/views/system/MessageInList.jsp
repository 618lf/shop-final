<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>站内信-收件箱</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/message.css" rel="stylesheet"/>
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
          <li class="active"><a href="${ctx}/system/message/inBox"><i class="fa fa-inbox blue"></i>收件箱</a></li>
          <li><a href="${ctx}/system/message/outBox"><i class="fa fa-location-arrow orange"></i>发件箱</a></li>
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
			var text =  "<i class='iconfont icon-edit edit' data-id='"+rowObject.id+"'  data-msg='"+rowObject.title+"' title='编辑'></i>";
		        text += "<i class='iconfont icon-remove trash' data-id='"+rowObject.id+"' title='删除'></i>";
		    return text;
		};
		var statusFmt = function (cellvalue, options, rowObject) {
			if(cellvalue == '0') {
				return '<i class="iconfont icon-message unread" title="未读"></i>';
			}
			return '<i class="iconfont icon-message readed" title="已读"></i>';
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/message/jSonInList?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
				rownumbers: !0,//序号
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['ID', ' ', '标题', '内容', '发件人', '发件时间', ''],
				colModel: [
                    {name:'id', index:'id', width:80,sortable:false,hidden:true},
                    {name:'status', index:'status', align:'center', width:40,sortable:false,formatter:statusFmt},
	                {name:'title', index:'title', width:150, sortable:false},
	                {name:'content', index:'content', width:350,sortable:false},
	                {name:'sendUserName', index:'sendUserName', width:120,sortable:false},
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
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>