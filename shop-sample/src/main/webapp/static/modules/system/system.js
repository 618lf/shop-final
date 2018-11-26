/**
 * 提供系统组件
 */
var System = System || {};

/**
 * 选择用户
 */
System.userSelect = function(ok) {
	Public.tableSelect('/admin/system/tag/user/tableSelect', '选择用户', 700, 500, function(iframe, ids, names) {
		var id = ids[0];
		if (!!id) {
			ok(id);
		}
	});
};

/**
 * 获取用户信息
 */
System.getUser = function(id) {
	var user = {};
	Public.getAjax('/admin/system/user/get/' + id, {}, function(data) {
		user = data.obj;
	}, false);
	return user;
};