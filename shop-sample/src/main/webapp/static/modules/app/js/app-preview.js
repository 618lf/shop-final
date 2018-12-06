/**
 * 组件初始化
 */
$(function(){
	
	// 所有的轮播 -- 自定义轮播
	$('.custom-swiper > .swiper-container').each(function() {
		var id = $(this).data('id');
		var _swiper = $(this); var _pagination = '#' + id +'-swiper-pagination';
		
		// 实例化组件
		var swiper = new Swiper(_swiper, {
	        pagination: _pagination,
	        slidesPerView: 'auto',
	        centeredSlides: true,
	        loop:true,
	        autoplay: 2000,
	    });
	});
	
	// 所有的商品列表 -- 不支持分页加载
	$('.custom-goods_list').each(function() {
		var template = '#' + $(this).data('template');
		var size = $(this).data('size');
		var href = ctxFront + '/shop/search/goods/category.json';
		var that = $(this);
		var param = [];
		var size = $(this).data('size'); var categoryId = $(this).data('group');
		if(size == 0) {param.push({name: 'param.pageSize', value : 6});}
		if(size == 1) {param.push({name: 'param.pageSize', value : 12});}
		if(size == 2) {param.push({name: 'param.pageSize', value : 18});}
		if(size == 3) {param.push({name: 'param.pageSize', value : 25});}
		param.push({name: 'categoryId', value : categoryId});
		Public.postAjax(href, param, function(page) {
			var goodss = page.data;
			var html = Public.runTemplate($(template).html(), {goodss: goodss});
			that.html(html);
			
			// 金额格式化
			$('[data-money]').each(function() {
				$(this).html('￥' + $._formatFloat($(this).data('money'), 2));
				$(this).removeAttr('data-money');
			});
		});
	});
	
	// 商品分组
	$('.custom-tag_list').each(function() {
		var template = '#' + $(this).data('template');
		var href = ctxFront + '/shop/search/goods/category.json';
		var that = $(this).find('.custom-group-main');
		var loadmore = $(this).find('.load-more');
		var loader = {};
		
		// 默认
		var categoryId = $(this).find('.js-tags-region a.active').data('id');
		    
		// 点击事件
		$(this).on('click', '.js-tags-region a', function() {
			categoryId = $(this).data('id');
			$(this).closest('.js-tags-region').find('a').removeClass('active');
			$(this).addClass('active');
			that.html(''); loader.pageCount = 1; loader.pageIndex = 1;
			loadGoods();
		});
		
		// 点击加载更多
		$(this).on('click', '.js-load-more', function() {
			loader.pageIndex ++;
			loadGoods();
		});
		
		// 加载商品
		var loadGoods = function() {
			if (loader.state) {
				Public.toast('正在加载数据');
				return;
			}
			loader.state = true;
			loadmore.html('<img src="'+ctxStatic+'/img/loading.gif">加载中...');
			var param = [];
			    param.push({name: 'categoryId', value: categoryId});
			    param.push({name: 'param.pageIndex', value: loader.pageIndex||1});
			Public.postAjax(href, param, function(page) {
				var goodss = page.data;
				var html = Public.runTemplate($(template).html(), {goodss: goodss});
				that.append(html);
				
				// 金额格式化
				$('[data-money]').each(function() {
					$(this).html('￥' + $._formatFloat($(this).data('money'), 2));
					$(this).removeAttr('data-money');
				});
				
				// 相关参数
				loader.pageCount = page.param.pageCount;
				loader.pageIndex = page.param.pageIndex;
				loader.state = false;
				
				if (loader.pageCount == -1 || loader.pageCount <= loader.pageIndex) {
					loadmore.html('数据加载完毕');
				} else {
					loadmore.html('<a class="js-load-more">加载更多</a>');
				}
			});
		};
		
		// 初始化加载
		loadGoods();
	});
	
	
	// 所有弹出的新人礼包（仅一个有效）
	$('.pop-xr_gift:first').each(function() {
		
		var that = $(this);
		
		// 关闭
		var _close = function() {
			$(that).hide();
		};
		
		// 去领取
		var _to = function() {
			window.location.href = ctxFront + "/member/promotion/newgift.html";
		};
		
		// 弹出 
		var _pop = function() {
			Public.postAjax(ctxFront + "/member/promotion/isGetNewGift.json",{},function(data){
				if (!data){
					$(that).on('click', function(e) {
						var target = $(e.target);
						if (target.hasClass('-anchor')) {
							_to();
						} else {
							_close();
						}
					}).show();
				}
			});
		};
		
		// 判断是否领取，并弹出提示引导用户获取
		_pop();
	});
	
	// 全副场景
	$('.custom-full_scene').each(function() {
		var _swiper = $(this).find('.swiper-container');
		    _swiper.height($(window).height())
		$('body').css({overflowY: 'hidden'});
		var loop = $(this).data('loop');
		var flipway = $(this).data('flipway');
		var autoplay = $(this).data('autoplay');
		// 实例化组件
		var swiper = new Swiper(_swiper, {
	        slidesPerView: 'auto',
	        centeredSlides: true,
	        loop: !!loop,
	        direction: flipway,
	        autoplay: autoplay * 1000,
	    });
		
		this.swiper = swiper;
		var $voice = $(this).find('audio').get(0);
		var $voice_state = 0;
		// 语音
		$(this).on('click', '.full_scene-music a', function() {
			if (!$voice) {
				return;
			}
			if ($voice_state == 0) {
				Public.toast('播放背景音乐');
				$voice.play();
				$voice_state = 1;
			} else {
				Public.toast('暂停背景音乐');
				$voice_state = 0;
				$voice.pause();
			}
		});
	});
	
	// cart-tag
	$('.cart-tag').eq(0).each(function() {
		User.cartQuantity();
		User.countOrderState();
	});
	
	// 加入购物车
	$(document).on('click', '.goods-buy', function(e) {
		e.stopPropagation();
		e.preventDefault();
		var id = $(this).data('id');
		User.addGoodsCart(id, function() {
			// 加载购物车的数量
			User.cartQuantity();
			Public.toast('成功添加到购物车');
		});
	});
	
	// 页面统计
	Statistics.pageStatistics('mpage_' + pageId);
});