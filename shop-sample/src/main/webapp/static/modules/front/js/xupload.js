/**
 * 上传文件的支持
 */
(function($) {
	
	'use strict';
	
	var NAMESPACE = 'xupload';
    var EVENT_CHANGE = 'change.' + NAMESPACE;
	
	/**
	 * 上传组件支持
	 */
	var Xupload = function($element, options) {
		if ($element.data('x_upload') != null ) {
		    return $element.data('x_upload');
		}
		this.options = $.extend({}, Xupload.DEFAULTS, options);
		this.$element = $element;
		this.init();
	};
	
	Xupload.prototype = {
		
		constructor: Xupload,
		
		init : function() {
			this.bind();
		},
		
		// 监听
		bind : function() {
		    var that = this;
		    this.$element.on(EVENT_CHANGE, function() {
			    var ele = this;
			    
			    // 没选择文件则不需要处理
			    if (!ele.files){return;}
			    
			    // 可以多选
			    var src, url = window.URL || window.webkitURL || window.mozURL, files = ele.files;
			    for(var i = 0, len = files.length; i < len; ++i) {
			    	var file = files[i];
			    	
			    	// 文件类型不支持
				    if (!that.test(file.type)) {
				    	this.log('请选择图片');
				    	continue;
				    }
				    
				    if (url) {
	                    src = url.createObjectURL(file);
	                } else {
	                    src = ele.result;
	                }
				    
				    // 构建数据
				    var fileData = {
				    	id : 'file_'+Public.generateChars(10),
				        file : file,
				        data : '',
				        url : '',
				        error : '',
				        src : src
				    };
				    
				    // 开始
				    that.doStart(fileData);
			    }
		    });
		},
		
		// 开始
		doStart : function(fileData) {
			var that = this;
			var flag = this.options.start(fileData);
			if (!flag) {
				return;
			}
			
			// 得到数据(只能异步)
		    Public.imgToBase64(fileData.file, function(data) {
		    	var image_base64 = data.replace('data:image/png;base64,',''); 
		    	fileData.data = image_base64;
		    	that.doUpload(fileData);
		    });
		},
		
		// 执行上传(这个地方有bug，应该传递压缩后的大小 -- 已修正)
		doUpload : function(fileData) {
			var that = this; var file = fileData.file;
			Public.postAjax(this.options.uploadUrl + Xupload.interfaces.check, {
				"size": fileData.data.length,
				"type": file.type
			}, function(res) {
				if (res.success) {
					that._doUpload(fileData);
				} else {
					Public.error(res.msg);
					that.options.error(fileData);
					that.$element.data('upld', '');
				}
			})
		},
		
		// 真实的上传
		_doUpload : function(fileData) {
			var that = this;
			
			// 正在上传
			that.$element.data('upld', '1');
			var fmData = {
				content: encodeURIComponent(fileData.data)
			};
			
			// 上传
			Public.postAjax(this.options.uploadUrl + Xupload.interfaces.upload, fmData, function(data) {
				if (data.success){
					fileData.url = data.obj;
					that.options.success(fileData);
					that.log('上传成功');
				} else {
					fileData.error = data.msg;
					that.options.error(fileData);
					that.log('上传失败');
				}
				that.$element.data('upld', '');
			});
		},
		
		// 测试有效性
		test : function(fileType) {
			if (fileType == 'image/jpg' || fileType =='image/jpeg' 
				|| fileType == 'image/png' || fileType == 'image/gif') {
				return true;
			}
			return false;
		},
		
		// 弹出提示
		log : function(msg, error) {
			Public.toast(msg)
		}
	}
	
	// 默认的配置
	Xupload.DEFAULTS = {
		id:null,
		start:function(){},//开始上传
		success:function(){},//上传成功
		error:function(){},//上传失败
		uploadUrl: webRoot + "/f/member/attachment",
		base64:'',
		max : 5
	};
	
	// 默认的接口
	Xupload.interfaces = {
		check :  "/check",
		upload: "/image/upload"
	};
	
	Xupload.setDefaults = function (options) {
        $.extend(Xupload.DEFAULTS, options);
    };

    Xupload.other = $.fn.xupload;
    
    $.fn.xupload = function (option) {
        var args = [].slice.call(arguments, 1);
        return this.each(function () {
            var $this = $(this);
            var data = $this.data(NAMESPACE);
            var options;
            var fn;
            if (!data) {
                if (/destroy/.test(option)) {
                    return;
                }
                options = $.extend({}, $this.data(), $.isPlainObject(option) && option);
                $this.data(NAMESPACE, (data = new Xupload($(this), options)));
            }
            // 无相关事件
            //if (typeof option === 'string' && $.isFunction(fn = data[option])) {
            //    fn.apply(data, args);
            //}
        });
    };
    $.fn.xupload.Constructor = Xupload;
    $.fn.xupload.setDefaults = Xupload.setDefaults;
    $.fn.xupload.noConflict = function () {
        $.fn.xupload = Xupload.other;
        return this;
    };
    $(function () {
        $('[data-toggle="x-upload"]').xupload();
    });
})(jQuery);