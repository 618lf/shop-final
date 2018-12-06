/**
 * 会员
 */
var VIP = function() {
	
	// 支付
	this.methods = PAY;
	this.methods();
	delete this.methods;
	
	this.getJsPayUrl = function(){
		return ctxFront + '/member/rank/payment/plugin_ajax_submit.json';
	};
	this.getNaPayUrl = function(){
		return ctxFront + '/member/rank/payment/plugin_ajax_submit/native.json';
	};
	this.getTouchUrl = function(){
		return ctxFront + '/member/rank/payment/touch.json';
	};
	this.success = function(id) {
		Public.close();
		Public.success('支付完成', function() {
			window.location.href = ctxFront + '/member/rank/centre.html';
		});
	};
	this.error = function(id) {
		$('.email').closest('.weui_cells').find('.weui_cell_ft').html('<i class="weui_icon_warn"></i>');
	};
};
/**
 * 初始化
 */
$(function() {
	
	// 支付
	var payment = new Payment();
	
	// 发起支付
	$(document).on('click', '.payBtn', function() {
		var id = $(this).data('id');
		payment.init(id);
	});
});