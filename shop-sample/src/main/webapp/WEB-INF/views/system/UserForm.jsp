<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<script type="text/html" id="initPasswordDiv">
      <div class="row-fluid">
		<form id="initPassword" name="initPassword" action="${ctx}/system/user/initPassword" class="form-horizontal" method="post">
			 <input type="hidden" name="id" class="required" value="${user.id}"/>
			 <div class="control-group formSep">
				<label class="control-label">新密码:</label>
				<div class="controls">
					<input type="text" name="newPassWord" id="newPassWord" class="required"/>
				</div>
			 </div>
			 <div class="control-group formSep">
				<label class="control-label">确认密码:</label>
				<div class="controls">
					<input type="text" name="rNewPassWord" id="rNewPassWord" class="required"/>
				</div>
			 </div>
		</form>
	  </div>
</script>
<style type="text/css">
   #initPassword .control-label{width: 100px;}
   #initPassword .controls{margin-left: 120px;}
   .user-headimg-wrap {
     position: absolute;
     background-color: #fff;
     width: 316px;
     right: 0;
     top: 90px;
     padding-right: 80px;
     padding-left: 20px;
     padding-bottom: 20px;
   }
   .user-headimg-wrap .user-headimg-inner {
     border: 1px solid #ddd;
     padding: 10px;
   }
   .user-headimg-wrap .user-headimg-img {
     border-radius:0;
     min-height: 150px;
   }
   .user-headimg-wrap img {
     max-width: 100%;
     max-height: 206px;
   }
   .user-headimg-wrap .user-headimg-ops {
     padding-top: 15px;
   }
</style>
</head>
<body>
<div class="row-fluid wrapper">
   <div class="bills">
    <div class="page-header">
         <h3>用户管理 <small> &gt;&gt; 编辑</small></h3>
    </div>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/system/user/save" method="post" class="form-horizontal">
		<tags:token/>
		<form:hidden path="id"/>
		<div class="control-group formSep">
			<label class="control-label">用户姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50" class="required userName"/>
			</div>
		</div>
		<c:if test="${user.root}">
		<div class="control-group formSep">
			<label class="control-label">用户帐号:</label>
			<div class="controls">
				<form:input path="loginName" htmlEscape="false" maxlength="50" class='userName' readonly="true"/>
				<div class="control-tip">超级管理员不能修改帐号</div>
			</div>
		</div>
		</c:if>
		<c:if test="${!user.root}">
		<div class="control-group formSep">
			<label class="control-label">用户帐号:</label>
			<div class="controls">
				<form:input path="loginName" htmlEscape="false" maxlength="50" class='userName' />
			</div>
		</div>
		</c:if>
		<div class="control-group formSep">
			<label class="control-label">用户编码:</label>
			<div class="controls">
				<c:if test="${user.no == null}">
		        <form:input path="no" htmlEscape="false" maxlength="50" class="abc"/>
		        </c:if>
		        <c:if test="${user.no != null}">
		        <form:input path="no" htmlEscape="false" maxlength="50" class="abc" readonly="true"/>
		        </c:if>
				<div class="control-tip">留空白则由系统自己生成,一旦保存不能修改</div>
			</div>
		</div>
		<div class="control-group user-headimg-wrap">
		  <div class="user-headimg-inner">
		    <form:hidden path="headimg" htmlEscape="false" maxlength="50" class="url"/>
			<div class="user-headimg">
			  <a class="user-headimg-img thumbnail"><img alt="" src="${user.headimg}"></a>
			  <div class="user-headimg-ops">
			    <a class="btn btn-success select-headimg">选择</a>
			  </div>
			</div>
		  </div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">所属组织:</label>
			<div class="controls">
				<tags:treeselect id="officeId" name="officeName" idValue="${user.officeId}" nameValue="${user.officeName}"
				      title="所属组织" url="${ctx}/system/office/treeSelect"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">手机:</label>
			<div class="controls">
			    <form:input path="mobile" maxlength="50" class="mobile"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">性别:</label>
			<div class="controls">
				<form:select path="sex" cssClass="iSelect">
				  <form:option value=""></form:option>
				  <form:option value="0">女</form:option>
				  <form:option value="1">男</form:option>
				</form:select>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">省:</label>
			<div class="controls">
			    <form:input path="province" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">市:</label>
			<div class="controls">
			    <form:input path="city" maxlength="50"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">电子邮件:</label>
			<div class="controls">
			    <div class="input-prepend">
				  <span class="add-on">@</span>
				  <form:input path="email"  maxlength="50" cssClass="email"/>
				</div>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">用户状态:</label>
			<div class="controls">
			     <label class='radio inline'>
			       <form:radiobutton path="status" value="1"/>&nbsp;待修改密码
			     </label>
			     <label class='radio inline'>
			       <form:radiobutton path="status" value="2"/>&nbsp;锁定
			     </label>
			     <label class='radio inline'>
			       <form:radiobutton path="status" value="3"/>&nbsp;待修改密码-锁定
			     </label>
			     <label class='radio inline'>
			       <form:radiobutton path="status" value="4"/>&nbsp;正常
			     </label>
			</div>
		</div>
		<div class="control-group formSep">
		    <label class="control-label">用户组:</label>
			<div class="controls">
			     <tags:multiselect name="groupNames" nameValue="${user.groupNames}" id="groupIds"
			           idValue="${user.groupIds}" defaultText="输入菜单 ..." selectUrl="${ctx}/system/group/treeSelect" title="用户组" checked="true"/>
			</div>
		</div>
		<div class="control-group formSep">
		    <label class="control-label">用户角色:</label>
			<div class="controls">
			     <tags:multiselect name="roleNames" nameValue="${user.roleNames}" id="roleIds"
			           idValue="${user.roleIds}" defaultText="输入菜单 ..." selectUrl="${ctx}/system/role/treeSelect" title="角色" checked="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">备注:</label>
			<div class="controls">
			    <form:textarea path="remarks"  maxlength="255" cssStyle="width:90%;" rows="4"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">注册时间:</label>
			<div class="controls">
				<input type="text" name="createDate" value="${fns:formatDate(user.createDate, 'yyyy-MM-dd HH:mm:ss')}" readonly="readonly" maxlength="50" class="Wdate">
			</div>
		</div>
		<div class="control-group formSep">
		    <label class="control-label">用户注册类型:</label>
			<div class="controls">
			   <input type="text" value="${user.userType != null?(user.userType.name):''}" readonly="readonly">
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">最后一次登录IP:</label>
			<div class="controls">
				<form:input path="loginIp" htmlEscape="false" maxlength="50" readonly="true"/>
			</div>
		</div>
		<div class="control-group formSep">
			<label class="control-label">最后一次登陆时间:</label>
			<div class="controls">
				<input type="text" name="loginDate" value="${fns:formatDate(user.loginDate, 'yyyy-MM-dd HH:mm:ss')}" readonly="readonly" maxlength="50" class="Wdate">
			</div>
		</div>
		<div class="form-actions">
			<input id="submitBtn" class="btn btn-primary" type="submit" value="保 存"/>
			<c:if test="${user.id != '-1'}">
			 <sys:isRoot><a class="btn btn-warning" onclick="top.switchto('${user.id}')">切换到此用户</a></sys:isRoot>
			 <input id="initPBtn" class="btn" type="button" value="初始化密码"/>
			</c:if>
			<input id="cancelBtn" class="btn" type="button" value="关闭"/>
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
			Public.validate({
				rules:{
					loginName:{
						 minlength:4,
						 remote: {
							    url: "${ctx}/system/user/check/account", 
							    type: "post",  
							    dataType: "json", 
							    data: {     
							    	userId: function() {
							            return $("#id").val();
							        },
							        id: function() {
							            return $("#loginName").val();
							        }
							    }
						 }
					},
					no:{
						 remote: {
							    url: "${ctx}/system/user/check/no", 
							    type: "post",  
							    dataType: "json", 
							    data: {     
							    	id: function() {
							            return $("#id").val();
							        },
							        no: function() {
							            return $("#no").val();
							        }
							    }
						 }
					},
					email:{
						 remote: {
							    url: "${ctx}/system/user/check/account",
							    type: "post",  
							    dataType: "json", 
							    data: {     
							    	userId: function() {
							            return $("#id").val();
							        },
							        id: function() {
							            return $("#email").val();
							        }
							    }
						 }
					},
					mobile:{
						 remote: {
							    url: "${ctx}/system/user/check/account",
							    type: "post",  
							    dataType: "json", 
							    data: {     
							    	userId: function() {
							            return $("#id").val();
							        },
							        id: function() {
							            return $("#mobile").val();
							        }
							    }
						 }
					}
				},
				messages: {
					loginName:{ remote:'账户名重复！' , minlength: $.format("账户名不能小于{0}个字符")},
					no:{remote:'用户编码重复！'},
					email:{remote:'用户邮箱重复！'},
					mobile:{remote:'用户手机重复！'}
				}
			})
		);
	},
	addEvent : function(){
		$(document).on('click','#cancelBtn',function(){
			Public.closeTab();
		});
		$(document).on('blur','#newPassWord',function(){
			$(this).next("label").remove();
		});
		$(document).on('blur','#rNewPassWord',function(){
			$(this).next("label").remove();
		});
		$(document).on('click','#initPBtn',function(){
			Public.openWindow("初始化密码", $("#initPasswordDiv").html(), 500,230, function() {
				var returnValue = false;
				$.ajaxSetup({async: false});
				$("#initPassword").ajaxSubmit({
					url: '${ctx}/system/user/initPassword',
					beforeSubmit:function(){
						var bFalg = true;
						//判断必填项
						$("#initPassword .required").each(function(index, item){
							if( !($(item).val())) {
								if( !$(item).parent().find("label").eq(0).get(0) ){
									$("<label for='password' class='error'>必填信息</label>").insertAfter(item);
								}
								bFalg = false;
							}
						});
						//判断是否一致
						if(bFalg && !($('#newPassWord').val() === $('#rNewPassWord').val())) {
							if( !$('#rNewPassWord').parent().find("label").eq(0).get(0) ) {
								$("<label for='password' class='error'>确认密码不一致</label>").insertAfter($('#rNewPassWord'));
							}
							bFalg = false;
						}
						return bFalg;
					},
					success: function(data){
						Public.success("初始化密码成功!");
						returnValue = true; 
					}
			    });
				$.ajaxSetup({async: true});
				return returnValue;
			});
		});
	    
	    //选择图片
	    $(document).on('click', '.select-headimg', function() {
	    	Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			$("#headimg").val(files[0].src);
	    			$('.user-headimg-wrap img').attr('src', files[0].src);
	    		}
	    		return true;
	    	});
	    });
	}
};
$(function(){
	THISPAGE._init();
});
</script>
</body>
</html>