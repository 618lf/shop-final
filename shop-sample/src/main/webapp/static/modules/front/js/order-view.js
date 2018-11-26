/**
 * 红包
 */
var Redenvelope = {
		
	// 初始化
	init : function() {
		this.render();
	},
	
	// 显示红包
	render : function() {
		var that = this;
		
		// 是否需要弹出红包
		if (giveAble != 1) {return;}
		
		//是否有红包
		Public.postAjax(webRoot + '/f/member/shop/order/has_redenvelope?id=' + orderId, {}, function(data) {
			if (data.success) {
				var html = $('#redenvelopeTemplate').html();
				$(html).appendTo($('body'));
				that.addEvent();
			}
		})
	},
	
	// 注册事件
	addEvent : function() {
		
		var that = this;
		
		// 红包出现后的点击事件
		$(document).on('click.redenvelope', '.order-redenvelope', function(e) {
			if ($(this).hasClass('disable')) {return;}
			var target = $(e.target);
			// 锚点
			if (target.hasClass('-anchor')) {
				that.receive();
			} else {
				$(this).hide();
			}
		})
	},
	
	receiveFalg : 0,
	
	// 领取红包
	receive : function() {
		
		// 是否正在领取
		if (this.receiveFalg == 1) {return;}
		
		// 领取状态 -- 清空事件
		this.receiveFalg = 1; $(document).off('click.redenvelope');
		
		// 去领取
		Public.postAjax(webRoot + '/f/member/promotion/give_redenvelope_form_order', {id: orderId}, function(data) {
			$('.order-redenvelope').remove();
			if (data.returnFlag == 99999) {
				Public.toast(data.msg);
			} else if(data.returnFlag == 10000) {
				var price = data.price;
				var html = Public.runTemplate($('#redenvelope2Template').html(), {price: price})
				$(html).appendTo($('body'));
				Public.delayPerform(function() {
				   $('.order-redenvelope').remove();
				}, 2500)
			} else if(data.returnFlag == 10001) {
				var fissions = data.fissions;
				var href = webRoot + '/f/member/coupon/redenvelope_from_fission/'+fissions+'.html';
				window.location.href = href;
			}
		});
	}
};

// 初始化
$(function(){
	
    // 切换显示
    $(document).on('click', '.model-y', function(e) {
    	$(this).hide();
    	$('.model-x').show();
	});
    
    // 定时
    $('[data-remain-time]').each(function(){
	   var id = $(this).attr('id');
	   var time = $(this).data('remain-time');
	   Public.countDown(id, {'setTime':time, finish:'订单已过期'});
	   
	   // 防止下次有触发
	   $(this).removeAttr('data-remain-time');
	});
    
    // 查看快照
    $(document).on('click', '.goods-image', function(e) {
    	var href = $(this).attr('href');
    	if (!href) {
    		Public.tip('商品快照生成中...请稍后在试');
    	}
    	e.stopPropagation();
    });
    
    // 申请退货
    $(document).on('click', '.toApplyReturn', function(e) {
    	var id = $(this).data('id');
    	Public.dialog('申请退货', '确定申请退货？', {
    		btns : [{
	  			  id  : 'cancel',
	  			  name: '取消',
	  			  clazz : 'default',
	  			  fnc: function(){}
	  		},{
	  			  id  : 'ok',
	  			  name: '确定',
	  			  clazz : 'primary',
	  			  fnc: function(){
	  				  Public.postAjax(webRoot + '/f/member/shop/order/apply_return', {id: id}, function(data) {
	  						if (data.success) {
	  							location.reload(true);
	  						} else {
	  							Public.error(data.msg);
	  						}
	  				   });
	  			  }
	  		}]
    	});
    });
    
    // 申请退款
    $(document).on('click', '.toApplyRefund', function(e) {
    	var id = $(this).data('id');
    	Public.dialog('申请退货', '确定申请退款？', {
    		btns : [{
	  			  id  : 'cancel',
	  			  name: '取消',
	  			  clazz : 'default',
	  			  fnc: function(){}
	  		},{
	  			  id  : 'ok',
	  			  name: '确定',
	  			  clazz : 'primary',
	  			  fnc: function(){
	  				  Public.postAjax(webRoot + '/f/member/shop/order/apply_refund', {id: id}, function(data) {
	  						if (data.success) {
	  							location.reload(true);
	  						} else {
	  							Public.error(data.msg);
	  						}
	  				   });
	  			  }
	  		}]
    	});
    });
    
    // 取消订单
    $(document).on('click', '.toCancel', function(e) {
    	var id = $(this).data('id');
    	Public.dialog('取消订单', '确定取消订单？', {
    		btns : [{
	  			  id  : 'cancel',
	  			  name: '取消',
	  			  clazz : 'default',
	  			  fnc: function(){}
	  		},{
	  			  id  : 'ok',
	  			  name: '确定',
	  			  clazz : 'primary',
	  			  fnc: function(){
	  				  Public.postAjax(webRoot + '/f/member/shop/order/cancel', {id: id}, function(data) {
	  						if (data.success) {
	  							location.reload(true);
	  						} else {
	  							Public.error(data.msg);
	  						}
	  				   });
	  			  }
	  		}]
    	});
    });
    
    // 签收
    $(document).on('click', '.toReceipt', function(e) {
    	var id = $(this).data('id');
    	Public.dialog('确认签收', '为保障您的权益，请确认已经收到货了？', {
    		btns : [{
	  			  id  : 'cancel',
	  			  name: '取消',
	  			  clazz : 'default',
	  			  fnc: function(){}
	  		},{
	  			  id  : 'ok',
	  			  name: '确定',
	  			  clazz : 'primary',
	  			  fnc: function(){
	  				  Public.postAjax(webRoot + '/f/member/shop/order/receipt', {id: id}, function(data) {
	  						if (data.success) {
	  							// location.reload(true);
	  							// 跳转到订单评价页面
	  							window.location.href = webRoot + '/f/member/order/appraise/form/' + id + '.html';
	  						} else {
	  							Public.error(data.msg);
	  						}
	  				   });
	  			  }
	  		}]
    	});
    });
    
    // 返回列表
    $(document).on('click', '.toList', function(e) {
    	location.href = webRoot + '/f/member/shop/order/list.html'
    });
    
    // 红包初始化
    Redenvelope.init();
}); 