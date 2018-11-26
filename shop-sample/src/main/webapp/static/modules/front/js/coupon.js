$(function(){
	
	//加载数据
	Public.initScrollLoad(webRoot + '/f/member/coupon/codeList/data', $('#couponCodeTemplate').text(), function(){}, 
		{   showNoData: function() {
			var html = '<div class="weui_msg"><div class="weui_icon_area"><img src="'+ webRoot + '/static/modules/front/img/no-data.png"/></div><div class="weui_text_area"><p class="weui_msg_desc">暂无优惠</p></div></div>';
			$('#ajax-load-page').append(html);
		}
	}); 
	
	//获取优惠券
	$(document).on("click",".query-btn",function(){
		var val = $("#code").val();
		if (!!val && val.length >= 18) {
			Public.postAjax(webRoot + "/f/member/coupon/fetchByCode",{"code":val}, function(data){
				if (data.success){
					var _val = data.obj;
					Public.tip('恭喜获得一张面值为' + _val +'元的优惠券');
					Public.resetScrollLoad();
				}else{
					Public.toast("获取失败！");
				}
			})
		} else {
			Public.toast("兑换码无效！");
		}
	});
	
	// 加载购物车数量
	User.cartQuantity();
});