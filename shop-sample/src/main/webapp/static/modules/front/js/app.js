(function(){
	
	//String startWith
	String.prototype.startWith = function(str){     
	  var reg=new RegExp("^"+str);     
	  return reg.test(this);        
	}; 
	//String endWith
	String.prototype.endWith = function(str){     
	  var reg=new RegExp(str+"$");     
	  return reg.test(this);        
	};
	//String endWith
	String.prototype.substringAfterLast = function(separator){
	  var pos = this.lastIndexOf(separator);
	  return this.substring(pos + separator.length);
	};
	//trim
	String.prototype.trim = function() {
	    return this.replace(/(^\s*)|(\s*$)/g,'');
	};
	//removeSpace
	String.prototype.removeSpace = function() {
	    return this.replace(/\s+/g, '');
	};
	$.fn.outerHTML = function() {
        // IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
        return (!this.length) ? this : (this[0].outerHTML ||
        (function(el) {
            var div = document.createElement('div');
            div.appendChild(el.cloneNode(true));
            var contents = div.innerHTML;
            div = null;
            return contents;
        })(this[0]));
    };
    
    //添加金额格式化的支持
	$.extend({
	    formatFloat: function(src, pos){    
	        var num = parseFloat(src).toFixed(pos);    
	        num = num.toString().replace(/\$|\,/g,'');    
	        if(isNaN(num)) num = "0";    
	        sign = (num == (num = Math.abs(num)));    
	        num = Math.floor(num*100+0.50000000001);    
	        cents = num%100;    
	        num = Math.floor(num/100).toString();    
	        if(cents<10) cents = "0" + cents;    
	        for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
	        num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));    
	        return (((sign)?'':'-') + num + '.' + cents);    
	    },
	    _formatFloat :  function(src, pos){    
	        var num = parseFloat(src).toFixed(pos);    
	        num = num.toString().replace(/\$|\,/g,'');    
	        if(isNaN(num)) num = "0";    
	        sign = (num == (num = Math.abs(num)));    
	        num = Math.floor(num*100+0.50000000001);    
	        cents = num%100;    
	        num = Math.floor(num/100).toString();    
	        if(cents<10) cents = "0" + cents;    
	        for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
	        num = num.substring(0,num.length-(4*i+3))+','+num.substring(num.length-(4*i+3));    
	        return '<span class="nums">' + ((sign)?'':'-') + num + '</span><span class="cents">' + '.' + cents + '</span>';    
	    }
	});
	
	//文本框字符输入
	$.fn.inputLimit = function() {
		return this.each(function() {
			
			//初始化参数
			var _area = $(this); var _wrap = $(_area).closest('.weui_cells');
			var _info = _area.next(); var _tip = $(_info).find('span');
			var _max = $(_info).contents().filter(function(index, content) {
				 return content.nodeType === 3;
			}).text();
			_max = parseInt(_max.substr(1));
			
			//校验函数
			var _check = function(obj) {
				var _val = $(obj).val();
			    var _length = !!_val?_val.length:0;
			    if(_length > _max) {
			       !_wrap.hasClass('weui_cells_error') && _wrap.addClass('weui_cells_error');
			    } else {
			       _wrap.hasClass('weui_cells_error') && _wrap.removeClass('weui_cells_error');
			    }
			    _tip.text(_length);
			};
			
			//初始化校验
			_check(_area);
			
			//事件绑定
			_area.bind("input propertychange", function () {
				_check(this);
			});
		});
	 };
})(jQuery);

/**
 * 公共函数库，主要是一些JS工具函数，各种插件的公共设置
 * 手机端公共函数库
 * @author LiFeng
 */
var Public = Public ||{};

/**
 * 获取地址的全路径
 */
Public.getBasePath = function(url) {
	if (!!webRoot) {
		return base + '/' + webRoot + url;
	}
	return base + url;
};

/**
 * 去掉html符号
 * 只需要去掉这两个符号： <>
 */
Public.removeScript = function(text) {
	var _text = $.trim(text);
	return !!_text ? _text.replace(/<>/g,"") : _text;
};

/**
 * 延时执行 -- 返回延时对象,可以添加回调函数
 */
Public.delayPerform = function(task, delay){
	var dtd = $.Deferred();
	var tasks = function(){
		task();
		dtd.resolve();
	};
	setTimeout(tasks,delay||0);
	return dtd.promise();
};

/**
 * 弹出框 -- simple tip
 * 无遮罩层
 */
Public.toast = function(message, times){
	$('.weui_toast_container').remove();
	var item = {sn : 'ui-' + Public.generateChars(10), message: message};
	var template = '<div class="weui_toast_container {{=item.sn}}"><div class="weui-toast-bd"></div><div class="weui-toast-message">{{=item.message}}</div></div>';
	var html = Public.runTemplate(template, {item: item});
	$('body').append(html);
	var target = $('.' + item.sn);
	target.css({marginLeft: target.width()/ 2 * -1, left: '50%'});
	Public.delayPerform(function() {
		target.remove();
	}, times || 2000);
};

/**
 * 弹出框 -- 提示
 */
Public.tip = function(msg, callback, time){
	var toast = $('<div class="weui_dialog_alert weui_dialog_tip" style="display: none;"><div class="weui_mask"></div><div class="weui_dialog">弹窗内容，告知当前页面信息等</div></div>');
	toast.appendTo('body').show().find('.weui_dialog').text(msg||'已完成');
	Public.delayPerform(function() {
	   $(toast).remove(), !!callback&&(typeof(callback) === 'function')&&callback();
	}, time||2000);
};

/**
 * 弹出框 -- 加载中...
 */
Public.loading = function(msg, callback, time){
	var toast = $('<div id="toast" class="weui_loading_toast weui_toast_wrap" style="display:none;"><div class="weui_mask_transparent"></div><div class="weui_toast"><div class="weui_loading"><div class="weui_loading_leaf weui_loading_leaf_0"></div><div class="weui_loading_leaf weui_loading_leaf_1"></div><div class="weui_loading_leaf weui_loading_leaf_2"></div><div class="weui_loading_leaf weui_loading_leaf_3"></div><div class="weui_loading_leaf weui_loading_leaf_4"></div><div class="weui_loading_leaf weui_loading_leaf_5"></div><div class="weui_loading_leaf weui_loading_leaf_6"></div><div class="weui_loading_leaf weui_loading_leaf_7"></div><div class="weui_loading_leaf weui_loading_leaf_8"></div><div class="weui_loading_leaf weui_loading_leaf_9"></div><div class="weui_loading_leaf weui_loading_leaf_10"></div><div class="weui_loading_leaf weui_loading_leaf_11"></div></div><p class="weui_toast_content">数据加载中</p></div></div>');
	toast.appendTo('body').show().find('.weui_toast_content').text(msg||'数据加载中');
};

/**
 * 弹出框 -- 成功
 */
Public.success = function(msg, callback, time){
	var toast = $('<div id="toast" style="display: none;" class="weui_toast_wrap"><div class="weui_mask_transparent"></div><div class="weui_toast"><i class="weui_icon_toast"></i><p class="weui_toast_content">已完成</p></div></div>');
	toast.appendTo('body').show().find('.weui_toast_content').text(msg||'已完成');
	Public.delayPerform(function() {
		$(toast).remove(), !!callback&&(typeof(callback) === 'function')&&callback();
	}, time||2000);
};

/**
 * 弹出框 -- 失败
 */
Public.error = function(msg, callback){
	var fnc = function() {
		$('.weui_dialog_alert .dialog_ok').off(),toast.remove(),!!callback&&(typeof(callback) === 'function')&&callback();
	};
	var toast = $('<div class="weui_dialog_alert" style="display: none;"><div class="weui_mask"></div><div class="weui_dialog"><div class="weui_dialog_hd"><strong class="weui_dialog_title">操作提醒</strong></div><div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div><div class="weui_dialog_ft"><a href="javascript:;" class="weui_btn_dialog primary dialog_ok">确定</a></div></div></div>');
	toast.appendTo('body').show().find('.weui_dialog_bd').html(msg||'操作失败');
	$('.weui_dialog_alert .dialog_ok').on('click', fnc);
};

/**
 * 弹出框
 */
Public.dialog = function(title, content, btns, end){
	var defaults = {
		title  : title || '操作提醒',
		content: content || '操作失败',
		btns: [{
		  id  : 'ok',
		  name: '确定',
		  clazz : 'primary',
		  fnc: function(){}
		}]
	};
	
	var options = $.extend({}, defaults, btns);
	
	var toast = $('<div class="weui_dialog_confirm" style="display: none;"><div class="weui_mask"></div><div class="weui_dialog"><div class="weui_dialog_hd"><strong class="weui_dialog_title">操作提醒</strong></div><div class="weui_dialog_bd">弹窗内容，告知当前页面信息等</div><div class="weui_dialog_ft"></div></div></div>');
	toast.appendTo('body').show().find('.weui_dialog_title').html(options.title);
	toast.find('.weui_dialog_bd').html(options.content);
	
	var _btns = options.btns;
	var btnWrap = toast.find('.weui_dialog_ft');
	$.each(_btns, function(index, item) {
		$('<a href="javascript:;" class="weui_btn_dialog '+(item.clazz || '')+'" data-action="'+(item.id)+'">'+(item.name)+'</a>').appendTo(btnWrap);
	});
	toast.on('click', function(e) {
		var target = e.target;
		if(!!$(target).data('action')) {
			var action = $(target).data('action');
			var fnc = null;
			$.each(_btns, function(index, item) {
				fnc = (!fnc && item.id == action)?item.fnc: fnc;
			});
			if(!!fnc && typeof(fnc) === 'function') {
			   var flag = fnc();
			       flag = flag === undefined?true:!!flag;
			   flag && (toast.off(), toast.remove(), !!end&&(typeof(end) === 'function')&&end());
			} else {
			   toast.off(), toast.remove(), !!end&&(typeof(end) === 'function')&&end();
			}
		}
	});
};

/**
 * 将tip 和 dialog 结合起来
 */
Public.tipDialog = function(content) {
	var toast = $('<div class="weui_dialog_alert weui_dialog_tip weui_dialog_tip_simple" style="display: none;"><div class="weui_mask"></div><div class="weui_dialog">弹窗内容，告知当前页面信息等</div></div>');
	toast.appendTo('body').show().find('.weui_dialog').html(content||'空内容');
	toast.on('click', function(e) {
		// 点一下就销毁
		toast.off(), toast.remove();
	});
};

/**
 * 关闭弹窗 - 可以设置时间
 */
Public.close = function(time) { 
	time||0>0?(setTimeout(function() {$('.weui_toast_wrap').remove();}, time||0)):($('.weui_toast_wrap').remove());
};

/**
 * 图片查看
 */
Public.photo = function(img) {
	var fnc = function() {
		toast.off().remove();
	};
	var toast = $('<div class="weui_photo"><div class="weui_mask"></div><div class="weui_photo_ops"><a class="weui_photo_close"><i class="weui_icon_cancel"></i></a></div><div class="weui_photos"></div></div>');
	toast.appendTo('body').show().find('.weui_photos').html('<img src="'+img+'">');
	toast.on('click', '.weui_photo_close', fnc);
};

//Ajax请求，
Public.uploadAjax = function(url, formdata, callback, async) {   
	var _async = async== undefined?true:!!async;
	$.ajax({  
	   type: "POST",
	   url: url,  
	   cache: false,  
	   async: _async, 
	   contentType: false,
       processData: false,
       dataType: "json",
	   enctype: 'multipart/form-data',
	   data: formdata,  
	   success: function(data, status){  
		   callback(data);  
	   },  
	   error: function(x, s, e){
		    var msg = $.parseJSON(x.responseText).msg;
		    Public.error(msg);
	   }  
	});  
};

//Ajax请求，
//url:请求地址， params：传递的参数[{name,value}]， callback：请求成功回调  
Public.postAjax = function(url, params, callback, async, dataType){   
	var _async = async == undefined?true:!!async;
	var _dataType = dataType == undefined ? 'json':dataType;
	$.ajax({  
	   type: "POST",
	   url: url,  
	   cache: false,  
	   async: _async, 
	   dataType: _dataType,
	   data: params,  
	   //当异步请求成功时调用  
	   success: function(data, status){  
		   callback(data);  
	   },  
	   //当请求出现错误时调用  只要状态码不是200 都会执行这个
	   error: function(x, s, e){
		   var msg = !'parsererror' == s ?$.parseJSON(x.responseText).msg: 'JSON解析错误';
		   Public.toast(msg);
	   }  
	});  
};

//Ajax请求，
//url:请求地址， params：传递的参数{...}， callback：请求成功回调  
Public.getAjax = function(url, params, callback, async, dataType){    
	var _async = async== undefined?true:!!async;
	$.ajax({  
	   type: "GET",
	   url: url,  
	   cache: false,  
	   async: _async, 
	   dataType: "json",  
	   data: params,  
	   //当异步请求成功时调用  
	   success: function(data, status){  
		   callback(data);  
	   },  
	   //当请求出现错误时调用  只要状态码不是200 都会执行这个
	   error: function(x, s, e){
		   var msg = $.parseJSON(x.responseText).msg;
		   Public.error(msg);
	   }  
	});  
};

/**
 * 使用artTemplate 模版技术(js原生)
 * artTemplate 模版技术  content  可以是对象,元素id,元素,字符串,对象格式{}
 *                    context  js对象  上下文
 *                    escape   是否格式化html，默认是true
 * -- 会格式化包含的html代码（如果字符串中包含html代码，将转换，页面不能解析）
 *               
 */
Public.runTemplate = function(content, context, escape){
	var _escape = (escape != undefined)?escape:true;//默认为true
	template.config('escape',_escape);
	var _get = template.get,_r = null;
	if( !(/^[a-zA-Z-\d]+$/g).test(content) ) {//替换get实现
		template.get = function(a){
			return template.compile(content.replace(/^\s*|\s*$/g, ''), { filename: '_TEMP', cache: false, openTag: '{{', closeTag: '}}'});
		};
	}
	_r = template(content, context);
	template.get = _get;//还原
	template.config('escape',true);
	return _r;
};

/**
 * 表单的验证
 */
Public.validate = function(options){
	var defaults = {
		submitHandler: function(form){
			Public.loading('正在提交，请稍等...');
			form.submit();
		},
		errorContainer: "#messageBox",
		errorPlacement: function(error, element) {
			$("#messageBox").text("输入有误，请先更正。");
			if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
				error.appendTo(element.closest(".controls"));
			} else {
				error.insertAfter(element);
			};
		}
	};
	return $.extend({},defaults,options);
};

/**
 * 读取cookie 的值
 */
Public.getCookie = function(name) {
	return $.cookie(name);
};

/**
 * 存储cookie 的值
 * expires(天)
 */
Public.setCookie = function(name, value, path, expires) {
	$.cookie(name, value, {path: path||'/', expires: expires||7});
};

/**
 * 删除cookie 的值
 * expires(天)
 */
Public.removeCookie = function(name, path) {
	$.cookie(name, '', {path: path||'/', expires: -1});
};

/**
 * 取一个随机的字符，长度可以自定义
 */
Public.generateChars = function (length) {
    var chars = '';
    for (var i = 0; i < length; i++) {
      var randomChar = Math.floor(Math.random() * 36);
      chars += randomChar.toString(36);
    }
    return chars;
};

/**
 * 分享事件
 */
Public.intShareEvent = function() {
	var shareDiv =  '<a class="share hide" href="javascript:void(0)">';
	    shareDiv += '<img src="' + webRoot + '/static/img/share_to.png" />';
	    shareDiv += '<div class="share-bg"></div>';
	    shareDiv += '</a>';
	
	//分享点击事件
	$(document).on('click.share', '.share-btn', function() {
		if( $('.share').get(0) && $('.share').hasClass('hide') ) {
			$('.share').removeClass('hide');
		} else if( $('.share').get(0) && !$('.share').hasClass('hide') ){
			$('.share').addClass('hide');
		} else if( !$('.share').get(0) ) {
			$('body').append(shareDiv);
			$('.share').removeClass('hide');
		}
	});
	
	//点击分享
	$(document).on('click.share', '.share', function() {
		$('.share').addClass('hide');
	});
};

/**
 * 创建一个滚动实例(适合局部滚动)
 */
Public.newScroll = function(dom, options) {
	var _options = $.extend({},{ 
	     disableMouse:false,
		 disablePointer:true,
		 preventDefault:true,
		 click:true,
		 preventDefaultException : {
		   tagName: /^(INPUT|TEXTAREA|BUTTON|SELECT|LABEL)$/
		 },
		 probeType:1
	}, options||{});
	return new IScroll(dom, _options);
};

//scrollLoad 实例
Public.scrollLoader = null;

/**
 * 滑动加载
 */
Public.initScrollLoad = function(url, template, end, options) {
	var self = this;  
	var defaults = {
	    state: 0,// 状态0：可运行，1运行中，2最大数据
	    loading: 0, // 加载数据中
	    pageIndex:1, // 当前页码
	    pageSize: 15, // 每页数量
	    pageCount:-1,// 页面数
	    always_call_end: 0, // 是否每次滚动都执行call_end方法， 默认不支持
	    hasNext:function(){
			if (this.pageCount == -1 || this.pageCount >= this.pageIndex) {
				return true;
			}
			return false;
		},
		next: function(pageCount){
			this.pageCount = pageCount;
			this.pageIndex++;
		},
		showResults : function(data) {
			var _data = {data:data.data, start: ((this.pageIndex - 1) * this.pageSize)};
			var html = Public.runTemplate(template, _data);
			$('#ajax-load-page').find('.load-more').remove();
			$('#ajax-load-page').append(html);
		},
		showNoData: function() {
			var html = '<div class="weui_msg"><div class="weui_icon_area"><i class="weui_icon_msg weui_icon_warn"></i></div><div class="weui_text_area"><p class="weui_msg_desc">'+(this.noDataMessage)+'</p></div></div>';
			$('#ajax-load-page').append(html);
		},
		showNoMore: function() {
			$('#ajax-load-page').append('<div class="load-more">数据全部加载完毕</div>');
		},
		showLoadMore: function() {
			$('#ajax-load-page').append('<div class="load-more">正在加载中...</div>');
		},
		reset : function() {
			this.state = 0;
			this.pageIndex = 1;
			this.pageSize = 15;
			this.pageCount = -1;
			$('#ajax-load-page').html('');
			this.load();
		},
		load : function() {
			var _loader = this;
			_loader.begin(function() {
			   var param = (function() {
					var $form = $('.ajax-load-form');
					if(!!$form.get(0)) {
						return $form.serializeArray();
					}
					return [];
			   })();
			   param.push({'name':'param.pageIndex', 'value':(_loader.pageIndex)});
			   param.push({'name':'param.pageSize', 'value':(_loader.pageSize)});
			   param.push({'name':'param.serializePage', 'value':false});
			   Public.postAjax(url, param, function(data){
				   _loader.end(data);
			   });
			});
		},
		begin : function(fnc) {
			var _loader = this;
			
			// 可以加载数据
			if (this.state == 0 && this.hasNext()) {
				this.state = 1; this.loading = 1;
				this.showLoadMore();
				Public.delayPerform(fnc, 500);
			} 
			
			// 没有数据了
			else if(this.state == 0 && !this.hasNext() && this.pageCount > 0) {
				this.state = 1;
				this.showNoMore();
				this.always_call_end != 0 && _loader.call_end();
			} 
			
			// 没数据的例外情况
			else if (this.state == 0) {
				setTimeout(function(){
					_loader.state = 1;
					_loader.always_call_end != 0 && _loader.call_end();
				},300);
			} 
			
			// 其他情况
			else if(this.state == 1 && this.loading == 0 && this.always_call_end != 0){
				_loader.call_end();
			}
		},
		end : function(data) {
			var pageCount = data.param.pageCount;
			this.showResults(data);
		    this.next(pageCount);
		    this.state = 0; this.loading = 0;
		    this.call_end(data);
		    if(pageCount <= 0) {
		       this.showNoData();
		    }
		},
	    call_end : function(data) {
	    	if (typeof(end) === 'function') {
	    		end(this, data);
		    }
	    },
	    
		noDataMessage : '没有数据',
		
		// 开启
		enable : function(adjust) {
			var _adjust = adjust || 10;
			$(document).off('scroll').on('scroll', function() {
				var scrollTop = parseInt($(this).scrollTop());
				var scrollHeight = parseInt(document.body.scrollHeight);
				var windowHeight = parseInt($(window).outerHeight());
				if (scrollHeight - (scrollTop + windowHeight) < _adjust){
				　  Public.scrollLoader.load();
				}
			});
		},
		
		// 关闭
		disable : function() {
			$(document).off('scroll');
		}
	};
	
	//scrollLoad对象
	Public.scrollLoader = $.extend({}, defaults, options||{});
	
	//加载事件
	Public.scrollLoader.enable();
	
	//加载
	Public.scrollLoader.load();
};

/**
 * 重置加载
 */
Public.resetScrollLoad = function() {
	if (Public.scrollLoader != null) {
		Public.scrollLoader.reset();
	}
};

/**
 * POST 提交用到
 * 将表单序列化为如下形式[ { name: a value: 1 }, { name: b value: 2 },...]
 */
Public.serialize = function(form){
	return $(form || '#queryForm').serializeArray();
};

/**
 * GET 提交用到
 * 将表单序列化为如下形式a=1&b=2&c=3&d=4&e=5 
 */
Public.serializeGet = function(form){
	return $(form || '#queryForm').serialize();
};

/**
 * js 定时任务
 */
Public.setInterval = function(fnc, time, times) {
	var _times = 0;
	var timer = setInterval(function() {
		_times ++;
		try{fnc(timer, _times);}catch(e){}
		if(_times >=times) {
		   clearTimeout(timer);
		}
	}, time || 1000);
}

/**
 * 获取URL参数
 */
Public.getQueryString = function(name){
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return unescape(r[2]); return null; 
};

/**
 * 自动减小数字（倒计时）
 */
Public.countDown = function(objId, pos) { 
	if(!objId)return null;
	var obj = document.getElementById(objId);
	var times = (typeof pos.setTime == 'undefined')?'':pos.setTime;
	
	// 显示装换
	var toDouble = function(num){
		var number = num;
		if (number<10&&number>=0){
			number = '0'+number;
		}else if(number>=10){
			number = ''+number;
		}else if(number<0 && number>-10){
			number = Math.abs(number);
			number = '-0'+number;
		}
		return number;
	};
	
	// 定时展示
	var timeFn = function(){
		setTimeout(function(){
		   
		   // 减小值
		   times = times - 1;
		   
		   var second = toDouble(Math.floor(times % 60));             // 计算秒     
		   var minite = toDouble(Math.floor((times / 60) % 60));      //计算分 
		   var hour = toDouble(Math.floor((times / 3600) % 24));      //计算小时 
		   var day = toDouble(Math.floor((times / 3600) / 24));        //计算天 
		   
		   var html = (function(){
				var _html = [];
				if (day>0) {
					_html.push(day);
				}
				if (day>0 || hour >0) {
					_html.push(hour);
				}
				if (day>0 || hour >0 || minite>0) {
					_html.push(minite);
				}
				_html.push(second);
				return _html.join(':');
		   })();
		   
		   obj.innerHTML = html;
		   // 循环执行
		   if (times > 0) {
			   setTimeout(arguments.callee, 1000);
		   } else {
			   obj.innerHTML = pos.finish||'';
		   }
		},1000);
	};
	
	// 第一次执行
	timeFn();
};

/**
 * @about  默认图
 * @param 无
 * @return 无
 * @author bob
 */
Public.defaultImage = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAUAAAAFACAYAAADNkKWqAAAWcElEQVR4Xu3diW4bxxJGYSr7vjp5/wfyeyRx9n2PL35eVNBpNDeLtkjWJ4CwLJHDqVOlg+plhnePHz9+utlsNk+fPt3c3d3lW18IIIBACwJ3BNgiz4JEAIEFgX8FmN/pANUIAgh0IvCvAMmvU9rFigAC26avhsAEqCAQQKAbAUPgbhkXLwII/EtAB6gYEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAE2roGnT59u7u7utgTyfX3Vz+rn4/9vDdfI4NZiE89hAgR4mNHNPmMlwFl2syBGUV47mMRa8dyy5K89T8/z/AnwedK9gmPvEsDq5/Wza5Xg2O3W96MEx3QR4hUU7xlOkQDPAPHaDrEa7h4SYWJ86aWXto9xyDwPl/O7S5LHHNcY+z///KMDvLbiPfP5EuCZgV7D4WYBzh3dPC/48ssvb1599dXt45VXXtkK7hSJPiSTXd1q5PfXX39tfv/9983ff/+9jeeSxP2QzDq9NwF2yvYQ6zHD2JLCW2+9tXnvvfc2+fe1117bK4pzSmTXwsypKZuH7jnHyO+3337bfP/995tff/1188cff/wnrnPGcer5ev6LI0CAL471xbxTiW2XBPPHnw4p/6bre//99zcff/zx9vt0g9f4NXd41QH++OOPmx9++GH7MAd4jZm93zkT4P34XeWrx3mxEt38x5+fZ77vzTff3Mrv0aNH26fk53/++ed/uqVRpJe8qjoufGQon690gV9//fXmyy+/JMCrrOb7nTQB3o/fVb760PC3OsB0e++8887mo48+2j4ivl9++WU7bMy82errkgWYc0tMGcZnSB+5Z+gbAT558sSCyFVW8/1OmgDvx+8qX32sANMBRhSR34cffrhdMMhQMbIoAa42UheU+8yjrc5xtY3llASke03nF/Glo43cI/USYH5/jnM/5Zw892EJEODD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4nnPjwDPy/NqjnZoL2AtiOQqjMyXPXr0aDt3mJsLZCi8+rr0RYSKKRLMZXb5N1eW5DNIchfp8UsHeDWlfK8TJcB74bvOF4975lZ792obTKLL9pEIMDcPSOeU/X638FUyz3XF9bnEtaFbF3gLGT4uBgI8jtNNPetQ9zcPh+suzLl2dr4F/Woe8FIEMsc5dnXpZDP8rbva5Pvx9zrAmyr5ncEQYI88/yfKUwSY59aNSDNsrC7w0BzgJQjkkAAzp5kbLMy38b8UgTcszRceMgG+cOSX8Yar+brVau78s0sQ2zkIJq56zNcy30qM5+B068cgwFvP8CK+cQ5w1xaWUZDHbnO5pEWQQ9t8Vr+/1D2NDUv0hYVMgC8M9eW80akCPPbML0mAOec5zn2d3SnPPZaH510+AQK8/Bw9lzM8ZQh8y0PCVad7y/E+l2K64oMS4BUn73mc+qV1cc8jRsdEoAgQoFpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIECAagABBNoSIMC2qRc4AggQoBpAAIG2BAiwbeoFjgACBKgGEECgLQECbJt6gSOAAAGqAQQQaEuAANumXuAIIPA/yu9euh7u2+cAAAAASUVORK5CYII=';
Public.spaceGif = "data:image/png;base64,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";


/**
 * @about emoji表情转换
 * @param value:{string} 消息内容，必填
 * @return string   带emoji表情的消息
 * @author bob
 */
Public.emoji = function(value) {
    var qqfaceMap = {"[微笑]":"0","[撇嘴]":"1","[色]":"2","[发呆]":"3","[得意]":"4","[流泪]":"5","[害羞]":"6","[闭嘴]":"7","[睡]":"8","[大哭]":"9","[尴尬]":"10","[发怒]":"11","[调皮]":"12","[呲牙]":"13","[惊讶]":"14","[难过]":"15","酷":"16","[冷汗]":"17","[抓狂]":"18","[吐]":"19","[偷笑]":"20","[愉快]":"21","[白眼]":"22","[傲慢]":"23","[饥饿]":"24","[困]":"25","[惊恐]":"26","[流汗]":"27","[憨笑]":"28","[悠闲]":"29","[奋斗]":"30","[咒骂]":"31","[疑问]":"32","[嘘]":"33","[晕]":"34","[疯了]":"35","[衰]":"36","[骷髅]":"37","[敲打]":"38","[再见]":"39","[擦汗]":"40","[抠鼻]":"41","[鼓掌]":"42","[糗大了]":"43","[坏笑]":"44","[左哼哼]":"45","[右哼哼]":"46","[哈欠]":"47","[鄙视]":"48","[委屈]":"49","[快哭了]":"50","[阴险]":"51","[亲亲]":"52","[吓]":"53","[可怜]":"54","[菜刀]":"55","[西瓜]":"56","[啤酒]":"57","[篮球]":"58","[乒乓]":"59","[咖啡]":"60","[饭]":"61","[猪头]":"62","[玫瑰]":"63","[凋谢]":"64","[嘴唇]":"65","[爱心]":"66","[心碎]":"67","[蛋糕]":"68","[闪电]":"69","[炸弹]":"70","[刀]":"71","[足球]":"72","[瓢虫]":"73","[便便]":"74","[月亮]":"75","[太阳]":"76","[礼物]":"77","[拥抱]":"78","[强]":"79","[弱]":"80","[握手]":"81","[胜利]":"82","[抱拳]":"83","[勾引]":"84","[拳头]":"85","[差劲]":"86","[爱你]":"87","[NO]":"88","[OK]":"89","[爱情]":"90","[飞吻]":"91","[跳跳]":"92","[发抖]":"93","[怄火]":"94","[转圈]":"95","[磕头]":"96","[回头]":"97","[跳绳]":"98","[投降]":"99","[激动]":"100","[乱舞]":"101","[献吻]":"102","[左太极]":"103","[右太极]":"104"};
    var str= "\\[微笑\\]|\\[撇嘴\\]|\\[色\\]|\\[发呆\\]|\\[得意\\]|\\[流泪\\]|\\[害羞\\]|\\[闭嘴\\]|\\[睡\\]|\\[大哭\\]|\\[尴尬\\]|\\[发怒\\]|\\[调皮\\]|\\[呲牙\\]|\\[惊讶\\]|\\[难过\\]|\\[酷\\]|\\[冷汗\\]|\\[抓狂\\]|\\[吐\\]|\\[偷笑\\]|\\[愉快\\]|\\[白眼\\]|\\[傲慢\\]|\\[饥饿\\]|\\[困\\]|\\[惊恐\\]|\\[流汗\\]|\\[憨笑\\]|\\[悠闲\\]|\\[奋斗\\]|\\[咒骂\\]|\\[疑问\\]|\\[嘘\\]|\\[晕\\]|\\[疯了\\]|\\[衰\\]|\\[骷髅\\]|\\[敲打\\]|\\[再见\\]|\\[擦汗\\]|\\[抠鼻\\]|\\[鼓掌\\]|\\[糗大了\\]|\\[坏笑\\]|\\[左哼哼\\]|\\[右哼哼\\]|\\[哈欠\\]|\\[鄙视\\]|\\[委屈\\]|\\[快哭了\\]|\\[阴险\\]|\\[亲亲\\]|\\[吓\\]|\\[可怜\\]|\\[菜刀\\]|\\[西瓜\\]|\\[啤酒\\]|\\[篮球\\]|\\[乒乓\\]|\\[咖啡\\]|\\[饭\\]|\\[猪头\\]|\\[玫瑰\\]|\\[凋谢\\]|\\[嘴唇\\]|\\[爱心\\]|\\[心碎\\]|\\[蛋糕\\]|\\[闪电\\]|\\[炸弹\\]|\\[刀\\]|\\[足球\\]|\\[瓢虫\\]|\\[便便\\]|\\[月亮\\]|\\[太阳\\]|\\[礼物\\]|\\[拥抱\\]|\\[强\\]|\\[弱\\]|\\[握手\\]|\\[胜利\\]|\\[抱拳\\]|\\[勾引\\]|\\[拳头\\]|\\[差劲\\]|\\[爱你\\]|\\[NO\\]|\\[OK\\]|\\[爱情\\]|\\[飞吻\\]|\\[跳跳\\]|\\[发抖\\]|\\[怄火\\]|\\[转圈\\]|\\[磕头\\]|\\[回头\\]|\\[跳绳\\]|\\[投降\\]|\\[激动\\]|\\[乱舞\\]|\\[献吻\\]|\\[左太极\\]|\\[右太极\\]";
    var reg = new RegExp(str,'g');
    var result = value.replace(reg,function(r){
        return '<img class="qqemoji qqemoji'+qqfaceMap[r]+'" src="'+Public.spaceGif+'">';
    });
    return result;
};

/**
 * @about 解决ios6、7 canvas2M以上图片压扁问题
 * @param
 * @return string   无
 * @author bob
 */
Public.drawImageIOSFix = function (ctx, img, sx, sy, sw, sh, dx, dy, dw, dh) {
    var vertSquashRatio = Public.detectVerticalSquash(img);
    ctx.drawImage(img, sx * vertSquashRatio, sy * vertSquashRatio,
        sw * vertSquashRatio, sh * vertSquashRatio,
        dx, dy, dw, dh);
}
Public.detectVerticalSquash = function (img) {
    var iw = img.naturalWidth, ih = img.naturalHeight;
    var canvas = document.createElement('canvas');
    canvas.width = 1;
    canvas.height = ih;
    var ctx = canvas.getContext('2d');
    ctx.drawImage(img, 0, 0);
    var data = ctx.getImageData(0, 0, 1, ih).data;
    var sy = 0;
    var ey = ih;
    var py = ih;
    while (py > sy) {
        var alpha = data[(py - 1) * 4 + 3];
        if (alpha === 0) {
            ey = py;
        } else {
            sy = py;
        }
        py = (ey + sy) >> 1;
    }
    var ratio = (py / ih);
    return (ratio===0)?1:ratio;
}

/**
 * @about 图片file文件转base64
 * @param
 *      canvasId:canvas的id,
 *      file:file文件,
 *      callback:回调函数
 *      params:{"width":320,"cut":0.1,"imgtype":'png'}  生成的图片尺寸、质量、格式   选填
 * @return string   无
 * @author bob
 */
Public.imgToBase64 = function(file, callback, params) {
    var prms = params || {"width":500,"cut":0.1,"imgtype":'png'};
    var tmpImage = new Image(), base64Str = "";
    var URL = URL || webkitURL;
    tmpImage.src = URL.createObjectURL(file);
    tmpImage.onload = function() {
    	var canvas = document.createElement('canvas');
    	    context = canvas.getContext("2d");
        var width = tmpImage.width, height = tmpImage.height;
        var scale = width / height;
        var width1 = prms.width?prms.width : 320;
        var height1 = parseInt(width1 / scale);
        canvas.width = width1;
        canvas.height = height1;
        if (navigator.userAgent.match(/iphone/i)) {
        	Public.drawImageIOSFix(context, tmpImage, 0, 0, width, height, 0, 0, width1, height1);
        } else {
        	context.drawImage(tmpImage, 0, 0, width1, height1);
        }
        var realCut = prms.cut?prms.cut :  0.1;
        base64Str = canvas.toDataURL("image/"+prms.type, realCut);
        callback(base64Str);
    };
};

/**
 * 用户相关数据
 */
var User = User || {};

/**
 * 添加到购物车
 * 添加的GoodsID(没有规格，则两个ID是一样的)
 */
User.addGoodsCart = function(goodsId, fnc) {
	Public.postAjax(webRoot + '/f/shop/cart/add', {productId: goodsId}, function() {
		typeof(fnc) === 'function' && fnc();
	});
};

/**
 * 用户购物车数量
 */
User.cartQuantity = function(fnc) {
   Public.postAjax(webRoot + '/f/shop/cart/quantity', {}, function(data) { 
	   var quantity = data.obj;
	   $('.cart-tag b').text(quantity);
	   
	   // 执行回调
	   !!fnc && typeof(fnc) === 'function' && fnc(quantity);
   });
};

/**
 * 用户未读消息
 */
User.unreadMsg = function(fnc) {
   Public.postAjax(webRoot + '/f/member/message/unread_count', {}, function(data) { 
	   var quantity = data.obj;
	   if (quantity > 0) {
		   $('.msg-tag b').css({'display':'inline-block'}).text(quantity);
	   }
   });
};

/**
 * 用户订单状态数量
 */
User.countOrderState = function(fnc) {
   Public.postAjax(webRoot + '/f/member/shop/order/count_order_state', {}, function(data) {
	   if (data.success) {
		   var quantitys = data.obj;
		   var unpay = quantitys.unpay;
		   var unshipped = quantitys.unshipped;
		   var unreceipted = quantitys.unreceipted;
		   var usableCount = quantitys.usableCount;
		   if (unpay > 0) {
			   $('.unpay b').show().text(unpay);
		   }
		   if (unshipped > 0) {
			   $('.unshipped b').show().text(unshipped);
		   }
		   if (unreceipted > 0) {
			   $('.unreceipted b').show().text(unreceipted);
		   }
		   //可用优惠券
		   if (usableCount > 0) {
			   $('.coupon-tag b').css({'display':'inline-block'}).text(usableCount);
		   }
		   
		   // 我的中设置总数
		   var total = unpay + unshipped + unreceipted;
		   if (total > 0) {
			   $('.member-tag b').css({'display':'inline-block'}).text(total);
		   }
	   }
   });
};

/**
 * 用户待评价订单
 */
User.appraiseState = function(fnc) {
   Public.postAjax(webRoot + '/f/member/order/appraise/count_appraise_state', {}, function(data) { 
	   var quantitys = data.obj;
	   var unappraise = quantitys.unappraise;
	   var unrappraise = quantitys.unrappraise;
	   var appraised = quantitys.appraised;
	   if (unappraise > 0) {
		   $('.appraise-tag b').css({'display':'inline-block'}).text(unappraise);
		   $('.appraise-tag .-num').text(unappraise);
	   }
	   if (unrappraise > 0) {
		   $('.rappraise-tag .-num').text(unrappraise);
	   }
	   if (appraised > 0) {
		   $('.appraised-tag .-num').text(appraised);
	   }
   });
};

/**
 * 用户没有头像
 */
User.notHeadimg = function() {
	var img = event.srcElement;
	$(img).attr('src', webRoot + '/static/img/default_user.jpg');
};

/**
 * 用户等级
 */
User.userRank = function() {
	Public.postAjax(webRoot + '/f/member/rank/get', {}, function(data) { 
		var rank = data.obj;
		if (rank) {
			$('.rank_jf .-num').text(rank.points);
			var state = '永久有效';
			if (rank.useAble == 1) {
				state = rank.expiryDate;
			} else if(rank.useAble == 0) {
				state = '已失效';
			}
			$('.rank_st .-num').text(state);
			if (!!rank.rankImage) {
				$('.rank_im img').attr('src', rank.rankImage);
			}
		}
    });
};

/**
 * 统计
 */
var Statistics = Statistics || {};

/**
 * 统计此页面的访问次数
 */
Statistics.pageStatistics = function(page) {
	Public.postAjax(webRoot + '/f/statistics/' + page + '?_t=' + Math.random(), {}, function(){
	}, true, 'text');
};


/**
 * 初始化方法(公共的方法)
 */
$(function() {
	// 输入框限制
	$('.weui_textarea').inputLimit();
	
	// 金额格式化
	$('[data-money]').each(function() {
		$(this).html('￥' + $.formatFloat($(this).data('money'), 2));
		$(this).removeAttr('data-money');
	});
});