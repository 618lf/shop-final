/**
 * 支付 -- 基础
 */
var PAY = function() {
	
	// 地址
	this.getJsPayUrl = function(){};
	this.getNaPayUrl = function(){};
	this.getTouchUrl = function(){};
	this.getInitUrl = function(){};
	
	// 支付成功之后的回调
	this.success = function() {};
	this.error = function() {};
	
	/**
	 * 校验支付 -- 主动尝试调用三次
	 */
	this.touch = function(id) {
		var that = this; var times = 3;
		Public.loading('支付校验中...')
		Public.setInterval(function(timer, _times) {
			Public.postAjax(that.getTouchUrl(), {id: id}, function(data) {
				if (data.returnFlag == 10000) {
				    Public.close();
				    clearTimeout(timer);
				    that.success(data.returnObj);
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
				that.success(data.returnObj);
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
				  that.success(data.returnObj);
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
	
	/**
	 * 初始化支付
	 */
	this.init = function(payable, epay, tip) {
		var that = this;
		Public.loading(tip || '支付申请...');
		Public.postAjax(that.getInitUrl(), {id: payable, epayId: epay}, function(data) {
			Public.close(); var _sub = data.returnObj;
			if (data.returnFlag == 10000) {
			    that.success(payable);
			} 
			//发起支付
			else if(data.returnFlag == 10001){
			   that.toPay(_sub);
			} 
			//错误
			else if(data.returnFlag == 99999) {
			   Public.error(_sub);
			   that.error(_sub);
			}
		});
	};
};