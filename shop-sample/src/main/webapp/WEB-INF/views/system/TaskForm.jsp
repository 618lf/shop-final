<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
.time-info-wrap { 
  font-size: 14px;
  padding: 10px;
}
</style>
</head>
<body>
<div class="row-fluid wrapper">
    <div class="bills">
      <div class="page-header">
       <h3>任务管理 <small> &gt;&gt; 编辑</small></h3>
      </div>
      <form:form id="inputForm" modelAttribute="task" action="${ctx}/system/task/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<div class="control-group formSep">
			<label class="control-label">任务名称<span class="red">*</span>:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">调度时间:</label>
			<div class="controls">
			    <form:input path="cronExpression" htmlEscape="false" maxlength="200"/>
			    <a id="timeTip" style="margin-left: 5px; text-decoration: underline;">如何设置时间？</a>
			</div>
		</div>
		<div class="control-group formSep hide">
			<label class="control-label">允许执行的次数:</label>
			<div class="controls">
				<form:input path="allowExecuteCount" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">业务对象:</label>
			<div class="controls">
				<form:select path="businessObject" items="${business}" itemLabel="label" itemValue="value" cssClass="iSelect"/>
			    <a style="margin-left: 5px;" class="refreshBtn"><i class="iconfont icon-refresh"></i></a>
			</div>
		</div> 
		<div class="control-group formSep">
			<label class="control-label">允许并行执行:</label>
			<div class="controls">
				<form:select path="concurrent">
				  <form:option value="0">否</form:option>
				  <form:option value="1">是</form:option>
				</form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">状态:</label>
			<div class="controls" id="taskStatusBar">${task.taskStatusName}</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>
		    <c:if test="${task.id != '-1'}">
			<input id="btnStart" class="btn btn-success" type="button" value="启 动"/>
			<input id="btnExecute" class="btn btn-success" type="button" value="执 行"/>
			<input id="btnPause" class="btn btn-warning" type="button" value="暂 停"/>
			<input id="btnStop" class="btn btn-danger" type="button" value="停 止"/>
			</c:if>
			<input id="cancelBtn" class="btn" type="button" value="返 回"/>
		</div>
	  </form:form>
    </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function(){
		$("#name").focus();
		$("#inputForm").validate(Public.validate());
	},
	addEvent : function(){
		$(document).on('click','#btnStart',function(){
			Public.executex("确定启动任务？","${ctx}/system/task/start",{id:'${task.id}'},function(data){
				if(data) {
					Public.doQuery();
				} else {
					Public.error('任务执行表达式填写不正确!');
				}
			});
		});
		$(document).on('click','#btnPause',function(){
			Public.executex("确定暂停任务？","${ctx}/system/task/pause",{id:'${task.id}'},function(data){
				if(data) {
					Public.doQuery();
				}
			});
		});
		$(document).on('click','#btnStop',function(){
			Public.executex("确定停止任务？","${ctx}/system/task/stop",{id:'${task.id}'},function(data){
				if(data) {
					Public.doQuery();
				}
			});
		});
		$(document).on('click','#btnExecute',function(){
			Public.openWindow("确定执行任务<span style='color:red'>【可以输入参数】</span>", $("#paramsTemplate").html(), 380,250, function() {
				var postData = $('#postData').val();
				Public.executexQuietly("${ctx}/system/task/execute",{id:'${task.id}', 'postData': postData},function(data){
					if(data) {
					   $('#taskStatusBar').text('执行中');
					   Public.delayPerform(Public.doQuery, 1000);
					}
				});
			});
		});
		$(document).on('click','#cancelBtn',function(){
			window.location.href = "${ctx}/system/task/list";
		});
		
		$(document).on('click', '#timeTip', function() {
			var html = $('#timeTemplate').html();
			Public.openViewWindow(html, 500, 460);
		});
		
		// 清除缓存
		$(document).on('click','.refreshBtn',function(){
			Public.postAjax('${ctx}/system/task/refresh', {}, function() {
				Public.success('刷新成功！');
			});
		});
		
		//获得名称
		$('#businessObject').on('change', function(e){
			var text = $(this).select2('data').text;
			if(!$('#name').val()) {
			   $('#name').val(text);
			}
		});
	},
	progress : function(timer) {
		Public.getAjax('${ctx}/system/task/progress/${task.id}', null, function(data) {
			var task = data.obj;
			if(!!task && !!task.progress) {
			   var progress = task.progress;
			   var html = Public.runTemplate($('#progressTemplate').html(), {progress: progress});
			   $('#taskStatusBar').html(html);
			} else {
			   $('#taskStatusBar').html(data.taskStatusName);
			}
			if(task.taskStatus != 'RUNNING' && !task.progress) {clearTimeout(timer);}
		});
	}
};
$(function(){
	THISPAGE._init();
	<c:if test="${task.id != '-1' && task.id != '' && task.taskStatus == 'RUNNING'}">
	Public.setInterval(function(timer) {
		THISPAGE.progress(timer);
	}, 1000);
	</c:if>
});	
</script>
<script type="text/html" id="paramsTemplate">
<div class="row-fluid">
  <div style="padding:10px;">
      <div>可以输入参数：</div>
      <textarea name="postData" id="postData" class="required" style="width:100%; height:100px;"></textarea>
  </div>
</div>
</script>
<script type="text/html" id="timeTemplate">
<div class="time-info-wrap">
<table class="table simple-table table-bordered time-info-table"><tbody><tr><th>时间</th><th>描述</th></tr><tr><td class="colorTd">0/10 * * * * ?</td><td class="colorTd">每10秒触发</td></tr><tr><td class="colorTd">0 0/3 * ? * *</td><td class="colorTd">每3分钟触发</td></tr><tr><td>0 0 12 * * ?</td><td>每天中午12点触发</td></tr><tr><td class="colorTd">0 15 10 ? * *</td><td class="colorTd">每天上午10:15触发 </td></tr><tr><td>0 15 10 * * ?</td><td>2005 2005年的每天上午10:15触发 </td></tr><tr><td class="colorTd">0 15 10 * * ? *</td><td class="colorTd">每天上午10:15触发 </td></tr><tr><td>0 * 14 * * ?</td><td>在每天下午2点到下午2:59期间的每1分钟触发 </td></tr><tr><td class="colorTd">0 0/5 14 * * ?</td><td class="colorTd"> 在每天下午2点到下午2:55期间的每5分钟触发 </td></tr><tr><td>0 0/5 14,18 * * ?</td><td>在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发</td></tr><tr><td class="colorTd">0 0-5 14 * * ?</td><td class="colorTd">在每天下午2点到下午2:05期间的每1分钟触发 </td></tr><tr><td>0 10,44 14 ? 3 WED</td><td>每年三月的星期三的下午2:10和2:44触发 </td></tr><tr><td class="colorTd">0 15 10 ? * MON-FRI</td><td class="colorTd">周一至周五的上午10:15触发 </td></tr><tr><td>0 15 10 15 * ?</td><td>每月15日上午10:15触发 </td></tr><tr><td class="colorTd">0 15 10 L * ?</td><td class="colorTd">每月最后一日的上午10:15触发 </td></tr><tr><td>0 15 10 ? * 6L</td><td>每月的最后一个星期五上午10:15触发 </td></tr><tr><td class="colorTd">0 15 10 ? * 6L 2002-2005</td><td class="colorTd">2002年至2005年的每月的最后一个星期五上午10:15触发</td></tr><tr><td>0 15 10 ? * 6#3</td><td>每月的第三个星期五上午10:15触发</td></tr><tr><td class="colorTd">0 0 06,18 * * ?</td><td class="colorTd">在每天上午6点和下午6点触发</td></tr><tr><td>0 30 5 * * ? *</td><td>在每天上午5:30触发</td></tr></tbody></table>
</div>
</script>
<script type="text/html" id="progressTemplate">
<div style="line-height:30px;font-size:16px;"><span>执行中</span>（<b>{{=progress.progress}}%</b>）</div>
<div><span>开始时间：</span>{{=progress.startDate}}</div>
<div><span>结束时间：</span>{{=progress.endDate}}</div>
<div><span>剩余时间：</span>{{=progress.prettySeconds}}</div>
</script>
</body>
</html>