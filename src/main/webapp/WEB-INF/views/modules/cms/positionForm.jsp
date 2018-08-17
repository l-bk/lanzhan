<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>推荐位管理</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#name").focus();
		$("#inputForm").validate({
			submitHandler : function(form) {
				loading('正在提交，请稍等...');
				form.submit();
			},
			errorContainer : "#messageBox",
			errorPlacement : function(error, element) {
				$("#messageBox").text("输入有误，请先更正。");
				if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
					error.appendTo(element.parent().parent());
				} else {
					error.insertAfter(element);
				}
			}
		});
	});
</script>
<style>
.controls label {
	display: inline-block;
	width: 200px
}
</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cms/position/">推荐位列表</a></li>
		<li class="active"><a href="${ctx}/cms/position/form?posid=${position.posid}">推荐位<shiro:hasPermission name="cms:category:edit">${not empty position.posid?'修改':'添加'}</shiro:hasPermission>
				<shiro:lacksPermission name="cms:category:edit">查看</shiro:lacksPermission></a></li>
	</ul>
	<br />
	<form:form id="inputForm" modelAttribute="position" action="${ctx}/cms/position/save" method="post" class="form-horizontal">
		<form:hidden path="posid" />
		<sys:message content="${message}" />
		<div class="control-group">
			<label class="control-label">推荐位名称:</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" maxlength="50" class="required" />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否启用:</label>
			<div class="controls">
				<form:radiobutton path="stat" value="1" />
				是 &nbsp;
				<form:radiobutton path="stat" value="0" />
				否
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">位置:</label>
			<div class="controls">
				<label> <form:radiobutton path="type" value="0" />头条焦点图
				</label> <label> <form:radiobutton path="type" value="1" />头条top热榜
				</label> <label> <form:radiobutton path="type" value="2" />头条多张缩略图
				</label> <label> <form:radiobutton path="type" value="3" />头条单张缩略图
				</label> <label> <form:radiobutton path="type" value="4" />内页精彩图片
				</label> <label> <form:radiobutton path="type" value="5" />内页推荐阅读
				</label> <label> <form:radiobutton path="type" value="6" />内页(多缩略图)
				</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">测试位置:</label>
			<div class="controls">
				<label> <form:radiobutton path="type" value="7" />头条焦点图(测试)
				</label> <label> <form:radiobutton path="type" value="8" />头条top热榜(测试)
				</label> <label> <form:radiobutton path="type" value="9" />头条多张缩略图(测试)
				</label> <label> <form:radiobutton path="type" value="10" />头条单张缩略图(测试)
				</label> <label> <form:radiobutton path="type" value="11" />内页精彩图片(测试)
				</label> <label> <form:radiobutton path="type" value="12" />内页推荐阅读(测试)
				</label> <label> <form:radiobutton path="type" value="13" />内页(多缩略图)(测试)
				</label> <label> <form:radiobutton path="type" value="14" />内页(女性内一推荐阅读)(测试)
				</label> <label> <form:radiobutton path="type" value="15" />内页(女性内二 多缩略图)(测试)
				</label>
			</div>
		</div>

		<div class="form-actions">
			<shiro:hasPermission name="cms:category:edit">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存" />&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)" />
		</div>
	</form:form>
</body>
</html>