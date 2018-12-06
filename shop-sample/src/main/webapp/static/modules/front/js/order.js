var Order = {
	
	// 设置备注
	remarks: function(remarks) {
		var that = this;
		Public.postAjax(ctxFront + '/member/shop/order/remarks.json',{remarks: remarks}, function(data) {});
	},
	
	// 选择收货信息
	toSelectReceiver : function() {
		$('#order-container').hide();
		$('#receiver-container').show();
		Receiver_Sel.init();
	},
	
	// 缓存的优惠券
	coupons : null,
	
	// 选择优惠券
	toSelectCoupon : function() {
		var _coupons = this.coupons;
		if (!_coupons) {
			Public.loading('操作中...');
			var href = ctxFront + '/member/shop/order/coupon/select.json';
			Public.getAjax(href, {}, function(data) {
				Public.close();
				_coupons = data.obj;
			}, false);
			
			// 缓存数据
			this.coupons = _coupons;
		}
		
		// 提供选择
		if (!_coupons || !(_coupons.length > 0)) {
			Public.toast('没有可用的优惠券');
			return;
		}
		
		var html = Public.runTemplate($('#couponsTemplate').html(), {coupons: _coupons});
		Public.tipDialog(html);
	},
	
	// 选择这张优惠券
	selectCoupon : function(id) {
		var href = ctxFront + '/member/shop/order/coupon/select.json';
		Public.postAjax(href, {id: id||''}, function(data) {
			if (data.success) {
				var obj = data.obj;
				var html = !!obj.id ? (Public.runTemplate($('#couponTemplate').html(), {coupon: obj})) : '选择优惠券';
				$('#goods-coupon-container').html(html);
				
				// 设置折扣的金额
				$('#tempAmount').html("￥" + $.formatFloat(obj.amount, 2));
				$('#tempDiscount').html("￥" + $.formatFloat(obj.discount, 2));
				$('#detailDiscount').html(obj.detail);
			} else {
				Public.toast(data.msg);
			}
		});
	},
	
	// 自动选择最大的优惠券
	autoCoupon : function() {
		var href = ctxFront + '/member/shop/order/coupon/auto_select.json'; var that = this;
		Public.postAjax(href, {}, function(data) {
			if (data.success) {
				var obj = data.obj;
				var html = !!obj.id ? (Public.runTemplate($('#couponTemplate').html(), {coupon: obj})) : '选择优惠券';
				$('#goods-coupon-container').html(html);
				
				// 设置折扣的金额
				$('#tempAmount').html("￥" + $.formatFloat(obj.amount, 2));
				$('#tempDiscount').html("￥" + $.formatFloat(obj.discount, 2));
				$('#detailDiscount').html(obj.detail);
			}
			
			// 顺序调用
			that.freePostage();
		});
	},
	
	// 自动设置自动包邮
	freePostage : function() {
		var href = ctxFront + '/member/shop/order/postage/free.json';
		Public.postAjax(href, {}, function(data) {
			if (data.success) {
				var obj = data.obj;
				$('#goods-postage-container').show();
				$('#tempAmount').html("￥" + $.formatFloat(obj.amount, 2));
				$('#tempDiscount').html("￥" + $.formatFloat(obj.discount, 2));
				$('#goods-postage-container .postage-name').html('会员包邮（' + obj.postage + '）');
				$('#detailDiscount').html(obj.detail);
			} else {
				$('#goods-postage-container').hide();
			}
		});
	},
	
	// 不想包邮
	closePostage : function() {
		var href = ctxFront + '/member/shop/order/postage/close.json';
		Public.postAjax(href, {}, function(data) {
			if (data.success) {
				var obj = data.obj;
				$('#tempAmount').html("￥" + $.formatFloat(obj.amount, 2));
				$('#tempDiscount').html("￥" + $.formatFloat(obj.discount, 2));
				$('#goods-postage-container .postage-name').html('会员包邮（' + obj.postage + '）');
				$('#detailDiscount').html(obj.detail);
			} else {
				$('#goods-postage-container').hide();
			}
		});
	},
	
	// 校验
	check: function() {
		try {
			var checked = false;
			Public.postAjax(ctxFront + '/member/shop/order/check.json', {}, function(data) { 
				if (data.success) {
					checked = true;
				} else if(!!data.obj){
					var item = data.obj;
					if ($('.product-' + item.id).hasClass('product-complex')) {
						$('.product-' + item.id).find('.store').html("(商品库存不足)")
					} else {
						$('.product-' + item.id).find('.store').html("(剩余库存 "+item.store+")")
					}
			        Public.toast(data.msg);
			        $('.model-y').click();
				} else {
					Public.toast(data.msg);
				}
			}, false);
			return checked;
		}catch(e){
			Public.toast('下单失败，请重试！');
			return false;
		}
	},
	
	// 初始化缩略图
	initComplex : function() {
		$('.model-x .order-goods').each(function() {
			var $this = $(this);
			if ($this.find('.goods-image').length >1) {
				var $l = $this.find('.goods-image').length;
				$this.addClass('order-complex complex-' + $l);
			}
		});
	}
};

$(function(){
	
	// 初始时，默认选择最大的优惠券
	Order.autoCoupon();
	
	// 套装的显示
	Order.initComplex();
	
    // 切换显示
    $(document).on('click', '.model-y', function(e) {
    	$(this).hide();
    	$('.model-x').show();
	});
    
    // 保存订单备注
	$('#remarks').bind('blur', function() {
		var remarks = $(this).val();
		if (!!remarks && remarks.length > 0) { 
			Order.remarks(remarks);
		}
	});
	
	// 选择收货信息
	$(document).on('click', '.to-select-receiver', function(e) {
		Order.toSelectReceiver();
	});
	
	// 优惠券
	$(document).on('click', '.to-select-coupon', function(e) {
		Order.toSelectCoupon();
	});
	
	// 使用优惠券
	$(document).on('click', '.change-coupon', function(e) {
		var id = $(this).data('id');
		Order.selectCoupon(id);
	});
	
	// 设置是否使用包邮
	$(document).on('click', '.change-postage', function(e) {
		var checked = $(this).is(':checked');
		if (!checked) {
			Order.closePostage();
		} else {
			Order.freePostage();
		}
	});
	
	// 提交订单
	$('#cartBuy').on('click', function(e) {
		e.stopPropagation(); e.preventDefault();
		var checked = Order.check();
		if (!!checked) {
			window.location.href = $(this).data('href');
		}
	});
});