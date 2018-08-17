<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>子节点管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#name").focus();
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
		<li><a href="${ctx}/cms/copy/">采集站点列表</a></li>
		<li class="active"><a href="${ctx}/cms/copy/form?cid=${contextdefine.cid}">站点<shiro:hasPermission name="cms:category:edit">${not empty contextdefine.cid?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cms:category:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contextdefine" action="${ctx}/cms/copy/save" method="post" class="form-horizontal">
		<form:hidden path="cid"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">是否启用:</label>
			<div class="controls">
                <form:radiobutton path="stat" value="1"/>是 &nbsp;<form:radiobutton path="stat" value="0"/>否
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">采集站点名称:</label>
			<div class="controls">
				<form:input path="description" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">采集站点的一级域名:</label>
			<div class="controls">
				<form:input path="url" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">采集的网站的页面编码:</label>
			<div class="controls">
				<form:input path="lang" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">文章页的页面编码:</label>
			<div class="controls">
				<form:input path="contentLang" htmlEscape="false" maxlength="50" class="required"/>
			</div>
		</div>
		
		<div class="control-group">
			<label class="control-label">定时采集的时间:</label>
			<div class="controls">
				<fmt:parseDate value="${contextdefine.beginTime}" pattern="yyyy-MM-dd HH:mm:ss" var="t"/>
				<input id="beginTime" type="text" name="beginTime" maxlength="20" class="input-medium Wdate" value='<fmt:formatDate value="${t}" pattern="yyyy-MM-dd HH:mm:ss"/>'
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">是否直接入库:</label>
			<div class="controls">
				<form:radiobutton path="ischeckflag" value="1"/>是 &nbsp;<form:radiobutton path="ischeckflag" value="0"/>否
			</div>
		</div>
		
		<!-- <div class="control-group">
			<label class="control-label">推荐位:</label>
			<div class="controls">
				<c:forEach var="t" items="${POSITIONDESC}">

				</c:forEach>
			</div>
		</div> -->

		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>