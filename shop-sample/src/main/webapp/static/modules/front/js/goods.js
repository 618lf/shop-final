// 促销
var Promotion = {
		
	href : webRoot + '/f/shop/goods/promotons/',
	
    // 获取促销
	showPromotions : function(productId) {
	   var that = this;
	   var url = this.href + productId;
	   Public.postAjax(url, {}, function(data) {
		   var ps = data.obj;
		   if (!!ps && ps.length > 0) {
			   for(var i = 0; i< ps.length; i++) {
				   that.addPromotion(ps[i].name);
			   }
		   } else {
			   $('.goods-promotions').remove();
		   }
	   });
	},
	addPromotion : function(p) {
		var html = '<a class="promotion">'+p+'</a>';
		$('.promotions').append(html);
	}
};

/**
 * 加载评论
 */
var Appraise = {
		
	// 加载
    load : function(productId) {
    	var url = webRoot + '/f/member/bbs/section/stat/' + productId;
    	Public.postAjax(url, {}, function(data) {
    		if (data.success) {
    			var data = data.obj;
    			var html = Public.runTemplate($('#appraiseTemplate').html(), {stat: data.stat, topic: data.topic});
    			$('.appraise-wrap').removeClass('hide').html(html);
    		}
    	});
    }
}

/**
 * 初始化数据
 */
$(function(){
   var swiper = new Swiper('.swiper-container', {
       pagination: '.swiper-pagination',
       slidesPerView: 'auto',
       centeredSlides: true,
       loop:true
   });
   
   // 异步加载图片
   $(".goods-big-images img").scrollLoading();
   
   var $specification = $('#specification dl');
   var $price = $('#price');
   var $addProductNotify = $('#addProductNotify');
   var $butCart = $('#addCart');
   var $easyBuy = $('#easyBuy');
   var $outOfStock = $('#outOfStock');
   var $cart = $('.fixed-cart').eq(0);
   
   //判断ID值是否有效
   var isValid = function(specificationValueIds) {
	   for(var key in productData) {
		   var ids = key.split(",");
		   if (match(specificationValueIds, ids)) {return true;}
	   }
	   return false;
   };
   //判断数组是否配比
   var match = function(array1, array2) {
	   if (array1.length != array2.length) {return false;}
	   for(var i = 0; i < array1.length; i ++) {
		   if (array1[i] != null && array2[i] != null && array1[i] != array2[i]) {return false;}
	   }
	   return true;
   };
   
   //锁定规格值
   var lockSpecificationValue = function() {
	   var currentSpecificationValueIds = $specification.map(function() {
			$selected = $(this).find("a.selected");
			return $selected.size() > 0 ? $selected.data("val") : [null];
	   }).get();
	   $specification.each(function(i) {
			$(this).find("a").each(function(j) {
				var $this = $(this);
				var specificationValueIds = currentSpecificationValueIds.slice(0);
				specificationValueIds[i] = $this.data("val");
				if (isValid(specificationValueIds)) {
					$this.removeClass("locked");
				} else {
					$this.addClass("locked");
				}
			});
	   });
	   var product = productData[currentSpecificationValueIds.join(",")];
	   if (product != null) {
		   productId = product.id;
		   $price.text('￥' + $.formatFloat(product.price));
		   if (product.isOutOfStock) {
			   $addProductNotify.show(); $outOfStock.show();
			   !$butCart.hasClass('disabled') && $butCart.addClass('disabled');
			   !$easyBuy.hasClass('disabled') && $easyBuy.addClass('disabled');
		   }else {
			   $addProductNotify.hide(); $outOfStock.hide();
			   $butCart.hasClass('disabled') && $butCart.removeClass('disabled');
			   $easyBuy.hasClass('disabled') && $easyBuy.removeClass('disabled');
		   }
	   }else {
		 productId = null;
	   }
   };
   
   //规格值选择
   $(document).on('click.specification', '.specification a', function() {
	   var $this = $(this);
	   if ($this.hasClass("locked")) {return false;}
	   $this.toggleClass("selected").parent().siblings().children("a").removeClass("selected");
	   lockSpecificationValue();
	   return false;
   });
   
   lockSpecificationValue();
   
   // 加入购物车
   $(document).on('click', '#addCart', function() {
	   if(productId == null) {
	      Public.tip('请选择商品规格');
		  return false;
	   }
	   if($butCart.hasClass('disabled')) {
		  $outOfStock.fadeIn(150).fadeOut(150).fadeIn(150);
    	  return false;
       }
	   Public.postAjax(frontPath + '/shop/cart/add', {productId: productId}, function() {
		   var $image = $('.goods-images').find("img:first");
		   var cartOffset = $cart.offset();
		   var imageOffset = $image.offset();
		   $image.clone().css({
			   width: 150,
			   height: 150,
			   position: "absolute",
			   "z-index": 2000,
			   top: imageOffset.top,
			   left: imageOffset.left,
			   opacity: 0.8,
			   border: "1px solid #dddddd",
			   "background-color": "#eeeeee"
		   }).appendTo("body").animate({
			   width: 30,
			   height: 30,
			   top: cartOffset.top,
			   left: cartOffset.left,
			   opacity: 0.2
		   }, 1000, function() {
			   $(this).remove();
			   // 用户购物车数量
			   User.cartQuantity();
		   });
	   });
   });
   
   // 立即购买
   $(document).on('click', '#easyBuy', function() {
	   if(productId == null) {
	      Public.tip('请选择商品规格');
		  return false;
	   }
	   
	   if($easyBuy.hasClass('disabled')) {
		  $outOfStock.fadeIn(150).fadeOut(150).fadeIn(150);
    	  return false;
       }
	   
	   //立即购买
	   window.location.href = webRoot + '/f/member/shop/order/easyBuy/'+productId+'.html';
   });
   
   // 下滑
   $(document).on('click', '.pulldown', function() {
	   $(document).scrollTop(300);
	   $('.pulldown').hide();
   });
   $(document).on('scroll', function() {
	   var scrollTop = parseInt($(this).scrollTop());
	   if (scrollTop == 0) {
		   $('.pulldown').show();
	   } else {
		   $('.pulldown').hide();
	   }
   });
   
   // 显示促销信息
   Promotion.showPromotions(productId);
   
   // 加载评论信息
   Appraise.load(productId);
   
   // 加载购物车数量
   User.cartQuantity();
   
   // 统计商品访问次数
   Statistics.pageStatistics('product_' + goodsId);
   
});