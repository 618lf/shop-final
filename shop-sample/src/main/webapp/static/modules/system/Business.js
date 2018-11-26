/**
 * 系统提供的链接地址
 */
var Business = {
	
    // 选择微页面
	_featureLink : function(callback) {
		var href = webRoot + '/admin/cms/mpage/table_select'
		Public.tableSelect(href, '选择微页面', 750, 500, function(iframe, ids, names) {
			
			var id = !!ids && ids.length >0 ? ids[0] : null;
			
			// 获取数据
			var mpage = {};
			var url = webRoot + '/admin/cms/mpage/get/' + id;
			Public.getAjax(url, {}, function(data) {
				mpage = data;
			}, false);
			
			// 封装数据 
			var data = {
				link_title: mpage.name,
				link_url : mpage.url
			}
			// 回调
			callback(data);
		});
	},
	
	// 商品搜索
	_searchLink : function(callback) {
		var data = {
			link_title: '商品搜索',
			link_url : base + '/f/shop/search/goods.html'
		}
		// 回调
		callback(data);
	},
	
	// 商品分类
	_categoryLink : function(callback) {
		var data = {
			link_title: '商品分类',
			link_url : base + '/f/shop/search/goods.html'
		}
		// 回调
		callback(data);
	},
	
	// 商品
	_goodsLink : function(callback) {
		Shop.selectGoods(function(goods) {
			var data = {
				link_title: goods.name,
				link_url : goods.url
			}
			// 回调
			callback(data);
		});
	},
	
	// 营销活动
	_activityLink : function(callback) {
		var url = webRoot + '/admin/shop/promotion/table_select';
		Public.tableSelect(url, '选择营销活动', 750, 420, function(iframe, ids, names) {
			
			var id = !!ids && ids.length >0 ? ids[0] : null;
			
			// 实例化
			var activity = {};
			var url = webRoot + '/admin/shop/promotion/get/' + id;
			Public.getAjax(url, {}, function(data) {
				activity = data;
			}, false);
			
			var data = {
				link_title: activity.name, 
				link_url : activity.url
			}
			// 回调
			callback(data);
		});
	},
	
	// 首页导航
	_indexLink : function(callback) {
		var data = {
			link_title: '首页',
			link_url : base + '/f/member/to_index.html'
		}
		// 回调
		callback(data);
	},
	
	// 会员中心
	_usercenterLink : function(callback) {
		var data = {
			link_title: '会员中心',
			link_url : base + '/f/member/index.html'
		}
		// 回调
		callback(data);
	},
	
	// link
	_customLink : function(callback) {
		
		// 模板
		var template = 
		'<div class="row-fluid">' +
		'<form id="custom_inputFrom" name="custom_inputFrom" class="form-horizontal form-min" method="post">' +
		'	 <div class="control-group formSep">' +
		'		<label class="control-label">链接名称:</label>' +
		'		<div class="controls">' +
		'			<input type="text" name="link_title" id="link_title" class="required"/>' +
		'		</div>' +
		'	 </div>' +
		'	 <div class="control-group formSep">' +
		'		<label class="control-label">链接地址:</label>' +
		'		<div class="controls">' +
		'			<input type="text" name="link_url" id="link_url" class="required"/>' +
		'		</div>' +
		'	 </div>' +
		'</form>' +
		'</div> ';
		
		Public.openWindow('自定义链接', template, 450, 280, function(iframe) {
			var name = $('#link_title').val();
			var url = $('#link_url').val();
			
			var data = {
				link_title: name,
				link_url : url	
			};
			
			// 回调
			callback(data);
		});
	},
    
	// 选择一个链接地址
	selectLink : function(type, callback) {
		if (type == 'feature') {
			this._featureLink(callback);
		} else if (type == 'search') {
			this._searchLink(callback);
		} else if (type == 'category') {
			this._categoryLink(callback);
		} else if (type == 'goods') {
			this._goodsLink(callback);
		} else if (type == 'activity') {
			this._activityLink(callback);
		} else if (type == 'index') {
			this._indexLink(callback);
		} else if (type == 'usercenter') {
			this._usercenterLink(callback);
		} else if (type == 'usercenter') {
			this._usercenterLink(callback);
		} else if (type == 'link') {
			this._customLink(callback);
		} else {
			Public.toast('暂不支持');
		}
	},
};