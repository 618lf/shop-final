<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<style type="text/css">
.degree {
  width: 28px;
  height: 28px;
  border: 1px solid #ddd;
  cursor: pointer;
}
.degrees {
  padding: 10px;
}
.degrees .degree {
  float: left;
  border: 0;
}
.degrees .dg-default {
  background-color: #fff;
}
.sub-menu-ops > a {
  margin-right: 10px;
}
.sub-menu-config > .-menu {
  margin-right: 10px;
  cursor: pointer;
}
</style>
<script type="text/html" id="degreesTemplate">
<div class="degrees clearfix">
  <div class="degree dg-default" data-degree=" "></div>
  <div class="degree dg-success" data-degree="dg-success"></div>
  <div class="degree dg-warning" data-degree="dg-warning"></div>
  <div class="degree dg-important" data-degree="dg-important"></div>
  <div class="degree dg-info" data-degree="dg-info"></div>
  <div class="degree dg-inverse" data-degree="dg-inverse"></div>
</div>
</script>
<script type="text/html" id="subMenuTemplate">
<div class="row-fluid">
  <form class="form-horizontal form-min" method="post">
	 <div class="control-group formSep">
		<label class="control-label">简称:</label>
		<div class="controls">
			<input type="text" name="subMenuName" id="subMenuName" value="{{=name}}"/>
		</div>
	 </div>
     <div class="control-group formSep">
		<label class="control-label">全称:</label>
		<div class="controls">
			<input type="text" name="subMenuName_full" id="subMenuName_full" value="{{=fullName}}"/>
		</div>
	 </div>
	 <div class="control-group formSep">
		<label class="control-label">链接:</label>
		<div class="controls">
			<input type="text" name="subMenuHref" id="subMenuHref" value="{{=href}}"/>
		</div>
	 </div>
     <div class="control-group formSep">
		<label class="control-label">样式:</label>
		<div class="controls sub-menu-config clearfix">
             <span class="-menu -menu-1">样式一</span>
             <span class="-menu -menu-2">样式二</span>
             <span class="-menu -menu-3">样式三</span>
		</div>
	 </div>
  </form>
</div>
</script>
</head>
<body>
<div class="row-fluid wrapper">
    <div class="bills">
      <div class="page-header">
         <h3>菜单管理 <small> &gt;&gt; 编辑</small></h3>
      </div>
      <form:form id="inputForm" modelAttribute="menu" action="${ctx}/system/menu/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<form:hidden path="level"/>
		<form:hidden path="parentIds"/>
		<div class="control-group formSep">
			<label class="control-label">上级菜单:</label>
			<div class="controls">
			    <tags:treeselect id="parentId" name="parentName" idValue="${menu.parentId}" nameValue="${menu.parentMenu.name}"
				      title="菜单" url="${ctx}/system/menu/treeSelect" extId="${menu.id}" cssClass="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">菜单名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">菜单类型:</label>
			<div class="controls">
			    <form:select path="type" cssClass="iSelect">
			       <form:option value="1">目录</form:option>
			       <form:option value="2">菜单</form:option>
			       <form:option value="3">权限</form:option>
			    </form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">链接:</label>
			<div class="controls">
				<form:input path="href" htmlEscape="false" maxlength="200"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">权限标识:</label>
			<div class="controls">
				<form:input path="permission" htmlEscape="false" maxlength="50"/>
			</div>
		</div> 
		<div class="control-group formSep">
			<label class="control-label">菜单图标:</label>
			<div class="controls">
			     <tags:iconselect path="iconClass"  value="${menu.iconClass}"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">副菜单:</label>
			<div class="controls">
			     <input type="hidden" id="subMenu" name="subMenu" value='${menu.subMenu}'>
			     <div class="sub-menu">${menu.subMenu}</div>
			     <div class="control-tip sub-menu-ops"><a class="add-sub-menu">设置</a><a class="remove-sub-menu">删除</a> -  副菜单显示在菜单的右边</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">快捷图标:</label>
			<div class="controls">
			     <form:input path="quickMenu" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">菜单重要度:</label>
			<div class="controls">
			     <form:hidden path="degree" htmlEscape="false" maxlength="50"/>
			     <div class="degree ${menu.degree}"></div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">同级排序:</label>
			<div class="controls">
				<form:input path="sort" htmlEscape="false" value="${menu.sort}" maxlength="50" class="required digits" />
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">是否显示:</label>
			<div class="controls">
				<form:select path="isShow" cssClass="iSelect">
			       <form:option value="1">显示</form:option>
			       <form:option value="0">隐藏</form:option>
			    </form:select>
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<input id="cancelBtn" class="btn " type="button" value="返 回"/>
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
		$("#inputForm").validate(
			Public.validate()
		);
	},
	addEvent : function(){
		$(document).on('click','#cancelBtn',function(){
			window.location.href = "${ctx}/system/menu/list?id="+$('#id').val();
		});
		$(document).on('click','.degree',function(){
			var type = $(this).closest('.controls').length;
			if(type != 0) {
			   var html = $('#degreesTemplate').html();
			   Public.tips(html, $(this).get(0), {
				   tips: [2, '#eee'], time: 3000
			   });
			} else {
			   var degree = $(this).data('degree');
			   $('#degree').val(degree);
			   $('#degree').next().attr('class', 'degree ' + degree);
			}
		});
		
		// 设置副菜单
		$(document).on('click', '.add-sub-menu', function() {
			var subMenu = $('.sub-menu').find('.-menu');
			var _menu = {
				name : subMenu.text(),
				href : subMenu.data('href'),
				fullName : subMenu.attr('title')
			};
			var html = Public.runTemplate($('#subMenuTemplate').html(), _menu);
			Public.openWindow("副菜单设置", html, 420, 280);
		});
		
		// 删除副菜单
		$(document).on('click', '.remove-sub-menu', function() {
			$('.sub-menu').html('');
			$('#subMenu').val('');
		});
		
		// 选择样式
		$(document).on('click', '.sub-menu-config .-menu', function() {
			var name = $('#subMenuName').val();
			var href = $('#subMenuHref').val();
			var fullName = $('#subMenuName_full').val();
			if (!!name && !!href) {
			    var style = $(this).outerHTML(); $('.sub-menu').html('');
			    $(style).data('href', href).html(name).attr('title', fullName).appendTo($('.sub-menu'));
			    var type = $(this).attr('class');
			    var _menu = {
		    		name : name,
					href : href,
					type : type,
					fullName: fullName
			    }
			    var html = Public.runTemplate('<span rel="pageTab" title="{{=fullName}}" data-href="{{=href}}" class="{{=type}}">{{=name}}</span>', _menu);
			    $('#subMenu').val(html);
			    Public.close();
			} else {
				Public.toast('请设置名称和地址');
			}
		});
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>