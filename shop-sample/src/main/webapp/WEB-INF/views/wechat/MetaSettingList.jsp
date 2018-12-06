<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>默认回复配置</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxModules}/system/wechat.css" rel="stylesheet" />
</head>
<body class="no-fixed-btns">
<div class="wechat-menu">
  <div class="tabs-left menu-left">
  <ul class="nav nav-tabs apps">
    <c:forEach items="${apps}" var="app" varStatus="i">
    <li <c:if test="${i.index == 0}">class="active"</c:if>><a data-app="${app.id}">${app.name}</a></li>
    </c:forEach>
  </ul>
  </div>
  <div class="menu-main">
     <div class="btns">
        <input id="saveBtn" class="btn btn-primary" type="button" value="保 存"/>
        <span class="hr-line"></span>
        <input id="viewBtn" class="btn btn-warning" type="button" value="关 闭"/>
     </div>
     <div class="settings">
        <div class="title">被关注时回复</div>
        <div id="attentionConfig" class="menuConfig"> 
          <ul class="nav nav-tabs">
             <li class="active" data-type="0"><a data-tab="config-none">不回复</a></li>
             <li data-type="1"><a data-tab="config-text">文本回复</a></li>
             <li data-type="2"><a data-tab="config-rich">图文回复</a></li>
             <li data-type="4"><a data-tab="config-pic">图片回复</a></li>
          </ul>
          <div class="tab-content">
             <div class="tab-pane active" data-id="config-none">
                当粉丝关注后，可以选择发送一条指定的回复，或不做任何回复
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
			 <div class="tab-pane" data-id="config-pic">
			    <div class="config-pic">
			       <input type="hidden" class="-config">
			       <div class="image-wrap"><img alt=""></div>
			       <div class="ops"><a class="btn config-pic-select">选择图片</a><a class="btn btn-danger config-pic-upload">上传</a><span style="margin-left: 10px;">永久素材</span></div>
			    </div>
			 </div>
          </div>
       </div>
       <div class="title">无匹配关键词时回复</div>
       <div id="defaultConfig" class="menuConfig">
          <ul class="nav nav-tabs">
             <li class="active" data-type="0"><a data-tab="config-none">不回复</a></li>
             <li data-type="1"><a data-tab="config-text">文本回复</a></li>
             <li data-type="2"><a data-tab="config-rich">图文回复</a></li>
             <li data-type="4"><a data-tab="config-pic">图片回复</a></li>
          </ul>
          <div class="tab-content">
             <div class="tab-pane" data-id="config-none">
                当粉丝关注后，可以选择发送一条指定的回复，或不做任何回复
			 </div>
			 <div class="tab-pane" data-id="config-text">
			    <div class="config-content iInputLimit-wrap">
			       <textarea class="config-content-textarea -config iInputLimit" placeholder="填写文本回复" maxlength="600" style="overflow: hidden; word-wrap: break-word; height: 120px;"></textarea>
			       <div class="config-content-bar">
			          <a class="-qq-emo -tools" title="插入表情"><i class="iconfont icon-biaoqing"></i></a>
			          <a class="-set-link -tools" title="插入链接"><i class="iconfont icon-lianjie"></i></a>
			          <div class="pull-right word-num">还能输入 600 字</div>
			       </div>
			    </div>
			 </div>
             <div class="tab-pane" data-id="config-rich">
                <div class="multi-meta">
			      <div class="metas"></div>
			      <a class="add-ops meta-select"><i class="iconfont icon-plus"></i>添加多图文</a>
			    </div>
             </div>
			 <div class="tab-pane active" data-id="config-pic">
			    <div class="config-pic">
			       <input type="hidden" class="-config">
			       <div class="image-wrap"><img alt=""></div>
			       <div class="ops"><a class="btn config-pic-select">选择图片</a><a class="btn btn-danger config-pic-upload">上传</a><span style="margin-left: 10px;">永久素材</span></div>
			    </div>
			 </div>
          </div>
       </div>
       <div class="title">LBS最大距离(米)</div>
       <div id="defaultConfig" class="menuConfig">
          <input type="text" value="10000">
       </div>
     </div>
  </div>
</div>
<div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxModules}/system/wechat.js" type="text/javascript"></script>
<script type="text/javascript">
var THISPAGE = {
	_init : function(){
		this.loadSettings();
		this.addEvent();
	},
	loadSettings : function() {
		
		// 显示菜单的配置信息
		var showConfig = function(dom, type, config) {
			
			// 显示
			$(dom).find('.active').removeClass('active');
			$(dom).find('[data-type="'+type+'"]').addClass('active');
			
			// 设置值
			var tab = $(dom).find('.active>[data-tab]').data('tab');
			$(dom).find('.tab-pane[data-id="'+tab+'"]').addClass('active');
			if (type == 1) {
				$(dom).find('.tab-pane.active  .-config').val(config);
				$(dom).find('.iInputLimit').trigger('input');
			} else if (type == 2) {
				if (!!config) {
					var html = Meta_Rich.render(config);
					$(dom).find('.metas').html(html);
				}
			} else if(type == 4) {
				if (!!config) {
					var obj = $.parseJSON(config);
					$(dom).find('.config-pic').find('img').attr('src', obj.url);
					$(dom).find('.config-pic').find('.-config').val(config);
				}
			}
		};
		
		var that = this;
		var appId = $('.apps > .active > a').data('app');
		if (!!appId) {
			Public.loading('加载配置');
			Public.postAjax('${ctx}/wechat/meta/setting/load', {appId: appId}, function(data) {
				Public.close();
				
				// 设置
				var setting = data.obj;
				
				// 关注的设置
				var attentionType = setting.attentionType;
				var attentionConfig = setting.attentionConfig;
				
				// 默认的设置
				var defaultType = setting.defaultType;
				var defaultConfig = setting.defaultConfig;
				
				showConfig('#attentionConfig', attentionType, attentionConfig);
				showConfig('#defaultConfig', defaultType, defaultConfig);
			});
		} else {
			Public.error('请先创建公众号！');
		}
	},
	getSetting: function() {
		var atype = $('#attentionConfig .active').data('type');
		var atab = $('#attentionConfig .active>[data-tab]').data('tab');
		var aconfig = $('#attentionConfig .tab-pane[data-id="'+atab+'"]').find('.-config').val();
		
		var dtype = $('#defaultConfig .active').data('type');
		var dtab = $('#defaultConfig .active>[data-tab]').data('tab');
		var dconfig = $('#defaultConfig .tab-pane[data-id="'+dtab+'"]').find('.-config').val();
		
		// 返回实例
		return {
			defaultType : dtype,
			defaultConfig: dconfig,
			attentionType: atype,
			attentionConfig: aconfig
		}
	},
	addEvent : function(){
		
		// 配置切换
		$(document).on('click', '[data-tab]', function() {
			var tab = $(this).data('tab');
			var menuConfig = $(this).closest('.menuConfig');
			menuConfig.find('.active').removeClass('active');
			$(this).parent().addClass('active');
			menuConfig.find('.tab-pane[data-id="'+tab+'"]').addClass('active');
		});
		
		var that = this;
		$(document).on('click', '#saveBtn', function() {
			var appId = $('.apps > .active > a').data('app');
			if (!!appId) {
				Public.loading('保存配置');
				var postData = that.getSetting();
				    postData.appId = appId;
				Public.postAjax('${ctx}/wechat/meta/setting/save', postData, function(data) {
					Public.close();
					if (data.success) {
						Public.success('保存成功！');
						publish = 0;
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error('请先创建公众号！');
			}
		});
    }
};
$(function() {
	THISPAGE._init();
});
</script>
</body>
</html>