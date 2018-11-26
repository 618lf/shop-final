<%@ tag language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fns" uri="/WEB-INF/tld/fns.tld"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="idValue" type="java.lang.String" required="true" description="隐藏域值（ID）"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="输入框名称（Name）"%>
<%@ attribute name="nameValue" type="java.lang.String" required="true" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="fnc" type="java.lang.String" required="false" description="回调"%>
<div class="treeselect-wrap input-append"><input id="${id}" name="${id}" class="treeselect-ids ${cssClass}" type="hidden" value="${idValue}"/><input id="${name}" name="${name}" class="treeselect-names ${cssClass}" readonly="readonly" type="text" value="${nameValue}"/><a data-fnc="${fnc}" data-clear="${allowClear}" data-rootS="${notAllowSelectRoot}" data-parentS="${notAllowSelectParent}" data-title="选择${title}" data-url="${url}" data-checked="${checked}" data-ext="${extId}" href="javascript:void(0)" class="treeselect btn ${disabled}">&nbsp;<i class="iconfont icon-search"></i>&nbsp;</a></div>