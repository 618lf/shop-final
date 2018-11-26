$(function(){
	
	// 加载数据
	Public.initScrollLoad(webRoot + '/f/member/message/list/data', $('#ordersTemplate').text(), function() {});
	
	// 未读处理
	$(document).on('click', '.unread', function() {
		var id = $(this).data('id');
		Public.postAjax(webRoot + '/f/member/message/read/' + id, {}, function(data) {}, false);
	});
});