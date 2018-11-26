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
		var href = webRoot + '/f/shop/search/goods/group';
		var that = $(this);
		Public.postAjax(href, {}, function(page) {
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
	
	// 所有弹出的新人礼包（仅一个有效）
	$('.pop-xr_gift:first').each(function() {
		
		var that = $(this);
		
		// 关闭
		var _close = function() {
			$(that).hide();
		};
		
		// 去领取
		var _to = function() {
			window.location.href = webRoot + "/f/member/promotion/newgift.html";
		};
		
		// 弹出 
		var _pop = function() {
			Public.postAjax(webRoot + "/f/member/promotion/isGetNewGift",{},function(data){
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
	
	// 统计页面访问
    Statistics.pageStatistics('mpage_' + pageId);
});