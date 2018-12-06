$(function(){
	
	// 执行滚动
	var doIScroll = function() {
		$('.iScroll').each(function(index, iScroll) {
			Public.newScroll(iScroll);
		});
	};
	
	doIScroll();
	
	//加载数据
	Public.initScrollLoad(ctxFront + '/shop/search/goods/category.json', $('#goodsTemplate').text(), function(_scroll, data) {
		// 设置金额
		if (data) {
			$('[data-money]').each(function() {
				$(this).html('<span class="pre">￥</span>' + $._formatFloat($(this).data('money'), 2));
				$(this).removeAttr('data-money');
			});
			
			// 标签
			$('[data-tags]').each(function() {
				var tags = $(this).data('tags');
				if (!!tags) {
					var thtml = Public.runTemplate($('#tagsTemplate').html(), {tag: tags.substr(0,1)});
					$(this).append(thtml);
				}
				$(this).removeAttr('data-tags');
			});
		}
	});

    // 加入购物车
    $(document).on('click', '.add-cart', function(e) {
    	e.stopPropagation(); e.preventDefault();
		var id = $(this).data('id'); var $ops = $(this).closest('.-ops');  var $b = $ops.find('b');
		User.addGoodsCart(id, function() {
			// 加载购物车的数量
			User.cartQuantity();
			$ops.addClass('has');
			var b = parseInt($b.text());
			$b.text(b + 1);
		});
    });
    
    // 添加和减少数量
    $(document).on('click', '.-ops a', function(e) {
		var $target = $(this);
		var id = $(this).data('id'); var $ops = $(this).closest('.-ops');  var $b = $ops.find('b'); var b = parseInt($b.text());
		if ($target.hasClass('minus')) {
			b = b - 1;
			User.removeGoodsCart(id, function() {
				User.cartQuantity();
			});
		} else if($target.hasClass('add')) {
			b = b + 1;
			User.addGoodsCart(id, function() {
				User.cartQuantity();
			});
		}
		b = b <=0 ? 0: b;
		$b.text(b);
		
		if (b == 0) {
			$ops.removeClass('has');
		} else {
			$ops.addClass('has');
		}
		
		// 禁止相关事件
		e.stopPropagation();
		e.preventDefault(); 
    });
    
    // 加入购物车
    $(document).on('click', '.iScroll-item', function(e) {
    	e.stopPropagation(); e.preventDefault();
		var id = $(this).data('id');
		$('.iScroll-item.cur').removeClass('cur');
		$(this).addClass('cur');
		$('#categoryId').val(id);
		Public.resetScrollLoad();
    });
    
	// 合并 加载购物车数量 - 我的订单数量
	User.mutilAbout('receiver');
	
    // 统计首页访问次数
    Statistics.pageStatistics('index');
});