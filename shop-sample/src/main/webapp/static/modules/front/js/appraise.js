/**
 * 评价操作
 */
var Appraise = {
		
	/**
	 * 列表页面
	 */
	initPage : function() {
		//加载数据
		Public.initScrollLoad(ctxFront + '/member/order/appraise/list/data.json', $('#ordersTemplate').text(), function() {
			
			// 定时
			$('[data-remain-time]').each(function(){
			   var id = $(this).attr('id');
			   var time = $(this).data('remain-time');
			   Public.countDown(id, {'setTime':time, finish:'订单已过期'});
			   
			   // 防止下次有触发
			   $(this).removeAttr('data-remain-time');
			});
			
			// 金额格式化
			$('[data-money]').each(function() {
				$(this).html('￥' + $.formatFloat($(this).data('money'), 2));
				$(this).removeAttr('data-money');
			});
			
		});
	},
	
	/**
	 * 评价页面
	 */
	initAppraise : function() {
		
		// 初始化评分
		$('[data-raty]').each(function(n, e) {
			var that = $(e);
			that.raty({
			  score: function() {
			    return $(that).attr('data-raty');
			  },
			  path: ctxStatic + '/raty/images_big',
			  readOnly : (function(){
				  var ro = $(that).data('readonly');
				  return !!ro;
			  })(),
			  hints : (function(){
				  var hints = $(that).data('hints');
				  if (!!hints) {
					  return hints.split(',');
				  }
				  return [];
			  })(),
			  target : (function(){
				  var hints = $(that).data('hints');
				  if (!!hints) {
					 return '.product-grade_hint';
				  }
				  return '';
			  })()
			});
		});
		
		// 获取提交的数据
		var getPostData = function() {
			var appraise = [];
			$('.product-appraise').each(function() {
				var that = $(this);
				var id = that.data('id');
				var gG = that.find('.product-grade').raty('score');
				var pG = that.find('.package-grade').raty('score');
				var dG = that.find('.delivery-grade').raty('score');
				var content = that.find('.weui_textarea').val();
				var images = (function(){
					var _images = [];
					that.find('.weui_uploader_file').each(function(n, m) {
					    var image = $(m).data('img');
					    if (!!image) {
					    	_images.push(image);
					    }
					});
					return _images.join(',');
				})();
				appraise.push({
					orderId : orderId,
					productId : id,
					productGrade : gG,
					packageGrade : pG,
					deliveryGrade : dG,
					content : content,
					images : images
				});
			});
			return appraise;
		};
		
		// 校验
		var check = function(postData) {
			var ok = false;
			$.each(postData, function(n, e) {
				if (!!e.content && e.content.length <= 500) {
					ok = true;
				}
			});
			
			// 错误提醒
			if (!ok) {
				Public.toast('请填写评论内容，且不超过500字');
			}
			return ok;
		};
		
		// 添加事件
		$(document).on('click', '.save_appraise', function() {
			// 判断图片是否成功
			var flag = true;
			$('.weui_uploader_file').each(function() {
				if ($(this).hasClass('weui-uploader_file_status')) {
					flag = false;
				}
			});
			if (!flag) {
				Public.toast('图片上传中，请稍等');
				return;
			}
			var postData = getPostData();
			if (check(postData)) {
				Public.loading('提交中...');
				var url = ctxFront + '/member/order/appraise/save.json';
				Public.postAjax(url, {
					orderId : orderId,
					postData : JSON.stringify(postData)
				}, function(data) {
					if (data.success) {
						Public.close();
						Public.success('评论成功!', function() {
							// window.location.href = ctxFront + '/member/order/appraise/view/' + orderId + '.html';
							// 跳转到评价中心首页
							window.location.href = ctxFront + '/member/order/appraise/list.html';
						});
					} else {
						Public.toast(data.msg);
					}
				});
			}
		});
		
		// 上传图片
		$('.weui_uploader_input').each(function(n, e) {
			var $ele = $(this); var max = 3; 
			var $files = $ele.closest('.weui_uploader').find('.weui_uploader_files');
			var $file = $ele.closest('.weui_uploader').find('.weui_uploader_input_wrp');
			$ele.xupload({
				start : function(file) {
				   if ($files.find('.weui_uploader_file').length >= max) {
					   $file.hide();
					   return false
				   }
				   var url = file.src;
				   var $li = $('<li class="weui_uploader_file weui-uploader_file_status" id="'+(file.id)+'" style="background-image:url('+ url +')"></li>');
				   $li.html('<div class="weui-uploader_file-content">上传中</div>').appendTo($files);
				   if ($files.find('.weui_uploader_file').length >= max) {
					   $file.hide();
				   }
				   return true;
				},
				success : function(file) {
				   var url = file.url;
				   var $li = $('#' + file.id);
				   $li.data('img', url).removeClass('weui-uploader_file_status');
				}, 
				error : function(file) {
					$('#' + file.id).remove();
				}
			})
		});
		var $file = null;
		// 预览图片
		$(document).on('click', '.weui_uploader_file', function() {
			var $gallery = $("#gallery"), $galleryImg = $("#galleryImg");
			$galleryImg.attr("style", this.getAttribute("style"));
            $gallery.fadeIn(100);
            $file = $(this);
		});
		
		// 预览图片
		$(document).on('click', '#gallery', function() {
			$("#gallery").fadeOut(100);
		});
		
		// 删除图片
		$(document).on('click', '.weui-icon_gallery-delete', function() {
			if (!!$file) {
				$file.remove();
			}
		});
	},
	
	/**
	 * 初始化追评
	 */
	initRappraise : function() {
		// 初始化评分
		$('[data-raty]').each(function(n, e) {
			var that = $(e);
			that.raty({
			  score: function() {
			    return $(that).attr('data-raty');
			  },
			  path: ctxStatic + '/raty/images_big',
			  readOnly : (function(){
				  var ro = $(that).data('readonly');
				  return !!ro;
			  })()
			});
		});
		
		// 获取提交的数据
		var getPostData = function() {
			var appraise = [];
			$('.product-appraise').each(function() {
				var that = $(this);
				var id = that.data('id');
				var content = that.find('.weui_textarea').val();
				appraise.push({
					orderId : orderId,
					id : id,
					content : content
				});
			});
			return appraise;
		}; 
		
		// 校验
		var check = function(postData) {
			var ok = false;
			$.each(postData, function(n, e) {
				if (!!e.content && e.content.length <= 200) {
					ok = true;
				}
			});
			
			// 错误提醒
			if (!ok) {
				Public.toast('请填写追评内容，且不超过200字');
			}
			return ok;
		};
		
		// 提交追评
		$(document).on('click', '.save_rappraise', function() {
			var postData = getPostData();
			if (check(postData)) {
				Public.loading('提交中...');
				var url = ctxFront + '/member/order/appraise/rsave.json';
				Public.postAjax(url, {
					orderId : orderId,
					postData : JSON.stringify(postData)
				}, function(data) {
					Public.close();
					if (data.success) {
						Public.success('追评成功!', function() {
							window.location.href = ctxFront + '/member/order/appraise/view/' + orderId + '.html';
						});
					} else {
						Public.toast(data.msg);
					}
				});
			} else {
				Public.toast('请填写评论内容');
			}
		});
	}
};