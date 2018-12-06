<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<c:if test="${not empty content}">
  <c:if test="${not empty type}"><c:set var="ctype" value="${type}"/></c:if><c:if test="${empty type}"><c:set var="ctype" value="${fns:indexOf(content,'失败') eq -1?'success':'error'}"/></c:if>
  <c:if test="${ctype eq 'success'}"><script type="text/javascript">(function(){Public.toast("${content}");})()</script></c:if>
  <c:if test="${!(ctype eq 'success')}"><script type="text/javascript">(function(){Public.alert("${content}", "${ctype}",null, null, 0);})()</script></c:if>
</c:if>