<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>信息源管理</title>
	<meta name="decorator" content="default"/>
			<link type="text/css" rel="Stylesheet" href="/static/kindeditor/WordPaster/css/WordPaster.css"/>
    <link type="text/css" rel="Stylesheet" href="/static/kindeditor/WordPaster/js/skygqbox.1.3.css" />
	<link type="text/css" rel="stylesheet" href="/static/kindeditor/themes/simple/simple.css" />
	<script type="text/javascript" charset="utf-8" src="/static/kindeditor/kindeditor-min.js"></script>
	<script type="text/javascript" charset="utf-8" src="/static/kindeditor/lang/zh_CN.js"></script>
    <script type="text/javascript" src="/static/kindeditor/WordPaster/js/WordPaster.js" charset="utf-8"></script>
    <script type="text/javascript" src="/static/kindeditor/WordPaster/js/skygqbox.1.3.js" charset="utf-8"></script>
	
	<script type="text/javascript">
		
		$(document).ready(function() {
			
			hideRadio();
            if($("#link").val()){
                $('#linkBody').show();
                $('#url').attr("checked", true);
            }
			$("#title").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
                    if ($("#categoryId").val()==""){
                        $("#categoryName").focus();
                        top.$.jBox.tip('请选择归属栏目','warning');
                    }else if (CKEDITOR.instances.content.getData()=="" && $("#link").val().trim()==""){
                        top.$.jBox.tip('请填写正文','warning');
                    }else{
                        loading('正在提交，请稍等...');
                        form.submit();
                    }
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
			$(":radio").click(function(){
				checkRadio($(this));
			});
			
		});
	function hideRadio(){
			$(":radio:checked").each(function(){
				checkRadio($(this));
			});
		}
		function checkRadio(ele){
			var v = $(ele).val();
			var n = $(ele).attr("name");
			if(n == "istime"){
				if (v == 0) {
					$(ele).parent().next().slideUp();
				} else {
					$(ele).parent().next().slideDown();
				}
			}else{
				if (v == 1) {
					$(ele).parent().next().slideUp();
				} else {
					$(ele).parent().next().slideDown();
				}
			}
		}
	function choseattention(id,name){
		$("#categoryId").val(id);
		$("#categoryName").val(name);
	}
	KindEditor.ready(function(K) {
			var editor2 = K.editor({
				allowFileManager: true,
				cssPath: '/static/kindeditor/plugins/code/prettify.css',
				uploadJson: '/static/kindeditor/jsp/upload_json.jsp',
				fileManagerJson: '/static/kindeditor/jsp/file_manager_json.jsp',
				allowFileManager: true,
			});

			K('#inserimage').click(function() {
				editor2.loadPlugin('image', function() {
					editor2.plugin.imageDialog({
						imageUrl: K('#url1').val(),
						clickFn: function(url, title, width, height, border, align) {
							$('#smallimage').attr("src", url);
							$("#smallimage").show();
							$('#image').val(url);
							/* alert(url); */
							editor.hideDialog();
						}
					});
				});
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	
		<li>
		<a href="${ctx}/cms/msgsource/">
		信息源列表		</a>
		</li >
		<li class="active"><a href="${ctx}/cms/msgsource/form">
		信息源添加
		</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="msgsource" action="${ctx}/cms/msgsource/save" method="post" class="form-horizontal">
		<form:hidden path="sid"/>
		
		<sys:message content="${message}"/>
		
		<div class="control-group">
			<label class="control-label">信息源名称:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-xlarge required" data-act="innum" data-target="#keywordsNum" data-length="25"/> 
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>
		
		
		<div class="control-group">
			<label class="control-label">信息源URL:</label>
			<div class="controls">
				<form:input path="url" htmlEscape="false" maxlength="200" class="input-xlarge required" data-act="innum" data-target="#keywordsNum" data-length="25"/> 
				<!--  <span class="help-inline">多个关键字，用空格分隔。</span>-->
			</div>
		</div>
		
		
		
		<div style="height:100px"></div>
		<div class="form-actions" style="position:fixed;width:100%;bottom:0">
			<shiro:hasPermission name="cms:article:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="发 布"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>