/**
 * 订单流程（获取订单的操作流程）
 */
var OrderFlow = {

	template : (function() {
		return '<div class="order-flow">'
				+ '<div class="node create">下单</div>'
				+ '<div class="line-wrap create-cancel"><div class="line"><div class="tip">30分钟未付款自动取消</div></div></div>'
				+ '<div class="node cancel">取消</div>'
				+ '<div class="line-wrap create-payment"><div class="line"><div class="tip"></div></div></div>'
				+ '<div class="node payment">支付</div>'
				+ '<div class="line-wrap payment-shipping"><div class="line"><div class="tip"></div></div></div>'
				+ '<div class="line-wrap payment-refunds"><div class="line"><div class="tip">用户线下反馈（后台退款）</div></div></div>'
				+ '<div class="node shipping">发货</div>'
				+ '<div class="line-wrap shipping-apply_returns"><div class="line"><div class="tip"></div></div></div>'
				+ '<div class="node apply_returns">申请退货</div>'
				+ '<div class="line-wrap apply_returns-returns"><div class="line"><div class="tip">同意</div></div></div>'
				+ '<div class="node returns">退货</div>'
				+ '<div class="line-wrap returns_returns-refunds"><div class="line"><div class="tip"></div></div></div>'
				+ '<div class="node refunds">退款</div>'
				+ '<div class="line-wrap payment-apply_refund"><div class="line"><div class="tip"></div></div></div>'
				+ '<div class="node apply_refund">申请退款</div>'
				+ '<div class="line-wrap apply_refund-refunds"><div class="line"><div class="tip">同意</div></div></div>'
				+ '<div class="line-wrap apply_refund-shipping"><div class="line"><div class="tip">拒绝（请和用户确认）</div></div></div>'
				+ '<div class="line-wrap refunds-complete"><div class="line"><div class="tip">3天自动完成</div></div></div>'
				+ '<div class="node complete">完成</div>'
				+ '<div class="line-wrap shipping-complete"><div class="line"><div class="tip">3天自动完成</div></div></div>'
				+ '<div class="line-wrap apply_returns-complete"><div class="line"><div class="tip">拒绝（请和用户确认）</div></div></div>'
				+ '</div>';
	}()),

	nodes : [],

	// 初始化数据
	init : function(id) {
		var that = this;
		if (that.nodes.length == 0) {
			Public.postAjax(webRoot + '/admin/shop/order/log/json', {
				orderId : id
			}, function(data) {
				var nodes = data.obj;
				that.nodes = nodes;

				// 实例化结果
				that.render();
			}, false)
		}
	},

	// 实例化结果
	render : function() {
		var nodes = this.nodes;
		var prev = null;
		$.each(nodes, function(index, item) {
			$('.order-flow').find('.' + item.type).addClass('active');
			if (prev != null) {
				$('.order-flow').find('.' + (prev.type + '-' + item.type))
						.addClass('active');
				$('.order-flow').find('.' + (item.type + '-' + prev.type))
						.addClass('active');
			}
			prev = item;
		});
	},

	// 页面展示
	open : function(id) {
		var that = this;
		var url = webRoot + '/admin/shop/order/log/flow?orderId=' + id;
		Public.openUrlWindow('订单流程', url, 930, 545, false, null);
	}
};

/**
 * 商城服务对象
 */
var Shop = {

	// 得到商品信息
	getGoods : function(id) {
		var goods = {};
		var url = webRoot + '/admin/shop/goods/get/' + id;
		Public.getAjax(url, {}, function(data) {
			goods = data;
		}, false);
		return goods;
	},

	// 选择商品(前台配置)
	selectGoods : function(callback) {
		var url = webRoot + '/admin/shop/goods/table_select';
		Public.tableSelect(url, '选择商品', 750, 420, function(iframe, ids, names) {
			var id = !!ids && ids.length > 0 ? ids[0] : null;
			var goods = Shop.getGoods(id);
			callback(goods);
			return true;
		});
	},

	// 得到货品信息
	getProduct : function(id) {
		var goods = {};
		var url = webRoot + '/admin/shop/product/get/' + id;
		Public.getAjax(url, {}, function(data) {
			goods = data;
		}, false);
		return goods;
	},

	// 选择货品(后台配置)
	selectProduct : function(callback) {
		var url = webRoot + '/admin/shop/product/table_select';
		Public.tableSelect(url, '选择商品', 750, 420, function(iframe, ids, names) {
			$.each(ids, function(n, e) {
				var goods = Shop.getProduct(e);
				callback(goods);
			});
			return true;
		});
	},

	// 得到货品信息
	getCoupon : function(id) {
		var goods = {};
		var url = webRoot + '/admin/shop/coupon/get/' + id;
		Public.getAjax(url, {}, function(data) {
			goods = data;
		}, false);
		return goods;
	},

	// 选择优惠券
	selectCoupon : function(callback) {
		var url = webRoot + '/admin/shop/coupon/table_select';
		Public.tableSelect(url, '选择优惠券', 750, 420,
				function(iframe, ids, names) {
					$.each(ids, function(n, e) {
						var goods = Shop.getCoupon(e);
						callback(goods);
					});
					return true;
				});
	},

	// 获得用户的默认收获地址
	getUserDefaultReceiver : function(userId, ok) {
		var url = webRoot + '/admin/shop/receiver/get_user_default?userId='
				+ userId;
		Public.getAjax(url, {}, function(data) {
			if (data.success) {
				ok(data.obj);
			}
		});
	}
};

/**
 * 订单拆分
 */
var OrderSplit = {

	orderId : null,
		
	// 是否正在移动
	move : false,

    // 目标元素
	dragable : null,
	
	// 目标容器
	target : null,
	
	// 移动坐标
	move_pos : {
		X : 0, Y : 0,
	},
	
	// 移动元素坐标
	dragable_pos : {
		X1 : 0, X2 : 0,
		Y1 : 0, Y2 : 0
	},
	
	// 目标容器坐标
	target_pos : {
		X1 : 0, X2 : 0,
		Y1 : 0, Y2 : 0
	},
	
	// 初始化事件
	init : function(id) {
		this.orderId = id;
		var that = this;
		$('.product').each(function() {
			var product = $(this);
			that.addDragEvent(product);
		});
		
		// 添加公共事件
		this.addEvent();
	},
	
	// 注册拖动事件
	addDragEvent : function(product) {
		var that = this;
		$(product).mousedown(function(e) {
			that.dragable = $(this); that.move = true;
			that.move_pos.X = $(this).parent().offset().left + $(this).position().left;
			that.move_pos.Y = $(this).parent().offset().top + $(this).position().top;
			return false;
		});
	},
	
	// 公共事件
	addEvent : function() {
		var that = this;
		
		// 移动
		$(document).mousemove(function(e) {
			
			// 没有移动中，不用处理
			if (!that.move) {return false;}
			
			$(that.dragable).addClass("drag");
			
			// 偏移的位置
			var X1 = 20;
			var Y1 = 20;
			
			// 这次的位置
			that.dragable_pos.X1 = event.pageX;
			that.dragable_pos.Y1 = event.pageY;
			that.dragable_pos.X2 = that.dragable_pos.X1 + that.dragable.innerWidth();
			that.dragable_pos.Y2 = that.dragable_pos.Y1 + that.dragable.innerHeight();
			
			$(that.dragable).css({
				 left: that.dragable_pos.X1 - that.move_pos.X - X1 + 'px',
	             top: that.dragable_pos.Y1 - that.move_pos.Y - Y1 + 'px'
			});
			
			// 清空 target
			that.target = null;
			
			// 设置 target
			$('.order-split').each(function() {
				// 当前容器
				var tarDiv = $(this);
				that.target_pos.X1 = tarDiv.offset().left;
				that.target_pos.X2 = that.target_pos.X1 + tarDiv.width();
				that.target_pos.Y1 = tarDiv.offset().top;
				that.target_pos.Y2 = that.target_pos.Y1 + tarDiv.height();
				if (that.pos_in()) {
					that.target = tarDiv;
					tarDiv.addClass("cur");
				} else {
					tarDiv.removeClass("cur");
				}
				
				// that.show_pos();
			});
		}).mouseup(function(e) {
			that.move = false;
			
			if (that.dragable && that.target) {
				var dragable = that.dragable;
				$(dragable).removeAttr('style').appendTo(that.target.find('.products'));
				that.target.removeClass('cur');
			} else if(that.dragable) {
				var dragable = that.dragable;
				$(dragable).removeAttr('style');
			}
			that.dragable = null;
			that.target = null;
			e.cancelBubble = true;
		});
		
		// 确定拆分 
		$(document).on('click', '.splitBtn', function() {
			var postData = that.getPostData();
			if (postData) {
				Public.loading('订单拆分中...');
				Public.postAjax(webRoot + '/admin/shop/order/split/do', {postData : postData}, function(data) {
					Public.close();
					if (data.success) {
						parent.Public.toast('拆分成功！')
					} else {
						parent.Public.toast(data.msg);
					}
					parent.Public.close();
				});
			}
		});
		
		// 取消
        $(document).on('click', '.cacelBtn', function() {
			parent.Public.close();
		});
	},
	
	// 得到提交的数据
	getPostData: function() {
		var orders = []; var that = this;
		$('.order-split').each(function() {
			var that = $(this); var sn = $(this).data('sn');
			var products = [];
			that.find('.product').each(function() {
				var _id = $(this).data('id');
				products.push({
					productId: _id
				});
			});
			orders.push({
				sn: sn,
				items: products
			})
		});
		
		// 校验
		if (orders[0].items.length == 0) {
			Public.toast('第一个订单的商品不能为空');
			return false;
		}
		return JSON.stringify({
			orderId: that.orderId,
			orders : orders
		});
	},
	
	// 坐标在这个范围内
	pos_in : function() {
		var that = this;
		var dragable_pos = that.dragable_pos;
		var target_pos = that.target_pos;
		if ((dragable_pos.X1 >= target_pos.X1 && dragable_pos.X1 <= target_pos.X2)
			&& ((dragable_pos.Y1 >= target_pos.Y1 && dragable_pos.Y1 <= target_pos.Y2) 
			|| (dragable_pos.Y2 >= target_pos.Y1 && dragable_pos.Y2 <= target_pos.Y2))) {
			return true;
		} else if((dragable_pos.X2 >= target_pos.X1 && dragable_pos.X2 <= target_pos.X2)
				&& ((dragable_pos.Y1 >= target_pos.Y1 && dragable_pos.Y1 <= target_pos.Y2) 
				|| (dragable_pos.Y2 >= target_pos.Y1 && dragable_pos.Y2 <= target_pos.Y2))) {
			return true;			
	    }
		return false;
	},
	
	// 显示坐标
	show_pos : function() {
		var that = this;
		var dragable_pos = that.dragable_pos;
		var target_pos = that.target_pos;
		var move_pos = that.move_pos;
		var html = "";
		html = 'dragable:' + dragable_pos.X1 + ','
		                      + dragable_pos.X2 + ',' 
		                      + dragable_pos.Y1 + ',' 
		                      + dragable_pos.Y2 + ','
		                      + move_pos.X + ','
		                      + move_pos.Y + '<br/>';
		html = html + 'target:' + target_pos.X1 + ',' 
					          + target_pos.X2 + ','
					          + target_pos.Y1 + ','
					          + target_pos.Y2 + '<br/>';
		$('.order-split-pos').html(html);
	}
}