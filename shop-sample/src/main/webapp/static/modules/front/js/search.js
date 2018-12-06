// 历史数据 base64存储
var History = {
	name : '_ks',
	template  : '{{ for(var i= 0; i< items.length; i++) { var item = items[i]; }}<a class="keyword">{{=item}}</a>{{ } }}',
	init : function() {
		var that = this;
		var his = Public.getCookie(that.name);
		if (!!his) {
			var _his = his.split(',');
			var html = Public.runTemplate(that.template, {items: _his});
			$('.search-keywords').show().html(html);
		}
	},
	
	put: function(value) {
		value = !!value ? value.replace(",",""):value;
		if (!!value) {
			var that = this; var hiss = '';
			var his = Public.getCookie(that.name);
			if (!!his) {
				var _his = his.split(','); 
				if (_his.length >= 9) {
					_his[8] = value;
				} else {
					_his.push(value);
				}
				hiss = _his.join(',');
			} else {
				hiss = value;
			}
			Public.setCookie(that.name, hiss, document.location.pathname, 7);
		}
	},
	
	clear: function() {
		var that = this;
		Public.removeCookie(that.name, document.location.pathname);
		$('.search-keywords').hide().html('');
	}
};

// 初始化
$(function(){
	
	// 合并 加载购物车数量 - 我的订单数量
	User.mutilAbout();
	
	// 只初始化一次
	var loadonce = false;
	
	// 加载数据
	var load = function() {
		if (loadonce) {
			Public.resetScrollLoad();
		} else {
			loadonce = true;
			$('#goods-tab').show();
			$('#history-tab').hide();
			// 分页数据
			Public.initScrollLoad(ctxFront + '/shop/search/goods/data.json', $('#goodsTemplate').text(), function(iscroll, data) {
				var recordCount = data.param.recordCount;
				$('.record-count').html('查询结果『共找到'+recordCount+'条记录』');
			});
		}
	};
	
	//查询
	$(document).on('click', '.search-btn', function() {
		var text = $('#_keyword').val();
		var _text = $('#keywork').val();
		if (!!text && text != _text) {
			$('#keywork').val(text);
			History.put(text);
			load();
		}
	});
	
	// 清除查询记录
	$(document).on('click', '#clear-keywords', function() {
		History.clear();
	})
	
	// 点击历史关键字
	$(document).on('click', '.search-keywords .keyword', function() {
		var text = $(this).text();
		if (!!text) {
			$('#_keyword').val(text);
			$('#keywork').val(text);
			load();
		}
	});
	
    // 加入购物车
    $(document).on('click', '.add-cart', function(e) {
    	e.stopPropagation();
		e.preventDefault();
		var id = $(this).data('id'); var b = $(this).closest('.ops').find('b');
		User.addGoodsCart(id, function() {
			// 加载购物车的数量
			User.cartQuantity();
			$(b).show().fadeOut(1000);
		});
    });
	
	// 查询历史加载
	History.init();
	
	// 统计分类访问次数
    Statistics.pageStatistics('search_goods');
});