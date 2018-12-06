/**
 * 报表
 */
var Report = {
	
	// 用户统计
	userStat : function() {
		
		// 用户登录
		Public.postAjax(webRoot + '/admin/report/user/a', {}, function(data) {
			$('#usera').find('.-p').eq(0).text(data[0]);
			$('#usera').find('.-p').eq(1).text(data[1]);
			$('#usera').find('.-p').eq(2).text(data[2]);
		});
		
		// 用户注册
		Public.postAjax(webRoot + '/admin/report/user/z', {}, function(data) {
			$('#userz').find('.-p').eq(0).text(data[0]);
			$('#userz').find('.-p').eq(1).text(data[1]);
			$('#userz').find('.-p').eq(2).text(data[2]);
		});
		
		// 用户登录
		Public.postAjax(webRoot + '/admin/report/user/stat', {}, function(data) {
			var htmls = [];
			for(var i = 0; i< data.length; i++) {
				var item = data[i];
				htmls.push('<tr class="tc no_'+(i + 1)+'"><td class="tc">'+(i + 1)+'</td><td>'+(item.USER_NAME)+'</td><td>'+(item.QUANTITY)+'</td><td class="tc">'+(item.AMOUNT)+'</td></tr>')
			}
			$('#user_orders').html(htmls.join(''));
		});
		
		var _chart = null;
		var option = {
			title: {text: ''},
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: [],
		    },
		    yAxis: {
		        type: 'value',
		        min : 0,
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value}次'
		        }
		    },
		    series: [{
		        name: '用户数',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        data: []
		    }],
		    toolbox: {
		        show : false,
		    },
		    animation : false
		};
		
		Chart.loadLineChart('newUser_chart', option, function(chart) {
			_chart = chart;
	    });
		
		// 销售额统计
		var _datas = [[], []];
		Public.postAjax(webRoot + '/admin/report/user/new_data', {}, function(datas) {
			for(var i = 0; i< datas.length; i++) {
				var time = datas[i].x;
				var quantity = datas[i].y;
				_datas[0].push(time)
				_datas[1].push(quantity)
			}
			_chart.setOption({xAxis:{data:_datas[0]}, series: [{data:_datas[1]}]});
		});
		
		var _chart2 = null;
		
		Chart.loadLineChart('activeUser_chart', option, function(chart) {
			_chart2 = chart;
	    });
		
		// 销售额统计
		var _datas2 = [[], []];
		Public.postAjax(webRoot + '/admin/report/user/active_data', {}, function(datas) {
			for(var i = 0; i< datas.length; i++) {
				var time = datas[i].x;
				var quantity = datas[i].y;
				_datas2[0].push(time)
				_datas2[1].push(quantity)
			}
			_chart2.setOption({xAxis:{data:_datas2[0]}, series: [{data:_datas2[1]}]});
		});
	},
	
	// 销售统计
	saleStat : function() {
		// 用户登录
		Public.postAjax(webRoot + '/admin/report/sale/a', {}, function(data) {
			$('#sales').find('.-p').eq(0).text('￥' + data[0]);
			$('#sales').find('.-p').eq(1).text('￥' + data[1]);
			$('#sales').find('.-p').eq(2).text('￥' + data[2]);
		});
		
		var _chart = null;
		var option = {
			title: {text: ''},
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: [],
		    },
		    yAxis: {
		        type: 'value',
		        min : 0,
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value}元'
		        }
		    },
		    series: [{
		        name: '销售额',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        data: []
		    }],
		    toolbox: {
		        show : false,
		    },
		    animation : false
		};
		
		Chart.loadLineChart('sale_chart', option, function(chart) {
			_chart = chart;
	    });
		
		// 销售额统计
		var _datas = [[], []];
		Public.postAjax(webRoot + '/admin/report/sale/stat_data', {}, function(datas) {
			for(var i = 0; i< datas.length; i++) {
				var time = datas[i].x;
				var quantity = datas[i].y;
				_datas[0].push(time)
				_datas[1].push(quantity)
			}
			_chart.setOption({xAxis:{data:_datas[0]}, series: [{data:_datas[1]}]});
		});
	},
	
	// 订单统计
	orderStat : function() {
		// 用户登录
		Public.postAjax(webRoot + '/admin/report/order/a', {}, function(data) {
			$('#orders').find('.-p').eq(0).text(data[0]);
			$('#orders').find('.-p').eq(1).text(data[1]);
			$('#orders').find('.-p').eq(2).text(data[2]);
		});
		
		// 用户登录
		Public.postAjax(webRoot + '/admin/report/order/stat', {}, function(data) {
			var htmls = [];
			for(var i = 0; i< data.length; i++) {
				var item = data[i];
				htmls.push('<li><span class="-p">'+(item.count)+'</span><span class="-t">'+(item.label)+'</span></li>')
			}
			$('#order_stats').html(htmls.join(''));
		});
		
		var _chart = null;
		var option = {
			title: {text: ''},
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: [],
		    },
		    yAxis: {
		        type: 'value',
		        min : 0,
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value}单'
		        }
		    },
		    series: [{
		        name: '订单数',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        data: []
		    }],
		    toolbox: {
		        show : false,
		    },
		    animation : false
		};
		
		Chart.loadLineChart('order_chart', option, function(chart) {
			_chart = chart;
	    });
		
		// 销售额统计
		var _datas = [[], []];
		Public.postAjax(webRoot + '/admin/report/order/book_data', {}, function(datas) {
			for(var i = 0; i< datas.length; i++) {
				var time = datas[i].x;
				var quantity = datas[i].y;
				_datas[0].push(time)
				_datas[1].push(quantity)
			}
			_chart.setOption({xAxis:{data:_datas[0]}, series: [{data:_datas[1]}]});
		});
	},
	
	// 商品统计
	productStat : function() {
		// 用户登录
		Public.postAjax(webRoot + '/admin/report/product/a', {}, function(data) {
			var htmls = [];
			for(var i = 0; i< data.length; i++) {
				var item = data[i];
				htmls.push('<tr class="tc no_'+(i + 1)+'"><td class="tc">'+(i + 1)+'</td><td>'+(item.PRODUCT_NAME)+'</td><td class="tc">'+(item.QUANTITY)+'</td></tr>')
			}
			$('#products').html(htmls.join(''));
		});
		
		// 库存
		Public.postAjax(webRoot + '/admin/report/product/stat', {}, function(data) {
			$('#product_stat .-p').eq(0).text(data.CS);
			$('#product_stat .-p').eq(1).text(data.XJ);
			$('#product_stat .-p').eq(2).text(data.KC100);
			$('#product_stat .-p').eq(3).text(data.KC10);
		});
	}
};