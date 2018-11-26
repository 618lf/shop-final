<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>图文回复</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/wechat.css" rel="stylesheet"/>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>图文回复<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="metaRich" action="${ctx}/wechat/meta/rich/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<input type="hidden" id="postData" name="postData">
		<div class="control-group formSep">
			<label class="control-label">公众号<span class="red">*</span>:</label>
			<div class="controls">
		      <form:select path="appId" items="${apps}" itemLabel="name" itemValue="id" cssClass="required"></form:select>
		      <div class="control-tip">所属公众号</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">关键词<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="keyword" htmlEscape="false" maxlength="255" class="required iTag"/>
		      <div class="control-tip">关键词用于匹配回复，支持填入多关键词。例如：电话,联系,地址。</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">标题<span class="red">*</span>:</label>
			<div class="controls">
		      <form:input path="title" htmlEscape="false" maxlength="255" class="required " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">摘要（选填）:</label>
			<div class="controls">
		      <form:textarea path="remarks"  maxlength="255" rows="4"  class=" "/>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">图片<span class="red">*</span>:</label>
			<div class="controls">
                 <tags:attachment name="image" value="${metaRich.image}"/>
                 <div class="control-tip">为了达到更好的显示效果，建议图片尺寸为：720(宽) × 400(高)。</div> 
			</div>
			<div class="controls" style="margin-top: 5px;">
			  <label class='checkbox inline'>
			      <form:checkbox path="topImage" value="1"/> 将图片显示到正文中
			  </label>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">正文<span class="red">*</span>:</label>
			<div class="controls">
		      <form:textarea path="content" maxlength="4294967295" cssStyle="width:100%; height:220px;" rows="4"  class="required "/>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">作者:</label>
			<div class="controls">
		      <form:input path="author" htmlEscape="false" maxlength="100" class=" " />
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">原文链接:</label>
			<div class="controls">
		      <form:input path="source" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">多图文:</label>
			<div class="controls">
			   <div class="multi-meta">
			      <a class="add-ops"><i class="iconfont icon-plus"></i>添加多图文</a>
			      <div class="metas">
			        <c:forEach items="${metaRich.relas}" var="rela">
			        <div class="item" data-id="${rela.relaId}"><a>${rela.relaName}</a><span class="ops"><i class="iconfont icon-shanglajiantou up"></i><i class="iconfont icon-xialajiantou down"></i><i class="iconfont icon-remove remove"></i></span></div>
			        </c:forEach>
			      </div>
			   </div>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="cancelBtn" class="btn" type="button" value="关闭"/>
		</div>
	</form:form>
  </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="/static/ueditor/ueditor.config.js" type="text/javascript"></script>
<script src="/static/ueditor/ueditor.all.min.js" type="text/javascript"></script>
<script src="/static/ueditor/lang/zh-cn/zh-cn.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.addEvent();
	},
	bindValidate : function() {
		$("#title").focus();
		$("#inputForm").validate(
			Public.validate({
				submitHandler: function(form){
					Public.loading('正在提交，请稍等...');
					// 拼接多图文
					var metas = [];
					$('.metas>.item').each(function() {
						var id = $(this).data('id');
						metas.push({
							relaId: id
						})
					});
					var postData = JSON.stringify(metas);
					$('#postData').val(postData);
					form.submit();
				}
			})
		);
	},
	addEvent : function() {
	    Public.simpleUEditor('content');    
        $(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
        
        // 列表数据
        var items = [];
        
        // 添加子图文
        $(document).on('click', '.add-ops', function() {
        	Public.tableSelect('${ctx}/wechat/meta/rich/tableSelect?extId=${metaRich.id}', '选择子图文（最多选择9个）', 800, 500, function(iframe, ids, names) {
        		if (!!ids) {
        			for(var i=0; i< ids.length; i++) {
        				var id = ids[i];
        				if (!!items[id]) {continue;}
        				
        				// 存储值
        				items[id] = id;
        				
        				// 显示出来
        				var html = Public.runTemplate($('#itemTemplate').html(), {rela: {relaId: ids[i], relaName: names[i]}});
        				$('.metas').append(html);
        			}
        		}
        	});
        });
        
        // 删除
        $(document).on('click', '.remove', function() {
        	var item = $(this).closest('.item');
        	if (item.lenght != 0) {
        		var id = item.data('id');
        		items[id] = false;
        		item.remove();
        	}
        });
        
        // 排序
        $(document).on('click', '.up', function() {
        	var item = $(this).closest('.item');
        	var prev = item.prev();
        	if (prev.length != 0) {
        	    item.remove().insertBefore(prev);
        	}
        });
        
        // 排序
        $(document).on('click', '.down', function() {
        	var item = $(this).closest('.item');
        	var next = item.next();
        	if (next.length != 0) {
        	    item.remove().insertAfter(next);
        	}
        })
	}
};
$(function(){
	THISPAGE._init();
});
</script>
<script type="text/html" id="itemTemplate">
<div class="item" data-id="{{=rela.relaId}}"><a>{{=rela.relaName}}</a><span class="ops"><i class="iconfont icon-shanglajiantou up"></i><i class="iconfont icon-xialajiantou down"></i><i class="iconfont icon-remove remove"></i></span></div>
</script>
</body>
</html>
