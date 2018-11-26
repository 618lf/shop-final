<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/webuploader/webfilemanager.css" rel="stylesheet"/>
<link href="${ctxStatic}/webuploader/webuploader.css" rel="stylesheet"/>
<link href="${ctxStatic}/jquery-smartMenu/css/smartMenu.css" rel="stylesheet"/>
<style type="text/css">
.file-manager .file-items {
  padding-left: 10px;
}
</style>
<script type="text/html" id="fileTemplate">
  {{ for(var i = 0; i< datas.length; i++ ) { }}
  {{     var data = datas[i]; }}
  <li><a data-type="{{=data.type}}" data-id="{{=data.id}}" data-name="{{=data.name}}" data-url="{{=data.storageUrl}}">
      <span class="img-wrap">
         {{ if( '|png|jpg|jpeg|bmp|gif|'.indexOf('|'+data.type+'|') == -1 ) { }}
         <i class="file-preview file-type-{{=data.type}}"></i>
         {{ } else { }}
         <img data-src="{{=data.storageUrl}}" data-img="1">
         {{ } }}
         <span class="file-title" title="{{=data.name}}">{{=data.name}}</span>
      </span>
      </a>
     <span class="file-panel"><span class="cancel">X</span></span>
     <span class="selected"></span>
  </li>
  {{ } }}
</script>
</head>
<body class="white no-fixed-btns">
<div class="file-manager">
   <div class="file-items-wrap">
     <div class="file-path">
       <input type="hidden" name="dirId" id="dirId" value="${dir.id}">
       <c:if test="${fns:length(paths) > 1}">
       <span class="back" data-id='${dir.parentId}'><a href="${ctx}/system/attachment/select/${dir.parentId}">返回上一级</a></span>
       </c:if>
       <c:forEach items="${paths}" var="path" varStatus="i">
       <span class="<c:if test="${i.index == (fns:length(paths) - 1)}">last-item</c:if><c:if test="${i.index != (fns:length(paths) - 1) && i.index == 0}">first-item</c:if> path-item" data-id="${path.id}"><a href="${ctx}/system/attachment/select/${path.id}">${path.name}</a></span>
       </c:forEach>
     </div>
     <ul class="nav file-items" style="overflow: hidden; overflow-y:auto;"></ul>
   </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/webuploader/webuploader.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-smartMenu/js/jquery-smartMenu.js" type="text/javascript"></script>
<script type="text/javascript">
	var THISPAGE = {
		loader: {
	      state: 0,// 状态0：可运行，1运行中，2最大数据
	      pageIndex:1, // 当前页码
	      pageCount:-1,// 页面数
	      hasNext:function(){
			if(this.pageCount == -1 || this.pageCount >= this.pageIndex) {
				return true;
			}
			return false;
		  },
		  next: function(pageCount){
			this.pageCount = pageCount;
			this.pageIndex++;
		  },
		  showLoading : function() {
			$('.file-items').append('<li class="file-loader"><a><img src="${ctxStatic}/img/loading.gif">数据加载中...</a></li>');
		  },
		  removeLoading: function() {
			$('.file-items li.file-loader').remove();
			$('.file-items li.file-null').remove();
		  }
	    },
	    _init : function() {
			this.loadFiles();
			this.addEvent();
		},
		getLayerIndex : function() {
			return parent.layer.getFrameIndex(window.name);
		},
		scale : function(img, w, h, type) {// 调整图片大小
			var ow = img.width,
	            oh = img.height;
		    if (type == 'justify') {
		        if (ow >= oh) {
		            img.width = w;
		            img.height = h * oh / ow;
		            img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
		        } else {
		            img.width = w * ow / oh;
		            img.height = h;
		            img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
		        }
		    } else {
		        if (ow >= oh) {
		            img.width = w * ow / oh;
		            img.height = h;
		            img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
		        } else {
		            img.width = w;
		            img.height = h * oh / ow;
		            img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
		        }
		    }
		},
		getSelectedFiles: function() {
			var files = [];
			return $('.file-items').find('li').each(function() {
				var file = $(this).children('a');
				if($(this).hasClass('active')) {
					files.push({
						id: file.data('id'),
						name: file.data('name'),
						type: file.data('type'),
						src: file.data('url')
					})
				}
			}), files;
		},
		loadFiles: function() {
			var loader = this.loader; var self = this;
			if (loader.state == 0 && loader.hasNext()) {
				loader.showLoading();
				loader.state = 1;
				var pageNo = loader.pageIndex;
				//加载数据
				Public.postAjax('${ctx}/system/attachment/page/' + pageNo, {dirId:'${dir.id}'}, function(data) {
					loader.removeLoading();
					if(data.data.length != 0) {
						var _data = {datas:data.data};
						var html = Public.runTemplate($('#fileTemplate').html(), _data);
						$('.file-items').append(html);
						loader.next(data.param.pageCount);
						loader.state = 0;
						
						//加载图片
						$('img[data-src]').each(function() {
							var src = $(this).data('src');
							$(this).removeAttr('data-src');
							$(this).attr('src', src);
							$(this).on('load', function() {
								var image = $(this).get(0);
								self.scale(image, image.parentNode.offsetWidth - 20, image.parentNode.offsetHeight - 25);
							});
							
						});
					} else {
						$('.file-items').append('<li class="file-null">还没有文件</li>');
					}
				});
			}
		},
		addEvent: function() {
			var self = this;
			//拦截所有的 A 标签
			$(document).on('click', '.file-items a', function(e) {
				var $li = $(this).closest('li');
				if(!$li.hasClass('active')) {
					$li.addClass('active')
				} else {
					$li.removeClass('active')
				}
				var selectCount = 0;
				$(this).closest('.file-items').find('li').each(function() {
					if($(this).hasClass('active')) selectCount ++;
				});
				var title = "选择文件 - 已选择[" + selectCount + "]个文件"
				if(parent.layer) {
					parent.layer.title(title, self.getLayerIndex());
				}
			});
			$(document).on('dblclick', '.file-items a', function(e) {
				e.preventDefault();
				var type = $(this).data('type')
				var id = $(this).data('id');
				if('DIR' == type) {
					window.location.href = '${ctx}/system/attachment/select/' + id;
				}else if('|png|jpg|jpeg|bmp|gif|'.indexOf('|'+type+'|') != -1) {//预览
					Public.photos('图片预览', [{
						alt: $(this).data('name'),
						src: $(this).find('img').attr('src'),
						thumb: $(this).find('img').attr('src')
					}]);
				}
			});
			$('.file-items').height($(window).outerHeight() - 65);
			$('.file-items').on('scroll', function() {
				var scrollTop = parseInt($(this).scrollTop());
				var scrollHeight = parseInt($(this).get(0).scrollHeight);
				var windowHeight = parseInt($(this).height() + 20);
				if( scrollTop + windowHeight == scrollHeight){
				　  self.loadFiles();
				}
				//box-show
				if(scrollTop == 0) {
					$('.file-path').removeClass('box-show');
				} else if(!$('.file-path').hasClass('box-show')){
					$('.file-path').addClass('box-show');
				}
			});
			$(document).on('click', '.file-panel>.cancel', function() {
				var file = $(this).closest('li').children('a');
				if( !!file.data('id')) {
					var params = [{name:'idList',value:file.data('id')}];
					Public.executex('确定删除选定的1个文件', '${ctx}/system/attachment/delete', params, function(data) {
						if( data.success ) {
							Public.success("文件删除成功", function() {
							  window.location.reload();
							}, null);
						} else {
							Public.error(data.msg);
						}
					});
				} else {
					Public.error("请选择要删除的文件");
				}
			});
		}
	}
    $(function() {
	   //初始化
	   THISPAGE._init();
	});
</script>
</body>
</html>