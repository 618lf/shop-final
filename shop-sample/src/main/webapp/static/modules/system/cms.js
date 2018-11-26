/**
 * 页面
 */
var _Mpage = {
		
	// 初始化
	init: function() {
		
		// 当前对象
		var self = this;
		
		// 保存
		$(document).on('click', '#publishBtn', function(e) {
			self.save();
		});
		
		// 保存
		$(document).on('click', '#saveBtn', function(e) {
			self.save();
		});
		
		// 保存
		$(document).on('click', '#previewBtn', function(e) {
			self.preview();
		});
		
		// 其他组件
		var url = webRoot + '/admin/cms/mpage/load_fields?pageId=' + Mpage.id;
		Public.postAjax(url, {}, function(data) {
			Mpage.fields = data.obj;
			AppDesign.init();
		});
	},
	
	// 保存
	save : function() {
		
		// 获的页面对象
		var page = AppDesign.getPage();
		
		// 提交数据
		var url = webRoot + '/admin/cms/mpage/save';
		Public.postAjax(url, {postData: JSON.stringify(page)}, function(data){
			var page = data.obj;
			// 新增的需要重新加载页面
			if (Mpage.id === '-1') {
				Public.success('操作成功', function() {
					window.location.href = webRoot + '/admin/cms/mpage/form?id=' + page.id;
				});
			} else {
				Public.success('操作成功');
			}
		});
		
	},
	
	// 预览
	preview : function() {
		if (Mpage.id === '-1') {
			Public.toast('请先保存');
		} else {
			this.save();
			Public.postAjax(webRoot + '/admin/cms/mpage/view/' + Mpage.id, {}, function(data) {
				Public.showQrcode(data.obj);
			});
		}
	},
	
	// 添加一个新页面
	newPage : function() {
		var url = webRoot + '/admin/cms/mtemplate/use_template';
		Public.openOnTab('mpage-add', '添加页面', url);
	}
};

/**
 * 模板
 */
var _Mtemplate = {
	
	init : function() {
		
		// 当前对象
		var self = this;
		
		// 保存
		$(document).on('click', '#saveBtn', function(e) {
			self.save();
		});
		
		// 关闭
		$(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
		
		// 编辑器初始化
		var url = webRoot + '/admin/cms/mtemplate/load_fields?pageId=' + Mpage.id;
		Public.postAjax(url, {}, function(data) {
			Mpage.fields = data.obj;
			AppDesign.init();
		});
	},
	
	// 保存
	save : function() {
		
		// 获的页面对象
		var page = AppDesign.getPage();
		
		// 模板
		var html = Public.runTemplate($('#saveDiv').html(), Mpage);
		
		// 保存
		Public.openWindow('保存模板', html, 420, 200, function() {
			var templateName = $('#templateName').val();
			if (!!templateName) {
				
				// 模板名称设置
				page.name = templateName;
				
				// 清除选中的红框
				var editing = $('.editing'); editing.removeClass('editing');
				
				// 只显示前4个
				var size = 4;
				$('.app-field').each(function(n, e) {
					if (n >= size) {
						$(e).hide();
					}
				});
				
				// 需要自定义大小
				Public.html2Image('#app-fields', {
					onrendered: function(canvas) {
					   var image = canvas.toDataURL();
					   page.thumbnail = image;
					   // 提交数据
					   var url = webRoot + '/admin/cms/mtemplate/save';
					   Public.postAjax(url, {postData: JSON.stringify(page)}, function(data){
						   var page = data.obj;
						   // 新增的需要重新加载页面
						   if (Mpage.id === '-1') {
							   Public.success('操作成功', function() {
								   window.location.href = webRoot + '/admin/cms/mtemplate/form?id=' + page.id;
							   });
						   } else {
							   Mpage.name = page.name;
							   Public.success('操作成功');
							   // 恢复选中
							   editing.addClass('editing');
							   $('.app-field').show();
						   }
					   });
					}
				});
			} else {
				Public.toast('请填写模板名称');
				return false;
			}
		});
	}
};
