/**
 * 支付
 */
var Payment = function() {
	
	// 支付
	this.methods = PAY;
	this.methods();
	delete this.methods;
	
	//地址
	this.getInitUrl = function(){
		return ctxFront + '/member/shop/order/payment/init.json';
	};
	this.getJsPayUrl = function(){
		return ctxFront + '/member/shop/order/payment/plugin_ajax_submit.json';
	};
	this.getNaPayUrl = function(){
		return ctxFront + '/member/shop/order/payment/plugin_ajax_submit/native.json';
	};
	this.getTouchUrl = function(){
		return ctxFront + '/member/shop/order/payment/touch.json';
	};
	this.success = function(id) {
		Public.close();
		Public.success('支付完成', function() {
			window.location.href = ctxFront + '/member/shop/order/view/' + id + '.html';
		});
	};
	this.error = function() {};
};

/**
 * 初始化
 */
$(function() {
	
	// 支付
	var payment = new Payment();
	
	// 定时
	$('[data-remain-time]').each(function(){
	   var id = $(this).attr('id');
	   var time = $(this).data('remain-time');
	   Public.countDown(id, {'setTime':time, finish:'订单已过期'});
	});
	
	// 发起支付
	$(document).on('click', '.payBtn', function() {
		var epay = $('.weui_check:checked').val();
		if (!!epay) {
			var id = $(this).data('id');
			payment.init(id, epay);
			return;
		}
		Public.error('请选择支付方式!');
	});
});