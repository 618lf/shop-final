$(function(){
	// 加载分类
	$('[data-href]').each(function(index, item) {
		var that = $(item);
		var url = that.data('href');
	        url = ((url.indexOf("?")>-1)?(url+"&_"):(url+"?_")) +"t=" + Math.random();
	        that.load(url, function() {
	        	$('#category-one').html($('#ones').html());
	        	var _level1 = category.level == 1? category.id: category.parentId;
	        	var _level2 = category.level == 1? 'all': category.id;
	        	var _two = $('.p-' + _level1).html();
	        	$('#category-two').html(_two);
	        	
	        	// 设置选中
	        	$('#category-one>.category-' + _level1).addClass('active');
	        	$('#category-two>.category-' + _level2).addClass('active');
	        });
	});
	
	// 创建滚动
	var scrollLoad = false;
	var initiScroll = function() {
		!scrollLoad && (function() {
			// 设置大小
			var _height1 = $('.category-one-iScroll').height();
			var _height2 = $('.category-two-iScroll').height();
			if (_height2 < _height1) {
				$('.category-two-iScroll').css({height: _height1});
			}
			return true;
		})() && $('.iScroll').each(function(index, iScroll) {
			Public.newScroll(iScroll);
			scrollLoad = true;
		});
	};
	
	// 重置
	var reset = function() {
		$('.dropdown').removeClass('cur');
		$('.filter-item-name').removeClass('cur');
		$('.category-filter').removeClass('open');
		$('.weui_mask').hide()
	};
	
	// 选择条件加载
	$('[data-dropdown]').on('click', function() {
		var dropdown = $(this).data('dropdown'); var that = $(this);
		var _target = $('.' + dropdown);
		_target.hasClass('cur')?(_target.removeClass('cur'),$('.weui_mask').hide()):($('.dropdown').removeClass('cur'), _target.addClass('cur'), $('.weui_mask').show());
	    
		// 分类选择
		_target.hasClass('category-dropdown') && _target.hasClass('cur') && initiScroll();
		
		// 下拉菜单是否open
		_target.hasClass('cur') ? $('.category-filter').addClass('open'):$('.category-filter').removeClass('open');
		
		// 当前打开的菜单
		_target.hasClass('cur') ? ($('.filter-item-name').removeClass('cur'), that.addClass('cur')): $('.filter-item-name').removeClass('cur');
	});
	
	// mask
	$(document).on('click', '.weui_mask', function() {
		reset();
	});
	
	// sort
	$(document).on('click', '.sort-dropdown .item', function() {
		reset();
		var sort = $(this).data('sort');
		var name = $(this).html();
		$('.filter-item-name[data-dropdown="sort-dropdown"]').find('span').html(name);
		$('.sort-dropdown .item').removeClass('active');
		$(this).addClass('active')
		
		var sortName = $(this).data("sort");
		$("#order").val(sortName == 'def'?'':sortName);
		
		// 查询数据
		Public.resetScrollLoad();
	});
	
	// 加载购物车数量
	User.cartQuantity();
	
	// 分页数据
	Public.initScrollLoad(webRoot + '/f/shop/search/goods/category', $('#goodsTemplate').text(), function() {});
    
	// 统计分类访问次数
    Statistics.pageStatistics('category_' + category.id);
});