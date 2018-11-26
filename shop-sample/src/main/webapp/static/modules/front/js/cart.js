var Cart = {
	editQuantity : function(id, quantity, ckecked) {
		var that = this;
		if (!!id && /^\d+$/.test(quantity)) {
			Public.postAjax(webRoot + '/f/shop/cart/edit', {id: id, quantity: quantity}, function(data) {
				User.cartQuantity();
				
				// 计算订单金额
				if (!!ckecked) {
					that.selectItem(id);
				}
			});
		}
	},
	selectItem : function(id) {
		var that = this;
		Public.postAjax(webRoot + '/f/shop/cart/item/select',{id: id}, function(data) {
			var order = data.obj;
			that.calculationOrder(order);
		});
	},
	cancelItem : function(id) {
		var that = this;
		Public.postAjax(webRoot + '/f/shop/cart/item/cancel',{id: id}, function(data) {
			var order = data.obj;
			that.calculationOrder(order);
		});
	},
	calculationOrder : function(order) {
		if (!!order) {
		   $('.cart-price > strong').text("￥" + $.formatFloat(order.amount, 2))
		}
	},
	selectedAll : function() {
		var that = this;
		Public.postAjax(webRoot + '/f/shop/cart/all_select',{}, function(data) {
			var order = data.obj;
			that.calculationOrder(order);
		});
	},
	cancelAll : function() {
		var that = this;
		Public.postAjax(webRoot + '/f/shop/cart/all_cancel',{}, function(data) {
			var order = data.obj;
			that.calculationOrder(order);
		});
	},
	deleteItems : function(ids) {
		var that = this;
		if (!!ids && ids.length > 0) {
			Public.postAjax(webRoot + '/f/shop/cart/item/delete', ids, function(data) {
				User.cartQuantity(that.nullCart);
				var order = data.obj;
				that.calculationOrder(order);
			});
		}
	},
	nullCart : function(quantity) {
		quantity == 0 && (function(){
			$('.cart-container').hide();
			$('.cart-goods-ops').hide();
			$('<div class="weui_msg">'  +
			    '<div class="weui_icon_area"><i class="weui_icon_msg weui_icon_info"></i></div>' +
			    '<div class="weui_text_area">' +
			        '<h2 class="weui_msg_title">购物车空空的</h2>' +
			        '<p class="weui_msg_desc"><a href="/">去首页逛逛吧</a></p>' +
			    '</div>' +
			'</div>').appendTo('body')
		})();
	},
	currCartItem : {},
	// 缓存商品的促销信息
	promotions : [],
	// 调取设置优惠
	toSelectPromotion : function(id, productId, obj) {
		var _promotions = this.promotions[productId]; 
		this.currCartItem = {
			 id : id, 
			 productId : productId,
			 obj : obj
		};
		if (!_promotions) {
			Public.loading('操作中...');
			var href = webRoot + '/f/shop/goods/promotons/' + productId;
			Public.postAjax(href, {}, function(data) {
				Public.close();
				_promotions = data.obj;
			}, false);
			
			// 缓存优惠信息
			this.promotions[productId] = _promotions;
		}
		
		// 提供选择
		if (!(_promotions.length > 0)) {
			Public.toast('选择的商品暂无促销');
			return;
		}
		var html = Public.runTemplate($('#promotionTemplate').html(), {promotions: _promotions});
		Public.tipDialog(html);
	},
	
	// 选择一个优惠券
	selectPromotion : function(promotionId) {
		var id = this.currCartItem.id; var productId = this.currCartItem.productId;
		var obj = this.currCartItem.obj;
		if (!id || !productId) {
			Public.toast('设置优惠有误，请重新选择');
			return;
		}
		// 校验是否已使用
		var has = false;
		if (!!promotionId) {
			$('[data-promotion]').each(function(n, e) {
				if ($(e).data('promotion') == promotionId) {
					has = true;
				}
			});
		}
		if (!has) {
			var that = this;
			// 切换
			Public.postAjax(webRoot + '/f/shop/cart/item/change_pm', {id: id, productId: productId, promotionId: promotionId}, function(data) {
				var promotion = data.obj;
				if (data.success && !!promotion) {
					var html = Public.runTemplate($('#goodsPromotionTemplate').html(), {promotion: promotion});
					$(obj).find('.cart-goods-promotion').removeClass('hide').html(html);
					Public.toast('设置成功');
				} else if(data.success && !promotion){
					$(obj).find('.cart-goods-promotion').addClass('hide').html('');
					Public.toast('设置成功');
				} else {
					Public.toast(data.msg);
				}
				
				// 是否选中状态
				var checked = $(obj).find('.weui_check').is(':checked');
				if (checked) {
					that.selectItem(id);
				}
			});
		} else {
			Public.toast('已选择此促销');
		}
	},
};

$(function(){
	// 模式
	var mode = 'purchase';
	
	// 添加和减少数量
	$(document).on('click', '.goods-quantity', function(e) {
		var target = $(e.target); var b = parseInt($(this).find('b').text());
		if (target.hasClass('minus')) {
			b = b - 1;
		} else if(target.hasClass('add')) {
			b = b + 1;
		}
		b = b <=0 ? 1: b;
		$(this).find('b').text(b);
		
		var id = $(this).closest('.cart-item').data('id');
		var checked = $(this).closest('.cart-item').find('.weui_check').is(':checked');
		
		// 改变数量
		Cart.editQuantity(id, b, checked);
		
		// 禁止相关事件
		e.stopPropagation();
		e.preventDefault(); 
	});
	
	// 编辑模式
	$(document).on('click', '.modeBtn', function() {
		mode = mode == 'purchase' ? 'edit': 'purchase';
		if (mode == 'purchase') {
			$('.delBtn').hide();
			$('.buyBtn').show();
			$(this).text('编辑');
		} else {
			$('.buyBtn').hide();
			$('.delBtn').show();
			$(this).text('完成');
		}
	});
	
	// 选择
    $(document).on('click', '.weui_check', function() {
		var checked = $(this).is(':checked');
		// 取消选择所有
		if ($(this).hasClass('check-all')) {
			$('.weui_check').each(function(index, item) {
				if (!$(item).hasClass('check-all')) {
					$(item).prop('checked', checked);
				}
			});
			
			if (checked) {
				Cart.selectedAll();
			} else {
				Cart.cancelAll();
			}
		} else {
			var _allChecked = true,  _checkAll = null;
			$('.weui_check').each(function(index, item){
				if (!$(item).hasClass('check-all')) {
					if (_allChecked && !$(item).is(':checked')) {
						_allChecked = false;
					}
				} else {
					_checkAll = item;
				}
			});
			if(_allChecked && _checkAll) {
			   $(_checkAll).prop('checked', true);
			} else if(_checkAll) {
				$(_checkAll).prop('checked', false);
			}
			
			var id = $(this).val();
			if (checked) {
				Cart.selectItem(id);
			} else {
				Cart.cancelItem(id);
			}
		}
	});
    
    // 删除选中的
    $(document).on('click', '.delBtn', function() {
    	var ids = [];
    	$('.weui_check').each(function(index, item) {
    		if (!$(item).hasClass('check-all') && $(item).is(':checked')) {
    			ids.push({
    				name: 'ids',
    				value : $(item).val()
    			});
    			
    			// 删除
    			$(item).closest('.cart-item').remove();
    		}
    	});
    	Cart.deleteItems(ids);
    	// 取消全部选择
    	$('.check-all').prop('checked', false);
    })
    
    // 结算
    $(document).on('click', '.buyBtn', function(e) {
    	var hasChecked = false;
    	$('.weui_check').each(function(index, item) {
    		if (!hasChecked && !$(item).hasClass('check-all') && $(item).is(':checked')) {
    			hasChecked = true;
    		}
    	});
    	if (!hasChecked) {
    		Public.tip('至少选择一件商品！');
    		// 禁止相关事件
    		e.stopPropagation();
    		e.preventDefault(); 
    	}
	});
    
    // 去选择优惠
    $(document).on('click', '.goods-pros-select', function(e) {
    	var id = $(this).data('id'); var productId = $(this).data('product');
    	if (!!id && !!productId) {
    		Cart.toSelectPromotion(id, productId, $(this).closest('.cart-item-wrap'));
    	}
    	e.stopPropagation();
		e.preventDefault(); 
	});
    
    // 切换优惠
    $(document).on('click', '.change-promotion', function(e) {
    	var promotionId = $(this).data('id');
    	Cart.selectPromotion(promotionId);
	});
    
	
	// 加载购物车数量
	User.cartQuantity(Cart.nullCart);
	
	// 我的订单数量
	User.countOrderState();
});