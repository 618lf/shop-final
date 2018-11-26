/**
 * 会员
 */
var VIP = function() {
	this.getJsPayUrl = function(){
		return webRoot + '/f/member/rank/payment/plugin_ajax_submit';
	};
	this.getNaPayUrl = function(){
		return webRoot + '/f/member/rank/payment/plugin_ajax_submit/native';
	};
	this.getTouchUrl = function(){
		return webRoot + '/f/member/rank/payment/touch';
	};
	this.render = function(id) {
		Public.close();
		Public.success('支付完成', function() {
			window.location.href = webRoot + '/f/member/rank/centre.html';
		});
	};
	
	/**
	 * 校验支付
	 */
	this.touch = function(id) {
		var that = this; var times = 3;
		Public.loading('支付校验中...')
		Public.setInterval(function(timer, _times) {
			Public.postAjax(that.getTouchUrl(), {id: id}, function(data) {
				if(data.returnFlag == 10000) {
				   Public.close();
				   clearTimeout(timer);
				   that.render(id);
				} else if(_times == times) {
				   Public.close();
				   Public.error('支付失败，如有问题请联系客服')
				}
			}, false);
		}, 1000, times);
	};
	
	/**
	 * 本地 支付
	 */
	this.navtivePay = function(id) {
		var that = this;
		Public.postAjax(that.getNaPayUrl(), {id: id}, function(data) {
			Public.close();
			if (data.returnFlag == 10000) {
				that.render(id);
			}
			//发起支付
			else if(data.returnFlag == 10001) {
			   var img = '<div class="native-pay-qrcode"><div class="hd">'+data.remarks+'</div><div class="image-wrap"><img src="'+(data.returnObj)+'"></div><div class="tip">长按或扫描图片【识别二维码】付款</div></div>';
			   Public.dialog('微信扫码支付', img, {
				  btns: [{
					   id: 'ok',
					   name: '我已完成支付',
					   fnc: function() {
						  that.touch(id);
					   }
				  }]
			   });
			}
			//错误
			else if(data.returnFlag == 99999) {
			   Public.error(data.returnObj);
			}
		});
	};
	
	/**
	 * JS 支付
	 */
	this.jsPay = function(id) {
		var that = this; Public.loading('发起微信支付中...');
		if(window.WeixinJSBridge) {
		   Public.postAjax(that.getJsPayUrl(), {id: id}, function(data) {
			  if (data.returnFlag == 10000) {
				  Public.close();
				  that.render(id);
			  }
			  //发起支付
			  else if(data.returnFlag == 10001){
				 Public.close();
				 var paySign = $.parseJSON(data.returnObj);
				 WeixinJSBridge.invoke('getBrandWCPayRequest', paySign, function(res) {
				    if(res.err_msg == "get_brand_wcpay_request:ok") {
					   that.touch(id);
				    } else {
					   that.navtivePay(id);
				    }
			     }); 
			  }
			  //错误
			  else if(data.returnFlag == 99999) {
			     that.navtivePay(id);
			  }
		  });
		} else {
		   that.navtivePay(id);
		}
	};
	
	/**
	 * 发起支付
	 */
	this.toPay = function(id) {
		var that = this; 
		if (window.WeixinJSBridge) {
			that.jsPay(id);
		} else {
			Public.success('正努力发起微信支付...', function() {
				that.jsPay(id);
			});
		}
	};
	
	// 发起订阅
	this.toSub = function(id) {
		var that = this;
		Public.loading('开通中...');
		Public.postAjax(webRoot + '/f/member/rank/payment/init', postData, function(data) {
			Public.close(); var _sub = data.returnObj;
			if(data.returnFlag == 10000) {
			   that.render(id);
			} 
			//发起支付
			else if(data.returnFlag == 10001){
			   that.toPay(data);
			} 
			//错误
			else if(data.returnFlag == 99999) {
			   Public.error(_sub);
			   $('.email').closest('.weui_cells').find('.weui_cell_ft').html('<i class="weui_icon_warn"></i>');
			}
		});
	};
}