/**
 * 收货地址选择
 */
var Receiver_Sel = {
	
	inited : false,
		
    // 初始化选择
	init : function() {
		if (!!this.inited) {return;}
		if (!this.inited) {
			this.inited = true;
			this.addEvent();
			this.load(false);
		}
	},
	
	// 注册事件
	addEvent : function() {
		
		var self = this;
		
		// 编辑事件
		$('#receiver-container').on('click.receiver.edit', '.receiver-edit', function(e){
			e.stopPropagation();
			e.preventDefault();
			var id = $(this).data('id');
			window.location.href = ctxFront + '/member/receiver/view/'+id+'.html?to=order';
		});
		
		// 选择 - 不设置为默认
		$('#receiver-container').on('click.receiver.sel', '.weui_check', function(e) {
			var id = $(this).val();
			// 切换收货地址
			Public.postAjax(ctxFront + '/member/shop/order/receiver/select.json', {id: id}, function(data) {
				$('#order-container').show();
				$('#receiver-container').hide();
				// 更新订单数据
				if (data.success) {
					var obj = data.obj;
					$('#tempAmount').html("￥" + $.formatFloat(obj.amount, 2));
					$('#tempFreight').html("￥" + $.formatFloat(obj.freight, 2));
					$('#location-address').html(obj.detail);
				} else {
					Public.toast(data.msg);
				}
			});
		});
		
		// 刷新
		$('#receiver-container').on('click', '.refreshBtn', function(e) {
			self.load(true);
		});
	},
	
	// 加载数据
	load : function(isRefresh) {
		var _isRefresh = isRefresh || false;
		Public.postAjax(ctxFront + '/member/receiver/load.json', {}, function(data) {
			var html = Public.runTemplate($('#receiverTemplate').html(), {receivers: data.obj})
			$('#receiver-container .receivers-wrap').html(html);
			!!_isRefresh && Public.toast('数据刷新成功');
		});
	}
};