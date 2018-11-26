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
</head>
<body class="white">
 <div class="file-manager">
   <div class="file-manager-toolbar">
     <a id="uploadBtn" class="btn btn-primary uploadBtn"><i class="iconfont icon-upload"></i>上传文件</a>
     <a id="newDirBtn" class="btn"><i class="iconfont icon-folder"></i>新建文件夹</a>
     <div class="btn-group">
       <a id="downloadBtn" class="btn downloadBtn"><i class="iconfont icon-download"></i>下载</a>
       <a id="delBtn" class="btn delBtn"><i class="iconfont icon-delete"></i>删除</a>
       <a id="renameBtn" class="btn renameBtn"><i class="iconfont icon-edit"></i>重命名</a>
       <a id="moveBtn" class="btn moveBtn"><i class="iconfont icon-move"></i>移动到</a>
       <a id="shareBtn" class="btn shareBtn"><i class="iconfont icon-share"></i>分享</a>
     </div>
     <span class="input-append">
	  <input class="span3" id="appendedInputButton" type="text" placeholder="输入关键字搜索文件...">
	  <a class="btn" id="searchBtn"><i class="iconfont icon-search"></i>搜索</a>
	 </span>
   </div>
   <div class="file-items-wrap">
     <div class="file-path">
       <input type="hidden" name="dirId" id="dirId" value="${dir.id}">
       <c:if test="${fns:length(paths) > 1}">
       <span class="back" data-id='${dir.parentId}'><a href="${ctx}/system/attachment/list/${dir.parentId}">返回上一级</a></span>
       </c:if>
       <c:forEach items="${paths}" var="path" varStatus="i">
       <span class="<c:if test="${i.index == (fns:length(paths) - 1)}">last-item</c:if><c:if test="${i.index != (fns:length(paths) - 1) && i.index == 0}">first-item</c:if> path-item" data-id="${path.id}"><a href="${ctx}/system/attachment/list/${path.id}">${path.name}</a></span>
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
	  },
	  reset: function() {
		this.state = 0;
		this.pageIndex = 1;
		this.pageCount = -1;
		$('.file-items').html('');
		THISPAGE.loadFiles();
	  }
    },
	_init : function() {
		this.loadFiles();
		this.addSmartMenu();
		this.addEvent();
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
					type: file.data('type')
				})
			}
		}), files;
	},
	loadFiles: function() {
		var loader = this.loader; var self = this;
		if( loader.state == 0 && loader.hasNext() ) {
			loader.showLoading();
			loader.state = 1;
			var pageNo = loader.pageIndex; var name = $('#appendedInputButton').val();
			//加载数据
			Public.postAjax('${ctx}/system/attachment/page/' + pageNo, {dirId:'${dir.id}', name: name}, function(data) {
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
						}).on('error', function() {
							$(this).attr('src', '${ctxStatic}/img/no_picture.gif');
						});
						
					});
				} else {
					$('.file-items').append('<li class="file-null">还没有文件，赶紧上传文件吧！<button type="button" class="btn btn-primary uploadBtn">上传文件</button></li>');
				}
			});
		}
	},
	addSmartMenu : function() {
		$('.file-items').smartMenu([[{ 
   			text: "新建文件夹",
   			icon: "iconfont icon-folder",
   		    func: function() {
   			   $('#newDirBtn').click();  
   		    }
   		},{
   			text: "上传文件",
   			icon: "iconfont icon-upload",
   			func: function() {
   			   $('#uploadBtn').click();   
   		    }  
   		}],[{
   			text: "刷新",
   			icon: "iconfont icon-refresh",
   			func: function() {
   				window.location.reload();       
   		    } 
   		}],[{
   			text: "下载",
   			icon: "iconfont icon-download",
   			func: function() {
   				$('#downloadBtn').click();        
   		    }
   	    },{
   	    	text: "删除",
   			icon: "iconfont icon-delete",
   			func: function() {
   			   $('#delBtn').click();     
   		    }
   	    },{
   	    	text: "重命名",
   			icon: "iconfont icon-edit",
   			func: function() {
   			   $('#renameBtn').click(); 
   		    }
   	    },{
   	    	text: "移动到",
   			icon: "iconfont icon-move",
   			func: function() {
   				$('#moveBtn').click();
   		    }
   	    }],[{
   	    	text: "选择文件",
   			icon: "iconfont icon-select",
   			func: function() {
   				Attachment.selectAttachments();
   		    }
   	    }]]);
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
			var hasSelected = false;
			$(this).closest('.file-items').find('li').each(function() {
				if($(this).hasClass('active')) hasSelected = true;
			});
			if(hasSelected) {
				$('.file-manager-toolbar .btn-group').addClass('show');
			} else {
				$('.file-manager-toolbar .btn-group').removeClass('show');
			}
		});
		$(document).on('dblclick', '.file-items a', function(e) {
			e.preventDefault();
			var type = $(this).data('type')
			var id = $(this).data('id');
			if('DIR' == type) {//进入
				window.location.href = '${ctx}/system/attachment/list/' + id;
			} else if('|png|jpg|jpeg|bmp|gif|'.indexOf('|'+type+'|') != -1) {//预览
				Public.photos('图片预览', [{
					alt: $(this).data('name'),
					src: $(this).find('img').attr('src'),
					thumb: $(this).find('img').attr('src')
				}]);
			}
		});
		$(document).on('click', '.uploadBtn', function() {
			Public.openUrlWindow('上传文件', '${ctx}/system/attachment/uploader?dirId=' + $('#dirId').val(), 700, 450, null, true, null, function() {
				window.location.reload();
			});
		});
		$(document).on('click', '.downloadBtn', function() {
			var files = self.getSelectedFiles();
			if(files.length == 1) {
			   var params = [];
				   params.push({name:'id',value:files[0].id});
			   Public.doExport('${ctx}/system/attachment/download', params);
			} else  {
			   Public.error("请选择要下载的文件（只支持单文件下载）");
			}
		});
		$(document).on('click', '.delBtn', function() {
			var files = self.getSelectedFiles();
			if(files.length != 0) {
				var params = [];
				$.each(files, function(index, item) {
					params.push({name:'idList',value:item.id});
				});
				Public.executex('确定删除选定的'+files.length+'个文件', '${ctx}/system/attachment/delete', params, function(data) {
					if( data.success ) {
						Public.success("文件删除成功");
						$.each(files, function(index, item) {
							$('[data-id="'+(item.id)+'"]').closest('li').remove();
						});
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error("请选择要删除的文件");
			}
		});
		$(document).on('click', '.file-panel>.cancel', function() {
			var file = $(this).closest('li').children('a');
			if( !!file.data('id')) {
				var params = [{name:'idList',value:file.data('id')}];
				Public.executex('确定删除选定的1个文件', '${ctx}/system/attachment/delete', params, function(data) {
					if( data.success ) {
						Public.success("文件删除成功");
						$(file).closest('li').remove();
					} else {
						Public.error(data.msg);
					}
				});
			} else {
				Public.error("请选择要删除的文件");
			}
		});
		$(document).on('click', '.renameBtn', function() {
			var files = self.getSelectedFiles();
			if(files.length == 1) {
				var id = files[0].id;
				Public.promptx('请输入新的名称', function(text) { 
					if(!!text) {
						Public.executexQuietly('${ctx}/system/attachment/rename/' + id, {name:text}, function(data) {
							if( data.success ) {
								Public.success("文件重命名成功", function() {
								  window.location.reload();
								}, null);
							} else {
								Public.error(data.msg);
							}
						});
					} else {
						Public.error("新的名称不能为空！");
					}
				});
			} else {
				Public.error("请选择一个要重命名的文件");
			}
		});
		$(document).on('click', '.moveBtn', function() {
			var files = self.getSelectedFiles();
			if(files.length > 0) {
				var params = []; var extIds = [];
				$.each(files, function(index, item) {
					params.push({name:'idList',value:item.id});
					if(item.type == 'DIR') {
					   extIds.push(item.id);
					}
				});
				Public.treeSelect('${ctx}/system/attachment/treeSelect', '移动到', 300, 420, function(iframe, ids, names) {
					if(!!ids && ids.length == 1) {
						params.push({name:'moveto',value:ids[0]});
						Public.executexQuietly('${ctx}/system/attachment/move', params, function(data) {
							if( data.success ) {
								Public.success("移动文件成功", function() {
								  window.location.reload();
								}, null);
							} else {
								Public.error(data.msg);
							}
						}, false);
						return true;
					}
					return false;
				}, null, null, (extIds.join(',')), null, false, true, true);
			} else {
				Public.error("请选择一个要移动的文件或文件夹");
			}
		});
		$(document).on('click', '.shareBtn', function() {
			var files = self.getSelectedFiles();
			if(files.length == 1 && files[0].type != 'DIR') {
			   var src = '${base}' + $('a[data-id="'+files[0].id+'"]').data('src');
			   Public.showQrcode(src, '分享附件');
			} else {
				Public.error("请选择一个要分享的文件（目前只支持文件）");
			}
		});
		$(document).on('click', '#newDirBtn', function() {
			Public.openWindow('新建文件夹', $('#newDirTemplate').html(), 500, 230, function() {
				var name = $('#name').val();
				if( !!name ) {
				  Public.postAjax('${ctx}/system/attachment/save/dir', {name:name, parentId:$('#dirId').val()}, function(data) {
					  window.location.reload();
				  } , false);
				} else {
				  Public.error("请填写文件夹名");
				}
				return false;
			});
		});
		$(document).on('click', '#searchBtn', function() {
			self.loader.reset();
		});
		$('.file-items').height($(window).outerHeight() - 112);
		$('.file-items').on('scroll', function() {
			var scrollTop = parseInt($(this).scrollTop());
			var scrollHeight = parseInt($(this).get(0).scrollHeight);
			var windowHeight = parseInt($(this).height() + 20);
			if (scrollTop + windowHeight == scrollHeight){
			　  self.loadFiles();
			}
			//box-show
			if(scrollTop == 0) {
				$('.file-path').removeClass('box-show');
			} else if(!$('.file-path').hasClass('box-show')){
				$('.file-path').addClass('box-show');
			}
		});
	}
};
$(function() {
	//初始化
	THISPAGE._init();
});
</script>
<script type="text/html" id="fileTemplate">
  {{ for(var i = 0; i< datas.length; i++ ) { }}
  {{     var data = datas[i]; }}
  <li><a data-type="{{=data.type}}" data-id="{{=data.id}}" data-name="{{=data.name}}" data-src="{{=data.storageUrl}}">
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
<script type="text/html" id="newDirTemplate">
<div class="row-fluid">
  <form id="newDirForm" name="newDirForm" class="form-horizontal form-min">
	  <div class="control-group formSep">
	    <label class="control-label">文件夹名:</label>
		<div class="controls">
		  <input type="text" name="name" id="name" class="required"/>
		</div>
	  </div>
  </form>
</div>  
</script>
</body>
</html>