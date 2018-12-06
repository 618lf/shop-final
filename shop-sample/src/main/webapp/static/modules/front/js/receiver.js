$(function() {
	
	// 初始化验证器
	var phone = $('#verifiedPhone').val();
	
	// 如果有手机号，则默认是验证通过的
	Verifition._init(phone, validator.methods.mobile(phone));
	
	//如果为更改且标签不是父母，朋友则显示出验证码
	(function(){
		if (!!$("#verifiedPhone").val() && !Verifition.isNotVerified()){
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
			return;
		}
		
		// 手机号码验证 -- 如果设置了不需要验证的情况
		if (!Verifition.isVerified() && !Verifition.isNotVerified()) {
			Public.error("手机号码验证不通过！");
			return;
		}
		
		var postData = Public.serialize('#inputForm');
		Public.postAjax(ctxFront + '/member/receiver/save.json', postData, function(data) {
			if (data.success) {
				Public.success('已完成', function() {
					var id = data.obj.id;
					if(!!_to) {
						window.location.href = ctxFront + '/member/receiver/select.html?to=' + _to;
					} else {
						window.location.href = ctxFront + '/member/receiver/list.html';
					}
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
	
	// 关闭选择
	$(document).on('click', '.weui_mask', function() {
		$('.actionSheet-wrap').hide();
	});
	
	// 打开定位页面
	$(document).on('click', '.to-city_picker', function() {
		$('.actionSheet-wrap').hide();
		$('#city_picker').show();
		CITY.doLocation();
	});
	
	// 打开定位页面
	$(document).on('click', '.to-street_picker', function() {
		$('.actionSheet-wrap').hide();
		$('#street_picker').show();
		STREET.doLocation();
	});
	
	// 显示验证码
	$(document).on('change', '#tag', function() {
		if (Verifition.isNotVerified()){
			$(".verification_code").addClass("hide");
		} else {
			$(".verification_code").removeClass("hide");
		}
	});
	
	// 默认区域支持（最好能自动识别用户区域）
	if (!$('#areaId').val()) {
		$('#areaId').val('42/420100000000');
		$('#areaName').val('湖北省武汉市');
	}
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
    	
    	// 其他情况都是不满足的
    	return false;
    },
    
    // 朋友、父母标签的不需要验证(现在都不需要验证)
    isNotVerified : function(){
    	return true;
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
			Public.postAjax(ctxFront + '/member/receiver/code/send.json', {'phone': phone}, function(data) {
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
			Public.postAjax(ctxFront + '/member/receiver/code/verify.json', postData, function(data) {
				if (data.success) {
					that._success(phone);
				} else {
					that._fail(phone, data.msg);
				}
			}, false);
		}, 100);
	}
};

/**
 * 设置地址接口
 */
var setAddress = function(address, location) {
//	$('#location').val(location);
//	$('#address').val(address);
//	
//	// 返回
//	//$('.receiver-container-wrap').show();
//	//$('.address-container-wrap').hide();
//	$('.actionSheet-wrap').hide();
};

// 设置城市
var setCity = function(cityId, cityName) {
	$('#areaId').val(cityId);
	$('#areaName').val(cityName);
	$('#address').val('');
	$('.actionSheet-wrap').hide();
};

//设置街道
var setStreet = function(streetId, streetName) {
	$('#areaId').val(streetId);
	$('#address').val(streetName);
	$('.actionSheet-wrap').hide();
};