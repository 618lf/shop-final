/**
 * 用户相关的操作
 */
var Member = {
	
	// 初始化
	init : function() {
		this.editHeadImg();
		this.save();
	},
	
	// 校验
	check : function() {
		var name = $('#name').val(); var pattern = new RegExp("[<>]"); 
		if(!(validator.methods.required(name) && validator.methods.maxlength(name, 16) && !validator.methods.regexp(name,pattern))) {
			Public.tip('昵称不能有特殊字符，不能为空，且不能超过16字');
			return false;
		}
		return true;
	},
	
	// 获得数据
	getPostData : function() {
		return Public.serialize('#inputForm');
	},
	
	// 保存监听
	save : function() {
		var that = this;
		$(document).on('click','.edit_save', function() {
			if (that.check()) {
			    Public.loading();
			    var postData = that.getPostData();
			    Public.postAjax('/f/member/save', postData, function() {
				   Public.close();
				   Public.success();
			    });
			}
		});
	},
	
	editHeadImg : function() {
		//不支持
		if (typeof FileReader === 'undefined'){ 
		    Public.toast('浏览器不支持');
		    return;
		}
		
		var that = this;
		var $file = $('#file_input');
		var $fileEdit = $('#file_editor');
		
		//预览头像
		$file.get(0).addEventListener('change', function() {
			Public.loading('图片预览中');
			var file = this.files[0];
			if(!/image\/\w+/.test(file.type)){ 
		        Public.error('只能选择图片');
		        $file.val('');
		        Public.close();
		        return false; 
		    }
			var reader = new FileReader();
			reader.readAsDataURL(file);
			reader.onload = function(e){ 
			    $fileEdit.html('<img src="'+this.result+'" alt=""/>');
			    
			    //显示
				$('body').addClass('edit_img_mode');
				
				//裁剪
				that.cropperHeadImgWrap();
				Public.close();
		    };
		}, false);
		
		//弹出图片选择
		$(document).on('click', '#changeImage', function() {
			$file.click();
		});
	},
	
	//判断是否微信手机端
	cropperHeadImgWrap : function() {
		this.noCropperHeadImg();
	},
	
	// 不支持裁剪
	noCropperHeadImg : function() {
		
		// 退回方法
	    var backFun = function(img) {
	    	  $('#file_input').val('');
	    	  $('.edit_main').addClass('enter');
	    	  $('.edit_img').addClass('leave');
	    	  $('body').removeClass('edit_img_mode');
	    	  Public.delayPerform(function() {
	    		  $('.edit_main').removeClass('enter');
		    	  $('.edit_img').removeClass('leave');
		    	  
		    	  !!img && $('#changeImage').find('img').attr('src', img + "?_t=" + Math.random());
  		      }, 500);
	    };
	    
	    var resizeMe = function() {
	    	var image = document.querySelector('#file_editor > img');
	    	var canvas = document.createElement('canvas');
	    	var width = image.width;
	        var height = image.height;
	        
	        if (width > height) {
	            if (width > 320) {
	                height = Math.round(height *= 320 / width);
	                width = 320;
	            }
	        } else {
	            if (height > 320) {
	                width = Math.round(width *= 320 / height);
	                height = 320;
	            }
	        }
	        canvas.width = width;
	        canvas.height = height;
	        var ctx = canvas.getContext("2d");
	            ctx.drawImage(image, 0, 0, width, height);
            
	        return canvas.toDataURL("image/png", 0.9);
	    };
	    
	    //保存图片
	    $('#file_btn').off().on('click', function() {
	    	
	    	//先弹出上传组件
	    	Public.loading();
	    	
	    	//上传图片
	    	var imageData = resizeMe();
    	    var _data = imageData.split(',')[1];   
    	        _data = window.atob(_data);
    	    var ia = new Uint8Array(_data.length);   
    	    for (var i = 0; i < _data.length; i++) {   
                ia[i] = _data.charCodeAt(i);   
            };  
    	    
            var blob = new Blob([ia], {   
                type: "image/png"  
            });
            
            var fd = new FormData();
                fd.append('myFile', blob);
            
            //实际的上传服务。
            Public.uploadAjax('/f/member/save/headimg', fd, function(data) {
            	Public.close();
            	if (data.success) {
    				backFun(data.obj);
    				Public.success();
            	} else {
            		Public.error('图片错误！');
            	}
			});
	    });
	    
	    //退回
	    $('#back_btn').off().on('click', function() {
	    	backFun(false);
	    });
	}
};

/**
 * 初始化页面 
 */
$(function() {
	Member.init();
});