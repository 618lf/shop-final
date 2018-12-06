// 促销
var Mutil = {
		
	href : ctxFront + '/shop/goods/mutil/',
	
    // 获取商品信息
	showMutil : function(productId) {
	   var that = this;
	   var url = this.href + productId + '.json';
	   Public.postAjax(url, {}, function(data) {
		   var result = data.obj;
		   
		   // 促销
		   var ps = result.promotons;
		   if (!!ps && ps.length > 0) {
			   for(var i = 0; i< ps.length; i++) {
				   that.addPromotion(ps[i]);
			   }
		   } else {
			   $('.goods-promotions').remove();
		   }
		   
		   // 优惠套装
		   var dis = result.discounts;
		   if (!!dis) {
			   that.addComplex(productId, 'disc', '优惠套装');
		   }
		   
		   // 人气组合
		   var hots = result.hots;
		   if (!!hots) {
			   that.addComplex(productId, 'hots', '人气组合');
		   }
		   
		   // 评论
		   var appraise = result.stat;
		   if (!!appraise) {
			   that.loadAppraise(appraise);
		   }
	   });
	},
	
	addPromotion : function(p) {
		var html = '<a class="promotion" href="'+ frontStatic +'/shop/promotion/' + p.id + '.html">'+p.name+'</a>';
		$('.promotions').append(html);
	},
	
	addComplex : function(id, type, name) {
		var html = '<a class="complex" href="'+ ctxFront + '/shop/goods/' + type + '/' + id + '.html">'+name+'</a>';
		$('.promotions').append(html);
	},
	
	loadAppraise : function(data) {
		var html = Public.runTemplate($('#appraiseTemplate').html(), {stat: data.stat, topic: data.topic});
		$('.appraise-wrap').removeClass('hide').html(html);
	}
};

/**
 * 商品相关的操作
 */
var GOODS = {
	
	init : function() {
	   
	   var self = this;
		
	   var swiper = new Swiper('.swiper-container', {
	       pagination: '.swiper-pagination',
	       slidesPerView: 'auto',
	       centeredSlides: true,
	       loop:true
	   });
	   
	   // 显示促销信息
	   Mutil.showMutil(productId);
	   
	   // 加载购物车数量
	   User.cartQuantity();
	   
	   // 初始化变量
	   this.$specification = $('#specification_picker dl');
	   this.$title = $('.-sel_title');
	   this.$price = $('.-sel_price');
	   this.$addProductNotify = $('#addProductNotify');
	   this.$butCart = $('.addCart');
	   this.$easyBuy = $('.easyBuy');
	   this.$outOfStock = $('#outOfStock');
	   this.$cart = $('.fixed-cart').eq(0);
	   
	   // 加载事件
	   this.addEvent();
	},
	
	addEvent : function() {
	   
	   var self = this;
	   
	   // 规格控件
	   $('#specification_picker .iScroll').each(function(index, iScroll) {
		  self.$specification_scroll = Public.newScroll(iScroll);
	   });
	   
	   // 异步加载图片
	   $(".goods-big-images img").scrollLoading();
		   
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
		   var currentSpecificationValueIds = self.$specification.map(function() {
				$selected = $(this).find("a.selected");
				return $selected.size() > 0 ? $selected.data("val") : [null];
		   }).get();
		   self.$specification.each(function(i) {
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
			   self.$price.text('￥' + $.formatFloat(product.price));
			   self.$title.text(product.name);
			   if (product.isOutOfStock) {
				   self.$addProductNotify.show(); self.$outOfStock.show();
				   !self.$butCart.hasClass('disabled') && self.$butCart.addClass('disabled');
				   !self.$easyBuy.hasClass('disabled') && self.$easyBuy.addClass('disabled');
			   }else {
				   self.$addProductNotify.hide(); self.$outOfStock.hide();
				   self.$butCart.hasClass('disabled') && self.$butCart.removeClass('disabled');
				   self.$easyBuy.hasClass('disabled') && self.$easyBuy.removeClass('disabled');
			   }
		   }else {
			   productId = null;
		   }
	   };
	   
	   // 初始化
	   lockSpecificationValue();
	   
	   //规格值选择
	   $(document).on('click.specification', '#specification_picker a', function() {
		   var $this = $(this);
		   if ($this.hasClass("locked")) {Public.toast('缺货！');return false;}
		   if ($this.hasClass("selected")) {return false;}
		   $this.toggleClass("selected").parent().siblings().children("a").removeClass("selected");
		   lockSpecificationValue();
		   return false;
	   });
	   
	   // 显示规格
	   $(document).on('click', '.goods-specification', function() {
		   $('#specification_picker').show();
		   self.$specification_scroll.refresh();
	   });
	   
	   // 关闭规格
	   $(document).on('click', '.weui_mask', function() {
		   $('#specification_picker').hide();
	   });
	   
	   // 加入购物车
	   $(document).on('click', '.addCart', function() {
		   if (productId == null) {
		       Public.toast('请选择商品规格');
			   return false;
		   }
		   if (self.$butCart.hasClass('disabled')) {
			   self.$outOfStock.fadeIn(150).fadeOut(150).fadeIn(150);
			   Public.toast('该商品缺货！');
	    	   return false;
	       }
		   
		   if ($(this).hasClass('min')) {
			   var quantity = parseInt($('.-quantity').find('b').text());
		           quantity = !quantity ? 0 : quantity;
		       Public.postAjax(ctxFront + '/shop/cart/add.json', {productId: productId, quantity: quantity}, function() {
				   Public.toast('加入购物车成功');
				   User.cartQuantity();
			   });
		   } else {
			   Public.toast('加入购物车成功');
			   Public.postAjax(ctxFront + '/shop/cart/add.json', {productId: productId}, function() {
				   var $image = $('.goods-images').find("img:first");
				   var cartOffset = self.$cart.offset();
				   var imageOffset = $image.offset();
				   $image.clone().css({
					   width: 150,
					   height: 150,
					   position: "absolute",
					   "z-index": 2000,
					   top: 0,
					   left: 0,
					   opacity: 1,
					   border: "1px solid #dddddd",
					   "background-color": "#eeeeee"
				   }).appendTo("body").animate({
					   width: 30,
					   height: 30,
					   top: cartOffset.top,
					   left: cartOffset.left,
					   opacity: 0.2
				   }, 1500, function() {
					   $(this).remove();
					   // 用户购物车数量
					   User.cartQuantity();
				   });
			   });
		   }
	   });
	   
	   // 添加和减少数量
	   $(document).on('click', '.-quantity a', function(e) {
			var $target = $(this); var $b = $(this).closest('.-quantity').find('b'); var b = parseInt($b.text());
			if ($target.hasClass('-minus')) {
				b = b - 1;
			} else if($target.hasClass('-add')) {
				b = b + 1;
			}
			b = b <=0 ? 1: b;
			$b.text(b);
			
			// 禁止相关事件
			e.stopPropagation();
			e.preventDefault(); 
	   });
	   
	   // 立即购买
	   $(document).on('click', '.easyBuy', function() {
		   if (productId == null) {
		       Public.toast('请选择商品规格');
			   return false;
		   }
		   
		   if (self.$easyBuy.hasClass('disabled')) {
			   self.$outOfStock.fadeIn(150).fadeOut(150).fadeIn(150);
			   Public.toast('该商品缺货！');
	    	   return false;
	       }
		   
		   // 数量
		   var quantity = 1;
		   if ($(this).hasClass('min')) {
			   quantity = parseInt($('.-quantity').find('b').text());
		       quantity = !quantity ? 0 : quantity;
		   }
		   
		   //立即购买
		   window.location.href = ctxFront + '/member/shop/order/easyBuy/'+productId+'.html?quantity=' + quantity;
	   });
	   
	   // 到货提醒
	   $(document).on('click', '.-remind', function() {
		   Public.postAjax(ctxFront + '/member/shop/product/remind/'+productId+'.json', {}, function() {
			   Public.toast('登记成功');
		   });
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
	}
};

/**
 * 初始化数据
 */
$(function(){
	GOODS.init();
    Statistics.pageStatistics('product_' + productId);
});