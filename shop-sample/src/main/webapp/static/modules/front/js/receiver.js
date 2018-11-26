$(function() {
	
	// 初始化验证器
	var phone = $('#verifiedPhone').val();
	
	// 如果有手机号，则默认是验证通过的
	Verifition._init(phone, validator.methods.mobile(phone));
	
	//如果为更改且标签不是父母，朋友则显示出验证码
	(function(){
		if(!!$("#verifiedPhone").val() && !Verifition.isNotVerified()){
			$(".verification_code").removeClass("hide");
		}
	})();
	
	// 验证必填项
	function isRequired(){
		var count = 0;
		$(".required").each(function(){
			if($(this).val() == ""){
				count ++
			}
		});
		return count;
	};
	
	// 保存收货信息
	$(document).on('click', '.savereceiver', function() {
		
		// 必填项
		if (isRequired()){
			Public.error("请补充完整必填信息！");
			return ;
		}
		
		// 手机号码验证
		if (!Verifition.isVerified()) {
			$("#isVerifiedPhone").val("0");//验证不通过置为0
			//如果标签是朋友或者父母无需验证
			if (!Verifition.isNotVerified()){
				Public.error("手机号码验证不通过！");
				return ;
			}
		}
		
		var postData = Public.serialize('#inputForm');
		Public.postAjax(webRoot + '/f/member/receiver/save', postData, function(data) {
			if (data.success) {
				Public.success('已完成', function() {
					var id = data.obj.id;
					window.history.go(-1);
				});
			} else {
				Public.tip(data.msg);
			}
		});
	});
	
	// 发送验证码
	$(document).on('click', '.sendVerification', function() {
		var phone = $("#phone").val();
		
		if (!validator.methods.mobile(phone)){
			Public.error("请输入手机号");
			return;
		}
		
		// 发送验证码
		Verifition.send(phone);
	});
	
	// 验证验证码
	$(document).on('input', '#verificationCode', function() {
		var phone = $("#phone").val(); var code = $("#verificationCode").val();
		if (validator.methods.mobile(phone) && validator.methods.digits(code)
				&& validator.methods.minlength(code, 4)) {
			// 验证验证码
			Verifition.verify(phone, code);
		}
	});
	
	// 打开定位页面
	$(document).on('click', '.to-address', function() {
		//$('.receiver-container-wrap').hide();
		//$('.address-container-wrap').show();
		$('.actionSheet-wrap').show();
		Address.doLocation();
	});
	
	// 显示验证码
	$(document).on('change', '#tag', function() {
		if(Verifition.isNotVerified() || Verifition.isVerifiedPhone()){
			$(".verification_code").addClass("hide");
		} else {
			$(".verification_code").removeClass("hide");
		}
	});
	
});

// 验证对象
var Verifition = {
		
	// 是否验证过
    isVerified : function() {
    	var that = this;
    	var verifiedPhone = $('#verifiedPhone').val();
    	var phone = $('#phone').val();
    	// 新增时必须满足这个条件(如果修改了号码，则满足这一个)
    	if (that.result == true && that.phone == phone) {
    		return true;
    	} 
    	
    	// 修改时必须满足这个条件
    	else if(!!verifiedPhone && verifiedPhone == phone) {
    		return true;
    	}
    	
    	// 如果是已经验证通过的手机号则无需再次验证
    	else if(that.isVerifiedPhone()) {
    		return true;
    	}
    	// 其他情况都是不满足的
    	return false;
    },
    
    //朋友、父母标签的不需要验证
    isNotVerified : function(){
    	var tag = $("#tag").val();
    	if(tag=="朋友" || tag=="父母"){
    		return true;
    	}
    	return false;
    },
    
    //判断手机号是否为已存在且经验证通过的
    isVerifiedPhone : function(){
    	var that = this;
    	var phone = $("#phone").val();
    	var flag = false;
    	Public.postAjax(webRoot + "/f/member/receiver/code/isVerifiedPhone",{"phone":phone},function(data){
    		if(data.success){
    			$("#isVerifiedPhone").val("1");
    			flag = true;
    		}else{
    			flag = false;
    			$("#isVerifiedPhone").val("0");
    		}
    	},false);
    	return flag;
    },
	
    // 初始化验证器
	_init: function(phone, result) {
		this.phone = phone;
		this.result = result;
		this.enable = true;
	},
	
	// 重置结果
	_reset : function(phone) {
		this.phone = phone;
		this.result = false;
		$('.verify-result').removeClass('success').html('');
		$("#verificationCode").val('')
	},
	
	// 验证成功
	_success : function(phone) {
		this.phone = phone;
		this.result = true;
		$('.verify-result').addClass('success').html('验证成功');
	},
	
	// 验证失败
	_fail : function(phone, msg) {
		this.phone = phone;
		this.result = false;
		$('.verify-result').removeClass('success').html(msg);
	},
	
	// 发送验证码
	send : function(phone) {
		var that = this;
		if (that.enable) {
			that._disabled();
			Public.postAjax(webRoot + '/f/member/receiver/code/send', {'phone': phone}, function(data) {
				if (data.success) {
					that._reset(phone);
					Public.toast('已发送，请在10分钟之内验证')
				} else {
					Public.error(data.msg);
				}
			});
		}
	},
	
	// 不可用状态
	_disabled : function() {
		var that = this;
		    that.enable = false;
		// 次数
		var times = 60;
		
		// 时钟
		var clock = function() {
			if (times > 0) {
				$("#sendVerification").addClass('disabled').html("重发(" + (times --) + ")"); 
				setTimeout(arguments.callee, 1000);
			} else {
				that._enabled();
			}
		};
		
		// 执行时钟
		clock();
	},
	
	// 可用状态
	_enabled : function() {
		var that = this; that.enable = true;
		$("#sendVerification").removeClass('disabled').html("发送验证码"); 
	},
	
	// 验证验证码
	verify : function(phone, code) {
		var that = this;
		var postData = {'phone' : phone, 'code' : code}
		setTimeout(function() {
			Public.postAjax(webRoot + '/f/member/receiver/code/verify', postData, function(data) {
				if (data.success) {
					that._success(phone);
					$("#isVerifiedPhone").val("1");//验证通过的手机号
				} else {
					that._fail(phone, data.msg);
					$("#isVerifiedPhone").val("0");//验证不通过的手机号
				}
			}, false);
		}, 100);
	}
};

/**
 * 设置地址接口
 */
var setAddress = function(address, location) {
	$('#location').val(location);
	$('#address').val(address);
	
	// 返回
	//$('.receiver-container-wrap').show();
	//$('.address-container-wrap').hide();
	$('.actionSheet-wrap').hide();
};