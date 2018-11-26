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
				      <span class="ui-btn ui-menu-btn" style='vertical-align: middle;'>
				         <strong>点击查询</strong><b></b>
				      </span>
				      <div class="dropdown-menu" style="width: 320px;">
				           <div class="control-group formSep">
								<label class="control-label">操作用户:</label>
								<div class="controls">
									<input type="text" name="createName" id='createName'/>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">日志类型:</label>
								<div class="controls">
									<select id='type' name='type' class="iSelect">
									    <option value=''></option>
									    <option value='1'>访问</option>
									    <option value='2'>异常</option>
									</select>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">操作用户:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="remoteAddr"/>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">操作URL:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="requestUri"/>
								</div>
						   </div>
						   <div class="control-group formSep">
								<label class="control-label">用户代理:</label>
								<div class="controls">
									<input type="text" class="input-txt" name="userAgent"/>
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
				   <input type="button" class="btn btn-primary"  onclick="optionsFmt4D()" value="&nbsp;导&nbsp;出&nbsp;">
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
		var typesFmt = function(cellvalue, options, rowObject) {
			if(cellvalue==1) { return "访问";}
			return "异常";
		};
		var optionsFmt = function (cellvalue, options, rowObject) {
			return "<i class='iconfont icon-list-alt detail' data-id='"+rowObject.id+"' title='明细'></i>";
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/system/log/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:false, 
				rownumbers: !0,//序号
				multiselect:false,//定义是否可以多选
				multiboxonly: false,
				colNames: ['', '日志类型', '操作用户', '操作时间', '用户的IP地址', '操作的URI', '操作方式', '处理时长'],
				colModel: [
					{name:'options', index:'options',align:'center',width:40,sortable:false,formatter:optionsFmt,frozen:true},
	                {name:'type', index:'type', align:'center',width:80,sortable:false,formatter:typesFmt,frozen:true},
	                {name:'createName', index:'createName', align:'center',width:100,sortable:false,frozen:true},
	                {name:'createDate', index:'createDate', align:'center',width:120,sortable:false,frozen:true},
					{name:'remoteAddr', index:'remoteAddr', align:'center',width:100,sortable:false,frozen:true},
					{name:'requestUri', index:'requestUri',width:200,sortable:false},
					{name:'method', index:'method',align:'center',width:80,sortable:false},
					{name:'dealTime', index:'dealTime',align:'center',width:80,sortable:false}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		var that = this;
		Public.initBtnMenu();
		$('#dataGrid').on('click','.detail',function(e){
			Public.openWindow('日志明细', 'iframe:${ctx}/system/log/form?id='+$(this).attr('data-id'), 700,450,function(){});
		});
		Public.autoCombo('#createName','${ctx}/system/tag/user/page');
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>