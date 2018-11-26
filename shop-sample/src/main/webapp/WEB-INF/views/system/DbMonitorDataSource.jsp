<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>数据源统计</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/common/opa-icons.css" rel="stylesheet" />
<style type="text/css">
 h4 { margin-bottom: 15px;}
 .td_lable {
   background-color: #E5E5E5;
 }
 .table td {
   font-size: 14px;
 }
</style>
<script type="text/html" id="dataSourceTemplate">
  <h4>Basic Info For {{=datasourceNow.Name}}</h4>
  <table class="table table-bordered" style="background-color: #fff">
    <tbody>
        <tr>
	      <td valign="top" class="td_lable" > *  <span class="lang"  langKey="UserName">UserName</span></td>
	      <td>{{=datasourceNow.UserName}}</td>
	      <td  class="lang"  langKey="UserNameDesc">Specify the username used when creating a new connection.</td>
	    </tr>
        <tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="URL">URL</span></td>
			<td>{{=datasourceNow.URL}}</td>
			<td  class="lang"  langKey="URLDesc">The JDBC driver connection URL</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="DbType">DbType</span></td>
			<td>{{=datasourceNow.DbType}}</td>
			<td  class="lang"  langKey="DbTypeDesc">database type</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="DriverClassName">DriverClassName</span></td>
			<td>{{=datasourceNow.DriverClassName}}</td>
			<td  class="lang"  langKey="DriverClassNameDesc">The fully qualifed name of the JDBC driver class</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="FilterClassNames">FilterClassNames</span></td>
			<td>{{ for(var i = 0 ; i < datasourceNow.FilterClassNames.length; i++ ) { }}
                {{=datasourceNow.FilterClassNames[i]}}
                {{ } }}
            </td>
			<td  class="lang"  langKey="FilterClassNamesDesc">All the fully qualifed name of the filter classes</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="TestOnBorrow">TestOnBorrow</span></td>
			<td>{{=datasourceNow.TestOnBorrow}}</td>
			<td  class="lang"  langKey="TestOnBorrowDesc">	Test or not when borrow a connection</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="TestWhileIdle">TestWhileIdle</span></td>
			<td>{{=datasourceNow.TestWhileIdle}}</td>
			<td  class="lang"  langKey="TestWhileIdleDesc">Test or not when a connection is idle for a while</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="TestOnReturn">TestOnReturn</span></td>
			<td>{{=datasourceNow.TestOnReturn}}</td>
			<td  class="lang"  langKey="TestOnReturnDesc">Test or not when return a connection</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="InitialSize">InitialSize</span></td>
			<td>{{=datasourceNow.InitialSize}}</td>
			<td  class="lang"  langKey="InitialSizeDesc">The size of datasource connections to create when initial a datasource</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="MinIdle">MinIdle</span></td>
			<td>{{=datasourceNow.MinIdle}}</td>
			<td  class="lang"  langKey="MinIdleDesc">The minimum number of connections a pool should hold. </td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="MaxActive">MaxActive</span></td>
			<td>{{=datasourceNow.MaxActive}}</td>
			<td  class="lang"  langKey="MaxActiveDesc">The maximum number of connections for a pool</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="QueryTimeout">QueryTimeout</span></td>
			<td>{{=datasourceNow.QueryTimeout}}</td>
			<td  class="lang"  langKey="QueryTimeoutDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="TransactionQueryTimeout">TransactionQueryTimeout</span></td>
			<td>{{=datasourceNow.TransactionQueryTimeout}}</td>
			<td  class="lang"  langKey="TransactionQueryTimeoutDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="LoginTimeout">LoginTimeout</span></td>
			<td>{{=datasourceNow.LoginTimeout}}</td>
			<td  class="lang"  langKey="LoginTimeoutDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="ValidConnectionCheckerClassName">ValidConnectionCheckerClassName</span></td>
			<td>{{=datasourceNow.ValidConnectionCheckerClassName}}</td>
			<td  class="lang"  langKey="ValidConnectionCheckerClassNameDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="ExceptionSorterClassName">ExceptionSorterClassName</span></td>
			<td>{{=datasourceNow.ExceptionSorterClassName}}</td>
			<td  class="lang"  langKey="ExceptionSorterClassNameDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="DefaultAutoCommit">DefaultAutoCommit</span></td>
			<td>{{=datasourceNow.DefaultAutoCommit}}</td>
			<td  class="lang"  langKey="DefaultAutoCommitDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="DefaultReadOnly">DefaultReadOnly</span></td>
			<td>{{=datasourceNow.DefaultReadOnly}}</td>
			<td  class="lang"  langKey="DefaultReadOnlyDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" > *  <span class="lang"  langKey="DefaultTransactionIsolation">DefaultTransactionIsolation</span></td>
			<td>{{=datasourceNow.DefaultTransactionIsolation}}</td>
			<td  class="lang"  langKey="DefaultTransactionIsolationDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="NotEmptyWaitCount">NotEmptyWaitCount</span></td>
			<td>{{=datasourceNow.NotEmptyWaitCount}}</td>
			<td  class="lang"  langKey="NotEmptyWaitCountDesc">Total times for wait to get a connection</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="NotEmptyWaitMillis">NotEmptyWaitMillis</span></td>
			<td>{{=datasourceNow.NotEmptyWaitMillis}}</td>
			<td  class="lang"  langKey="NotEmptyWaitMillisDesc">Total millins for wait to get a connection</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="WaitThreadCount">WaitThreadCount</span></td>
			<td>{{=datasourceNow.WaitThreadCount}}</td>
			<td  class="lang"  langKey="WaitThreadCountDesc">The current waiting thread count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="StartTransactionCount">StartTransactionCount</span></td>
			<td>{{=datasourceNow.StartTransactionCount}}</td>
			<td  class="lang"  langKey="StartTransactionCountDesc">The count of start transaction</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="TransactionHistogram">TransactionHistogram</span></td>
			<td>{{=datasourceNow.TransactionHistogram}}</td>
			<td  class="lang"  langKey="TransactionHistogramDesc">The histogram values of transaction time, [0-10 ms, 10-100 ms, 100-1 s, 1-10 s, 10-100 s, >100 s]</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PoolingCount">PoolingCount</span></td>
			<td>{{=datasourceNow.PoolingCount}}</td>
			<td  class="lang"  langKey="PoolingCountDesc">The current usefull connection count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PoolingPeak">PoolingPeak</span></td>
			<td>{{=datasourceNow.PoolingPeak}}</td>
			<td  class="lang"  langKey="PoolingPeakDesc">The usefull connection peak count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PoolingPeakTime">PoolingPeakTime</span></td>
			<td>{{=datasourceNow.PoolingPeakTime}}</td>
			<td  class="lang"  langKey="PoolingPeakTimeDesc">The usefull connection peak time</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="ActiveCount">ActiveCount</span></td>
			<td>{{=datasourceNow.ActiveCount}}</td>
			<td  class="lang"  langKey="ActiveCountDesc">The current active connection count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="ActivePeak">ActivePeak</span></td>
			<td>{{=datasourceNow.ActivePeak}}</td>
			<td  class="lang"  langKey="ActivePeakDesc">The current active connection peak count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="ActivePeakTime">ActivePeakTime</span></td>
			<td>{{=datasourceNow.ActivePeakTime}}</td>
			<td  class="lang"  langKey="ActivePeakTimeDesc">The active connection peak time</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="LogicConnectCount">LogicConnectCount</span></td>
			<td>{{=datasourceNow.LogicConnectCount}}</td>
			<td  class="lang"  langKey="LogicConnectCountDesc">Total connect times from datasource</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="LogicCloseCount">LogicCloseCount</span></td>
			<td>{{=datasourceNow.LogicCloseCount}}</td>
			<td  class="lang"  langKey="LogicCloseCountDesc">Total close connect times from datasource</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="LogicConnectErrorCount">LogicConnectErrorCount</span></td>
			<td>{{=datasourceNow.LogicConnectErrorCount}}</td>
			<td  class="lang"  langKey="LogicConnectErrorCountDesc">Total connect error times</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PhysicalConnectCount">PhysicalConnectCount</span></td>
			<td>{{=datasourceNow.PhysicalConnectCount}}</td>
			<td  class="lang"  langKey="PhysicalConnectCountDesc">Create physical connnection count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PhysicalCloseCount">PhysicalCloseCount</span></td>
			<td>{{=datasourceNow.PhysicalCloseCount}}</td>
			<td  class="lang"  langKey="PhysicalCloseCountDesc">Close physical connnection count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PhysicalConnectErrorCount">PhysicalConnectErrorCount</span></td>
			<td>{{=datasourceNow.PhysicalConnectErrorCount}}</td>
			<td  class="lang"  langKey="PhysicalConnectErrorCountDesc">Total physical connect error times</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="ExecuteCount">ExecuteCount</span></td>
			<td>{{=datasourceNow.ExecuteCount}}</td>
			<td  class="lang"  langKey="ExecuteCountDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="ErrorCount">ErrorCount</span></td>
			<td>{{=datasourceNow.ErrorCount}}</td>
			<td  class="lang"  langKey="ErrorCountDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="CommitCount">CommitCount</span></td>
			<td>{{=datasourceNow.CommitCount}}</td>
			<td  class="lang"  langKey="CommitCountDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="RollbackCount">RollbackCount</span></td>
			<td>{{=datasourceNow.RollbackCount}}</td>
			<td  class="lang"  langKey="RollbackCountDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PSCacheAccessCount">PSCacheAccessCount</span></td>
			<td>{{=datasourceNow.PSCacheAccessCount}}</td>
			<td  class="lang"  langKey="PSCacheAccessCountDesc">PerpareStatement access count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PSCacheHitCount">PSCacheHitCount</span></td>
			<td>{{=datasourceNow.PSCacheHitCount}}</td>
			<td  class="lang"  langKey="PSCacheHitCountDesc">PerpareStatement hit count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="PSCacheMissCount">PSCacheMissCount</span></td>
			<td>{{=datasourceNow.PSCacheMissCount}}</td>
			<td  class="lang"  langKey="PSCacheMissCountDesc">PerpareStatement miss count</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="ConnectionHoldTimeHistogram">ConnectionHoldTimeHistogram</span></td>
			<td>{{=datasourceNow.ConnectionHoldTimeHistogram}}</td>
			<td  class="lang"  langKey="ConnectionHoldTimeHistogramDesc">The histogram values of connection hold time, [0-1 ms, 1-10 ms, 10-100 ms, 100ms-1s, 1-10 s, 10-100 s, 100-1000 s, >1000 s]</td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="ClobOpenCount">ClobOpenCount</span></td>
			<td>{{=datasourceNow.ClobOpenCount}}</td>
			<td  class="lang"  langKey="ClobOpenCountDesc"></td>
		</tr>
		<tr>
			<td valign="top" class="td_lable" >   <span class="lang"  langKey="BlobOpenCount">BlobOpenCount</span></td>
			<td>{{=datasourceNow.BlobOpenCount}}</td>
			<td  class="lang"  langKey="BlobOpenCountDesc"></td>
		</tr>
    </tbody>
  </table>
</script>
</head>
<body class="white">
<div class="row-fluid wrapper">
   <div class="bills" style="width: 93%;">
	  <div class="widget-box transparent" id="list">
	    <div class='widget-header widget-header-flat'>
		    <h3><span class="icon32 icon-rssfeed"></span><small>数据源</small></h3>
	    </div>
	    <div class="widget-body">
		     <div class="widget-body-inner">
		     <div class="widget-main" id="dataSourceShow"></div>
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
	Public.postAjax('${ctx}/system/dbmonitor/datasource',null, function(data) {
		var content = data.obj[0];
		var _data = {datasourceNow:content}
		var html = Public.runTemplate($('#dataSourceTemplate').html(), _data);
		$('#dataSourceShow').html(html);
		Public.lang.trigger();
	});
});
</script>
</body>
</html>