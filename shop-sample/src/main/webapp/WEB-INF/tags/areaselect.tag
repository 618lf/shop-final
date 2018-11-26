<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="idValue" type="java.lang.String" required="true" description="输入框值"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="nameValue" type="java.lang.String" required="true" description="输入框名称"%>
<div class="areaselect-wrap">
  <input type="text" class="areaselect" id="${id}" name="${id}" value="${idValue}" data-name="${nameValue}">
  <input type="hidden" id="${name}" name="${name}" value="${nameValue}" class="areaselect-name">
</div>