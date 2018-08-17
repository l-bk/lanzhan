<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>采集过滤管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/copy/contextNodeDefineFilter/">采集过滤列表</a></li>
		<li class="active"><a href="${ctx}/copy/contextNodeDefineFilter/form?cid=${contextNodeDefineFilter.cid}">采集过滤<shiro:hasPermission name="copy:contextNodeDefineFilter:edit">${not empty contextNodeDefineFilter.cid?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="copy:contextNodeDefineFilter:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contextNodeDefineFilter" action="${ctx}/copy/contextNodeDefineFilter/save" method="post" class="form-horizontal">
		<form:hidden path="cid"/>
		<form:hidden path="nodeDefineId"/>
		<form:hidden path="parentid"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">过滤规则标题：</label>
			<div class="controls">
				<form:input path="title" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始表达式：</label>
			<div class="controls">
				<form:input path="begin" htmlEscape="false" maxlength="512" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束表达式：</label>
			<div class="controls">
				<form:input path="end" htmlEscape="false" maxlength="512" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">类型:</label>
			<div class="controls">
				<form:select path="type">
					<form:options items="${fns:getDictList('context_node_filter')}"
						itemLabel="label" itemValue="value" htmlEscape="false" />
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<shiro:hasPermission name="copy:contextNodeDefineFilter:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>