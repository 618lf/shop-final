/**
 * web socket
 */
var WS = {
	connect : function() {
		var self = this; var ws = null; var serverUrl = base.substr(7);
		if ('WebSocket' in window) {
            ws = new WebSocket("ws://"+serverUrl+"/actual/notice");
        } else if ('MozWebSocket' in window) {
            ws = new MozWebSocket("ws://"+serverUrl+"/actual/notice");
        }
		if (!ws) {
			self.log('[系统提示]: 不能连接到服务器.');
			return;
		}
		ws.onopen = function () {
			self.log('[系统提示]: 已连接到服务器，会收到系统发送的消息（用户注册、登录、下订单）.');
        };
        ws.onmessage = function (event) {
        	self.log(event.data);
        };
        ws.onerror = function (evnt) {
        	self.log(event.data);
        };
        ws.onclose = function (event) {
        	self.log('[系统提示]: 连接已关闭.');
        	self.ws = null;
        };
        this.ws = ws;
        this._start();
	},
	log : function(message) {
		var $t = $('.report-site > .-main');
		$t.prepend("<div class=\"-message\">"+message+"</div>");
    	if (message.indexOf('下单') != -1) {
    		new Audio(webRoot + "/static/voice/order_book.wav").play();
    	}
    	$t.find('.-message').each(function(n, e) {
    		if ($(e).offset().top -  $t.offset().top > $t.height()) {
    			$(e).remove();
    		}
    	});
    },
    sendMessage : function(message) {
    	if (this.ws) {
    		this.ws.send(message);
    	}
    },
    _start : function() {
    	var self = this;
    	// 10秒一次心跳
    	Public.setInterval(function() {
    		self.sendMessage('');
    	}, 10000);
    }
};

/**
 * 代办
 */
var TODO = {
	load : function() {
		Public.postAjax(webRoot + '/admin/system/todo',{}, function(data){
			 $.each(data.obj,function(index, item){
				 if (item.name === '待审核') {
					 $('.order-stat_sh .-num').text(item.count);
				 } else if (item.name === '待付款') {
					 $('.order-stat_fk .-num').text(item.count);
				 } else if (item.name === '待发货') {
					 $('.order-stat_fh .-num').text(item.count);
				 }
			 });
		 });
	}
};