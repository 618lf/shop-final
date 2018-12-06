/**
 * 组合
 */
var complex = {
	
	init : function() {
		this.addEvent();
	},
	
	addEvent : function() {
		$(document).on('click', '.addCart', function() {
			var id = $(this).data('id');
			User.addComplexCart(id, function() {
				Public.toast('添加成功！');
			});
		});
	}
};

/**
 * 初始化
 * @returns
 */
$(function() {
	complex.init();
});