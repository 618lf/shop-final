<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>附件上传</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/webuploader/webfilemanager.css" rel="stylesheet"/>
<link href="${ctxStatic}/webuploader/webuploader.css" rel="stylesheet"/>
</head>
<body class="white no-fixed-btns" style="overflow: hidden;">
<div id="uploader" class="uploader-warp uploader-dialog">
 <div class="uploader">
   <div class="queueList">
      <div class="placeholder">
		<div class="filePicker webuploader-container"></div>
		<p>或将文件拖到这里，最多选择100个文件</p>
      </div>
   </div>
   <div class="statusBar" style="display: none;">
       <div class="progress" style="display: none;">
           <span class="text">0%</span>
           <span class="percentage" style="width: 0%;"></span>
       </div>
       <div class="info">共0张（0B），已上传0张</div>
       <div class="btns">
        <div class="filePicker2 filePicker-btn">选择文件</div>
        <a class="uploadBtn">开始上传</a>
       </div>
   </div>
 </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/webuploader/webuploader.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/webuploader/jquery-uploader.js" type="text/javascript"></script>
<script type="text/javascript">
  var THISPAGE = {
	 _init : function() {
		  $('#uploader').height($(window).height());
		  $('#uploader .queueList').height($(window).height() - 64);
		  var uploader = $('#uploader').uploader({
			  auto: false,
			  fileNumLimit: 100,
			  formData: {
				uid: "catax",
				action: 'uploadfile',
				dirId: '${dirId}'
			  }
		  }); 
		  this.uploader = uploader;
	 },
	 getUploadFiles : function() {
		 return this.uploader.getUploadedFileObjs();
	 },
	 getSelectedFiles : function() {
		 return THISPAGE.getUploadFiles();
	 }
  };
  $(function() {
	  //设置界面
	  THISPAGE._init();
  });
</script>
</body>
</html>