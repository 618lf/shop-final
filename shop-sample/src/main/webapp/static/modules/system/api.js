var Project = {
	
	init : function() {
		this.addEvent();
	},
	
	addEvent : function() {
		$(document).on('click', '.add-document', function() {
			var url = webRoot + '/admin/api/document/form?projectId=' + projectId;
			Public.openOnTab('doc-add', '添加接口', url);
		});
		$(document).on('click', '.-detail', function() {
			var url = webRoot + '/admin/api/document/detail?id=' + $(this).closest('tr').data('id');
			Public.openOnTab('doc-detail', '接口详情', url);
		});
		$(document).on('click', '.-edit', function() {
			var url = webRoot + '/admin/api/document/form?id=' + $(this).closest('tr').data('id');
			Public.openOnTab('doc-edit', '编辑接口', url);
		});
		$(document).on('click', '.add-group', function() {
			Public.promptx('添加分组', function(text) {
				var url = webRoot + '/admin/api/group/save';
				Public.postAjax(url, {name : text, projectId: projectId}, function(data) {
					$('.groups').append('<li><a data-id="'+(data.id)+'"><i class="iconfont icon-project"></i>'+name+'</a></li>')
				}, false);
			});
		});
		$(document).on('click', '.groups a', function() {
			var groupId = $(this).data('id');
			if (!(groupId == 0)) {
				groupId = groupId || '';
			}
			$('.groups .cur').removeClass('cur');
			$(this).closest('li').addClass('cur');
			Doc.list(groupId);
		});
	}
};

var Doc = {
	
	/**
	 * 列表
	 */
    list : function(group) {
    	var url = webRoot + '/admin/api/document/page';
    	var params = {
			'param.pageIndex' : 1,
			'param.pageSize': 1000,
			'groupId' : group
		};
    	Public.postAjax(url, params, function(data) {
    		var html = Public.runTemplate($('#docTemplate').html(), {datas: data.data});
    		$('#docs').html(html);
    	});
    },
    
    /**
     * 初始化展示
     */
    init : function() {
    	
    	// 解析
    	var headers = (function() {
    		var _h = $('#requestHeaders').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	var reqParams = (function() {
    		var _h = $('#queryParams').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	var resParams = (function() {
    		var _h = $('#responseParams').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	
    	// 头部
    	$.each(headers, function(n, e) {
    		var html = $('#headersTemplate').html();
    		var $tr = $(html).appendTo('#headers');
		    for(var i in e) {
		    	$tr.find('.' + i).val(e[i]);
		    }
    	});
    	$.each(reqParams, function(n, e) {
    		var html = $('#reqParamsTemplate').html();
    		var $tr = $(html).appendTo('#reqParams');
		    for(var i in e) {
		    	$tr.find('.' + i).val(e[i]);
		    }
    	});
    	$.each(resParams, function(n, e) {
    		var html = $('#resParamsTemplate').html();
    		var $tr = $(html).appendTo('#resParams');
		    for(var i in e) {
		    	$tr.find('.' + i).val(e[i]);
		    }
    	});
    },
	
    /**
     * 初始化详情
     */
    initDetail : function() {
    	
    	// 解析
    	var headers = (function() {
    		var _h = $('#requestHeaders').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	var reqParams = (function() {
    		var _h = $('#queryParams').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	var resParams = (function() {
    		var _h = $('#responseParams').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	
    	// 头部
    	$.each(headers, function(n, e) {
    		var html = $('#headersTemplate').html();
    		var $tr = $(html).appendTo('#headers');
		    for(var i in e) {
		    	$tr.find('.' + i).html(e[i]);
		    }
    	});
    	$.each(reqParams, function(n, e) {
    		var html = $('#reqParamsTemplate').html();
    		var $tr = $(html).appendTo('#reqParams');
		    for(var i in e) {
		    	$tr.find('.' + i).html(e[i]);
		    }
    	});
    	$.each(resParams, function(n, e) {
    		var html = $('#resParamsTemplate').html();
    		var $tr = $(html).appendTo('#resParams');
		    for(var i in e) {
		    	$tr.find('.' + i).html(e[i]);
		    }
    	});
    	
    	// 添加接口测试
    	$(document).on('click', '.add-test', function() {
    		var id = $(this).data('id');
    		var url = webRoot + '/admin/api/test/add?documentId=' + id;
    		Public.openUrlWindow('添加接口测试', url, 700, 500, function(iframe) {
    			var postData = iframe.THISPAGE.getPostData();
    			if (!postData) {
    				return false;
    			}
    			// 提交数据
    			Public.loading();
    			var url = webRoot + '/admin/api/test/save';
    			Public.postAjax(url, postData, function(data) {
    				Public.loaded();
    				window.location.reload();
    			}, false);
    			return true;
    		});
    	});
    	
    	// 修改接口测试
    	$(document).on('click', '.modify-test', function() {
    		var id = $(this).closest('tr').data('id');
    		var url = webRoot + '/admin/api/test/modify?id=' + id;
    		Public.openUrlWindow('修改接口测试', url, 700, 500, function(iframe) {
    			var postData = iframe.THISPAGE.getPostData();
    			if (!postData) {
    				return false;
    			}
    			// 提交数据
    			Public.loading();
    			var url = webRoot + '/admin/api/test/save';
    			Public.postAjax(url, postData, function(data) {
    				Public.loaded();
    			}, false);
    			return true;
    		});
    	});
    	
    	// 接口运行
    	$(document).on('click', '.-doc_run', function() {
    		var id = $(this).closest('tr').data('id');
    		var url = webRoot + '/admin/api/test/run?id=' + id;
    		Public.loading();
    		Public.postAjax(url, {}, function(data) {
    			Public.success('结果：【' + data.obj + '】', -1, function() {});
    		});
		});
    	
    	// 接口运行
    	$(document).on('click', '.-doc_del', function() {
    		var url = webRoot + '/admin/api/test/delete';
    		var id = $(this).closest('tr').data('id'); var $tr = $(this).closest('tr');
    		var param = [{'name': 'idList', 'value' : id}];
    		Public.postAjax(url, param, function() {
    			$tr.remove();
    		});
		});
    },
    
	/**
	 * 构造 json 请求参数
	 */
	buildPostData : function() {
		
		var falg = true;
		
		var headers = [];
		$('#headers').find('tr').each(function() {
			var name = $(this).find('.name').val();
			var value = $(this).find('.value').val();
			if (!!value) {
				headers.push({
					name : name,
					value : value
				});
			} else {
				falg = false;
				Public.toast("请求头内容不能为空！");
			}
		});
		
		var reqParams = [];
		$('#reqParams').find('tr').each(function() {
			var name = $(this).find('.name').val();
			var value = $(this).find('.value').val();
			var notNull = $(this).find('.notNull').val();
			var type = $(this).find('.type').val();
			if (!!value && !!name) {
				reqParams.push({
					name : name,
					value : value,
					type : type,
					notNull : notNull
				});
			} else {
				falg = false;
				Public.toast("请求参数名称或内容不能为空！");
			}
		});
		
		var resParams = [];
		$('#resParams').find('tr').each(function() {
			var name = $(this).find('.name').val();
			var value = $(this).find('.value').val();
			var notNull = $(this).find('.notNull').val();
			var type = $(this).find('.type').val();
			if (!!value && !!name) {
				resParams.push({
					name : name,
					value : value,
					type : type,
					notNull : notNull
				});
			} else {
				falg = false;
				Public.toast("响应参数名称或内容不能为空！");
			}
		});
		
		// 返回结果
		if (falg) {
			return {
				headers : headers,
				reqParams : reqParams,
				resParams : resParams
			};
		}
		return false;
	}
};

/**
 * 测试
 */
var Test = {
		
	/**
	 * 初始化
	 */
	init : function() {
		// 解析
    	var headers = (function() {
    		var _h = $('#requestHeaders').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	var reqParams = (function() {
    		var _h = $('#queryParams').val();
    		if (!!_h) {
    			return $.parseJSON(_h);
    		}
    		return [];
    	})();
    	
    	// 头部
    	$.each(headers, function(n, e) {
    		var html = $('#headersTemplate').html();
    		var $tr = $(html).appendTo('#headers');
		    for(var i in e) {
		    	$tr.find('.' + i).html(e[i]);
		    }
    	});
    	$.each(reqParams, function(n, e) {
    		var html = $('#reqParamsTemplate').html();
    		var $tr = $(html).appendTo('#reqParams');
		    for(var i in e) {
		    	$tr.find('.' + i).html(e[i]);
		    	$tr.find('.' + i).val(e[i]);
		    }
    	});
	},
	
	/**
	 * 构建提交的参数
	 */
	buildPostData : function() {
		var reqParams = []; var falg = true;
		$('#reqParams').find('tr').each(function() {
			var name = $(this).find('.name').text();
			var value = $(this).find('.value').text();
			var notNull = $(this).find('.notNull').text();
			var type = $(this).find('.type').text();
			var pval = $(this).find('.val').val();
			if (!(notNull === 'true' && !pval)) {
				reqParams.push({
					name : name,
					value : value,
					type : type,
					notNull : notNull,
					val : pval
				});
			} else {
				falg = false;
				Public.toast("请求参数值不能为空！");
			}
		});
		
		if (!falg) {
			return false;
		}
		return reqParams;
	},
}
