<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/common/common-file.css" rel="stylesheet" />
<link href="${ctxModules}/system/area.css" rel="stylesheet"/>
</head>
<body>
<div class="wrapper">
    <div class="wrapper-inner">
		<div class="top">
		    <form name="queryForm" id="queryForm">
				<div class="fl">
				   <input type="button" id="syncBtn" class="btn btn-success" value="同步">
				   <input type="button" id="pickerBtn" class="btn btn-success" value="刷新控件数据">
				</div>
			</form>
		</div>
	</div>
	<div id="dataGrid" class="area-wrap">
		<div class="area area-province">
		   <div class="area-border">
		   <div class="search">
		     <input type="text" name="name" placeholder="输入名称查找"><a class="query"><i class="iconfont icon-search"></i></a>
		   </div>
		   <div class="area-ops"><a class="add add-province">添加省份</a></div>
		   <div class="area-inner"></div>
		   </div>
		</div>
		<div class="area area-city">
		   <div class="area-border">
		   <div class="search">
		     <input type="text" name="name" placeholder="输入名称查找"><a class="query"><i class="iconfont icon-search"></i></a>
		   </div>
		   <div class="area-ops"><a class="add add-city">添加城市</a></div>
		   <div class="area-inner"></div>
		   </div>
		</div>
		<div class="area area-county">
		   <div class="area-border">
		   <div class="search">
		     <input type="text" name="name" placeholder="输入名称查找"><a class="query"><i class="iconfont icon-search"></i></a>
		   </div>
		   <div class="area-ops"><a class="add add-county">添加区县</a></div>
		   <div class="area-inner"></div>
		   </div>
		</div>
		<div class="area area-street">
		   <div class="area-border">
		   <div class="search">
		     <input type="text" name="name" placeholder="输入名称查找"><a class="query"><i class="iconfont icon-search"></i></a>
		   </div>
		   <div class="area-ops"><a class="add add-street">添加街道</a></div>
		   <div class="area-inner"></div>
		   </div>
		</div>
	</div> 
</div>
<%@ include file="/WEB-INF/views/include/list-footer.jsp"%>
<script src="${ctxStatic}/common/common-file.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.addEvent();
		this.loadProvince();
	},
	addEvent : function(){
		var that = this;
		var h = $(window).height() - $("#dataGrid").offset().top - 10;
		$("#dataGrid").height(h)
		$('.area-inner').each(function() {
			var p = $(this).parent().parent();
			$(this).height(p.height() - 90)
		});
		$(document).on('click', '.area-province .item', function() {
			var id = $(this).data('id');
			that.loadCity(id);
			$('.area-province .cur').removeClass('cur');
			$(this).addClass('cur');
		});
		$(document).on('click', '.area-city .item', function() {
			var id = $(this).data('id');
			that.loadCounty(id);
			$('.area-city .cur').removeClass('cur');
			$(this).addClass('cur');
		});
		$(document).on('click', '.area-county .item', function() {
			var id = $(this).data('id');
			that.loadStreet(id);
			$('.area-county .cur').removeClass('cur');
			$(this).addClass('cur');
		});
		$(document).on('click', '.area-street .item', function() {
			$('.area-street .cur').removeClass('cur');
			$(this).addClass('cur');
		});
        $(document).on('click','#pickerBtn',function(){
        	Public.doExport('${ctx}/system/area/extend/picker/data');
        });
        
        // 添加
        $(document).on('click','.add-province',function() {
        	var url = '${ctx}/system/area/form?parentId=0';
        	Public.openOnTab('add-area', '添加省份', url);
        });
        $(document).on('click','.add-city',function() {
        	var pId = $('.area-province .cur').data('id');
        	if (!pId) {
        		Public.toast('请先添加省');
        		return;
        	}
        	var url = '${ctx}/system/area/form?parentId=' + pId;
        	Public.openOnTab('add-area', '添加城市', url);
        });
        $(document).on('click','.add-county',function() {
        	var pId = $('.area-city .cur').data('id');
        	if (!pId) {
        		Public.toast('请先添加城市');
        		return;
        	}
        	var url = '${ctx}/system/area/form?parentId=' + pId;
        	Public.openOnTab('add-area', '添加区县', url);
        });
        $(document).on('click','.add-street',function() {
        	var pId = $('.area-county .cur').data('id');
            if (!pId) {
        		Public.toast('请先添加区县');
        		return;
        	}
            var url = '${ctx}/system/area/form?parentId=' + pId;
        	Public.openOnTab('add-area', '添加街道', url);
        });
        // 编辑
        $(document).on('click','.edit',function(e){
        	var id = $(this).closest('.item').data('id');
        	var url = "${ctx}/system/area/form?id=" + id;
        	Public.openOnTab('edit-area', '编辑区域', url);
		});
        // 删除
        $(document).on('click','.delete',function(e){
        	var id = $(this).closest('.item').data('id'); var that = $(this).closest('.item'); var p = that.parent();
        	var param = []; param.push({name:'idList', value:id});
        	Public.deletex("确定删除选中的区域？", "${ctx}/system/area/delete", param, function(data){
				if (!!data && data.success ){
					$(that).remove();
					Public.toast("删除成功");
					$(p).children().eq(0).click();
				} else {
					Public.toast(data.msg);
				}
			});
		});
        // 同步
        $(document).on('click', '#syncBtn', function() {
        	var url = "${ctx}/system/area/sync";
        	Public.confirmx('确定同步数据?', function(){
        		Public.loading('正在提交，请稍等...');
        		
        		// 定时刷新数据
        		var timer = Public.setInterval(function() {
        			that.loadProvince();
            	}, 5000);
        		
        		// 同步数据
        		Public.postAjax(url, {}, function(data){
            		if (typeof(data) == "string") {
                  		 data = $.parseJSON(data);
                  	}
            		Public.close();
            		if (data.success) {
            			Public.toast('同步成功！');
            			
            			// 结束同步
            			clearTimeout(timer);
            		} else {
            			Public.toast(data.msg);
            		}
                });
        	});
        });
        
        // 查询
        $(document).on('click', '.query', function() {
        	var area = $(this).closest('.area');
        	if ($(area).hasClass('area-province')) {
        		that.loadProvince();
        	} else if($(area).hasClass('area-city')) {
        		var pid = $('.area-province .cur').data('id');
        		!!pid && that.loadCity(pid);
        	} else if($(area).hasClass('area-county')) {
        		var pid = $('.area-city .cur').data('id');
        		!!pid && that.loadCounty(pid);
        	} else if($(area).hasClass('area-street')) {
        		var pid = $('.area-county .cur').data('id');
        		!!pid && that.loadStreet(pid);
        	}
        });
	},
	loadProvince : function() {
		var that = this;
		var url = '${ctx}/system/tag/area/2';
		var name = $('.area-province input[name="name"]').val();
		Public.postAjax(url, {name : name}, function(data) {
			var html = Public.runTemplate($('#areaTemplate').html(), {datas:data.obj});
			$('.area-province .area-inner').html(html);
			
			// 加载第一个省的市
			var first = $('.area-province .area-inner >.item').eq(0);
			!!first.get(0) && first.addClass('cur') && that.loadCity(first.data('id'));
		});
	},
	loadCity : function(pId) {
		var that = this;
		var url = '${ctx}/system/tag/area/3';
		var name = $('.area-city input[name="name"]').val();
		Public.postAjax(url, {name : name, parentId: pId}, function(data) {
			var html = Public.runTemplate($('#areaTemplate').html(), {datas:data.obj});
			$('.area-city .area-inner').html(html);
			
			// 加载第一个市的县
			var first = $('.area-city .area-inner >.item').eq(0);
			!!first.get(0) && first.addClass('cur') && that.loadCounty(first.data('id'));
		});
	},
	loadCounty : function(pId) {
		var that = this;
		var url = '${ctx}/system/tag/area/4';
		var name = $('.area-county input[name="name"]').val();
		Public.postAjax(url, {name : name, parentId: pId}, function(data) {
			var html = Public.runTemplate($('#areaTemplate').html(), {datas:data.obj});
			$('.area-county .area-inner').html(html);
			
			var first = $('.area-county .area-inner >.item').eq(0);
			!!first.get(0) && first.addClass('cur') && that.loadStreet(first.data('id'));
		});
	},
	loadStreet : function(pId) {
		var that = this;
		var url = '${ctx}/system/tag/area/5';
		var name = $('.area-street input[name="name"]').val();
		Public.postAjax(url, {name : name, parentId: pId}, function(data) {
			var html = Public.runTemplate($('#areaTemplate').html(), {datas:data.obj});
			$('.area-street .area-inner').html(html);
			
			var first = $('.area-street .area-inner >.item').eq(0);
			!!first.get(0) && first.addClass('cur')
		});
	},
};
$(function(){
	THISPAGE._init();
});
</script>
<script type="text/html" id="areaTemplate">
{{ for(var i = 0; i< datas.length; i++) {var data = datas[i]; }}
<div class="item" data-id="{{=data.id}}">{{=data.name}}<div class="ops"><a class="edit"><i class="iconfont icon-edit"></i></a><a class="delete"><i class="iconfont icon-remove"></i></a></div></div>
{{ } }}
</script>
</body>
</html>