<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="${fns:getWebRoot()}${fns:getAdminPath()}"/>
<%@ attribute name="name" type="java.lang.String" required="true" description="验证码输入框名称"%>
<input type="text" id="${name}" name="${name}" maxlength="4" class="txt required" style="width:60px; margin-bottom: 0; border-radius: 0;" placeholder="验证码"/>
<img src="${ctx}/validate/code" onclick="$('.${name}Refresh').click();" class="mid ${name}"/>
<a href="javascript:" onclick="$('.${name}').attr('src','${ctx}/validate/code?'+new Date().getTime());" class="mid ${name}Refresh">看不清</a>