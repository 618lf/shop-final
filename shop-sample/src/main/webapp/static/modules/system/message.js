/**
 * 站内信 -- 提供的服务
 */
var Message = Message||{};

/**
 * 初始化事件
 */
Message.initEvent = function(prexUrl){
	var delMessage = function(checkeds){
		if( !!checkeds && checkeds.length ) {
			var param = [];
			if(typeof(checkeds) === 'object'){
				$.each(checkeds,function(index,item){
					var userId = $('#grid').getRowData(item).id;
					param.push({name:'idList',value:userId});
				});
			} else {
				param.push({name:'idList',value:checkeds});
			}
			Public.deletex("确定删除选中的站内信？",prexUrl + "/system/message/delete",param,function(data){
				if(!!data.success) {
					Public.success('删除站内信成功');
					Public.doQuery();
				} else {
					Public.error(data.msg);
				}
			});
		} else {
			Public.error("请选择要删除的站内信!");
		}
	};
	var traMessage = function(checkeds){
		if( !!checkeds && checkeds.length ) {
			var param = [];
			if(typeof(checkeds) === 'object'){
				$.each(checkeds,function(index,item){
					var userId = $('#grid').getRowData(item).id;
					param.push({name:'idList',value:userId});
				});
			} else {
				param.push({name:'idList',value:checkeds});
			}
			Public.deletex("确定删除选中的站内信？",prexUrl + "/system/message/toTrash",param,function(data){
				if(!!data.success) {
					Public.success('站内信已经放入垃圾箱');
					Public.doQuery();
				} else {
					Public.error(data.msg);
				}
			});
		} else {
			Public.error("请选择要删除的站内信!");
		}
	};
	//按钮切换
	$(document).on('click','.box',function(e){
		var id = $(this).attr('id');
		if(id == 'inBox') {
			window.location.href = prexUrl + "/system/message/inBox";
		}else if(id == 'outBox'){
			window.location.href = prexUrl + "/system/message/outBox";
		}else if(id == 'trashBox'){
			window.location.href = prexUrl + "/system/message/trashBox";
		}else if(id == 'draftBox'){
			window.location.href = prexUrl + "/system/message/draftBox";
		}else if(id == 'addBox'){
			var url = prexUrl + "/system/message/form";
			Public.openOnTab("add-box", '写站内信',url);
		}else if(id == 'templateBox'){
			var url = prexUrl + "/system/message/build";
			Public.openOnTab("template-box", '模板写信',url);
		}
	});
	
	//查看事件
	$(document).on('click','.edit',function(e){
		var id = $(this).data('id');
		var msg = "站内信 - " + $(this).data('msg');
		var url = prexUrl + "/system/message/view?id=" + id;
		Public.openOnTab('view-message', msg,url);
	});
	
	//查看事件
	$(document).on('click','.draft',function(e){
		var id = $(this).data('id');
		var msg = "站内信 - " + $(this).data('msg');
		var url = prexUrl + "/system/message/form?id=" + id;
		Public.openOnTab('edit-message', msg, url);
	});

	//垃圾箱的删除
	$(document).on('click','.delete',function(e){
		delMessage(''+$(this).data('id'));
	});
	$(document).on('click','#delBtn',function(e){
		var checkeds = $('#grid').getGridParam('selarrrow');
		delMessage(checkeds);
	});
	//收件、发件的删除
	$(document).on('click','.trash',function(e){
		traMessage($(this).attr('data-id'));
	});
	$(document).on('click','#trashBtn',function(e){
		var checkeds = $('#grid').getGridParam('selarrrow');
		traMessage(checkeds);
	});
};

/**
 * 已读
 */
Message.read = function(messageId){
	//发送已读
	Public.postAjax(webRoot + '/admin/system/message/read', {id:messageId}, function(){});
};

/**
 * 未读消息的前几条
 */
Message.unReadTops = function(fnc) {
	var _url = webRoot + '/f/member/message/unReadTops';
	Public.postAjax(_url, null, function(infos) {
		if( infos && infos.success ) {
			fnc(infos);
		}
	});
};