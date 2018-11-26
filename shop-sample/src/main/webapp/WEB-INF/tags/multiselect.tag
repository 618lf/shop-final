<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="idValue" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="nameValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="defaultText" type="java.lang.String" required="false" description="输入框值（Name）"%>
<%@ attribute name="selectUrl" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="true" description="输入框值（Name）"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="输入框值（Name）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<div class="tags-wrap">
	<input id="${id}" name="${id}" readonly="readonly" type="text" value="${idValue}" data-tags='${nameValue}' class="tags-treeselect treeselect-ids ${disabled}" data-clear="${allowClear}" data-rootS="${notAllowSelectRoot}" data-parentS="${notAllowSelectParent}" data-title="选择${title}" data-url="${selectUrl}" data-checked="${checked}"/>
	<input id="${name}" name="${name}" readonly="readonly" type="hidden" value="${nameValue}" class="tags treeselect-names"/>
</div>