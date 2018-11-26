var Chart = Chart ||{};

//加载图形
Chart.loadChart = function(dom, option, callback){
	var myChart = echarts.init(document.getElementById(dom)); 
        myChart.setOption(option); 
	if (!!callback) {
		callback(myChart, echarts);
	}
};

//柱状图
Chart.loadBarChart = function(dom, option, callback) {
	var _option = {
		title : {text: '柱状统计图'},
	    tooltip : { show:true, trigger: 'item' },
	    yAxis : [  { type : 'value' } ],
	    toolbox: {
	        show : true,
	        orient: 'vertical',
            x: 'right',
            y: 'top',
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true
	};
	Chart.loadChart(dom, $.extend({},_option, option), callback);
};

//折线
Chart.loadLineChart = function(dom, option, callback) {
	var _option = {
		tooltip : {
			trigger: 'axis'
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar', 'stack', 'tiled']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true
	};
	Chart.loadChart(dom, $.extend({},_option, option), callback);
};