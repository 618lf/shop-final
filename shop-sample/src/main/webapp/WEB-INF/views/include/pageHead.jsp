<%@ page pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sys" uri="/WEB-INF/tld/fns-tag.tld"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<c:set var="base" value="${fns:getBasePath()}"/>
<c:set var="version" value="${fns:getConfig('server.version')}"/>
<c:set var="webRoot" value="${fns:getWebRoot()}"/>
<c:set var="ctx" value="${fns:getWebRoot()}${fns:getAdminPath()}"/>
<c:set var="ctxStatic" value="${fns:getWebRoot()}/static"/>
<c:set var="ctxModules" value="${fns:getWebRoot()}/static/modules"/>
<c:set var="ctxFront" value="${fns:getWebRoot()}${fns:getFrontPath()}"/>