<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>自定义菜单</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/wechat.css" rel="stylesheet"/>
</head>
<body class="no-fixed-btns">
<div class="wechat-menu">
  <div class="tabs-left menu-left">
  <ul class="nav nav-tabs apps">
    <c:forEach items="${apps}" var="app" varStatus="i">
    <li <c:if test="${i.index == 0}">class="active"</c:if>><a data-app="${app.id}">${app.name}</a></li>
    </c:forEach>
  </ul>
  </div>
  <div class="menu-main">
     <div class="btns">
        <input id="saveBtn" class="btn btn-primary" type="button" value="保 存"/>
        <span class="hr-line"></span>
        <input id="publishBtn" class="btn btn-warning" type="button" value="保存并发布"/>
        <input id="viewBtn" class="btn btn-warning" type="button" value="预 览"/>
     </div>
     <div class="note note-info">
		<p>最多可以同时显示3个主菜单，每个主菜单下5个子菜单；仅当您点击了【保存并发布】后，菜单才会生效（已关注的粉丝需要稍等片刻才能生效）</p>
	 </div>
	 <div class="menu-tabs">
	    <div class="menu-tab left-tab">
	       <h4>菜单管理<a class="addRoot">添加主菜单</a></h4>
	       <div class="list-group" id="appMenus"></div>
	    </div>
	    <div class="menu-tab right-tab">
	       <h4>菜单功能设置</h4>
	       <div id="menuConfig" class="menuConfig">
	          <ul class="nav nav-tabs">
	             <li class="active" data-type="5"><a data-tab="config-url">跳转网页</a></li>
	             <li data-type="6"><a data-tab="config-site">业务模块</a></li>
	             <li data-type="7"><a data-tab="config-keyword">匹配关键字</a></li>
	             <li data-type="1"><a data-tab="config-text">文本回复</a></li>
	             <li data-type="2"><a data-tab="config-rich">图文回复</a></li>
	             <li data-type="4"><a data-tab="config-pic">图片回复</a></li>
	          </ul>
	          <div class="tab-content">
	             <div class="tab-pane active" id="config-url">
				    <input type="text" id="config-url-input" name="config-url-input" placeholder="请填写跳转的地址，外部地址请添加http或https">
				 </div>
				 <div class="tab-pane" id="config-site">
				    <select name="config-site-select" id="config-site-select">
				      <option value=""></option>
				    </select>
				 </div>
	             <div class="tab-pane" id="config-keyword">
	                <div class="keyword-wrap">
	                   <input type="text" class="keyword-input -config" placeholder="请输入关键词">
	                   <div class="keyword-dropdown"></div>
	                </div>
	             </div>
				 <div class="tab-pane" id="config-text">
					 <div class="config-content iInputLimit-wrap">
				       <textarea class="config-content-textarea -config iInputLimit" placeholder="填写文本回复" maxlength="600" style="overflow: hidden; word-wrap: break-word; height: 120px;"></textarea>
				       <div class="config-content-bar">
				          <a class="-qq-emo -tools" title="插入表情"><i class="iconfont icon-biaoqing"></i></a>
				          <a class="-set-link -tools" title="插入链接"><i class="iconfont icon-lianjie"></i></a>
				          <div class="pull-right word-num">还能输入<span class="-num">600</span> / <span class="-total">600</span>字</div>
				       </div>
				     </div>
				 </div>
				 <div class="tab-pane" id="config-rich">
				    <div class="multi-meta">
				      <div class="metas"></div>
				      <a class="add-ops meta-select"><i class="iconfont icon-plus"></i>添加多图文</a>
				    </div>
				 </div>
				 <div class="tab-pane" id="config-pic">
					<div class="config-pic">
				       <input type="hidden" class="-config">
				       <div class="image-wrap"><img alt=""></div>
				       <div class="ops"><a class="btn config-pic-select">选择图片</a><a class="btn btn-danger config-pic-upload">上传</a><span style="margin-left: 10px;">永久素材</span></div>
				    </div>
				 </div>
	          </div>
	       </div>
	    </div>
	 </div>
  </div>
</div>
<div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxModules}/system/wechat.js" type="text/javascript"></script>
<script src="${ctxModules}/system/Business.js" type="text/javascript"></script>
<script type="text/javascript">
var selectId = null;
var THISPAGE = {
	_init : function(){
		this.loadPages();
		this.addEvent();
		this.loadMenus();
	},
	loadPages : function() {
		var _pages = Pages.getPages();
		var html = [];
		for(var i in _pages) {
			html.push('<option value="'+_pages[i].value+'">'+_pages[i].label+'</option>');
		}
		$('#config-site-select').append(html.join(''));
		Public.combo($('#config-site-select').get(0));
	},
	insertMenus: function(target, menus) {
		var html = Public.runTemplate($('#menuTemplate').html(), {datas: menus})
		if (target == null) {
		   $('#appMenus').append(html);
		} else {
		   $(target).after(html);
		}
	},
	setMenus : function(menus) {
		var html = Public.runTemplate($('#menuTemplate').html(), {datas: menus})
		$('#appMenus').html(html);
	},
	getMenus : function() {
		var flag = true;
		var curr = null; var menus = []; var menu = null; var errors = [];
		$('#appMenus > a').each(function() {
			curr = $(this); var config = typeof(curr.data('config')) === 'object'? JSON.stringify(curr.data('config')) : curr.data('config');
			if (curr.hasClass('-menu')) {
				menu = {
				   id: curr.data('id'),
				   name : curr.find('input').val(),
				   parentId: '0',
				   type: curr.data('type'),
				   config: config,
				   menus : []
				}
				menus.push(menu);
				
				if (!menu.name || !menu.type) {
					errors.push('菜单“' + menu.name + '“设置不完整（名称不能为空且需要设置功能）');
					flag = false;
				}
			} else if(curr.hasClass('-submenu') && menu != null) {
				var _menu = {
				   id: curr.data('id'),
				   name : curr.find('input').val(),
				   parentId: '-1',
				   type: curr.data('type'),
				   config: config
				}
				menu.menus.push(_menu);
				
				if (!_menu.name || !_menu.type) {
					errors.push('菜单“' + _menu.name + '“设置不完整（名称不能为空且需要设置功能）');
					flag = false;
				}
			}
		});
		
		// check
		if (!flag) {
			Public.toast(errors.join(';'));
			return false;
		}
		// 返回JSON格式的数据
		return JSON.stringify(menus);
	},
	loadMenus : function(){
		var that = this;
		var appId = $('.apps > .active > a').data('app');
		if (!!appId) {
			Public.loading('加载菜单项');
			Public.postAjax('${ctx}/wechat/menu/page', {appId: appId}, function(data) {
				Public.close();
				that.setMenus(data.obj);
				
				// 显示第一个的配置数据
				$('#appMenus .list-group-item:first').click();
			});
		} else {
			Public.error('请先创建公众号！');
		}
	},
	addEvent : function(){
		var that = this;
		// 添加菜单项
		$(document).on('click', '.add', function(e) {
			var menu = $(this).closest('a');
			// 菜单项目
			var menus = [{
				id : '-' + Public.random(),
				parentId : '-1',
				name:''
			}];
			
			// 追加
			that.insertMenus(menu, menus);
			
			// 事件不传播
			e.stopPropagation();
		});
		
		// 删除菜单项
		$(document).on('click', '.remove', function(e) {
			var menu = $(this).closest('a');
			if (menu.hasClass('-submenu')) {
				menu.remove();
			} else if(menu.hasClass('-menu')) {
				var curr = menu; var next = null; var removes = [];
				while((next = curr.next()) != null && next.length != 0) {
					if (!(next.hasClass('-submenu'))) {break;}
					removes.push(next);
					curr = next;
					next = null;
				}
				// 删除子节点
				$.each(removes, function() {
					this.remove();
				});
				// 删除自己
				menu.remove();
			}
			
			// 选中第一个(没有则默认添加一个)
			var one = $('#appMenus .list-group-item:first');
			if (one.length == 0) {
				$('.addRoot').trigger('click');
				one = $('#appMenus .list-group-item:first');
			}
			one.trigger('click');
			
			// 事件不传播
			e.stopPropagation();
		});
		
		// 添加主菜单
        $(document).on('click', '.addRoot', function() {
			
			// 菜单项目
			var menus = [{
				id :  '-' + Public.random(),
				parentId : '0',
				name:''
			}];
			
			// 追加
			that.insertMenus(null, menus);
		});
		
		// 折叠
		$(document).on('click', '.menu-ops', function(e) {
			var menu = $(this).closest('a');
			var ishide = menu.hasClass('menu-close');
			var curr = menu; var next = null; var removes = [];
			while((next = curr.next()) != null && next.length != 0) {
				if (!(next.hasClass('-submenu'))) {break;}
				removes.push(next);
				curr = next;
				next = null;
			}
			ishide ? ($(this).removeClass('icon-caret-right'), $(this).addClass('icon-caret-down'), menu.removeClass('menu-close'), $.each(removes, function() {
				this.show();
			})) : ($(this).removeClass('icon-caret-down'), $(this).addClass('icon-caret-right'), menu.addClass('menu-close'), $.each(removes, function() {
				this.hide();
			}));
		});
		
		// 顺序
		$(document).on('click', '.up', function(e) {
			var menu = $(this).closest('a');
			if (menu.hasClass('-submenu')) {
				var pre = menu.prev();
				if (pre.hasClass('-submenu')) {
					menu.remove().insertBefore(pre);
				}
			} else if(menu.hasClass('-menu') && menu.prev().lenght != 0){
				// 要移动的数据
				var curr = menu; var next = null; var moves = [];
				while((next = curr.next()) != null && next.length != 0) {
					if (!(next.hasClass('-submenu'))) {break;}
					moves.push(next);
					curr = next;
					next = null;
				}
				
				// 移动的位置
				curr = menu; var prev = null; var pPrev = null;
				while((prev = curr.prev()) != null && prev.length != 0) {
					if (prev.hasClass('-menu')) {
						pPrev = prev;
						break;
					}
					curr = prev;
					prev = null;
				}
				if(pPrev == null || pPrev.length == 0) {return;}
				
				// 移动
				menu.remove().insertBefore(pPrev);
				prev = menu;
				$.each(moves, function() {
					this.remove().insertAfter(prev);
					prev = this;
				});
			}
			
			// 事件不传播
			e.stopPropagation();
		});
		
		$(document).on('click', '.down', function(e) {
			var menu = $(this).closest('a');
			if (menu.hasClass('-submenu')) {
				var next = menu.next();
				if (next.hasClass('-submenu')) {
					menu.remove().insertAfter(next);
				}
			}else if(menu.hasClass('-menu') && menu.next().length != 0){
				// 要移动的数据
				var curr = menu; var next = null; var moves = [];
				while((next = curr.next()) != null && next.length != 0) {
					if (!(next.hasClass('-submenu'))) {break;}
					moves.push(next);
					curr = next;
					next = null;
				}
				
				// 当前位置
				var pNext = next;
				
				// 移动的位置
				curr = pNext; next = null;
				while((next = curr.next()) != null && next.length != 0) {
					if (!(next.hasClass('-submenu'))) {break;}
					curr = next;
					next = null;
				}
				pNext = curr;
				
				if(pNext == null || pNext.length == 0) {return;}
				
				// 移动
				menu.remove().insertAfter(pNext);
				prev = menu;
				$.each(moves, function() {
					this.remove().insertAfter(prev);
					prev = this;
				});
			}
			
			// 事件不传播
			e.stopPropagation();
		});
		
		// 保存
		var publish = 0;
		$(document).on('click', '#saveBtn', function() {
			var appId = $('.apps > .active > a').data('app');
			if (!!appId) {
				Public.loading('加载菜单项');
				
				// 会验证提交的数据正确性
				var postData = that.getMenus();
				if(!postData) {
					Public.close();
					return;
				}
				
				// 提交数据
				Public.postAjax('${ctx}/wechat/menu/save', {postData: postData, appId:appId, publish: publish}, function(data) {
					Public.close();
					if (data.success) {
						Public.success('保存成功！');
						publish = 0;
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error('请先创建公众号！');
			}
		});
		
		// 保存并发布
		$(document).on('click', '#publishBtn', function() {
			publish = 1;
			$('#saveBtn').click();
		});
		
		// 配置切换
		$(document).on('click', '[data-tab]', function() {
			var tab = $(this).data('tab');
			$('#menuConfig .active').removeClass('active');
			$(this).parent().addClass('active');
			$('#' + tab).addClass('active');
		});
		
		// 显示菜单的配置信息
		var showConfig = function(type, config) {
			
			// 重置
			var reset = function() {
				$('.tab-pane input').val('');
				$('.tab-pane .-config').val('');
				$('#config-site-select').select2('val', '');
				$('.config-pic img').attr('src', '');
				$('.metas').html('');
			};
			reset();
			
			// 默认是网页跳转
			type = !type?5:type;
			config = !config?'':config;
			
			// 选中
			$('#menuConfig .active').removeClass('active');
			$('#menuConfig [data-type="'+type+'"]').addClass('active');
			var tab = $('#menuConfig .active>[data-tab]').data('tab');
			$('#' + tab).addClass('active');
			if (type == 5) {
				$('.tab-pane.active > input').val(config);
			} else if(type == 6) {
				$('#config-site-select').select2('val', config);
			} else if(type == 7) {
				$('.tab-pane.active .-config').val(config);
			} else if (type == 1) { // 文本
				$('.tab-pane.active .-config').val(config);
				$('.iInputLimit').trigger('input');
			} else if (type == 2) { // 图文
				var id = config;
			    if (!!id) {
			    	var html = Meta_Rich.render(id);
	    			$('.metas').html(html);
			    } else {
			    	$('.metas').html('');
			    }
			} else if(type == 4) { // 图片
				if (!!config) {
					try{
						$('.tab-pane.active img').attr('src', config.url);
						$('.tab-pane.active .-config').val(JSON.stringify(config));
					}catch(e){}
				}
			}
		};
		
		// 点击菜单加载配置项
		$(document).on('click', '.list-group-item', function() {
			var id = $(this).data('id');
			var type = $(this).data('type');
			var config = $(this).data('config');
			if (!!id) {
				showConfig(type, config);
			}
			selectId = id;
			
			$('.list-group-item.cur').removeClass('cur');
			$(this).addClass('cur');
		});
		
		// 保存值 - url设置
		$('#config-url-input').on('blur', function() {
			if (!!selectId) {
				var url = $(this).val();
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('type', 5);
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('config', url);
			}
		});
		$('.config-content-textarea').on('blur', function() {
			if (!!selectId) {
				var content = $(this).val();
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('type', 1);
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('config', content);
			}
		});
		$('.keyword-input').on('blur', function() {
			if (!!selectId) {
				var content = $(this).val();
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('type', 7);
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('config', content);
			}
		});
		$('#config-site-select').on('change', function(e){
			if (!!selectId) {
				var url = $('#config-site-select').val();
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('type', 6);
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('config', url);
			}
		});
		
		// 图片选择框选择事件
		$('.config-pic .-config').on('input', function(e){
			if (!!selectId) {
				var url = $(this).val();
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('type', 4);
				$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('config', url);
			}
		});
    }
};
$(function() {
	THISPAGE._init();
});

/**
 * 选择图文
 */
Meta_Rich.callback = function(id) {
	if (!!selectId) {
		var url = $('#config-site-select').val();
		$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('type', 2);
		$('#appMenus .list-group-item[data-id="'+selectId+'"]').data('config', id);
	}
};
</script>
<script type="text/html" id="menuTemplate">
{{ for(var i = 0; i< datas.length; i++) { var item = datas[i]; }}
{{ if(item.parentId === '0') { }}
<a class="list-group-item -menu" data-id="{{=item.id}}" data-type="{{=item.type}}" data-config='{{=#item.config}}'>
   <i class="iconfont icon-caret-down menu-ops"></i><input type="text" value="{{=item.name}}">
   <span class="ops"><i class="iconfont icon-plus add"></i><i class="iconfont icon-shanglajiantou up"></i><i class="iconfont icon-xialajiantou down"></i><i class="iconfont icon-remove remove"></i></span>
</a>
{{ } else { }}
<a class="list-group-item -submenu" data-id="{{=item.id}}" data-type="{{=item.type}}" data-config='{{=#item.config}}'>
   <span class="tree-line"></span><input type="text" value="{{=item.name}}">
   <span class="ops"><i class="iconfont icon-shanglajiantou up"></i><i class="iconfont icon-xialajiantou down"></i><i class="iconfont icon-remove remove"></i></span>
</a>
{{ } }}
{{ } }}
</script>
</body>
</html>