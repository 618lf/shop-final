<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>SQL统计</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/common/opa-icons.css" rel="stylesheet" />
<style type="text/css">
 #dataTable td {
   font-size: 14px;
   text-align: center;
 }
</style>
<script type="text/html" id="sqlTemplate">
{{ for(var i = 0; i< stats.length; i++ ) { }}
{{ var stat = stats[i], statSQL=stat.SQL; var newStatSQL=statSQL.replace(/\"/g,"'");}}
   <tr title="{{=newStatSQL}}">
      <td>{{=(i+1)}}</td>
      <td><a data-dismiss="alert" data-id="{{=stat.ID}}">SQL</a></td>
      <td>{{=stat.ExecuteCount}}</td>
      <td>{{=stat.TotalTime}}</td>
      <td>{{=stat.MaxTimespan}}</td>
      <td>{{=stat.InTransactionCount}}</td>
      <td>{{=stat.ErrorCount}}</td>
      <td>{{=stat.EffectedRowCount}}</td>
      <td>{{=stat.FetchRowCount}}</td>
      <td>{{=stat.RunningCount}}</td>
      <td>{{=stat.ConcurrentMax}}</td>
      <td>
          {{ for(var j = 0; j <stat.Histogram.length; j++ ) { }}
          {{=stat.Histogram[j]}} 
          {{ } }}
      </td>
      <td>
          {{ for(var j = 0; j <stat.ExecuteAndResultHoldTimeHistogram.length; j++ ) { }}
          {{=stat.ExecuteAndResultHoldTimeHistogram[j]}} 
          {{ } }}
      </td>
      <td>
          {{ for(var j = 0; j <stat.FetchRowCountHistogram.length; j++ ) { }}
          {{=stat.FetchRowCountHistogram[j]}} 
          {{ } }}
      </td>
      <td>
          {{ for(var j = 0; j <stat.EffectedRowCountHistogram.length; j++ ) { }}
          {{=stat.EffectedRowCountHistogram[j]}} 
          {{ } }}
      </td>
   </tr>
{{ } }} 
</script>
<style type="text/css">
</style>
</head>
<body class="white">
<div class="row-fluid wrapper">
   <div class="bills" style="width: 93%;">
	  <div class="widget-box transparent" id="list">
	    <div class='widget-header widget-header-flat'>
		    <h3><span class="icon32 icon-rssfeed"></span><small>SQL监控</small></h3>
	    </div>
	    <div class="widget-body">
		     <div class="widget-body-inner">
		     <div class="widget-main">
		        <table id="dataTable" class="table table-bordered table-striped responsive-utilities">
		          <thead>
		            <tr>
						<th>序号</th>
						<th width="60"><a id="th-SQL" >SQL</a></th>
						<th width="40" ><a id="th-ExecuteCount"  class="lang" data-langKey="ExecuteCount">ExecCount</a></th>
						<th width="40"><a id="th-TotalTime" class="lang" data-langKey="ExecuteTimeMillis">ExecTime</a></th>
						<th width="40" class="langTitle" data-langKey="MaxTimespanDesc" title="Execute Time Millis Max"><a id="th-MaxTimespan" class="lang" data-langKey="MaxTimespan">ExecMax</a></th>
						<th width="40" class="langTitle" data-langKey="InTransactionCountDesc" title="Execute In Transaction Count"><a id="th-InTransactionCount" class="lang" data-langKey="InTransactionCount">Txn</a></th>
						<th width="40" ><a id="th-ErrorCount" class="lang" data-langKey="ErrorCount">Error</a></th>
						<th width="40"><a id="th-EffectedRowCount" class="lang" data-langKey="JdbcUpdateCount">Update</a></th>
						<th width="40"><a id="th-FetchRowCount" class="lang" data-langKey="JdbcFetchRowCount">FetchRow</a></th>
						<th width="40"><a id="th-RunningCount" class="lang" data-langKey="RunningCount">Running</a></th>
						<th width="40"><a id="th-ConcurrentMax" class="lang" data-langKey="ConcurrentMax">Concurrent</a></th>
						<th align="left" width="100"><span class="lang" data-langKey="ExecHisto">ExecHisto</span> <br />[ 
							<a id="th-Histogram[0]" class="langTitle" data-langKey="count1ms" title="count of '0-1 ms'" >-</a>
							<a id="th-Histogram[1]" class="langTitle" data-langKey="count10ms" title="count of '1-10 ms'" >-</a>
							<a id="th-Histogram[2]" class="langTitle" data-langKey="count100ms" title="count of '10-100 ms'" >-</a>
							<a id="th-Histogram[3]" class="langTitle" data-langKey="count1s" title="count of '100ms-1 s'" >-</a>
							<a id="th-Histogram[4]" class="langTitle" data-langKey="count10s" title="count of '1-10 s'" >-</a>
							<a id="th-Histogram[5]" class="langTitle" data-langKey="count100s" title="count of '10-100 s'" >-</a>
							<a id="th-Histogram[6]" class="langTitle" data-langKey="count1000s" title="count of '100-1000 s'" >-</a>
							<a id="th-Histogram[7]" class="langTitle" data-langKey="countBg1000s" title="count of '> 1000 s'" >-</a> ]
						</th>
						<th align="left" width="100"><span class="lang" data-langKey="ExecRsHisto">ExecRsHisto</span> <br />[ 
							<a id="th-ExecuteAndResultHoldTimeHistogram[0]" class="langTitle" data-langKey="count1ms" title="count of '0-1 ms'" >-</a>
							<a id="th-ExecuteAndResultHoldTimeHistogram[1]" class="langTitle" data-langKey="count10ms" title="count of '1-10 ms'" >-</a>
							<a id="th-ExecuteAndResultHoldTimeHistogram[2]" class="langTitle" data-langKey="count100ms" title="count of '10-100 ms'" >-</a>
							<a id="th-ExecuteAndResultHoldTimeHistogram[3]" class="langTitle" data-langKey="count1s" title="count of '100ms-1s'" >-</a>
							<a id="th-ExecuteAndResultHoldTimeHistogram[4]" class="langTitle" data-langKey="count10s" title="count of '1-10 s'" >-</a>
							<a id="th-ExecuteAndResultHoldTimeHistogram[5]" class="langTitle" data-langKey="count100s" title="count of '10-100 s'" >-</a>
							<a id="th-ExecuteAndResultHoldTimeHistogram[6]" class="langTitle" data-langKey="count1000s" title="count of '100-1000 s'" >-</a>
							<a id="th-ExecuteAndResultHoldTimeHistogram[7]" class="langTitle" data-langKey="countBg1000s" title="count of '> 1000 s'" >-</a> ]
						</th>
						<th align="left" width="100"><span class="lang" data-langKey="FetchRowHisto">FetchRowHisto</span> <br />[ 
							<a id="th-FetchRowCountHistogram[0]" class="langTitle" data-langKey="fetch0" title="count of '0 FetchRow'" >-</a>
							<a id="th-FetchRowCountHistogram[1]" class="langTitle" data-langKey="fetch9" title="count of '1-9 FetchRow'" >-</a>
							<a id="th-FetchRowCountHistogram[2]" class="langTitle" data-langKey="fetch99" title="count of '10-99 FetchRow'" >-</a>
							<a id="th-FetchRowCountHistogram[3]" class="langTitle" data-langKey="fetch999" title="count of '100-999 FetchRow'" >-</a>
							<a id="th-FetchRowCountHistogram[4]" class="langTitle" data-langKey="fetch9999" title="count of '1000-9999 FetchRow'" >-</a>
							<a id="th-FetchRowCountHistogram[5]" class="langTitle" data-langKey="fetch99999" title="count of '> 9999 FetchRow'" >-</a> ]
						</th>
						<th align="left" width="100"><span class="lang" data-langKey="UpdateHisto">UpdateHisto</span> <br />[ 
							<a id="th-EffectedRowCountHistogram[0]" class="langTitle" data-langKey="update0" title="count of '0 UpdateCount'" >-</a>
							<a id="th-EffectedRowCountHistogram[1]" class="langTitle" data-langKey="update9" title="count of '1-9 UpdateCount'" >-</a>
							<a id="th-EffectedRowCountHistogram[2]" class="langTitle" data-langKey="update99" title="count of '10-99 UpdateCount'" >-</a>
							<a id="th-EffectedRowCountHistogram[3]" class="langTitle" data-langKey="update999" title="count of '100-999 UpdateCount'" >-</a>
							<a id="th-EffectedRowCountHistogram[4]" class="langTitle" data-langKey="update9999" title="count of '1000-9999 UpdateCount'" >-</a>
							<a id="th-EffectedRowCountHistogram[5]" class="langTitle" data-langKey="update99999" title="count of '> 9999 UpdateCount'" >-</a> ]
						</th>
					</tr>
		          </thead>
		          <tbody id="sqlShow"></tbody>
		        </table>
		     </div>
		     </div>
	    </div> 
	  </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxModules}/system/dbmonitor-lang.js" type="text/javascript" charset="utf8"></script>
<script type="text/javascript">
$(function(){
	Public.lang.init();
	var param = {
	   orderBy	 : 'SQL',
	   orderType : 'desc',
	   page : 1,
	   perPageCount : 100000,
	   setOrderBy : function(orderBy) {
		 this.orderBy = orderBy;  
	   }
	};
	var loadSqls = function() {
		Public.postAjax('${ctx}/system/dbmonitor/sql?orderBy=SQL&orderType=desc&page=1&perPageCount=1000000', param, function(data) {
			var content = data.obj;
			var _data = {stats:content}
			var html = Public.runTemplate($('#sqlTemplate').html(), _data);
			$('#sqlShow').html(html);
			Public.lang.trigger();
		});
	};
	$("#dataTable th a").click(function(obj) {
		param.setOrderBy(obj.target.id.substring(3))
	})
	loadSqls();
	setInterval(loadSqls,5000);
	
	//sql详情
	$(document).on('click', '[data-dismiss]',function(obj) {
		var url = '${ctx}/system/dbmonitor/sql/' + $(this).data('id');
		Public.openOnTab('sql-detail', 'SQL详情', url);
	})
});
</script>
</div>
</body>
</html>