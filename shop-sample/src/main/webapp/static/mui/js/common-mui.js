/**
 * 工具类
 */
var Public = Public || {}; 

//异步提交时如果提交错误,判断是否是没有用户
Public.isUserLogin = function(data){
	if(!!data && (data.code == 40005 || (!!data.obj && data.obj.code == 40005))) {
	   Public.loaded();
	   User.loginDialog();
	   return false;
	}
	return true;
};

//Ajax请求，
//url:请求地址， params：传递的参数[{name,value}]， callback：请求成功回调  
Public.postAjax = function(url, params, callback, async){    
	var _async = async== undefined?true:!!async;
	$.ajax({  
	   type: "POST",
	   url: url,  
	   cache: false,  
	   async: _async,
	   dataType: "json",  
	   data: params,  
	   //当异步请求成功时调用  
	   success: function(data, status){  
		   var r = Public.isUserLogin(data);
		   if(r) {
			   callback(data);   
		   }
	   },  
	   //当请求出现错误时调用  只要状态码不是200 都会执行这个
	   error: function(x, s, e){
		    if(!!x.responseText) {
		    	var msg = $.parseJSON(x.responseText).msg;
				Public.openWindow(msg,"系统错误",800,500,{
					buttons:{"关闭":true},
					submit:function(v, h, f){
					}, loaded:function(h){
			        }, closed:function(){ 
			        }
			    });
		    }
	   }  
	});  
};
//Ajax请求，
//url:请求地址， params：传递的参数{...}， callback：请求成功回调  
Public.getAjax = function(url, params, callback, async){  
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
		   var r = Public.isUserLogin(data);
		   if(r) {
			   callback(data);   
		   }
	   },  
	   //当请求出现错误时调用  只要状态码不是200 都会执行这个
	   error: function(x, s, e){
		   if(!!x.responseText) {
		    	var msg = $.parseJSON(x.responseText).msg;
				Public.openWindow(msg,"系统错误",800,500,{
					buttons:{"关闭":true},
					submit:function(v, h, f){
					}, loaded:function(h){
			        }, closed:function(){ 
			        }
			    });
		   }
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