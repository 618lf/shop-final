<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>图片回复</title>
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
				           <div class="control-group formSep">
							  <label class="control-label">关键词:</label>
							  <div class="controls">
								<input type="text" name="keyword" maxlength="255">							    
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
		var keywordsFmt = function (cellvalue, options, rowObject) {
			var keys = [];
			$.each(cellvalue.split(','), function(index, item) {
				keys.push('<a class="keyword">' + item + '</a>');
			});
			return keys.join('');
		};
		$('#grid').jqGrid(
			Public.defaultGrid({
				url: '${ctx}/wechat/meta/image/page?timeid='+ Math.random(),
				height:init.h,
				shrinkToFit:!1, 
                rownumbers: !0,//序号
				multiselect:true,//定义是否可以多选
				multiboxonly: false,
				colNames: ['ID', '关键词', '图片', '创建时间', '修改时间', ''],
				colModel: [
                    {name:'id', index:'id', width:80,sortable:false,hidden:true},
                    {name:'keyword', index:'keyword', width:200, align:'left', sortable:false,formatter:keywordsFmt},
                    {name:'content', index:'content', width:250, align:'left', sortable:false},
                    {name:'createDate', index:'createDate', width:120, align:'left', sortable:false},
                    {name:'updateDate', index:'updateDate', width:120, align:'left', sortable:false},
					{name:'options', index:'options',align:'center',width:80,sortable:false,formatter:optionsFmt}
				]
			})		
		);
		$('#grid').jqGrid('setFrozenColumns');
	},
	addEvent : function(){
		Public.initBtnMenu();
		var delMetaImage = function(checkeds){
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
				Public.deletex("确定删除选中的图片？","${ctx}/wechat/meta/image/delete", param, function(data){
					if(!!data.success) {
						Public.success('删除图片成功');
						Public.doQuery();
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error("请选择要删除的图片!");
			}
		};
		$('#dataGrid').on('click','.edit',function(e){
			var url = "${ctx}/wechat/meta/image/form?id="+$(this).data('id');
		    Public.openOnTab('metaImage-edit', '编辑图片', url);
		});
		$('#dataGrid').on('click','.delete',function(e){
			delMetaImage($(this).data('id'));
		});
		$(document).on('click','#addBtn',function(e){
			var url = "${ctx}/wechat/meta/image/form";
			Public.openOnTab('metaImage-add', '添加图片', url);
		});
		$(document).on('click','#delBtn',function(e){
			var checkeds = $('#grid').getGridParam('selarrrow');
			delMetaImage(checkeds);
		});
	}};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>