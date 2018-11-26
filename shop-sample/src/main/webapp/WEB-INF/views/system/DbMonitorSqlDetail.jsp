<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>报表中心 - 行业统计</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/common/opa-icons.css" rel="stylesheet" />
<style type="text/css">
 .table td {
   font-size: 14px;
 }
 .table td.td_lable {
   background-color: #E5E5E5;
 }
</style>
</head>
<body class="white">
<div class="row-fluid wrapper">
   <div class="bills" style="width: 93%;">
	  <div class="widget-box transparent" id="list">
	    <div class='widget-header widget-header-flat'>
		    <h3><span class="icon32 icon-rssfeed"></span><small>SQL详情</small></h3>
	    </div>
	    <div class="widget-body">
		     <div class="widget-body-inner">
		     <div class="widget-main">
		      <h5 id="fullSql">${sql.SQL}</h5>
		      <h2> Format View:</h2>
			  <textarea style='width:99%;height:120px;;border:1px #A8C7CE solid;line-height:20px;font-size:12px;' id="formattedSql">${sql.formattedSql} </textarea>
			  <br/>
		      <br/>
		      <h3>ParseView:</h3>
		      <table class="table table-bordered" style="background-color: #fff">		
		      	<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="Tables"> Tables</td>
					<td id="parsedtable">${sql.parsedTable}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="Fields">Fields</td>
					<td id="parsedfields">${sql.parsedFields}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="Coditions">Coditions</td>
					<td id="parsedConditions">${sql.parsedConditions}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="Relationships">Relationships</td>
					<td id="parsedRelationships">${sql.parsedRelationships}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="OrderByColumns">OrderByColumns</td>
					<td id="parsedOrderbycolumns">${sql.parsedOrderbycolumns}</td>
				</tr>
			  </table>
			  <h3>LastSlowView:</h3>
			  <table class="table table-bordered" style="background-color: #fff">		
			    <tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="MaxTimespan">MaxTimespan</td>
					<td id="MaxTimespan">${sql.MaxTimespan}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="MaxTimespanOccurTime">MaxTimespanOccurTime</td>
					<td id="MaxTimespanOccurTime">${sql.MaxTimespanOccurTime}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="LastSlowParameters">LastSlowParameters</td>
					<td id="LastSlowParameters">${sql.LastSlowParameters}</td>
				</tr>
			  </table>
			  <h3>LastErrorView:</h3>
			  <table class="table table-bordered" style="background-color: #fff">		<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="LastErrorMessage">LastErrorMessage</td>
					<td id="LastErrorMessage">${sql.LastErrorMessage}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="LastErrorClass">LastErrorClass</td>
					<td id="LastErrorClass">${sql.LastErrorClass}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="LastErrorTime">LastErrorTime</td>
					<td id="LastErrorTime">${sql.LastErrorTime}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="LastErrorStackTrace">LastErrorStackTrace</td>
					<td id="LastErrorStackTrace">${sql.LastErrorStackTrace}</td>
				</tr>
			  </table>
			  <h3>OtherView:</h3>
			  <table class="table table-bordered" style="background-color: #fff">		
			    <tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="BatchSizeMax">BatchSizeMax</td>
					<td id="BatchSizeMax">${sql.BatchSizeMax}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="BatchSizeTotal">BatchSizeTotal</td>
					<td id="BatchSizeTotal">${sql.BatchSizeTotal}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="BlobOpenCount">BlobOpenCount</td>
					<td id="BlobOpenCount">${sql.BlobOpenCount}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="ClobOpenCount">ClobOpenCount</td>
					<td id="ClobOpenCount">${sql.ClobOpenCount}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="ReaderOpenCount">ReaderOpenCount</td>
					<td id="ReaderOpenCount">${sql.ReaderOpenCount}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="InputStreamOpenCount">InputStreamOpenCount</td>
					<td id="InputStreamOpenCount">${sql.InputStreamOpenCount}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="ReadStringLength">ReadStringLength</td>
					<td id="ReadStringLength">${sql.ReadStringLength}</td>
				</tr>
				<tr>
					<td class='td_lable' width='160'  class="lang" data-langKey="ReadBytesLength">ReadBytesLength</td>
					<td id="ReadBytesLength">${sql.ReadBytesLength}</td>
				</tr>
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
	Public.lang.trigger();
});
</script>
</div>
</body>
</html>