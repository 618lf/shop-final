$(function(){
	
	// 编辑事件
	$(document).on('click', '.receiver-edit', function(e){
		e.stopPropagation();
		e.preventDefault();
		var id = $(this).data('id');
		window.location.href = ctxFront + '/member/receiver/view/'+id+'.html?to=' + _to;
	});
	
	// 设置默认
	$(document).on('click', '.weui_check', function(e) {
		var id = $(this).val();
		Public.postAjax(ctxFront + '/member/receiver/def/'+id +'.json', {}, function() {
			if (_to == 'cart') {
				window.location.href = ctxFront + '/shop/cart/list.html';
			} else if(_to == 'order') {
				// 切换收货地址
				Public.postAjax(ctxFront + '/member/shop/order/receiver/select.json', {id: id}, function() {
					window.location.href = ctxFront + '/member/shop/order/confirm.html';
				});
			} else if(_to == 'index') {
				window.location.href = ctxFront + '/shop/store/index.html?_t=' + updateTime;
			}
		});
	});
	
	// 刷新
	$(document).on('click', '.refreshBtn', function(e) {
		load(true);
	});
	
	// 加载数据
	var load = function(isRefresh) {
		var _isRefresh = isRefresh || false;
		Public.postAjax(ctxFront + '/member/receiver/load.json', {}, function(data) {
			var html = Public.runTemplate($('#receiverTemplate').html(), {receivers: data.obj})
			$('#receiver-container').html(html);
			!!_isRefresh && Public.toast('数据刷新成功');
		});
	};
	
	load();
});