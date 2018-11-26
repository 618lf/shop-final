<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>系统监控</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
.chart-wrap {
  width: 50%;
  float: left;
  padding: 10px;
  box-sizing: border-box;
}
.chart {
  height: 400px;
  border: 1px solid #f4f4f4;
  overflow: hidden;
}
.chart-big {
  width: 100%;
}
.chart-table {
  padding: 0 30px;
}
.chart-table .table{
  margin-top: 30px;
  box-sizing: border-box;
}
.td_lable {
  background-color: #E5E5E5;
}
</style>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills clearfix" style="width: 90%;">
      <div class="chart-wrap">
         <div id="os_main" class="chart chart-table">
         <table class="table sample-table table-bordered">
			<thead>
				<tr>
					<th>名称</th><th>值</th>
				</tr>
			</thead>
			<tbody>
			<tr><td class="td_lable">Name</td><td>${os.Name}</td></tr>
			<tr><td class="td_lable">Vendor</td><td>${os.Vendor}</td></tr>
			<tr><td class="td_lable">VendorCodeName</td><td>${os.VendorCodeName}</td></tr>
			<tr><td class="td_lable">VendorVersion</td><td>${os.VendorVersion}</td></tr>
			<tr><td class="td_lable">Machine</td><td>${os.Machine}</td></tr>
			<tr><td class="td_lable">Version</td><td>${os.Version}</td></tr>
			</tbody>
	     </table>
         </div>
      </div>
      <div class="chart-wrap">
         <div id="disk_main" class="chart chart-table">
         <table class="table sample-table table-bordered">
            <thead>
				<tr>
					<th>磁盘</th><th>总量</th><th>已用</th><th>剩余</th><th>已用%</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${disk}" var="dir">
			<tr><td class="td_lable">${dir.dir}</td><td>${dir.total}</td><td>${dir.used}</td><td>${dir.avail}</td><td>${dir.pct}%</td></tr>
			</c:forEach>
			</tbody>
	     </table>
         </div>
      </div>
      <div class="chart-wrap">
         <div id="cpu_main" class="chart"></div>
      </div>
      <div class="chart-wrap">
         <div id="mem_main" class="chart"></div>
      </div>
      <div class="chart-wrap">
         <div id="net_main" class="chart"></div>
      </div>
      <div class="chart-wrap">
         <div id="proc_main" class="chart">
         <table id="sample-table" class="table sample-table table-striped table-bordered">
            <thead>
				<tr>
					<th>PID</th>
					<th>名称</th>
					<th>线程数</th>
					<th>CPU总时长</th>
					<th>最后使用时间</th>
				</tr>
			</thead>
			<tbody id="procs"></tbody>
	     </table>
         </div>
      </div>
      <div class="chart-wrap chart-big">
         <div id="access_main" class="chart"></div>
      </div>
      <div class="chart-wrap chart-big">
         <div id="user_main" class="chart"></div>
      </div>
   </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/echarts/dist/echarts.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/echarts/theme/macarons.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common-charts.js" type="text/javascript"></script>
<script type="text/javascript">
// cpu 监控
var CPU = {
	
	//
    chart : null,
    
	// 数据
	data : [[],[]],
		
	// 初始化
	init : function() {
		
		var self = this;
		
		var option = {
			title: {text: 'CPU监控'},
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: self.data[0]
		    },
		    yAxis: {
		        type: 'value',
		        min : 0,
		        max: 100,
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value} %'
		        }
		    },
		    series: [{
		        name: '使用率',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        data: self.data[1]
		    }],
		    animation : false
		};
		
		Chart.loadLineChart('cpu_main', option, function(chart) {
			self.chart = chart;
	    });
	},
	
	// 加载数据
	load : function(stat, time) {
		var self = this;
		if (self.data[0].length >= 15) {
			self.data[0].shift();
			self.data[1].shift();
		}
		self.data[0].push(time);
		self.data[1].push(stat.combined);
		self.chart.setOption({xAxis:{data:self.data[0]}, series: [{data: self.data[1]}]});
	}
};

//内存 监控
var MEM = {
		
	//
    chart : null,
	    
	// 数据
	data : [[],[]],
		
	// 初始化
	init : function() {
		
		var self = this;
		
		var option = {
			title: {text: '内存监控'},
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: self.data[0]
		    },
		    yAxis: {
		        type: 'value',
		        min : 0,
		        max: 100,
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value} %'
		        }
		    },
		    series: [{
		        name: '使用率',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        data: self.data[1]
		    }],
		    animation : false
		};
		
		Chart.loadLineChart('mem_main', option, function(chart) {
	    	self.chart = chart;
	    });
	},
	
	// 加载数据
	load : function(stat, time) {
		var self = this;
		if (self.data[0].length >= 15) {
			self.data[0].shift();
			self.data[1].shift();
		}
		self.data[0].push(time);
		self.data[1].push(parseInt(stat.memUsed / stat.memTtotal * 100));
		self.chart.setOption({xAxis:{data:self.data[0]}, series: [{data: self.data[1]}]});
	}
};

//内存 监控
var NET = {
		
	//
    chart : null,
	    
	// 数据
	data : [[],[],[]],
		
	// 初始化
	init : function() {
		
		var self = this;
		
		var option = {
			title: {text: '流量监控'},
			legend: {
		        data:['接受','发送']
		    },
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: self.data[0]
		    },
		    yAxis: {
		        type: 'value',
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value} KB'
		        }
		    },
		    series: [{
		        name: '接受',
		        type: 'line',
		        showSymbol: true,
		        hoverAnimation: false,
		        data: self.data[1]
		    },{
		        name: '发送',
		        type: 'line',
		        showSymbol: true,
		        hoverAnimation: false,
		        data: self.data[1]
		    }],
		    animation : false
		};
		
		Chart.loadLineChart('net_main', option, function(chart) {
	    	self.chart = chart;
	    });
	},
	
	// 加载数据
	load : function(stat, time) {
		var self = this;
		if (self.data[0].length >= 15) {
			self.data[0].shift();
			self.data[1].shift();
			self.data[2].shift();
		}
		self.data[0].push(time);
		self.data[1].push(stat.rxBytes);
		self.data[2].push(stat.txBytes);
		self.chart.setOption({xAxis:{data:self.data[0]}, series: [{data: self.data[1]}, {data: self.data[2]}]});
	}
};

//内存 监控
var PROC = {
		
	// 初始化
	init : function() {},
	
	// 加载数据
	load : function(stat) {
		var html = Public.runTemplate($('#procTemplate').html(), {datas: stat});
		$('#procs').html(html);
	}
};

//cpu 监控
var ACCESS = {
	
	//
    chart : null,
    
	// 数据
	data : [[],[]],
		
	// 初始化
	init : function() {
		
		var self = this;
		
		var option = {
			title: {text: '并发监控(3分钟之前)'},
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: self.data[0]
		    },
		    yAxis: {
		        type: 'value',
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value} 次'
		        }
		    },
		    series: [{
		        name: '请求',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        data: self.data[1]
		    }],
		    animation : false
		};
		
		Chart.loadLineChart('access_main', option, function(chart) {
			self.chart = chart;
	    });
		
		var self = this;
    	setInterval(function() {
    		Public.getAjax(webRoot + '/admin/system/monitor/access?step=3', {}, function(data) {
    			var stat = data.obj; var time = stat.time;
    			self.load(stat, time);
    		});
    	}, 3000);
	},
	
	// 加载数据
	load : function(stat, time) {
		var self = this;
		if (self.data[0].length >= 60) {
			self.data[0].shift();
			self.data[1].shift();
		}
		self.data[0].push(time);
		self.data[1].push(stat.access);
		self.chart.setOption({xAxis:{data:self.data[0]}, series: [{data: self.data[1]}]});
	}
};

//cpu 监控
var USER = {
	
	//
    chart : null,
    
	// 数据
	data : [[],[]],
		
	// 初始化
	init : function() {
		
		var self = this;
		
		var option = {
			title: {text: '活跃用户(3分钟之前)'},
			xAxis: {
		        type: 'category',
		        splitLine: {
		            show: true
		        },
		        data: self.data[0]
		    },
		    yAxis: {
		        type: 'value',
		        splitLine: {
		            show: false
		        },
		        axisLabel: {
		            formatter: '{value} 个'
		        }
		    },
		    series: [{
		        name: '请求',
		        type: 'line',
		        showSymbol: false,
		        hoverAnimation: false,
		        data: self.data[1]
		    }],
		    animation : false
		};
		
		Chart.loadLineChart('user_main', option, function(chart) {
			self.chart = chart;
	    });
		
		var self = this;
    	setInterval(function() {
    		Public.getAjax(webRoot + '/admin/system/monitor/user?step=10', {}, function(data) {
    			var stat = data.obj; var time = stat.time;
    			self.load(stat, time);
    		});
    	}, 10000);
	},
	
	// 加载数据
	load : function(stat, time) {
		var self = this;
		if (self.data[0].length >= 60) {
			self.data[0].shift();
			self.data[1].shift();
		}
		self.data[0].push(time);
		self.data[1].push(stat.access);
		self.chart.setOption({xAxis:{data:self.data[0]}, series: [{data: self.data[1]}]});
	}
};

// 统计
var Stat = {
	
    init : function() {
    	CPU.init();
    	MEM.init();
    	NET.init();
    	PROC.init();
    	ACCESS.init();
    	USER.init();
    	this.start();
    },
    
    // 开始
    start : function() {
    	var self = this;
    	setInterval(function() {
    		Public.getAjax(webRoot + '/admin/system/monitor/stat', {}, function(data) {
    			var stat = data.obj; var time = stat.time;
    			CPU.load(stat.cpu, time);
    			MEM.load(stat.mem, time);
    			NET.load(stat.net, time);
    			PROC.load(stat.proc);
    		});
    	}, 2000);
    }
}
$(function() {
	Stat.init();
});
</script>
<script type="text/html" id="procTemplate">
{{ for(var i = 0; i< datas.length; i++) { var proc = datas[i]; }}
   <tr>
      <td>{{=proc.pid}}</td>
	  <td>{{=proc.name}}</td>
	  <td>{{=proc.threads}}</td>
	  <td>{{=proc.total}}</td>
      <td>{{=proc.last}}</td>
   </tr>
{{ } }}
</script>
</body>
</html>