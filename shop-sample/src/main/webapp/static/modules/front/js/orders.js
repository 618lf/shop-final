/**
 * 订单
 */
$(function() {
	
	//加载数据
	Public.initScrollLoad(ctxFront + '/member/shop/order/list/data.json', $('#ordersTemplate').text(), function() {
		
		// 定时
		$('[data-remain-time]').each(function(){
		   var id = $(this).attr('id');
		   var time = $(this).data('remain-time');
		   Public.countDown(id, {'setTime':time, finish:'订单已过期'});
		   
		   // 防止下次有触发
		   $(this).removeAttr('data-remain-time');
		});
		
		// 金额格式化
		$('[data-money]').each(function() {
			$(this).html('￥' + $.formatFloat($(this).data('money'), 2));
			$(this).removeAttr('data-money');
		});
		
	});
	
	// 签收
    $(document).on('click', '.toReceipt', function(e) {
    	var id = $(this).data('id');
    	Public.dialog('确认签收', '为保障您的权益，请确认已经收到货了？', {
    		btns : [{
	  			  id  : 'cancel',
	  			  name: '取消',
	  			  clazz : 'default',
	  			  fnc: function(){}
	  		},{
	  			  id  : 'ok',
	  			  name: '确定',
	  			  clazz : 'primary',
	  			  fnc: function(){
	  				  Public.postAjax(ctxFront + '/member/shop/order/receipt.json', {id: id}, function(data) {
	  						if (data.success) {
	  							Public.success('签收成功', function() {
	  								location.reload(true);
	  							});
	  						} else {
	  							Public.error(data.msg);
	  						}
	  				   });
	  			  }
	  		}]
    	});
    });
});