/**
 * 地址操作
 */
var Points = {
	
	// 初始化
	init : function() {
		
		// 加载数据
		Public.initScrollLoad(ctxFront + '/member/point/list/data.json', $('#pointTemplate').text());
	}
};

/**
 * 初始化数据
 */
$(function(){
	Points.init();
});