<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>数据选择</title>
<link href="${ctxStatic}/bootstrap/2.3.2/css/bootstrap.css" rel="stylesheet" />
<link href="${ctxStatic}/fonts/iconfont.css" rel="stylesheet"/>
<link href="${ctxStatic}/common/common.css" rel="stylesheet" type="text/css"/> 
<link href="${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/zTreeStyle.min.css" rel="stylesheet" type="text/css"/>
<style type="text/css">
#search {
 position: fixed;
 padding: 5px 45px 5px 5px;
 border-width: 0px;
 border-bottom-width: 1px;
 top: 0px;
 z-index: 2;
 width: 100%;
 left: 0;
 -webkit-box-sizing: border-box;
 -moz-box-sizing: border-box;
 box-sizing: border-box;
}
.ops {
  position: absolute;
  width: 40px;
  height: 42px;
  top: 0;
  right: 0;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  padding: 5px;
}
.ops a{
  display: block;
  width: 100%;
  height: 15px;
  line-height: 15px;
  text-align: center;
  font-size: 14px;
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
  border: 1px solid #ddd;
  color: #666;
}
.ops a:FIRST-CHILD {
  margin-bottom: 2px;
}
.ops .iconfont { 
  font-size: 11px;
}
</style>
</head>
<body class="white">
<div id="search" class="form-search">
	<label for="key" class="control-label" style="padding:5px 5px 3px 0;">名称：</label>
	<input type="text" class="empty" id="key" name="key" maxlength="50" style="width:160px;">
	<div class="ops">
	  <a class="up"><i class="iconfont icon-caret-up"></i></a>
	  <a class="down"><i class="iconfont icon-caret-down"></i></a>
	</div>
</div>
<div id="tree" class="ztree" style="padding:15px 20px; margin-top: 42px; z-index: 1;"></div>
<script src="${ctxStatic}/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-cookie/jquery.cookie.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.core-3.5.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/jquery-ztree/3.5.12/js/jquery.ztree.excheck-3.5.min.js" type="text/javascript"></script>
<script type="text/javascript">
	var key, lastValue = "", nodeList = []; var cu = 0;
	var tree, setting = {view:{selectedMulti:false},check:{enable:("${checked}" == "true"),nocheckInherit:true},
		data:{simpleData:{enable:true}},
		view:{
			fontCss:function(treeId, treeNode) {
				return (!!treeNode.highlight) ? {"font-weight":"bold", "color":"#D64846"} : {"font-weight":"normal", "color":"#333"};
			}
		},
		callback:{beforeClick:function(id, node){
				if("${checked}" == "true"){
					tree.checkNode(node, !node.checked, true, true);
					return false;
				}
			}, 
			onDblClick:function(){
				top.$.jBox.getBox().find("button[value='ok']").trigger("click");
			}
	}};
	$(document).ready(function(){
		Public.postAjax("${url}${fns:indexOf(url,'?')==-1?'?':'&'}extId=${extId}&module=${module}&t="+new Date().getTime(), null, function(zNodes){
			if(typeof(zNodes) == 'string') {
				zNodes = $.parseJSON(zNodes);
			}
			// 初始化树结构
			tree = $.fn.zTree.init($("#tree"), setting, zNodes);
			
			// 默认展开一级节点
			var nodes = tree.getNodesByParam("level", 0);
			for(var i=0; i<nodes.length; i++) {
				tree.expandNode(nodes[i], true, false, false);
			}
			// 默认选择节点
			if( !!"${selectIds}" ) {
				var ids = "${selectIds}".split(",");
				for(var i=0; i<ids.length; i++) {
					if( !ids[i]) continue;
					var node = tree.getNodeByParam("id", ids[i]);
					if("${checked}" == "true"){
						try{tree.checkNode(node, true, true);}catch(e){}
						tree.selectNode(node, false);
					}else{
						tree.selectNode(node, true);
					}
				}
			}
		});
		key = $("#key");
		key.bind("focus", focusKey).bind("blur", blurKey).bind("keydown", function(e) {
			if (e.keyCode == '13') {
				searchNode();
			} else if(e.keyCode == '38'){
				cu = (--cu)<0?0:cu;
				if(nodeList != null && nodeList.length != 0) {
				   scrollTo(nodeList[cu].tId);
				}
			} else if(e.keyCode == '40') {
				cu = (++cu);
				if(nodeList != null && nodeList.length != 0) {
				   cu = cu >= nodeList.length?(nodeList.length - 1):cu;
				   scrollTo(nodeList[cu].tId);
				}
			}
		});
		$('.up').bind('click', function() {
			cu = (--cu)<0?0:cu;
			if(nodeList != null && nodeList.length != 0) {
			   scrollTo(nodeList[cu].tId);
			}
	    });
        $('.down').bind('click', function() {
        	cu = (++cu);
			if(nodeList != null && nodeList.length != 0) {
				cu = cu >= nodeList.length?(nodeList.length - 1):cu;
			   scrollTo(nodeList[cu].tId);
			}
	    });
	});
  	function focusKey(e) {
		if (key.hasClass("empty")) {
			key.removeClass("empty");
		}
	}
	function blurKey(e) {
		if (key.get(0).value === "") {
			key.addClass("empty");
		}
		searchNode(e);
	}
	function searchNode(e) { 
		// 取得输入的关键字的值
		var value = $.trim(key.get(0).value);
		// 按名字查询
		var keyType = "name";
		if (key.hasClass("empty")) {value = "";}
		// 如果和上次一次，就退出不查了。
		if (lastValue === value) {return;}
		// 保存最后一次
		lastValue = value;
		// 如果要查空字串，就退出不查了。
		if (value === "") {return;}
		updateNodes(false);
		nodeList = tree.getNodesByParamFuzzy(keyType, value);
		updateNodes(true);
		if(nodeList != null && nodeList.length != 0) {
		   scrollTo(nodeList[0].tId);
		}
	}
	function updateNodes(highlight) {
		for(var i=0, l= nodeList.length; i<l; i++) {
			nodeList[i].highlight = highlight;				
			tree.updateNode(nodeList[i]);
			tree.expandNode(nodeList[i].getParentNode(), true, false, false);
		}
		cu = 0;
	}
	function scrollTo(id) {
		$('body').animate({scrollTop: $("#" + id).offset().top - 40}, 500);
	}
</script>
</body>