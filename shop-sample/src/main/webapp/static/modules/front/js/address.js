/**
 * 地址操作
 */
var Address = {
		
	// 是否可用
	enable : true,
	
	// 执行查询
	doSearch : function() {
		var that = this;
		
		// 保证只有一次在查询
		if(!that.enable) {return;}
		
		// 置为不可用
		that.enable = false;
		
		// 延迟半秒中获取数据查询
		Public.delayPerform(function() {
			var query = $('#key').val();
			if (!!query) {
				// 获取数据
				Public.postAjax(ctxFront + '/member/map/getPoisByKey.json', {"key": query}, function(data) {
					if (data.success) {
						var html = Public.runTemplate($('#addressTemplate').html(), {pois: data.obj})
						$('#address-container').html(html);
					} else {
						Public.toast(data.msg);
					}
				}, false);
			}
			
			// 置为可用
			that.enable = true;
		}, 500);
	},
	
	// 定位当前地址（周边）
	doLocation : function() {
		Public.postAjax(ctxFront + '/member/map/getLocationPois.json', {}, function(data) {
			if (data.success) {
				var html = Public.runTemplate($('#addressTemplate').html(), {pois: data.obj})
				$('#address-container').html(html);
			} else {
				Public.toast(data.msg + '，可以通过查询获取地址');
			}
		});
	}
};

/**
 * 初始化数据
 */
$(function(){
	// 选择地址事件
	$(document).on('click', '.address', function(e){
		var address = $(this).data('address');
		var location = $(this).data('location');
		!!window.setAddress && window.setAddress(address, location);
	});
	
	// 当前位置
	$(document).on("click","#locationAddress",function(){
		Address.doLocation();
	});
	
	// 查询地址
	$(document).on("input", "#key", function(){
		Address.doSearch();
	});
});