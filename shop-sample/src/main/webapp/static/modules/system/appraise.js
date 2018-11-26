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
			Public.toast('数据已加载完毕');
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
			$('#ajax-load-page').on('scroll', function() {
				var scrollTop = parseInt($(this).scrollTop());
				var scrollHeight = parseInt($(this).get(0).scrollHeight);
				var windowHeight = parseInt($(this).height());
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
 * 
 */
var Appraise = {
	
	// 初始化
	init : function() {
		
		var that = this;
		
		// 界面大小
		this.initHeight();
		
		//加载数据
		Public.initScrollLoad(webRoot + '/admin/shop/product/appraise/page', $('#appraiseTemplate').html(), function(_scroll, data) {
			if (data) {
				$.each(data.data, function(n, e) {
					that.doRaty($('[data-id="'+(e.id)+'"]').find('[data-raty]'));
				});
			}
		});
		
		// 加载事件
		this.addEvent();
	},
	
	// 初始化大小
	initHeight : function() {
		var _adjust = $('.mappraise-container').hasClass('list')?60:0;
		var heigth = $(window).height();
		var t = $('.mappraise-container');
		t.height(heigth - t.offset().top - _adjust);
		
		t = $('.mappraise-appraises');
		t.height(heigth - t.offset().top);
	},
	
	// 执行 raty
	doRaty : function(ratys) {
		ratys.each(function(n, e) {
			var that = $(e);
			that.raty({
			  score: function() {
			    return $(that).attr('data-raty');
			  },
			  path: '/static/raty/images_big',
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
				  var target = $(that).data('target');
				  if (!!target) {
					 return $(that).closest('.controls').find(target).eq(0)
				  }
				  return '';
			  })(),
			  targetType : (function(){
				  var target = $(that).data('targettype');
				  if (!!target) {
					 return target;
				  }
				  return '';
			  })(),
			  targetKeep : true
			});
		});
	},
	
	// 获得评价
	formAppraise : function(appraiseId) {
		var that = this;
		var url = webRoot + '/admin/shop/product/appraise/form?id=' + appraiseId;
		Public.postAjax(url, {}, function(data) {
			var html = Public.runTemplate($('#editTemplate').html(), {appraise: data});
			$('.mappraise-edit').html(html);
			that.doRaty($('.mappraise-edit').find('[data-raty]'));
		});
	},
	
	// 添加事件
	addEvent : function() {
		var that = this;
		$(document).on('click', '.edit', function() {
			// 编辑模式
			$('.mappraise-container').addClass('edit_mode');
			// 加载数据
			var id = $(this).closest('.product-appraise').data('id');
			that.formAppraise(id);
		});
		
		// 删除图片
		$(document).on('click', '.weui_uploader_file .del', function() {
			$(this).closest('.weui_uploader_file').remove();
		});
		
		// 保存
		$(document).on('click', '.save', function() {
			that.doSave();
		});
		
		// 选择标签
		$(document).on('click', '.-tag', function() {
			var tag = $(this).text();
			    tag = tag === '全部'?'':tag;
			$('#tags').val(tag); $('.-tag.cur').removeClass('cur');$(this).addClass('cur');
			Public.resetScrollLoad();
		});
		
		// 点击高级查询
		$(document).on('click', '.-more', function() {
			var open = $(this).closest('.mappraise-ops_ft').hasClass('open');
			if (open) {
				$(this).closest('.mappraise-ops_ft').removeClass('open');
			} else {
				$(this).closest('.mappraise-ops_ft').addClass('open');
			}
		});
		
		// 其他按钮
		$(document).bind('click.more_menu',function(e){
			var target  = e.target || e.srcElement;
			$('.mappraise-ops_ft').each(function(){
				var menu = $(this);
				if ($(target).closest(menu).length == 0 && $('.dropdown').is(':visible')){
					menu.closest('.mappraise-ops_ft').removeClass('open');
				};
			});
		});
		
		// 重置
		$(document).bind('click.more_menu',function(e){
			var target  = e.target || e.srcElement;
			$('.mappraise-ops_ft').each(function(){
				var menu = $(this);
				if ($(target).closest(menu).length == 0 && $('.dropdown').is(':visible')){
					menu.closest('.mappraise-ops_ft').removeClass('open');
				};
			});
			
			//重置事件
			if ($(target).hasClass("reset")) {
				that.resetQuery();
			}
			
			//查询事件
			if ($(target).hasClass("query")) {
				$(target).closest('.mappraise-ops_ft').removeClass('open');
				that.doQuery();
			}
		});
		
		// 赠送积分
		$(document).on('click', '.add_points', function(e) {
			var appraise = $(this).closest('.product-appraise').data('id');
			var points = $(this).closest('.points-wrap').find('.points').val();
			var mode = $(this).closest('.points-wrap').find('.ui-switch').is(':checked');
			if (!points) {
				Public.toast('请输入赠送的积分值');
				return;
			}
			var that = this;
			var msg = '确定赠送'+(points)+'积分, 首次赠送保护已' + (mode?'开启':'关闭'); 
			Public.confirmx(msg, function() {
				var url = webRoot + '/admin/shop/product/appraise/points/' + appraise;
				Public.postAjax(url, {points: points, mode: mode}, function(data) {
					if (data.success) {
						Public.success('赠送积分成功');
						$(that).closest('.points-wrap').find('.points').val('');
					} else {
						Public.error(data.msg);
					}
				});
			});
		});
	},
	
	// 重置查询
	resetQuery : function() {
		$(".mappraise-ops_ft").find("input[type='text']").each(function(index,item){
			$(item).val('');
		});
		$(".mappraise-ops_ft").find("select").each(function(index,item){
			$(item).val('');
		});
	},
	
	// 查询
	doQuery : function() {
		Public.resetScrollLoad();
	},
	
	// 执行保存
	doSave : function() {
		Public.loading('数据保存中');
		var postData = this.getPostData();
		if (!postData) {
			Public.error('数据错误');
			return;
		}
		
		// 提交保存
		var url = webRoot = '/admin/shop/product/appraise/save';
		Public.postAjax(url, postData, function(data) {
			Public.close();
			if (data.success) {
				Public.success('保存成功', function() {
					Public.doQuery();
				});
			} else {
				Public.error(data.msg);
			}
		});
	},
	
	// 获得保存数据
	getPostData : function() {
		
		var $form = $('#inputForm');
		
		// 数据
		var postData = Public.serialize($form);
		
		var images = [];
		
		// 添加图片
		$form.find('.weui_uploader_file').each(function(n, e) {
			images.push($(e).data('img'));
		});
		
		// 加入图片
		postData.push({'name': 'images', 'value': images.join(',')});
		
		// 评价判断
		var productGrade = $form.find('.productGrade').val();
		var _productGrade = $form.find('.productGrade').next().raty('score');
		if (productGrade != _productGrade) {
			return null;
		}
		var packageGrade = $form.find('.packageGrade').val();
		var _packageGrade = $form.find('.packageGrade').next().raty('score');
		if (packageGrade != _packageGrade) {
			return null;
		}
		var deliveryGrade = $form.find('.deliveryGrade').val();
		var _deliveryGrade = $form.find('.deliveryGrade').next().raty('score');
		if (deliveryGrade != _deliveryGrade) {
			return null;
		}
		return postData;
	}
}