<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ attribute name="path" type="java.lang.String" required="true" description="输入框名称"%>
<%@ attribute name="value" type="java.lang.String" required="true" description="输入框值"%>
<div class="iconselect-wrap"><i class="${not empty value?value:''}" style="font-size: 24px; vertical-align: middle;"></i>&nbsp;<label style="display: inline-block;">${not empty value?value:'无'}</label><input name="${path}" type="hidden" value="${value}"/><a href="javascript:" class="iconselect btn">选择</a></div>