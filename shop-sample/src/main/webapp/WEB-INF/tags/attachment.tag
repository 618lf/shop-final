<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框"%>
<%@ attribute name="dvalue" type="java.lang.String" required="false" description="输入框"%>
<%@ attribute name="title" type="java.lang.String" required="false" description="输入框"%>
<%@ attribute name="multi" type="java.lang.Boolean" required="false" description="是否允许多选"%>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="是否查看模式"%>
<div class="attachment-wrap" data-readonly="${readonly}"><input type="hidden" id="${name}" name="${name}" value="${(value == null || value eq '')?dvalue:value}"/><ol class="preview"></ol><c:if test="${!readonly}"><a href="javascript:" data-multi="${multi}" class="-select btn">${selectMultiple?'添加':'选择'}</a><a href="javascript:" class="-clear btn">清除</a></c:if></div>