/**
 * 促销相关
 */
var Promotion = {
	
	// 获取新人礼包
	getXrGift : function() {
		
		var href = webRoot + '/f/member/promotion/getNewGift?recommendId=' + recommendId;
		
		// 获取新人礼包
		Public.postAjax(href, {}, function(data) {
			
			// 获取成功，显示获取的数据
			if (data.success) {
				var html = Public.runTemplate($('#couponsTemplate').html(), {data: data.obj});
				$('#container').html(html);
			} 
			// 获取显示失败页面
			else {
				var html = $('#errorTemplate').html();
				$('#container').html(html);
			}
		});
	},
	
	// 初始化验证
	init : function() {
		Public.postAjax(webRoot + "/f/member/promotion/isGetNewGift",{},function(data){
			if (data){
				var html = $('#errorTemplate').html();
				$('#container').html(html);
			}
		});
	}
};

/**
 * 初始化
 */
$(function() {
	
	// 获取新人礼包
	$(document).on('click', '.getBtn', function() {
		Promotion.getXrGift();
	});
});