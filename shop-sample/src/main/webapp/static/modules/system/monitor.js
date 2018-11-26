var Monitor = Monitor || {};

/**
 * 加载磁盘信息
 */
Monitor.loadDiskInfos = function() {
	Public.loadingInner('#diskMonitor');
	var template = $('#diskTemplate').html();
	Public.getAjax(webRoot + '/admin/system/monitor/disk', {}, function(data) {
		var html = Public.runTemplate(template, {datas: data.obj});
		$('#diskMonitor').html(html);
		Public.loadedInner('#diskMonitor');
	});
};

/**
 * 加载CPU信息
 */
Monitor.loadCpuInfos = function() {
	var option = {
		series : [{
              name:'业务指标',
              type:'gauge',
              detail : {formatter:'{value}%'},
              data:[{value: 50, name: '使用率'}]
        }]
	};
	Public.loadingInner('#requestMonitor');
	var myChart = null;
	var template = $('#requestTemplate').html();
	Public.getAjax(webRoot + '/admin/system/monitor/cpu', {}, function(data) {
		var html = Public.runTemplate(template, {datas: data.obj});
		$('#requestMonitor .info').html(html);
		Public.loadedInner('#requestMonitor');
		option.series[0].data[0].value = data.obj.combined;
		myChart = Chart.loadGauguChart('requestMonitorChart', option, function(c) {
			myChart = c;
		});
	});
	
	var timer = null;
	
	//刷新
	var refrash = function(){
		Public.getAjax(webRoot + '/admin/system/monitor/cpu', {}, function(data) {
			var html = Public.runTemplate(template, {datas: data.obj});
			$('#requestMonitor .info').html(html);
			option.series[0].data[0].value = data.obj.combined;
			myChart.setOption(option);
		});
	}
	timer = Public.setInterval(function() {
		refrash();
	}, 10000); 
};


/**
 * 加载当天不同时段页面访问量信息
 */
Monitor.loadTodayPageVidewInfos = function() {
	var option = {
	    title : {
	        text: '今日页面访问量统计报表'
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            saveAsImage : {show: true}
	        }
	    },
		xAxis : [
		         {
		             data : ['0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23']
		         }
		     ],
	    legend: {
	        data:['首页','搜索','分类','商品']
	    },
		series : [
		{
            name:'首页',
            type:'line',
            data:['0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']
        },
        {
            name:'搜索',
            type:'line',
            data:['0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']
        },
        {
            name:'分类',
            type:'line',
            data:['0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']
        },
        {
            name:'商品',
            type:'line',
            data:['0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0','0']
        }
      ]
	};
	Public.loadingInner('#pvMonitor');
	var myChart = null;
	Public.getAjax(webRoot + '/admin/report/getTodayPVReportInfo', {}, function(data) {
		Public.loadedInner('#pvMonitor');
		option.series[0].data = data.obj[0];
		if(!!data.obj[1]){option.series[1].data = data.obj[1];}
		if(!!data.obj[2]){option.series[2].data = data.obj[2];}
		if(!!data.obj[3]){option.series[3].data = data.obj[3];}
		myChart = Chart.loadLineChart('pvMonitorChart', option, function(c) {
			myChart = c;
		});
	});
	
	var timer = null;
	
	//刷新
	var refrash = function(){
		Public.getAjax(webRoot + '/admin/report/getTodayPVReportInfo', {}, function(data) {
			option.series[0].data = data.obj[0];
			if(!!data.obj[1]){option.series[1].data = data.obj[1];}
			if(!!data.obj[2]){option.series[2].data = data.obj[2];}
			if(!!data.obj[3]){option.series[3].data = data.obj[3];}
			myChart.setOption(option);
		});
	}
	timer = Public.setInterval(function() {
		refrash();
	}, 1000 * 60 *60);  
};