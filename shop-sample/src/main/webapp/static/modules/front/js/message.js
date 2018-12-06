$(function(){
	
	// 加载数据
	Public.initScrollLoad(ctxFront + '/member/message/list/data.json', $('#ordersTemplate').text(), function() {});
	
	// 未读处理
	$(document).on('click', '.unread', function() {
		var id = $(this).data('id');
		Public.postAjax(ctxFront + '/member/message/read/' + id + '.json', {}, function(data) {}, false);
	});
});