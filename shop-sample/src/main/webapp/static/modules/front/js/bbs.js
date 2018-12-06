/**
 * BBS 相关的操作
 */
var BBS = {
	
	// 浮动按钮的相关的事件
	int_flex_btns : function() {
		
		var $add_topic = $('.add_topic');
		var $to_top = $('.to_top');
		
		$(document).on('scroll.flex_btns', function() {
    		var scrollTop = parseInt($(document).scrollTop());
     	    if (scrollTop == 0) {
     	    	$to_top.removeClass('show');
     	    } else {
     	    	$to_top.addClass('show');
     	    }
        });
		
		// 点击向上
		$(document).on('click.to_top', '.to_top', function() {
			$('body').animate({scrollTop: 0}, 500, function() {});
        });
		
		
		// 发表动态
		$(document).on('click.to_top', '.add_topic', function() {
			window.location.href = ctxFront + '/member/bbs/topic/add.html';
        });
	},
	
	// 热点页面的操作
	init_hotspot : function() {
		
		var that = this;
		
		// 监听事件
		this.addEvent();
		
		//加载数据
		Public.initScrollLoad(ctxFront + '/member/bbs/hotspot/page.json', $('#topicTemplate').html(), function(_scroll, data) {
			if (data) {
				that.me_hits(data)
				that.show_images(data.data);
			}
		});
		
		// 添加浮动菜单的支持(不然滚动事件会被取消)
		this.int_flex_btns();
	},
	
	// 最新页面的操作
	init_newest : function() {
		
		var that = this;
		
		// 监听事件
		this.addEvent();
		
		//加载数据
		Public.initScrollLoad(ctxFront + '/member/bbs/newest/page.json', $('#topicTemplate').html(), function(_scroll, data) {
			if (data) {
				that.me_hits(data)
				that.show_images(data.data);
			}
		});
		
		// 添加浮动菜单的支持(不然滚动事件会被取消)
		this.int_flex_btns();
	},
	
	// 产品页面的更新
	init_section : function() {
        
		var that = this;
		
		// 监听事件
		this.addEvent();
		
		//加载数据
		Public.initScrollLoad(ctxFront + '/member/bbs/section/page.json', $('#topicTemplate').html(), function(_scroll, data) {
			if (data) {
				that.me_hits(data)
				that.show_images(data.data);
			}
		});
		
		// 添加浮动菜单的支持(不然滚动事件会被取消)
		this.int_flex_btns();
	},
	
	// 刷新分类
	refresh_section : function() {
		Public.resetScrollLoad();
	},
	
	// 初始化添加动态
	init_add_topic : function() {
		
		// 监听事件
		this.addEvent();
		
		// 背景
		$('.mood-wrap').on('click', '.mood', function() {
			if ($(this).hasClass('cur')) {
				$('.mood-wrap .cur').removeClass('cur');
			} else {
				$('.mood-wrap .cur').removeClass('cur');
				$(this).addClass('cur')
			}
		});
		
		// 上传图片
		$('.weui_uploader_input').each(function(n, e) {
			var $ele = $(this); var max = 4; 
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
				$file.closest('.weui_uploader').find('.weui_uploader_input_wrp').show();
				$file.remove();
			}
		});
	},
	
	// 动态详情
	init_topic_detail : function(topicId) {
		
		var that = this;
		
		// 监听事件
		this.addEvent();
		
		if (!!topicId) {
			
			// 显示图片
			this.show_images([{
				id: topicId
			}], true);
			
			//加载数据
			Public.initScrollLoad(ctxFront + '/member/bbs/reply/page/' + topicId + '.json', $('#replyTemplate').html(), function(_scroll, data) {
				if (data) {
					var replys = data.data;
					$("[data-time]").each(function(){
						var dateStr = $(this).data("time");
						$(this).html($.timeago(dateStr));
						$(this).removeAttr('data-time');
					});
					that.me_hits_reply(replys);
				}
			}, {
				showNoData : function() {
					Public.toast('没有回复');
				}
			});
		}
		
		// 添加浮动菜单的支持(不然滚动事件会被取消)
		this.int_flex_btns();
		
		// 是否点赞过
		this.me_hits({
			data : [{
				id : topicId
			}]
		});
	},
	
	// 滚动图片 $as 源， index 显示第几个
	swiper_images : function($as, index) {
		var images = [];
		$as.each(function(n, e) {
			var src = $(e).data('src');
			images.push(src);
		});
		var $galleryImg = $("#galleryImg");
		// 如果只有一张图片
		if (images.length == 1) {
			$galleryImg.attr("style", 'background-image:url('+ images[0] +')');
		} 
		
		// 多张图片
		else if(images.length > 1) {
			var template = $('#swiperTemplate').html();
			var html = Public.runTemplate(template, {images: images});
			$galleryImg.html(html);
			// 实例化组件
			var $previewSwiper = $('#previewSwiper');
			var id = $previewSwiper.attr('id');
			var _swiper = $($previewSwiper);
			
			// 实例化组件
			var swiper = new Swiper(_swiper, {
		        pagination: '.swiper-pagination',
		        slidesPerView: 'auto',
		        centeredSlides: true,
		        loop:false,
		    });
			// 添加引用
			this.$swiper = swiper;
			
			// 定位到
			this.$swiper.slideTo(index)
		}
	},
	
	// 销毁预览
	destory_swiper : function() {
		if (this.$swiper != null) {
			this.$swiper.destroy();
		}
	},
	
	// 加载图片 type == true 详情
	show_images : function(topics, detail) {
		
		// 显示图片
		var _render = function(img) {
			//if (!!detail) {
				return '<a class="-image" data-src="' + img + '" href="javascript:void(0)" style="background-image:url('+ img +')"></a>'
			//}
			//return '<b class="-image" data-src="' + img + '" href="javascript:void(0)" style="background-image:url('+ img +')"></b>'
		};
		
		// 循环
		$.each(topics, function(n, e) {
			var id = e.id;
			var $images = $('#' + id).find('.-images');
			var images = $images.data('images');
			if (!!images) {
				images = images.split(',');
				$.each(images, function(m, f) {
					$images.append(_render(f));
				});
			}
		});
	},
	
	// 添加动态
	addTopic : function() {
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
		var content = $('#content').val();
		var mood = $('.mood.cur').data('mood') || '';
		if (!content) {
			Public.toast('说点什么吧！');
			return;
		}
		if (content.length > 500) {
			Public.toast('动态最多500字');
			return;
		}
		var images = (function(){
			var _images = [];
			$('.weui_uploader_file').each(function(n, m) {
			    var image = $(m).data('img');
			    if (!!image) {
			    	_images.push(image);
			    }
			});
			return _images.join(',');
		})();
		Public.loading('提交中...');
		// 提交
		var url = ctxFront + '/member/bbs/topic/save.json';
		Public.postAjax(url, {content : content, mood: mood, images: images}, function(data) {
			Public.close();
			Public.success('发布成功', function() {
				window.location.href = ctxFront + '/member/bbs/topic/add.html';
			});
		});
	},
	
	// 点赞动态
	hitTopic: function($topic) {
		var that = this; var topicId = $topic.data('id');
		var url = ctxFront + '/member/bbs/topic/hit/' + topicId + '.json';
		Public.postAjax(url, {}, function() {
			that._hited($topic, true);
		});
	},
	
	// 添加颜色支持
	_hited : function(topic, add) {
		$(topic).find('.topic-hits').addClass('hited');
		var $num = $(topic).find('.topic-hits').find('.-num'); var _num = parseInt($num.text());
		if (!!add) {
			$num.text(_num + 1);
		} else {
			if (_num == 0) { // 防止延迟
				$num.text(1);
			}
		}
	},
	
	// 我点击的
	me_hits : function(data) {
		var that = this;
		var ids = [];
		$.each(data.data, function(n, e) {
			ids.push(e.id);
		});
		
		// 点击
		var url = ctxFront + '/member/bbs/topic/me_hits.json';
		Public.postAjax(url, {ids: ids.join(',')}, function(data) {
			$.each(data.obj, function(n, e) {
				if (e.hited == 1) {
					that._hited($('#' + e.id));
				}
			});
		});
		
		// 回复
		var url = ctxFront + '/member/bbs/topic/me_replys.json';
		Public.postAjax(url, {ids: ids.join(',')}, function(data) {
			$.each(data.obj, function(n, e) {
				if (e.hited == 1) {
					that.postReply($('#' + e.id));
				}
			});
		});
		
	},
	
	// 去回复
	toReply : function($topic, $reply, replyUser) {
		$('body').addClass('reply_mode');
		var topicId = $topic.data('id'); 
		var replyId = !!$reply ? $reply.data('id'): '';
		$('.topic-detail-bottom').data('topic', topicId);
		$('.topic-detail-bottom').data('reply', replyId);
		$('.topic-detail-bottom').data('ruser', replyUser||'');
		if (!!replyUser) {
			$('.topic-detail-bottom').find('.content').attr('placeholder', '回复' + replyUser);
		}
	},
	
	// 不回复
	unReply : function() {
		$('body').removeClass('reply_mode');
	},
	
	// 回复
	doReply : function() {
		var that = this;
		var $reply = $('.topic-detail-bottom');
		var content = $reply.find('.content').val();
		var topicId = $reply.data('topic');
		var replyId = $reply.data('reply');
		var replyUser = $reply.data('ruser');
		if (!content) {
			Public.toast('说点什么吧！');
			return;
		}
		if (content.length > 100) {
			Public.toast('回复最多100字');
			return;
		}
		if (!topicId) {
			Public.toast('参数错误');
			that.unReply();
			return;
		}
		Public.loading('提交中...');
		var url = ctxFront + '/member/bbs/reply/topic/' + topicId + '.json';
		Public.postAjax(url, {content : content, replyId : replyId, replyUser: replyUser}, function() {
			Public.close();
			Public.success('评论成功!');
			$reply.find('.content').val(''); // 清空数据
			that.postReply($('#' + topicId), true);
		});
	},
	
	// 回复结束后
	postReply : function(topic, add) {
		$(topic).find('.topic-replys').addClass('hited');
		var $num = $(topic).find('.topic-replys').find('.-num'); var _num = parseInt($num.text());
		if (!!add) {
			$num.text(_num + 1);
		}else {
			if (_num == 0) { // 防止延迟
				$num.text(1);
			}
		}
		this.unReply();
	},
	
	// 3条评论
	top3_replys : function($topic) {
		var topicId = $topic.data('id'); var that = this;
		var url = ctxFront + '/member/bbs/reply/top3/' + topicId + '.json';
		Public.postAjax(url, {}, function(data) {
			var replys = data.obj;
			if (data.success) {
				var html = Public.runTemplate($('#replyTemplate').html(), {data: replys});
				$topic.find('.topic-replay-wrap').html(html);
				$("[data-time]").each(function(){
					var dateStr = $(this).data("time");
					$(this).html($.timeago(dateStr));
					$(this).removeAttr('data-time');
				});
				that.me_hits_reply(replys);
			}
		});
	},
	
	// 我是否点击过回复的赞
	me_hits_reply : function(replys) {
		var that = this;
		var ids = [];
		$.each(replys, function(n, e) {
			ids.push(e.id);
		});
		// 点击
		var url = ctxFront + '/member/bbs/reply/me_hits.json';
		Public.postAjax(url, {ids: ids.join(',')}, function(data) {
			if (!!data.obj) {
				$.each(data.obj, function(n, e) {
					if (e.hited == 1) {
						that._reply_hited($('#' + e.id));
					}
				});
			}
		});
	},
	
	// 回复点赞
	_reply_hited : function(reply, add) {
		$(reply).find('.replay-hits').addClass('hited');
		var $num = $(reply).find('.replay-hits').find('.-num'); var _num = parseInt($num.text());
		if (!!add) {
			$num.text(_num + 1);
		} else {
			if (_num == 0) { // 防止延迟
				$num.text(1);
			}
		}
	},
	
	// 点赞动态
	hitReply: function($topic) {
		var that = this; var topicId = $topic.data('id');
		var url = ctxFront + '/member/bbs/reply/hit/' + topicId + '.json';
		Public.postAjax(url, {}, function() {
			that._reply_hited($topic, true);
		});
	},
	
	// 添加事件
	addEvent : function() {
		var that = this;
		
		// 保存
		$(document).on('click', '.save_topic', function() {
			that.addTopic();
		});
		
		// 动态点赞
		$(document).on('click', '.topic-hits', function() {
			if (!$(this).hasClass('hited')) {
				var $topic = $(this).closest('.topic');
				that.hitTopic($topic);
			}
		});
		// 动态回复
		$(document).on('click', '.topic-replys', function() {
			var $topic = $(this).closest('.topic');
			that.toReply($topic);
		});
		// 动态回复
		$(document).on('click', '.reply_mode_mask', function() {
			that.unReply();
		});
		// 动态回复
		$(document).on('click', '.reply_topic', function() {
			that.doReply();
		});
		// 加载3条评论
		$(document).on('click', '.topic-reply-ops', function() {
			if ($(this).find('.-img').hasClass('icon-arrow-down')) {
				var $topic = $(this).closest('.topic');
				that.top3_replys($topic);
				$(this).find('.-img').removeClass('icon-arrow-down').addClass('icon-arrow-up');
			}
		});
		// 回复点赞
		$(document).on('click', '.replay-hits', function() {
			if (!$(this).hasClass('hited')) {
				var $reply = $(this).closest('.topic-replay');
				that.hitReply($reply);
			}
		});
		// 动态回复
		$(document).on('click', '.replay-replys', function() {
			var $topic = $(this).closest('.topic');
			var $reply = $(this).closest('.topic-replay');
			var replyUser = $reply.find('.-name').text();
			that.toReply($topic, $reply, replyUser);
		});
		// 选择产品
		$(document).on('click', '.sections .section', function() {
			var id = $(this).data('id'); var name = $(this).data('name')
			if(id === '全部') {id = '';} if(name === '全部') {name = '';}
			$('#sectionId').val(id);$('#tag').val(name||'');
			$('.sections .cur').removeClass('cur');
			$(this).addClass('cur').parent().prepend($(this)); 
			$('.sections').removeClass('more');
			that.refresh_section();
		});
		// 更多
		$(document).on('click', '.sections .-sel', function() {
			$('.sections').addClass('more');
		});
		// 返回
		$(document).on('click', '.back', function() {
			window.history.go(-1);
		});
		
		// 关闭预览
		$(document).on('click', '.weui-gallery__del', function() {
			$("#gallery").fadeOut(100);
			that.destory_swiper();
		});
		
		// 打开预览
		$(document).on('click', '.topic .-images > .-image', function() {
			var $as = $(this).parent().find('.-image');
			var index = $as.index(this);
			$("#gallery").fadeIn(100, function() {
				that.swiper_images($as, index);
			});
		});
	}
}