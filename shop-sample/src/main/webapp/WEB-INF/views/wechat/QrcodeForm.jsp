<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>二维码</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/wechat.css" rel="stylesheet" />
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
		<h3>二维码<small> &gt;&gt; 编辑</small></h3>
	</div>
	<form:form id="inputForm" modelAttribute="qrcode" action="${ctx}/wechat/qrcode/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="type"/>
		<form:hidden path="config"/>
		<form:hidden path="actionName"/>
           <div class="control-group formSep">
			<label class="control-label">公众号<span class="red">*</span>:</label>
			<div class="controls">
			  <c:if test="${qrcode.id != -1}">
		      <tags:treeselect url="${ctx}/wechat/app/treeSelect" id="appId" name="appName" idValue="${qrcode.appId}" 
		            nameValue="${qrcode.appName}" title="微信公众号" disabled="disabled"/>
		      </c:if>
		      <c:if test="${qrcode.id == -1}">
		      <tags:treeselect url="${ctx}/wechat/app/treeSelect" id="appId" name="appName" idValue="${qrcode.appId}" 
		            nameValue="${qrcode.appName}" title="微信公众号"/>
		      </c:if>
		      <div class="control-tip">保存之后不能修改</div>
			</div>
		</div>
           <div class="control-group formSep">
			<label class="control-label">二维码唯一标识<span class="red">*</span>:</label>
			<div class="controls">
			  <c:if test="${qrcode.id != -1}">
		      <form:input path="sceneStr" htmlEscape="false" maxlength="32" class="required " readonly="true"/>
		      </c:if>
		      <c:if test="${qrcode.id == -1}">
		      <form:input path="sceneStr" htmlEscape="false" maxlength="32" class="required " />
		      </c:if>
		      <div class="control-tip">用户扫码后，微信推送此字符串到服务器；保存之后不能修改</div>
			</div>
		</div>
		<c:if test="${not empty qrcode.image}">
		<div class="control-group formSep">
			<label class="control-label">二维码图片:</label>
			<div class="controls qrcode-image">
			   <div class="image-wrap"><img alt="" src="${qrcode.image}"></div>
			   <div class="control-tip">扫描此二维码可以测试消息推送</div>
			</div>
		</div>
		</c:if>
           <div class="control-group formSep">
			<label class="control-label">描述:</label>
			<div class="controls">
		      <form:input path="remarks" htmlEscape="false" maxlength="255" class=" " />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">扫码时:</label>
			<div class="controls menuConfig">
	          <ul class="nav nav-tabs">
	             <li class="active" data-type="0"><a data-tab="config-none">不回复</a></li>
	             <li data-type="1"><a data-tab="config-text">文本回复</a></li>
	             <li data-type="2"><a data-tab="config-rich">图文回复</a></li>
	             <li data-type="3"><a data-tab="config-voice">语音回复</a></li>
	             <li data-type="4"><a data-tab="config-pic">图片回复</a></li>
	          </ul>
	          <div class="tab-content">
	             <div class="tab-pane active" data-id="config-none">
	                当粉丝扫码后，可以选择发送一条指定的回复，或不做任何回复
				 </div>
				 <div class="tab-pane" data-id="config-text">
				    <div class="config-content iInputLimit-wrap">
				       <textarea class="config-content-textarea -config iInputLimit" placeholder="填写文本回复" maxlength="600" style="overflow: hidden; word-wrap: break-word; height: 120px;"></textarea>
				       <div class="config-content-bar">
				          <a class="-qq-emo -tools" title="插入表情"><i class="iconfont icon-biaoqing"></i></a>
				          <a class="-set-link -tools" title="插入链接"><i class="iconfont icon-lianjie"></i></a>
				          <div class="pull-right word-num">还能输入<span class="-num">600</span> / <span class="-total">600</span>字</div>
				       </div>
				    </div>
				 </div>
	             <div class="tab-pane" data-id="config-rich">
	                <div class="multi-meta">
				      <div class="metas"></div>
				      <a class="add-ops meta-select"><i class="iconfont icon-plus"></i>添加多图文</a>
				    </div>
	             </div>
				 <div class="tab-pane" data-id="config-voice">暂不支持</div>
				 <div class="tab-pane" data-id="config-pic">暂不支持</div>
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
<script src="${ctxModules}/system/wechat.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.bindValidate();
		this.showSetting();
		this.addEvent();
	},
	bindValidate : function() {
		var that = this;
		$("#title").focus();
		$("#inputForm").validate(
			Public.validate({
				submitHandler: function(form){
					Public.loading('正在提交，请稍等...');
					that.getSetting();
					form.submit();
				},
				rules:{
					sceneStr:{
						 remote: {
							    url: "${ctx}/wechat/qrcode/check/sceneStr", 
							    type: "post",  
							    dataType: "json", 
							    data: {     
							    	id: function() {
							            return $("#id").val();
							        },
							        sceneStr: function() {
							            return $("#sceneStr").val();
							        }
							    }
						 }
					},
				},
				messages: {
					sceneStr:{remote:'唯一编码重复！'}
				}
			})
		);
	},
	getSetting : function() {
		var atype = $('.menuConfig .active').data('type');
		var atab = $('.menuConfig .active>[data-tab]').data('tab');
		var aconfig = $('.menuConfig .tab-pane[data-id="'+atab+'"]').find('.-config').val();
		
		$('#type').val(atype);
		$('#config').val(aconfig);
	},
	showSetting : function() {
		
       var showConfig = function(type, config) {
			// 显示
			$('.menuConfig').find('.active').removeClass('active');
			$('.menuConfig').find('[data-type="'+type+'"]').addClass('active');
			
			// 设置值
			var tab = $('.menuConfig').find('.active>[data-tab]').data('tab');
			$('.menuConfig').find('.tab-pane[data-id="'+tab+'"]').addClass('active');
			if (type == 1) {
				$('.menuConfig').find('.tab-pane.active  .-config').val(config);
				$('.menuConfig').find('.iInputLimit').trigger('input');
			} else if (type == 2) {
				if (!!config) {
					var html = Meta_Rich.render(config);
					$('.menuConfig').find('.metas').html(html);
				}
			}
		};
		
		// 显示配置
		var type = $('#type').val();
		var config = $('#config').val();
		showConfig(type, config);
	},
	addEvent : function() {
		// 配置切换
		$(document).on('click', '[data-tab]', function() {
			var tab = $(this).data('tab');
			var menuConfig = $(this).closest('.menuConfig');
			menuConfig.find('.active').removeClass('active');
			$(this).parent().addClass('active');
			menuConfig.find('.tab-pane[data-id="'+tab+'"]').addClass('active');
		});
		
        $(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>
