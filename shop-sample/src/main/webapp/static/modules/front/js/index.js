$(function(){
	
	//是否显示新人礼包
	Public.postAjax(webRoot + "/f/member/promotion/isGetNewGift",{},function(data){
		if (!data){
			$("#newGift").removeClass("hide");
		}
	});
	
	//首页分享地址
	window.customHref = Public.getBasePath("/f/member/to_index.html");
	
	// 图片轮播
	var swiper = new Swiper('.swiper-container', {
        pagination: '.swiper-pagination',
        slidesPerView: 'auto',
        centeredSlides: true,
        autoplay : 2000,
        loop:true
    });
	
	// 加载购物车数量
	User.cartQuantity();
	
	// 我的订单数量
	User.countOrderState();
	
	// 自动定位 -- 不需要
	var locationDef = function() {
		Public.postAjax(webRoot + '/f/location/def', {}, function(data) {
			if (data.success) {
				$('.top-ops-wrap .location-name').html(data.obj + '<i class="iconfont icon-right"></i>');
			}
		});
	};
	
	//locationDef();
	
	// 回调函数的访问控制
	var call_end_state = 0;
	var index_footer_top = -1;
	
	//加载数据
	Public.initScrollLoad(webRoot + '/f/shop/search/goods/index', $('#goodsTemplate').text(), function(_scroll, data) {
		// 设置金额
		if (data) {
			$('[data-money]').each(function() {
				$(this).html('<span class="pre">￥</span>' + $._formatFloat($(this).data('money'), 2));
				$(this).removeAttr('data-money');
			});
		} else if(call_end_state == 0){
			call_end_state = 1;
//			Public.delayPerform(function() {
//				_scroll.disable();
//				var scrollTop = parseInt($(document).scrollTop());
//				var scrollHeight = parseInt(document.body.scrollHeight);
//				var windowHeight = parseInt($(window).outerHeight());
//				var top = scrollHeight - windowHeight - 87;
//				    top = top < 0 ? 0: top;
//				//$('body').animate({scrollTop: (top)}, 500, function() {
//					//_scroll.enable(87), call_end_state = 0;
//				//});
//			}, 1000);
			
			// 显示
			$('.load-more').hide();
			$('.index-footer').show();
			$('.index-footer [data-src]').each(function() {
				var _src = $(this).data('src');
				$(this).attr('src', _src);
			});
			
			// 顶部
			index_footer_top = $('.index-footer').offset().top;
			footer_scroll();
		}
	}, {always_call_end: 1});

    // 统计首页访问次数
    Statistics.pageStatistics('index');
    
    // 加入购物车
    $(document).on('click', '.add-cart', function(e) {
    	e.stopPropagation();
		e.preventDefault();
		var id = $(this).data('id'); var b = $(this).closest('.-ops').find('b');
		User.addGoodsCart(id, function() {
			// 加载购物车的数量
			User.cartQuantity();
			$(b).show().fadeOut(1000);
		});
    });
    
    // 关闭新人礼包
    $(document).on("click","#giftClose",function(){
    	$("#newGift").addClass("hide");
    });
    
    // 定位到顶部
    $(document).on("click",".toTop",function(){
    	$('body').animate({scrollTop: 0}, 500, function() {});
    });
    
    // 底部滚动
    var footer_scroll = function() {
    	$(document).on('scroll', function() {
    		var scrollTop = parseInt($(document).scrollTop());
     	    if (index_footer_top != -1 && scrollTop <= index_footer_top) {
     	    	$('.toTop').hide();
     	    } else {
     	    	$('.toTop').show();
     	    }
        });
    };
});